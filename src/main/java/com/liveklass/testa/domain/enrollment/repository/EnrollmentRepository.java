package com.liveklass.testa.domain.enrollment.repository;

import com.liveklass.testa.domain.classmate.domain.Classmate;
import com.liveklass.testa.domain.enrollment.domain.Enrollment;
import com.liveklass.testa.domain.enrollment.domain.EnrollmentStatus;
import com.liveklass.testa.domain.klass.domain.Klass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByClassmateAndKlassAndStatusNot(Classmate classmate, Klass klass, EnrollmentStatus status);

    List<Enrollment> findByClassmate(Classmate classmate);

    int countByKlassAndStatusIn(Klass klass, List<EnrollmentStatus> statuses);

    List<Enrollment> findByKlassAndStatusNot(Klass klass, EnrollmentStatus status);

    Optional<Enrollment> findFirstByKlassAndStatusOrderByEnrolledAtAsc(Klass klass, EnrollmentStatus status);
}
