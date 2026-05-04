package com.liveklass.testa.infrastructure.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties(
                "test-secret-key-must-be-at-least-32-bytes-long", 3600000);
        jwtTokenProvider = new JwtTokenProvider(jwtProperties);
    }

    @Test
    @DisplayName("생성한 토큰이 유효하다")
    void createdTokenIsValid() {
        // given
        String token = jwtTokenProvider.createToken(1L, "test@test.com", "CREATOR");

        // when
        boolean result = jwtTokenProvider.validateToken(token);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("잘못된 토큰은 유효하지 않다")
    void invalidTokenReturnsFalse() {
        // given
        String invalidToken = "invalid.token.here";

        // when
        boolean result = jwtTokenProvider.validateToken(invalidToken);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("빈 문자열은 유효하지 않다")
    void emptyTokenReturnsFalse() {
        // given
        String emptyToken = "";

        // when
        boolean result = jwtTokenProvider.validateToken(emptyToken);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("accountId를 정확히 추출한다")
    void extractsAccountId() {
        // given
        String token = jwtTokenProvider.createToken(42L, "test@test.com", "CLASSMATE");

        // when
        Long accountId = jwtTokenProvider.getAccountId(token);

        // then
        assertThat(accountId).isEqualTo(42L);
    }

    @Test
    @DisplayName("role을 정확히 추출한다")
    void extractsRole() {
        // given
        String token = jwtTokenProvider.createToken(1L, "test@test.com", "CREATOR");

        // when
        String role = jwtTokenProvider.getRole(token);

        // then
        assertThat(role).isEqualTo("CREATOR");
    }
}
