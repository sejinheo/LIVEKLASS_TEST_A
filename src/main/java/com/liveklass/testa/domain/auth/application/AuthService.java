package com.liveklass.testa.domain.auth.application;

import com.liveklass.testa.domain.auth.controller.dto.LoginRequest;
import com.liveklass.testa.domain.auth.domain.Account;
import com.liveklass.testa.domain.auth.exception.AccountNotFoundException;
import com.liveklass.testa.domain.auth.exception.InvalidPasswordException;
import com.liveklass.testa.domain.auth.repository.AccountRepository;
import com.liveklass.testa.domain.classmate.repository.ClassmateRepository;
import com.liveklass.testa.domain.creator.repository.CreatorRepository;
import com.liveklass.testa.infrastructure.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final AccountRepository accountRepository;
    private final CreatorRepository creatorRepository;
    private final ClassmateRepository classmateRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional(readOnly = true)
    public String login(LoginRequest request) {
        Account account = accountRepository.findByEmail(request.email())
                .orElseThrow(AccountNotFoundException::new);

        if (!passwordEncoder.matches(request.password(), account.getPassword())) {
            throw new InvalidPasswordException();
        }

        String role = resolveRole(account);
        return jwtTokenProvider.createToken(account.getId(), account.getEmail(), role);
    }

    private String resolveRole(Account account) {
        if (creatorRepository.existsByAccount(account)) {
            return "CREATOR";
        }
        if (classmateRepository.existsByAccount(account)) {
            return "CLASSMATE";
        }
        throw new AccountNotFoundException();
    }
}
