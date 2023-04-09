package hours22.daedeokserver.guide.service;

import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.EntityNotFoundException;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.file.service.FileService;
import hours22.daedeokserver.guide.domain.Guide;
import hours22.daedeokserver.guide.domain.GuideRepository;
import hours22.daedeokserver.guide.dto.GuideRequest;
import hours22.daedeokserver.guide.dto.GuideResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuideService {

    private final GuideRepository guideRepository;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public GuideResponse.List find(Pageable pageable) {
        Page<Guide> guidePage = guideRepository.findAll(pageable);

        return new GuideResponse.List(GuideResponse.Summary.of(guidePage.getContent()), guidePage.getTotalElements(), guidePage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public GuideResponse.List find(String keyword, Pageable pageable) {
        Page<Guide> guidePage = guideRepository.findAllByTitleContains(keyword, pageable);

        return new GuideResponse.List(GuideResponse.Summary.of(guidePage.getContent()), guidePage.getTotalElements(), guidePage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public GuideResponse find(Long id) {
        Guide guide = guideRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.GUIDE_NOT_FOUND));
        Guide after = guideRepository.findFirstByIdGreaterThan(id).orElse(null);
        Page<Guide> beforePage = guideRepository.findByIdLessThan(id, PageRequest.of(0, 1, Sort.by("id").descending()));
        List<Guide> beforeList = beforePage.getContent();
        Guide before = beforeList.size() == 0 ? null : beforeList.get(0);

        return GuideResponse.of(guide, GuideResponse.Summary.of(after), GuideResponse.Summary.of(before));
    }

    @Transactional
    public Long save(GuideRequest request) {
        fileService.uploadReal(CommonService.getUrl(request.getContent(), FileType.AC_INFO));

        return guideRepository.save(request.toEntity()).getId();
    }

    @Transactional
    public void update(Long id, GuideRequest request) {
        Guide guide = guideRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.GUIDE_NOT_FOUND));
        guide.update(request);
    }

    @Transactional
    public void delete(Long id) {
        guideRepository.deleteById(id);
    }
}
