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

    @Query("SELECT k FROM Klass k JOIN FETCH k.creator WHERE k.status = :status")
    List<Klass> findByStatus(@Param("status") ClassStatus status);

    @Query("SELECT k FROM Klass k JOIN FETCH k.creator")
    List<Klass> findAllWithCreator();

    @Query("SELECT k FROM Klass k JOIN FETCH k.creator WHERE k.id = :id")
    Optional<Klass> findByIdWithCreator(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT k FROM Klass k WHERE k.id = :id")
    Optional<Klass> findByIdWithLock(@Param("id") Long id);
}
