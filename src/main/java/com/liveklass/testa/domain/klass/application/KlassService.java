package com.liveklass.testa.domain.klass.application;

import com.liveklass.testa.domain.auth.domain.Account;
import com.liveklass.testa.domain.auth.repository.AccountRepository;
import com.liveklass.testa.domain.creator.domain.Creator;
import com.liveklass.testa.domain.creator.repository.CreatorRepository;
import com.liveklass.testa.domain.klass.controller.dto.KlassCreateRequest;
import com.liveklass.testa.domain.klass.domain.Klass;
import com.liveklass.testa.domain.klass.exception.CreatorNotFoundException;
import com.liveklass.testa.domain.klass.repository.KlassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KlassService implements KlassUseCase {

    private final AccountRepository accountRepository;
    private final CreatorRepository creatorRepository;
    private final KlassRepository klassRepository;

    @Override
    @Transactional
    public void create(Long accountId, KlassCreateRequest request) {
        Account account = accountRepository.getReferenceById(accountId);
        Creator creator = creatorRepository.findByAccount(account)
                .orElseThrow(CreatorNotFoundException::new);

        Klass klass = Klass.create(
                creator,
                request.title(),
                request.description(),
                request.price(),
                request.maxCapacity(),
                request.startDate(),
                request.endDate()
        );

        klassRepository.save(klass);
    }
}
