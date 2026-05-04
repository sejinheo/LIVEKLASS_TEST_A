package com.liveklass.testa.domain.enrollment.application;

import com.liveklass.testa.domain.auth.domain.Account;
import com.liveklass.testa.domain.auth.repository.AccountRepository;
import com.liveklass.testa.domain.classmate.domain.Classmate;
import com.liveklass.testa.domain.classmate.repository.ClassmateRepository;
import com.liveklass.testa.domain.enrollment.domain.Enrollment;
import com.liveklass.testa.domain.enrollment.domain.EnrollmentStatus;
import com.liveklass.testa.domain.enrollment.exception.ClassCapacityExceededException;
import com.liveklass.testa.domain.enrollment.exception.ClassNotOpenException;
import com.liveklass.testa.domain.enrollment.exception.ClassmateNotFoundException;
import com.liveklass.testa.domain.enrollment.exception.DuplicateEnrollmentException;
import com.liveklass.testa.domain.enrollment.repository.EnrollmentRepository;
import com.liveklass.testa.domain.klass.domain.Klass;
import com.liveklass.testa.domain.klass.exception.KlassNotFoundException;
import com.liveklass.testa.domain.klass.repository.KlassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentService implements EnrollmentUseCase {

    private final AccountRepository accountRepository;
    private final ClassmateRepository classmateRepository;
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

        if (enrollmentRepository.existsByClassmateAndKlass(classmate, klass)) {
            throw new DuplicateEnrollmentException();
        }

        int currentEnrollment = enrollmentRepository.countByKlassAndStatusNot(klass, EnrollmentStatus.CANCELLED);
        if (currentEnrollment >= klass.getMaxCapacity()) {
            throw new ClassCapacityExceededException();
        }

        Enrollment enrollment = Enrollment.create(classmate, klass);
        enrollmentRepository.save(enrollment);
    }
}
