package com.liveklass.testa.domain.klass.domain;

import com.liveklass.testa.domain.creator.domain.Creator;
import com.liveklass.testa.domain.klass.exception.InvalidKlassException;
import com.liveklass.testa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "classes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Klass extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Creator creator;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassStatus status;

    public static Klass create(Creator creator, String title, String description,
                               int price, int maxCapacity, LocalDate startDate, LocalDate endDate) {
        validatePrice(price);
        validateMaxCapacity(maxCapacity);
        validateDateRange(startDate, endDate);

        Klass klass = new Klass();
        klass.creator = creator;
        klass.title = title;
        klass.description = description;
        klass.price = price;
        klass.maxCapacity = maxCapacity;
        klass.startDate = startDate;
        klass.endDate = endDate;
        klass.status = ClassStatus.DRAFT;
        return klass;
    }

    private static void validatePrice(int price) {
        if (price < 0) {
            throw new InvalidKlassException("가격은 0 이상이어야 합니다.");
        }
    }

    private static void validateMaxCapacity(int maxCapacity) {
        if (maxCapacity < 1) {
            throw new InvalidKlassException("최대 정원은 1명 이상이어야 합니다.");
        }
    }

    private static void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new InvalidKlassException("종료일은 시작일 이후여야 합니다.");
        }
    }

    public void changeStatus(ClassStatus target) {
        if (!this.status.canTransitionTo(target)) {
            throw new IllegalStateException(
                    this.status + "에서 " + target + "(으)로 변경할 수 없습니다.");
        }
        this.status = target;
    }

    public boolean isOwnedBy(Creator creator) {
        return this.creator.getId().equals(creator.getId());
    }
}
