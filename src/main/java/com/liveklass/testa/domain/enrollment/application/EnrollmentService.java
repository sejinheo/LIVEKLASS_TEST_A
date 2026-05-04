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
import com.liveklass.testa.domain.enrollment.exception.ClassCapacityExceededException;
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

@Service
@RequiredArgsConstructor
public class EnrollmentService implements EnrollmentUseCase {

    private final AccountRepository accountRepository;
    private final ClassmateRepository classmateRepository;
    private final CreatorRepository creatorRepository;
    private final KlassRepository klassRepository;
    private final EnrollmentRepository enrollmentRepository;

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

        int currentEnrollment = enrollmentRepository.countByKlassAndStatusNot(klass, EnrollmentStatus.CANCELLED);
        if (currentEnrollment >= klass.getMaxCapacity()) {
            throw new ClassCapacityExceededException();
        }

        Enrollment enrollment = Enrollment.create(classmate, klass);
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

        enrollment.cancel();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnrollmentResponse> findMyEnrollments(Long accountId, Pageable pageable) {
        Account account = accountRepository.getReferenceById(accountId);
        Classmate classmate = classmateRepository.findByAccount(account)
                .orElseThrow(ClassmateNotFoundException::new);

        return enrollmentRepository.findByClassmate(classmate, pageable)
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

        return enrollmentRepository.findByKlassAndStatusNot(klass, EnrollmentStatus.CANCELLED, pageable)
                .map(ClassEnrollmentResponse::from);
    }
}
