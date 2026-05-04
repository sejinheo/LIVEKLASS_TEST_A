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
            Klass klass = Klass.create(null, "강의", "설명", 10000, 10,
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

            assertThat(klass.getStatus()).isEqualTo(ClassStatus.DRAFT);
        }

        @Test
        @DisplayName("가격이 음수이면 예외")
        void negativePriceThrows() {
            assertThatThrownBy(() -> Klass.create(null, "강의", "설명", -1, 10,
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)))
                    .isInstanceOf(InvalidKlassException.class);
        }

        @Test
        @DisplayName("정원이 0이면 예외")
        void zeroCapacityThrows() {
            assertThatThrownBy(() -> Klass.create(null, "강의", "설명", 10000, 0,
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)))
                    .isInstanceOf(InvalidKlassException.class);
        }

        @Test
        @DisplayName("종료일이 시작일보다 앞이면 예외")
        void endDateBeforeStartDateThrows() {
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
            Klass klass = createDefaultKlass();

            klass.changeStatus(ClassStatus.OPEN);

            assertThat(klass.getStatus()).isEqualTo(ClassStatus.OPEN);
        }

        @Test
        @DisplayName("OPEN → CLOSED 전이 성공")
        void openToClosed() {
            Klass klass = createDefaultKlass();
            klass.changeStatus(ClassStatus.OPEN);

            klass.changeStatus(ClassStatus.CLOSED);

            assertThat(klass.getStatus()).isEqualTo(ClassStatus.CLOSED);
        }

        @Test
        @DisplayName("DRAFT → CLOSED 전이 불가")
        void draftToClosedThrows() {
            Klass klass = createDefaultKlass();

            assertThatThrownBy(() -> klass.changeStatus(ClassStatus.CLOSED))
                    .isInstanceOf(InvalidStatusTransitionException.class);
        }

        @Test
        @DisplayName("CLOSED → OPEN 전이 불가")
        void closedToOpenThrows() {
            Klass klass = createDefaultKlass();
            klass.changeStatus(ClassStatus.OPEN);
            klass.changeStatus(ClassStatus.CLOSED);

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
            Klass klass = createDefaultKlass();
            klass.changeStatus(ClassStatus.OPEN);

            assertThat(klass.isOpen()).isTrue();
        }

        @Test
        @DisplayName("DRAFT 상태이면 false")
        void draftReturnsFalse() {
            Klass klass = createDefaultKlass();

            assertThat(klass.isOpen()).isFalse();
        }
    }

    private Klass createDefaultKlass() {
        return Klass.create(null, "강의", "설명", 10000, 10,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));
    }
}
