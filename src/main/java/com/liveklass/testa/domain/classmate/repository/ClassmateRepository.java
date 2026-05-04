package com.liveklass.testa.domain.classmate.repository;

import com.liveklass.testa.domain.classmate.domain.Classmate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassmateRepository extends JpaRepository<Classmate, Long> {
}
