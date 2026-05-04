package com.liveklass.testa.domain.klass.repository;

import com.liveklass.testa.domain.klass.domain.ClassStatus;
import com.liveklass.testa.domain.klass.domain.Klass;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KlassRepository extends JpaRepository<Klass, Long> {

    List<Klass> findByStatus(ClassStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT k FROM Klass k WHERE k.id = :id")
    Optional<Klass> findByIdWithLock(@Param("id") Long id);
}
