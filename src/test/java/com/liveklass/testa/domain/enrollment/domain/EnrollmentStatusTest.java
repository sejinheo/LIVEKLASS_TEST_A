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
            assertThat(EnrollmentStatus.PENDING.canTransitionTo(EnrollmentStatus.CONFIRMED)).isTrue();
        }

        @Test
        @DisplayName("CANCELLED로 전이 가능")
        void canTransitionToCancelled() {
            assertThat(EnrollmentStatus.PENDING.canTransitionTo(EnrollmentStatus.CANCELLED)).isTrue();
        }
    }

    @Nested
    @DisplayName("CONFIRMED 상태")
    class ConfirmedStatus {

        @Test
        @DisplayName("CANCELLED로 전이 가능")
        void canTransitionToCancelled() {
            assertThat(EnrollmentStatus.CONFIRMED.canTransitionTo(EnrollmentStatus.CANCELLED)).isTrue();
        }

        @Test
        @DisplayName("PENDING으로 전이 불가")
        void cannotTransitionToPending() {
            assertThat(EnrollmentStatus.CONFIRMED.canTransitionTo(EnrollmentStatus.PENDING)).isFalse();
        }
    }

    @Nested
    @DisplayName("CANCELLED 상태")
    class CancelledStatus {

        @Test
        @DisplayName("PENDING으로 전이 불가")
        void cannotTransitionToPending() {
            assertThat(EnrollmentStatus.CANCELLED.canTransitionTo(EnrollmentStatus.PENDING)).isFalse();
        }

        @Test
        @DisplayName("CONFIRMED로 전이 불가")
        void cannotTransitionToConfirmed() {
            assertThat(EnrollmentStatus.CANCELLED.canTransitionTo(EnrollmentStatus.CONFIRMED)).isFalse();
        }
    }
}
