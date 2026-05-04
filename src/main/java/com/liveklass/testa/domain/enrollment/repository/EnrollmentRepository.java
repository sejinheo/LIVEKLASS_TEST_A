package com.liveklass.testa.domain.enrollment.repository;

import com.liveklass.testa.domain.classmate.domain.Classmate;
import com.liveklass.testa.domain.enrollment.domain.Enrollment;
import com.liveklass.testa.domain.enrollment.domain.EnrollmentStatus;
import com.liveklass.testa.domain.klass.domain.Klass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByClassmateAndKlassAndStatusNot(Classmate classmate, Klass klass, EnrollmentStatus status);

    Page<Enrollment> findByClassmate(Classmate classmate, Pageable pageable);

    int countByKlassAndStatusIn(Klass klass, List<EnrollmentStatus> statuses);

    Page<Enrollment> findByKlassAndStatusNot(Klass klass, EnrollmentStatus status, Pageable pageable);

    Optional<Enrollment> findFirstByKlassAndStatusOrderByEnrolledAtAsc(Klass klass, EnrollmentStatus status);
}
