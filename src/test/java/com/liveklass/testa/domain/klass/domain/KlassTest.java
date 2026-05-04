package com.liveklass.testa.domain.klass.domain;

import com.liveklass.testa.domain.klass.exception.InvalidKlassException;
import com.liveklass.testa.domain.klass.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KlassTest {

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("DRAFT 상태로 생성된다")
        void createWithDraftStatus() {
            // given & when
            Klass klass = Klass.create(null, "강의", "설명", 10000, 10,
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

            // then
            assertThat(klass.getStatus()).isEqualTo(ClassStatus.DRAFT);
        }

        @Test
        @DisplayName("가격이 음수이면 예외")
        void negativePriceThrows() {
            // given & when & then
            assertThatThrownBy(() -> Klass.create(null, "강의", "설명", -1, 10,
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)))
                    .isInstanceOf(InvalidKlassException.class);
        }

        @Test
        @DisplayName("정원이 0이면 예외")
        void zeroCapacityThrows() {
            // given & when & then
            assertThatThrownBy(() -> Klass.create(null, "강의", "설명", 10000, 0,
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)))
                    .isInstanceOf(InvalidKlassException.class);
        }

        @Test
        @DisplayName("종료일이 시작일보다 앞이면 예외")
        void endDateBeforeStartDateThrows() {
            // given & when & then
            assertThatThrownBy(() -> Klass.create(null, "강의", "설명", 10000, 10,
                    LocalDate.of(2026, 6, 30), LocalDate.of(2026, 6, 1)))
                    .isInstanceOf(InvalidKlassException.class);
        }
    }

    @Nested
    @DisplayName("changeStatus()")
    class ChangeStatus {

        @Test
        @DisplayName("DRAFT → OPEN 전이 성공")
        void draftToOpen() {
            // given
            Klass klass = createDefaultKlass();

            // when
            klass.changeStatus(ClassStatus.OPEN);

            // then
            assertThat(klass.getStatus()).isEqualTo(ClassStatus.OPEN);
        }

        @Test
        @DisplayName("OPEN → CLOSED 전이 성공")
        void openToClosed() {
            // given
            Klass klass = createDefaultKlass();
            klass.changeStatus(ClassStatus.OPEN);

            // when
            klass.changeStatus(ClassStatus.CLOSED);

            // then
            assertThat(klass.getStatus()).isEqualTo(ClassStatus.CLOSED);
        }

        @Test
        @DisplayName("DRAFT → CLOSED 전이 불가")
        void draftToClosedThrows() {
            // given
            Klass klass = createDefaultKlass();

            // when & then
            assertThatThrownBy(() -> klass.changeStatus(ClassStatus.CLOSED))
                    .isInstanceOf(InvalidStatusTransitionException.class);
        }

        @Test
        @DisplayName("CLOSED → OPEN 전이 불가")
        void closedToOpenThrows() {
            // given
            Klass klass = createDefaultKlass();
            klass.changeStatus(ClassStatus.OPEN);
            klass.changeStatus(ClassStatus.CLOSED);

            // when & then
            assertThatThrownBy(() -> klass.changeStatus(ClassStatus.OPEN))
                    .isInstanceOf(InvalidStatusTransitionException.class);
        }
    }

    @Nested
    @DisplayName("isOpen()")
    class IsOpen {

        @Test
        @DisplayName("OPEN 상태이면 true")
        void openReturnsTrue() {
            // given
            Klass klass = createDefaultKlass();
            klass.changeStatus(ClassStatus.OPEN);

            // when
            boolean result = klass.isOpen();

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("DRAFT 상태이면 false")
        void draftReturnsFalse() {
            // given
            Klass klass = createDefaultKlass();

            // when
            boolean result = klass.isOpen();

            // then
            assertThat(result).isFalse();
        }
    }

    private Klass createDefaultKlass() {
        return Klass.create(null, "강의", "설명", 10000, 10,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));
    }
}
