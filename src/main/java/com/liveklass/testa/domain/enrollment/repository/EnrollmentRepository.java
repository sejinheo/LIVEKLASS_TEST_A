package com.liveklass.testa.domain.enrollment.repository;

import com.liveklass.testa.domain.classmate.domain.Classmate;
import com.liveklass.testa.domain.enrollment.domain.Enrollment;
import com.liveklass.testa.domain.enrollment.domain.EnrollmentStatus;
import com.liveklass.testa.domain.klass.domain.Klass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByClassmateAndKlassAndStatusNot(Classmate classmate, Klass klass, EnrollmentStatus status);

    @Query(value = "SELECT e FROM Enrollment e JOIN FETCH e.klass WHERE e.classmate = :classmate",
           countQuery = "SELECT COUNT(e) FROM Enrollment e WHERE e.classmate = :classmate")
    Page<Enrollment> findByClassmateWithKlass(@Param("classmate") Classmate classmate, Pageable pageable);

    int countByKlassAndStatusIn(Klass klass, List<EnrollmentStatus> statuses);

    @Query(value = "SELECT e FROM Enrollment e JOIN FETCH e.classmate WHERE e.klass = :klass AND e.status != :status",
           countQuery = "SELECT COUNT(e) FROM Enrollment e WHERE e.klass = :klass AND e.status != :status")
    Page<Enrollment> findByKlassAndStatusNotWithClassmate(@Param("klass") Klass klass, @Param("status") EnrollmentStatus status, Pageable pageable);

    Optional<Enrollment> findFirstByKlassAndStatusOrderByEnrolledAtAsc(Klass klass, EnrollmentStatus status);
}
