package com.liveklass.testa.domain.enrollment.application;

import com.liveklass.testa.domain.auth.domain.Account;
import com.liveklass.testa.domain.auth.repository.AccountRepository;
import com.liveklass.testa.domain.classmate.domain.Classmate;
import com.liveklass.testa.domain.classmate.repository.ClassmateRepository;
import com.liveklass.testa.domain.creator.domain.Creator;
import com.liveklass.testa.domain.creator.repository.CreatorRepository;
import com.liveklass.testa.domain.enrollment.domain.Enrollment;
import com.liveklass.testa.domain.enrollment.domain.EnrollmentStatus;
import com.liveklass.testa.domain.enrollment.exception.ClassNotOpenException;
import com.liveklass.testa.domain.enrollment.exception.DuplicateEnrollmentException;
import com.liveklass.testa.domain.enrollment.exception.EnrollmentAccessDeniedException;
import com.liveklass.testa.domain.enrollment.exception.EnrollmentNotFoundException;
import com.liveklass.testa.domain.enrollment.repository.EnrollmentRepository;
import com.liveklass.testa.domain.klass.domain.ClassStatus;
import com.liveklass.testa.domain.klass.domain.Klass;
import com.liveklass.testa.domain.klass.repository.KlassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ClassmateRepository classmateRepository;
    @Mock
    private CreatorRepository creatorRepository;
    @Mock
    private KlassRepository klassRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;

    private Account account;
    private Classmate classmate;
    private Classmate otherClassmate;
    private Klass klass;

    @BeforeEach
    void setUp() {
        account = Account.create("test@test.com", "encoded");
        ReflectionTestUtils.setField(account, "id", 1L);

        classmate = Classmate.create(account, "수강생");
        ReflectionTestUtils.setField(classmate, "id", 1L);

        otherClassmate = Classmate.create(null, "다른수강생");
        ReflectionTestUtils.setField(otherClassmate, "id", 2L);

        klass = Klass.create(null, "테스트강의", "설명", 10000, 2,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));
        ReflectionTestUtils.setField(klass, "id", 1L);
        klass.changeStatus(ClassStatus.OPEN);
    }

    @Nested
    @DisplayName("enroll()")
    class Enroll {

        @Test
        @DisplayName("정원 내 신청 시 PENDING 상태로 저장")
        void enrollWithinCapacity() {
            // given
            given(accountRepository.getReferenceById(1L)).willReturn(account);
            given(classmateRepository.findByAccount(account)).willReturn(Optional.of(classmate));
            given(klassRepository.findByIdWithLock(1L)).willReturn(Optional.of(klass));
            given(enrollmentRepository.existsByClassmateAndKlassAndStatusNot(
                    classmate, klass, EnrollmentStatus.CANCELLED)).willReturn(false);
            given(enrollmentRepository.countByKlassAndStatusIn(klass,
                    List.of(EnrollmentStatus.PENDING, EnrollmentStatus.CONFIRMED))).willReturn(0);

            // when
            enrollmentService.enroll(1L, 1L);

            // then
            ArgumentCaptor<Enrollment> captor = ArgumentCaptor.forClass(Enrollment.class);
            verify(enrollmentRepository).save(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(EnrollmentStatus.PENDING);
        }

        @Test
        @DisplayName("OPEN 아닌 강의 신청 시 예외")
        void enrollToNonOpenClassThrows() {
            // given
            Klass draftKlass = Klass.create(null, "Draft강의", "설명", 10000, 10,
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

            given(accountRepository.getReferenceById(1L)).willReturn(account);
            given(classmateRepository.findByAccount(account)).willReturn(Optional.of(classmate));
            given(klassRepository.findByIdWithLock(1L)).willReturn(Optional.of(draftKlass));

            // when & then
            assertThatThrownBy(() -> enrollmentService.enroll(1L, 1L))
                    .isInstanceOf(ClassNotOpenException.class);
        }

        @Test
        @DisplayName("중복 신청 시 예외")
        void duplicateEnrollmentThrows() {
            // given
            given(accountRepository.getReferenceById(1L)).willReturn(account);
            given(classmateRepository.findByAccount(account)).willReturn(Optional.of(classmate));
            given(klassRepository.findByIdWithLock(1L)).willReturn(Optional.of(klass));
            given(enrollmentRepository.existsByClassmateAndKlassAndStatusNot(
                    classmate, klass, EnrollmentStatus.CANCELLED)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> enrollmentService.enroll(1L, 1L))
                    .isInstanceOf(DuplicateEnrollmentException.class);
        }
    }

    @Nested
    @DisplayName("confirm()")
    class Confirm {

        @Test
        @DisplayName("본인 enrollment 확정 성공")
        void confirmOwnEnrollment() {
            // given
            Enrollment enrollment = Enrollment.create(classmate, klass, EnrollmentStatus.PENDING);
            ReflectionTestUtils.setField(enrollment, "id", 1L);

            given(accountRepository.getReferenceById(1L)).willReturn(account);
            given(classmateRepository.findByAccount(account)).willReturn(Optional.of(classmate));
            given(enrollmentRepository.findById(1L)).willReturn(Optional.of(enrollment));

            // when
            enrollmentService.confirm(1L, 1L);

            // then
            assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CONFIRMED);
        }

        @Test
        @DisplayName("타인의 enrollment 확정 시 예외")
        void confirmOtherEnrollmentThrows() {
            // given
            Enrollment enrollment = Enrollment.create(otherClassmate, klass, EnrollmentStatus.PENDING);
            ReflectionTestUtils.setField(enrollment, "id", 1L);

            given(accountRepository.getReferenceById(1L)).willReturn(account);
            given(classmateRepository.findByAccount(account)).willReturn(Optional.of(classmate));
            given(enrollmentRepository.findById(1L)).willReturn(Optional.of(enrollment));

            // when & then
            assertThatThrownBy(() -> enrollmentService.confirm(1L, 1L))
                    .isInstanceOf(EnrollmentAccessDeniedException.class);
        }

        @Test
        @DisplayName("존재하지 않는 enrollment 확정 시 예외")
        void confirmNonExistentThrows() {
            // given
            given(accountRepository.getReferenceById(1L)).willReturn(account);
            given(classmateRepository.findByAccount(account)).willReturn(Optional.of(classmate));
            given(enrollmentRepository.findById(999L)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> enrollmentService.confirm(1L, 999L))
                    .isInstanceOf(EnrollmentNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("cancel()")
    class Cancel {

        @Test
        @DisplayName("본인 enrollment 취소 성공")
        void cancelOwnEnrollment() {
            // given
            Enrollment enrollment = Enrollment.create(classmate, klass, EnrollmentStatus.PENDING);
            ReflectionTestUtils.setField(enrollment, "id", 1L);

            given(accountRepository.getReferenceById(1L)).willReturn(account);
            given(classmateRepository.findByAccount(account)).willReturn(Optional.of(classmate));
            given(enrollmentRepository.findById(1L)).willReturn(Optional.of(enrollment));
            given(enrollmentRepository.findFirstByKlassAndStatusOrderByEnrolledAtAsc(
                    klass, EnrollmentStatus.WAITLISTED)).willReturn(Optional.empty());

            // when
            enrollmentService.cancel(1L, 1L);

            // then
            assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
        }

        @Test
        @DisplayName("타인의 enrollment 취소 시 예외")
        void cancelOtherEnrollmentThrows() {
            // given
            Enrollment enrollment = Enrollment.create(otherClassmate, klass, EnrollmentStatus.PENDING);
            ReflectionTestUtils.setField(enrollment, "id", 1L);

            given(accountRepository.getReferenceById(1L)).willReturn(account);
            given(classmateRepository.findByAccount(account)).willReturn(Optional.of(classmate));
            given(enrollmentRepository.findById(1L)).willReturn(Optional.of(enrollment));

            // when & then
            assertThatThrownBy(() -> enrollmentService.cancel(1L, 1L))
                    .isInstanceOf(EnrollmentAccessDeniedException.class);
        }
    }
}
