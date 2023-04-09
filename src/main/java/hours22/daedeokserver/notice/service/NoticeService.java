package hours22.daedeokserver.notice.service;

import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.BusinessException;
import hours22.daedeokserver.exception.business.EntityNotFoundException;
import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.file.service.FileService;
import hours22.daedeokserver.notice.domain.*;
import hours22.daedeokserver.notice.dto.*;
import hours22.daedeokserver.user.domain.Role;
import hours22.daedeokserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final TutorNoticeRepository tutorNoticeRepository;
    private final TutorNoticeCommentRepository tutorNoticeCommentRepository;
    private final TutorNoticeQueryDSL tutorNoticeQueryDSL;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public NoticeResponse.List find(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findAll(pageable);

        return new NoticeResponse.List(NoticeResponse.Summary.of(noticePage.getContent()), noticePage.getTotalElements(), noticePage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public NoticeResponse.List find(String keyword, Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findAllByTitleContains(keyword, pageable);

        return new NoticeResponse.List(NoticeResponse.Summary.of(noticePage.getContent()), noticePage.getTotalElements(), noticePage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public TutorNoticeResponse.List findTutorNotice(Pageable pageable) {
        Page<TutorNotice> noticePage = tutorNoticeRepository.findAll(pageable);

        return new TutorNoticeResponse.List(TutorNoticeResponse.Summary.of(noticePage.getContent()), noticePage.getTotalElements(), noticePage.getTotalPages());

    }

    @Transactional(readOnly = true)
    public TutorNoticeResponse.List findTutorNotice(String keyword, Pageable pageable) {
        Page<TutorNotice> noticePage = tutorNoticeRepository.findAllByTitleContains(keyword, pageable);

        return new TutorNoticeResponse.List(TutorNoticeResponse.Summary.of(noticePage.getContent()), noticePage.getTotalElements(), noticePage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public NoticeResponse find(Long id) {
        String parentId = CommonService.getParentId(FileType.GLOBAL_NOTICE, id);
        List<FileDTO> fileList = fileService.find(parentId);
        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        Notice after = noticeRepository.findFirstByIdGreaterThan(id).orElse(null);
        Page<Notice> beforePage = noticeRepository.findByIdLessThan(id, PageRequest.of(0, 1, Sort.by("id").descending()));
        List<Notice> beforeList = beforePage.getContent();
        Notice before = beforeList.size() == 0 ? null : beforeList.get(0);

        return NoticeResponse.of(notice, fileList, NoticeResponse.Summary.of(after), NoticeResponse.Summary.of(before));
    }

    @Transactional(readOnly = true)
    public TutorNoticeResponse findTutorNotice(Long id) {
        String parentId = CommonService.getParentId(FileType.TUTOR_NOTICE, id);
        List<FileDTO> fileList = fileService.find(parentId);
        TutorNotice notice = tutorNoticeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        TutorNotice after = tutorNoticeRepository.findFirstByIdGreaterThan(id).orElse(null);
        Page<TutorNotice> beforePage = tutorNoticeRepository.findByIdLessThan(id, PageRequest.of(0, 1, Sort.by("id").descending()));
        List<TutorNotice> beforeList = beforePage.getContent();
        TutorNotice before = beforeList.size() == 0 ? null : beforeList.get(0);
        List<TutorNoticeComment> commentList = tutorNoticeQueryDSL.findCommentList(notice);

        return TutorNoticeResponse.of(notice, TutorNoticeCommentResponse.of(commentList), fileList, TutorNoticeResponse.Summary.of(after), TutorNoticeResponse.Summary.of(before));
    }

    @Transactional
    public Long save(NoticeRequest noticeRequest) {
        fileService.uploadReal(CommonService.getUrl(noticeRequest.getContent(), FileType.GLOBAL_NOTICE));

        Long id = noticeRepository.save(noticeRequest.toNotice()).getId();
        String parentId = CommonService.getParentId(FileType.GLOBAL_NOTICE, id);
        fileService.save(parentId, noticeRequest.getAttachment_list(), FileType.GLOBAL_NOTICE);

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        noticeRepository.delete(notice);
        String parentId = CommonService.getParentId(FileType.GLOBAL_NOTICE, id);
        fileService.delete(parentId);
    }

    @Transactional
    public void update(Long id, NoticeRequest noticeRequest) {
        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        notice.update(noticeRequest);
        String parentId = CommonService.getParentId(FileType.GLOBAL_NOTICE, id);
        fileService.delete(parentId);
        fileService.save(parentId, noticeRequest.getAttachment_list(), FileType.GLOBAL_NOTICE);
    }

    @Transactional
    public Long save(User user, TutorNoticeRequest request) {
        fileService.uploadReal(CommonService.getUrl(request.getContent(), FileType.TUTOR_NOTICE));

        Long id = tutorNoticeRepository.save(request.toTutorNotice(user)).getId();
        String parentId = CommonService.getParentId(FileType.TUTOR_NOTICE, id);
        fileService.save(parentId, request.getAttachment_list(), FileType.TUTOR_NOTICE);

        return id;
    }

    @Transactional
    public void deleteTutorNotice(User user, Long id) {

        TutorNotice notice = tutorNoticeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (!Objects.equals(user.getId(), notice.getUser().getId()) && !user.getRole().equals(Role.ROLE_ADMIN))
            throw new BusinessException(ErrorCode.NO_AUTH);

        tutorNoticeRepository.delete(notice);
        String parentId = CommonService.getParentId(FileType.TUTOR_NOTICE, id);
        fileService.delete(parentId);
    }

    @Transactional
    public void update(User user, Long id, TutorNoticeRequest request) {
        TutorNotice notice = tutorNoticeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (!Objects.equals(user.getId(), notice.getUser().getId()) && !user.getRole().equals(Role.ROLE_ADMIN))
            throw new BusinessException(ErrorCode.NO_AUTH);

        notice.update(request);
        String parentId = CommonService.getParentId(FileType.TUTOR_NOTICE, id);
        fileService.delete(parentId);
        fileService.save(parentId, request.getAttachment_list(), FileType.TUTOR_NOTICE);
    }

    @Transactional
    public Long save(User user, Long id, TutorNoticeCommentRequest request) {
        TutorNotice notice = tutorNoticeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        TutorNoticeComment tutorNoticeComment = request.toTutorNoticeComment(user, notice);

        if (request.getParent_id() != null) {
            TutorNoticeComment parent = tutorNoticeCommentRepository.findById(request.getParent_id()).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
            tutorNoticeComment.setParent(parent);
        }

        return tutorNoticeCommentRepository.save(tutorNoticeComment).getId();
    }

    @Transactional
    public void deleteTutorNoticeComment(User user, Long id) {

        TutorNoticeComment comment = tutorNoticeCommentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (!Objects.equals(user.getId(), comment.getAuthor().getId()) && !user.getRole().equals(Role.ROLE_ADMIN))
            throw new BusinessException(ErrorCode.NO_AUTH);

        tutorNoticeCommentRepository.delete(comment);
    }

    @Transactional
    public void updateComment(User user, Long id, TutorNoticeCommentRequest request) {
        TutorNoticeComment comment = tutorNoticeCommentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (!Objects.equals(user.getId(), comment.getAuthor().getId()) && !user.getRole().equals(Role.ROLE_ADMIN))
            throw new BusinessException(ErrorCode.NO_AUTH);

        comment.update(request);
    }
}
