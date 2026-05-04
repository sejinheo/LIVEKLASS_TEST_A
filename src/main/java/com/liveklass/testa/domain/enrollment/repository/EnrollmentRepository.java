package com.liveklass.testa.domain.enrollment.repository;

import com.liveklass.testa.domain.classmate.domain.Classmate;
import com.liveklass.testa.domain.enrollment.domain.Enrollment;
import com.liveklass.testa.domain.klass.domain.Klass;
import org.springframework.data.jpa.repository.JpaRepository;

import com.liveklass.testa.domain.enrollment.domain.EnrollmentStatus;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByClassmateAndKlassAndStatusNot(Classmate classmate, Klass klass, EnrollmentStatus status);

    List<Enrollment> findByClassmate(Classmate classmate);

    int countByKlassAndStatusNot(Klass klass, EnrollmentStatus status);
}
