package com.liveklass.testa.domain.klass.application;

import com.liveklass.testa.domain.auth.domain.Account;
import com.liveklass.testa.domain.auth.repository.AccountRepository;
import com.liveklass.testa.domain.creator.domain.Creator;
import com.liveklass.testa.domain.creator.repository.CreatorRepository;
import com.liveklass.testa.domain.klass.controller.dto.KlassCreateRequest;
import com.liveklass.testa.domain.klass.controller.dto.KlassDetailResponse;
import com.liveklass.testa.domain.klass.controller.dto.KlassResponse;
import com.liveklass.testa.domain.klass.controller.dto.KlassStatusUpdateRequest;
import com.liveklass.testa.domain.klass.domain.ClassStatus;
import com.liveklass.testa.domain.klass.domain.Klass;
import com.liveklass.testa.domain.klass.exception.CreatorNotFoundException;
import com.liveklass.testa.domain.klass.exception.KlassAccessDeniedException;
import com.liveklass.testa.domain.klass.exception.KlassNotFoundException;
import com.liveklass.testa.domain.klass.repository.KlassRepository;
import com.liveklass.testa.domain.enrollment.domain.EnrollmentStatus;
import com.liveklass.testa.domain.enrollment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KlassService implements KlassUseCase {

    private final AccountRepository accountRepository;
    private final CreatorRepository creatorRepository;
    private final KlassRepository klassRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public void create(Long accountId, KlassCreateRequest request) {
        Account account = accountRepository.getReferenceById(accountId);
        Creator creator = creatorRepository.findByAccount(account)
                .orElseThrow(CreatorNotFoundException::new);

        Klass klass = Klass.create(
                creator,
                request.title(),
                request.description(),
                request.price(),
                request.maxCapacity(),
                request.startDate(),
                request.endDate()
        );

        klassRepository.save(klass);
    }

    @Override
    @Transactional
    public void updateStatus(Long accountId, Long classId, KlassStatusUpdateRequest request) {
        Account account = accountRepository.getReferenceById(accountId);
        Creator creator = creatorRepository.findByAccount(account)
                .orElseThrow(CreatorNotFoundException::new);

        Klass klass = klassRepository.findById(classId)
                .orElseThrow(KlassNotFoundException::new);

        if (!klass.isOwnedBy(creator)) {
            throw new KlassAccessDeniedException();
        }

        klass.changeStatus(request.status());
    }

    @Override
    @Transactional(readOnly = true)
    public List<KlassResponse> findAll(ClassStatus status) {
        List<Klass> classes;
        if (status != null) {
            classes = klassRepository.findByStatus(status);
        } else {
            classes = klassRepository.findAll();
        }

        return classes.stream()
                .map(KlassResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public KlassDetailResponse findById(Long classId) {
        Klass klass = klassRepository.findById(classId)
                .orElseThrow(KlassNotFoundException::new);

        int currentEnrollment = enrollmentRepository.countByKlassAndStatusIn(klass,
                List.of(EnrollmentStatus.PENDING, EnrollmentStatus.CONFIRMED));

        return KlassDetailResponse.of(klass, currentEnrollment);
    }
}
