package com.liveklass.testa.domain.creator.application;

import com.liveklass.testa.domain.auth.domain.Account;
import com.liveklass.testa.domain.auth.repository.AccountRepository;
import com.liveklass.testa.domain.creator.controller.dto.CreatorSignUpRequest;
import com.liveklass.testa.domain.creator.domain.Creator;
import com.liveklass.testa.domain.creator.exception.DuplicateEmailException;
import com.liveklass.testa.domain.creator.repository.CreatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreatorService implements CreatorUseCase {

    private final AccountRepository accountRepository;
    private final CreatorRepository creatorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void signUp(CreatorSignUpRequest request) {
        if (accountRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException();
        }

        Account account = Account.create(request.email(), passwordEncoder.encode(request.password()));
        accountRepository.save(account);

        Creator creator = Creator.create(account, request.name());
        creatorRepository.save(creator);
    }
}
