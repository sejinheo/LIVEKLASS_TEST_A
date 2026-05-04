package com.liveklass.testa.domain.enrollment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnrollmentStatusTest {

    @Nested
    @DisplayName("PENDING 상태")
    class PendingStatus {

        @Test
        @DisplayName("CONFIRMED로 전이 가능")
        void canTransitionToConfirmed() {
            // given
            EnrollmentStatus status = EnrollmentStatus.PENDING;

            // when
            boolean result = status.canTransitionTo(EnrollmentStatus.CONFIRMED);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("CANCELLED로 전이 가능")
        void canTransitionToCancelled() {
            // given
            EnrollmentStatus status = EnrollmentStatus.PENDING;

            // when
            boolean result = status.canTransitionTo(EnrollmentStatus.CANCELLED);

            // then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("CONFIRMED 상태")
    class ConfirmedStatus {

        @Test
        @DisplayName("CANCELLED로 전이 가능")
        void canTransitionToCancelled() {
            // given
            EnrollmentStatus status = EnrollmentStatus.CONFIRMED;

            // when
            boolean result = status.canTransitionTo(EnrollmentStatus.CANCELLED);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("PENDING으로 전이 불가")
        void cannotTransitionToPending() {
            // given
            EnrollmentStatus status = EnrollmentStatus.CONFIRMED;

            // when
            boolean result = status.canTransitionTo(EnrollmentStatus.PENDING);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("CANCELLED 상태")
    class CancelledStatus {

        @Test
        @DisplayName("PENDING으로 전이 불가")
        void cannotTransitionToPending() {
            // given
            EnrollmentStatus status = EnrollmentStatus.CANCELLED;

            // when
            boolean result = status.canTransitionTo(EnrollmentStatus.PENDING);

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("CONFIRMED로 전이 불가")
        void cannotTransitionToConfirmed() {
            // given
            EnrollmentStatus status = EnrollmentStatus.CANCELLED;

            // when
            boolean result = status.canTransitionTo(EnrollmentStatus.CONFIRMED);

            // then
            assertThat(result).isFalse();
        }
    }
}
