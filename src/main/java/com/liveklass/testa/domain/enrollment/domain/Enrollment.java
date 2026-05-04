package com.liveklass.testa.domain.enrollment.domain;

import com.liveklass.testa.domain.classmate.domain.Classmate;
import com.liveklass.testa.domain.enrollment.exception.InvalidEnrollmentStatusException;
import com.liveklass.testa.domain.klass.domain.Klass;
import com.liveklass.testa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrollment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classmate_id", nullable = false)
    private Classmate classmate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Klass klass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status;

    @Column(nullable = false)
    private LocalDateTime enrolledAt;

    private LocalDateTime confirmedAt;

    private LocalDateTime cancelledAt;

    public static Enrollment create(Classmate classmate, Klass klass) {
        Enrollment enrollment = new Enrollment();
        enrollment.classmate = classmate;
        enrollment.klass = klass;
        enrollment.status = EnrollmentStatus.PENDING;
        enrollment.enrolledAt = LocalDateTime.now();
        return enrollment;
    }

    public void confirm() {
        validateStatusTransition(EnrollmentStatus.CONFIRMED);
        this.status = EnrollmentStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
    }

    public void cancel() {
        validateStatusTransition(EnrollmentStatus.CANCELLED);
        this.status = EnrollmentStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    public boolean isOwnedBy(Classmate classmate) {
        return this.classmate.getId().equals(classmate.getId());
    }

    private void validateStatusTransition(EnrollmentStatus target) {
        if (!this.status.canTransitionTo(target)) {
            throw new InvalidEnrollmentStatusException(
                    this.status + "에서 " + target + "(으)로 변경할 수 없습니다.");
        }
    }
}
