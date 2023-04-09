package hours22.daedeokserver.faq.service;

import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.EntityNotFoundException;
import hours22.daedeokserver.faq.domain.FAQ;
import hours22.daedeokserver.faq.domain.FAQRepository;
import hours22.daedeokserver.faq.dto.FAQRequest;
import hours22.daedeokserver.faq.dto.FAQResponse;
import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.file.service.FileService;
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
public class FAQService {

    private final FAQRepository faqRepository;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public FAQResponse.List find(Pageable pageable) {
        Page<FAQ> faqPage = faqRepository.findAll(pageable);

        return new FAQResponse.List(FAQResponse.Summary.of(faqPage.getContent()), faqPage.getTotalElements(), faqPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public FAQResponse.List find(String keyword, Pageable pageable) {
        Page<FAQ> faqPage = faqRepository.findAllByTitleContains(keyword, pageable);

        return new FAQResponse.List(FAQResponse.Summary.of(faqPage.getContent()), faqPage.getTotalElements(), faqPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public FAQResponse find(Long id) {
        String parentId = CommonService.getParentId(FileType.FAQ, id);
        List<FileDTO> fileList = fileService.find(parentId);
        FAQ faq = faqRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.FAQ_NOT_FOUND));
        FAQ after = faqRepository.findFirstByIdGreaterThan(id).orElse(null);
        Page<FAQ> beforePage = faqRepository.findByIdLessThan(id, PageRequest.of(0, 1, Sort.by("id").descending()));
        List<FAQ> beforeList = beforePage.getContent();
        FAQ before = beforeList.size() == 0 ? null : beforeList.get(0);

        return FAQResponse.of(faq, fileList, FAQResponse.Summary.of(after), FAQResponse.Summary.of(before));
    }

    @Transactional
    public Long save(FAQRequest faqRequest) {
        fileService.uploadReal(CommonService.getUrl(faqRequest.getContent(), FileType.FAQ));

        Long id = faqRepository.save(faqRequest.toFAQ()).getId();
        String parentId = CommonService.getParentId(FileType.FAQ, id);
        fileService.save(parentId, faqRequest.getAttachment_list(), FileType.FAQ);

        return id;
    }

    @Transactional
    public void update(Long id, FAQRequest faqRequest) {
        FAQ faq = faqRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.FAQ_NOT_FOUND));
        String parentId = CommonService.getParentId(FileType.FAQ, id);
        fileService.delete(parentId);
        fileService.save(parentId, faqRequest.getAttachment_list(), FileType.FAQ);
        faq.update(faqRequest);
    }

    @Transactional
    public void delete(Long id) {
        faqRepository.deleteById(id);
        String parentId = CommonService.getParentId(FileType.FAQ, id);
        fileService.delete(parentId);
    }
}
