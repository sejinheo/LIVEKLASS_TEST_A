package com.liveklass.testa.domain.creator.repository;

import com.liveklass.testa.domain.creator.domain.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<Creator, Long> {
}
