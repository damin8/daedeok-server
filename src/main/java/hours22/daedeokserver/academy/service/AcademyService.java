package hours22.daedeokserver.academy.service;

import hours22.daedeokserver.academy.domain.Academy;
import hours22.daedeokserver.academy.domain.AcademyRepository;
import hours22.daedeokserver.academy.dto.AcademyRequest;
import hours22.daedeokserver.academy.dto.AcademyResponse;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AcademyService {

    private final AcademyRepository academyRepository;

    @Transactional(readOnly = true)
    public AcademyResponse find() {
        Academy academy = academyRepository.findById(1L).orElse(null);

        return AcademyResponse.of(academy);
    }

    @Transactional
    public void update(AcademyRequest request) {
        Academy academy = academyRepository.findById(1L).orElse(null);

        if (academy == null)
            academy = new Academy(1L, "");

        academy.update(request);
    }

    @Transactional(readOnly = true)
    public AcademyResponse findEduvision() {
        Academy academy = academyRepository.findById(2L).orElse(null);

        return AcademyResponse.of(academy);
    }

    @Transactional
    public void updateEduvision(AcademyRequest request) {
        Academy academy = academyRepository.findById(2L).orElse(null);

        if (academy == null)
            academy = new Academy(2L, "");

        academy.update(request);
    }
}
