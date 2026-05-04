package com.liveklass.testa.domain.klass.repository;

import com.liveklass.testa.domain.klass.domain.ClassStatus;
import com.liveklass.testa.domain.klass.domain.Klass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KlassRepository extends JpaRepository<Klass, Long> {

    List<Klass> findByStatus(ClassStatus status);
}
