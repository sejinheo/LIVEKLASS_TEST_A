package com.liveklass.testa.infrastructure.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        String secret = "test-secret-key-must-be-at-least-32-bytes-long";
        long expirationMs = 3600000;
        jwtTokenProvider = new JwtTokenProvider(secret, expirationMs);
    }

    @Test
    @DisplayName("생성한 토큰이 유효하다")
    void createdTokenIsValid() {
        String token = jwtTokenProvider.createToken(1L, "test@test.com", "CREATOR");

        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("잘못된 토큰은 유효하지 않다")
    void invalidTokenReturnsFalse() {
        assertThat(jwtTokenProvider.validateToken("invalid.token.here")).isFalse();
    }

    @Test
    @DisplayName("빈 문자열은 유효하지 않다")
    void emptyTokenReturnsFalse() {
        assertThat(jwtTokenProvider.validateToken("")).isFalse();
    }

    @Test
    @DisplayName("accountId를 정확히 추출한다")
    void extractsAccountId() {
        String token = jwtTokenProvider.createToken(42L, "test@test.com", "CLASSMATE");

        assertThat(jwtTokenProvider.getAccountId(token)).isEqualTo(42L);
    }

    @Test
    @DisplayName("role을 정확히 추출한다")
    void extractsRole() {
        String token = jwtTokenProvider.createToken(1L, "test@test.com", "CREATOR");

        assertThat(jwtTokenProvider.getRole(token)).isEqualTo("CREATOR");
    }
}
