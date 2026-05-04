package com.liveklass.testa.domain.enrollment.repository;

import com.liveklass.testa.domain.classmate.domain.Classmate;
import com.liveklass.testa.domain.enrollment.domain.Enrollment;
import com.liveklass.testa.domain.klass.domain.Klass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.liveklass.testa.domain.enrollment.domain.EnrollmentStatus;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByClassmateAndKlassAndStatusNot(Classmate classmate, Klass klass, EnrollmentStatus status);

    Page<Enrollment> findByClassmate(Classmate classmate, Pageable pageable);

    int countByKlassAndStatusNot(Klass klass, EnrollmentStatus status);

    Page<Enrollment> findByKlassAndStatusNot(Klass klass, EnrollmentStatus status, Pageable pageable);
}
