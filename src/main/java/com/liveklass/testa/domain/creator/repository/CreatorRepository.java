package com.liveklass.testa.domain.creator.repository;

import com.liveklass.testa.domain.auth.domain.Account;
import com.liveklass.testa.domain.creator.domain.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Creator, Long> {

    boolean existsByAccount(Account account);

    Optional<Creator> findByAccount(Account account);
}
