package com.liveklass.testa.domain.classmate.application;

import com.liveklass.testa.domain.auth.domain.Account;
import com.liveklass.testa.domain.auth.repository.AccountRepository;
import com.liveklass.testa.domain.classmate.controller.dto.ClassmateSignUpRequest;
import com.liveklass.testa.domain.classmate.domain.Classmate;
import com.liveklass.testa.domain.classmate.exception.DuplicateEmailException;
import com.liveklass.testa.domain.classmate.repository.ClassmateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClassmateService implements ClassmateUseCase {

    private final AccountRepository accountRepository;
    private final ClassmateRepository classmateRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void signUp(ClassmateSignUpRequest request) {
        if (accountRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException();
        }

        Account account = Account.create(request.email(), passwordEncoder.encode(request.password()));
        accountRepository.save(account);

        Classmate classmate = Classmate.create(account, request.name());
        classmateRepository.save(classmate);
    }
}
