package com.liveklass.testa.domain.enrollment.domain;

import com.liveklass.testa.domain.classmate.domain.Classmate;
import com.liveklass.testa.domain.enrollment.exception.CancellationPeriodExpiredException;
import com.liveklass.testa.domain.enrollment.exception.InvalidEnrollmentStatusException;
import com.liveklass.testa.domain.klass.domain.Klass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnrollmentTest {

    private Classmate classmate;
    private Classmate otherClassmate;
    private Klass klass;

    @BeforeEach
    void setUp() {
        classmate = Classmate.create(null, "수강생");
        ReflectionTestUtils.setField(classmate, "id", 1L);

        otherClassmate = Classmate.create(null, "다른수강생");
        ReflectionTestUtils.setField(otherClassmate, "id", 2L);

        klass = Klass.create(null, "테스트강의", "설명", 10000, 10,
                java.time.LocalDate.of(2026, 6, 1), java.time.LocalDate.of(2026, 6, 30));
    }

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("PENDING 상태로 생성된다")
        void createWithPendingStatus() {
            Enrollment enrollment = Enrollment.create(classmate, klass);

            assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.PENDING);
            assertThat(enrollment.getClassmate()).isEqualTo(classmate);
            assertThat(enrollment.getKlass()).isEqualTo(klass);
            assertThat(enrollment.getEnrolledAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("confirm()")
    class Confirm {

        @Test
        @DisplayName("PENDING → CONFIRMED 전이 성공")
        void confirmFromPending() {
            Enrollment enrollment = Enrollment.create(classmate, klass);

            enrollment.confirm();

            assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CONFIRMED);
            assertThat(enrollment.getConfirmedAt()).isNotNull();
        }

        @Test
        @DisplayName("CANCELLED 상태에서 confirm 시 예외")
        void confirmFromCancelledThrows() {
            Enrollment enrollment = Enrollment.create(classmate, klass);
            enrollment.cancel();

            assertThatThrownBy(enrollment::confirm)
                    .isInstanceOf(InvalidEnrollmentStatusException.class);
        }
    }

    @Nested
    @DisplayName("cancel()")
    class Cancel {

        @Test
        @DisplayName("PENDING → CANCELLED 전이 성공")
        void cancelFromPending() {
            Enrollment enrollment = Enrollment.create(classmate, klass);

            enrollment.cancel();

            assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
            assertThat(enrollment.getCancelledAt()).isNotNull();
        }

        @Test
        @DisplayName("CONFIRMED → CANCELLED 전이 성공 (7일 이내)")
        void cancelFromConfirmedWithinPeriod() {
            Enrollment enrollment = Enrollment.create(classmate, klass);
            enrollment.confirm();

            enrollment.cancel();

            assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
        }

        @Test
        @DisplayName("CONFIRMED 후 7일 초과 시 취소 불가")
        void cancelFromConfirmedAfterPeriodThrows() {
            Enrollment enrollment = Enrollment.create(classmate, klass);
            enrollment.confirm();
            ReflectionTestUtils.setField(enrollment, "confirmedAt",
                    LocalDateTime.now().minusDays(8));

            assertThatThrownBy(enrollment::cancel)
                    .isInstanceOf(CancellationPeriodExpiredException.class);
        }

        @Test
        @DisplayName("이미 CANCELLED 상태에서 cancel 시 예외")
        void cancelFromCancelledThrows() {
            Enrollment enrollment = Enrollment.create(classmate, klass);
            enrollment.cancel();

            assertThatThrownBy(enrollment::cancel)
                    .isInstanceOf(InvalidEnrollmentStatusException.class);
        }
    }

    @Nested
    @DisplayName("isOwnedBy()")
    class IsOwnedBy {

        @Test
        @DisplayName("본인 enrollment이면 true")
        void ownedByOwner() {
            Enrollment enrollment = Enrollment.create(classmate, klass);

            assertThat(enrollment.isOwnedBy(classmate)).isTrue();
        }

        @Test
        @DisplayName("타인이면 false")
        void notOwnedByOther() {
            Enrollment enrollment = Enrollment.create(classmate, klass);

            assertThat(enrollment.isOwnedBy(otherClassmate)).isFalse();
        }
    }
}
