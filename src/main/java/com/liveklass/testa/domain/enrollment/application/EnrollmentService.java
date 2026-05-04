package com.liveklass.testa.domain.enrollment.application;

import com.liveklass.testa.domain.auth.domain.Account;
import com.liveklass.testa.domain.auth.repository.AccountRepository;
import com.liveklass.testa.domain.classmate.domain.Classmate;
import com.liveklass.testa.domain.classmate.repository.ClassmateRepository;
import com.liveklass.testa.domain.enrollment.controller.dto.ClassEnrollmentResponse;
import com.liveklass.testa.domain.enrollment.controller.dto.EnrollmentResponse;
import com.liveklass.testa.domain.creator.domain.Creator;
import com.liveklass.testa.domain.creator.repository.CreatorRepository;
import com.liveklass.testa.domain.klass.exception.KlassAccessDeniedException;
import com.liveklass.testa.domain.enrollment.domain.Enrollment;
import com.liveklass.testa.domain.enrollment.domain.EnrollmentStatus;
import com.liveklass.testa.domain.enrollment.exception.ClassNotOpenException;
import com.liveklass.testa.domain.enrollment.exception.ClassmateNotFoundException;
import com.liveklass.testa.domain.enrollment.exception.DuplicateEnrollmentException;
import com.liveklass.testa.domain.enrollment.exception.EnrollmentAccessDeniedException;
import com.liveklass.testa.domain.enrollment.exception.EnrollmentNotFoundException;
import com.liveklass.testa.domain.enrollment.repository.EnrollmentRepository;
import com.liveklass.testa.domain.klass.domain.Klass;
import com.liveklass.testa.domain.klass.exception.CreatorNotFoundException;
import com.liveklass.testa.domain.klass.exception.KlassNotFoundException;
import com.liveklass.testa.domain.klass.repository.KlassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnrollmentService implements EnrollmentUseCase {

    private final AccountRepository accountRepository;
    private final ClassmateRepository classmateRepository;
    private final CreatorRepository creatorRepository;
    private final KlassRepository klassRepository;
    private final EnrollmentRepository enrollmentRepository;

    private static final List<EnrollmentStatus> ACTIVE_STATUSES =
            List.of(EnrollmentStatus.PENDING, EnrollmentStatus.CONFIRMED);

    @Override
    @Transactional
    public void enroll(Long accountId, Long classId) {
        Account account = accountRepository.getReferenceById(accountId);
        Classmate classmate = classmateRepository.findByAccount(account)
                .orElseThrow(ClassmateNotFoundException::new);

        Klass klass = klassRepository.findByIdWithLock(classId)
                .orElseThrow(KlassNotFoundException::new);

        if (!klass.isOpen()) {
            throw new ClassNotOpenException();
        }

        if (enrollmentRepository.existsByClassmateAndKlassAndStatusNot(classmate, klass, EnrollmentStatus.CANCELLED)) {
            throw new DuplicateEnrollmentException();
        }

        int activeCount = enrollmentRepository.countByKlassAndStatusIn(klass, ACTIVE_STATUSES);
        if (activeCount >= klass.getMaxCapacity()) {
            Enrollment enrollment = Enrollment.create(classmate, klass, EnrollmentStatus.WAITLISTED);
            enrollmentRepository.save(enrollment);
            return;
        }

        Enrollment enrollment = Enrollment.create(classmate, klass, EnrollmentStatus.PENDING);
        enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional
    public void confirm(Long accountId, Long enrollmentId) {
        Account account = accountRepository.getReferenceById(accountId);
        Classmate classmate = classmateRepository.findByAccount(account)
                .orElseThrow(ClassmateNotFoundException::new);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(EnrollmentNotFoundException::new);

        if (!enrollment.isOwnedBy(classmate)) {
            throw new EnrollmentAccessDeniedException();
        }

        enrollment.confirm();
    }

    @Override
    @Transactional
    public void cancel(Long accountId, Long enrollmentId) {
        Account account = accountRepository.getReferenceById(accountId);
        Classmate classmate = classmateRepository.findByAccount(account)
                .orElseThrow(ClassmateNotFoundException::new);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(EnrollmentNotFoundException::new);

        if (!enrollment.isOwnedBy(classmate)) {
            throw new EnrollmentAccessDeniedException();
        }

        Klass klass = klassRepository.findByIdWithLock(enrollment.getKlass().getId())
                .orElseThrow(KlassNotFoundException::new);

        EnrollmentStatus previousStatus = enrollment.getStatus();
        enrollment.cancel();

        if (previousStatus == EnrollmentStatus.PENDING || previousStatus == EnrollmentStatus.CONFIRMED) {
            promoteNextWaitlisted(klass);
        }
    }

    private void promoteNextWaitlisted(Klass klass) {
        Optional<Enrollment> nextWaitlisted = enrollmentRepository
                .findFirstByKlassAndStatusOrderByEnrolledAtAsc(klass, EnrollmentStatus.WAITLISTED);
        nextWaitlisted.ifPresent(Enrollment::promote);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnrollmentResponse> findMyEnrollments(Long accountId, Pageable pageable) {
        Account account = accountRepository.getReferenceById(accountId);
        Classmate classmate = classmateRepository.findByAccount(account)
                .orElseThrow(ClassmateNotFoundException::new);

        return enrollmentRepository.findByClassmateWithKlass(classmate, pageable)
                .map(EnrollmentResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassEnrollmentResponse> findClassEnrollments(Long accountId, Long classId, Pageable pageable) {
        Account account = accountRepository.getReferenceById(accountId);
        Creator creator = creatorRepository.findByAccount(account)
                .orElseThrow(CreatorNotFoundException::new);

        Klass klass = klassRepository.findById(classId)
                .orElseThrow(KlassNotFoundException::new);

        if (!klass.isOwnedBy(creator)) {
            throw new KlassAccessDeniedException();
        }

        return enrollmentRepository.findByKlassAndStatusNotWithClassmate(klass, EnrollmentStatus.CANCELLED, pageable)
                .map(ClassEnrollmentResponse::from);
    }
}
