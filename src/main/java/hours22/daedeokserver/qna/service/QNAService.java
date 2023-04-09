package hours22.daedeokserver.qna.service;

import hours22.daedeokserver.category.domain.QNACategory;
import hours22.daedeokserver.category.service.CategoryService;
import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.BusinessException;
import hours22.daedeokserver.exception.business.EntityNotFoundException;
import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.file.service.FileService;
import hours22.daedeokserver.qna.domain.*;
import hours22.daedeokserver.qna.dto.QNACommentRequest;
import hours22.daedeokserver.qna.dto.QNACommentResponse;
import hours22.daedeokserver.qna.dto.QNARequest;
import hours22.daedeokserver.qna.dto.QNAResponse;
import hours22.daedeokserver.user.domain.Role;
import hours22.daedeokserver.user.domain.User;
import hours22.daedeokserver.user.util.RedisUtil;
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
public class QNAService {

    private final QNARepository qnaRepository;
    private final QNACommentRepository qnaCommentRepository;
    private final QNAQueryDSL qnaQueryDSL;
    private final RedisUtil redis;
    private final CategoryService categoryService;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public QNAResponse.List find(Pageable pageable) {
        Page<QNA> qnaPage = qnaRepository.findAll(pageable);

        return new QNAResponse.List(QNAResponse.Summary.of(qnaPage.getContent()), qnaPage.getTotalElements(), qnaPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public QNAResponse.List find(String keyword, Pageable pageable) {
        Page<QNA> qnaPage = qnaRepository.findAllByTitleContains(keyword, pageable);

        return new QNAResponse.List(QNAResponse.Summary.of(qnaPage.getContent()), qnaPage.getTotalElements(), qnaPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public QNAResponse.List findCategory(String category, Pageable pageable) {
        QNACategory entity = categoryService.findQNACategory(category);
        Page<QNA> qnaPage = qnaRepository.findAllByCategory(entity, pageable);

        return new QNAResponse.List(QNAResponse.Summary.of(qnaPage.getContent()), qnaPage.getTotalElements(), qnaPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public QNAResponse.List findCategory(String category, String keyword, Pageable pageable) {
        QNACategory entity = categoryService.findQNACategory(category);
        Page<QNA> qnaPage = qnaRepository.findAllByCategoryAndTitleContains(entity, keyword, pageable);

        return new QNAResponse.List(QNAResponse.Summary.of(qnaPage.getContent()), qnaPage.getTotalElements(), qnaPage.getTotalPages());
    }

    @Transactional
    public QNAResponse find(User user, Long id) {
        QNA qna = qnaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        String parentId = CommonService.getParentId(FileType.QNA, id);
        List<FileDTO> fileList = fileService.find(parentId);

        if (qna.isSecret()) {
            if (user == null || (user.getRole() != Role.ROLE_ADMIN && !Objects.equals(qna.getAuthor().getId(), user.getId())))
                throw new BusinessException(ErrorCode.NO_AUTH);
        }

        QNA after = qnaRepository.findFirstByIdGreaterThan(id).orElse(null);
        Page<QNA> beforePage = qnaRepository.findByIdLessThan(id, PageRequest.of(0, 1, Sort.by("id").descending()));
        List<QNA> beforeList = beforePage.getContent();
        QNA before = beforeList.size() == 0 ? null : beforeList.get(0);
        List<QNAComment> commentList = qnaQueryDSL.findCommentList(qna);

        if (user != null) {
            String viewKey = "qna_view_user_" + user.getId() + "_qna_" + id;
            if (redis.getData(viewKey) == null) {
                qna.increaseView();
                redis.setDataExpire(viewKey, viewKey, 60 * 60 * 24);
            }
        }

        return QNAResponse.of(qna, QNACommentResponse.of(commentList), fileList, QNAResponse.Summary.of(after), QNAResponse.Summary.of(before));
    }

    @Transactional
    public Long save(User user, QNARequest qnaRequest) {
        QNACategory category = categoryService.findQNACategory(qnaRequest.getCategory());
        fileService.uploadReal(CommonService.getUrl(qnaRequest.getContent(), FileType.QNA));

        Long id= qnaRepository.save(qnaRequest.toQNA(user, category)).getId();
        String parentId = CommonService.getParentId(FileType.QNA, id);
        fileService.save(parentId, qnaRequest.getAttachment_list(), FileType.QNA);

        return id;
    }

    @Transactional
    public void delete(User user, Long id) {
        QNA qna = qnaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (!Objects.equals(user.getId(), qna.getAuthor().getId()) && !user.getRole().equals(Role.ROLE_ADMIN))
            throw new BusinessException(ErrorCode.NO_AUTH);

        qnaRepository.delete(qna);
        String parentId = CommonService.getParentId(FileType.QNA, id);
        fileService.delete(parentId);
    }

    @Transactional
    public void update(User user, Long id, QNARequest qnaRequest) {
        QNA qna = qnaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (!Objects.equals(user.getId(), qna.getAuthor().getId()) && !user.getRole().equals(Role.ROLE_ADMIN))
            throw new BusinessException(ErrorCode.NO_AUTH);

        String parentId = CommonService.getParentId(FileType.QNA, id);
        fileService.delete(parentId);
        fileService.save(parentId, qnaRequest.getAttachment_list(), FileType.QNA);
        QNACategory category = categoryService.findQNACategory(qnaRequest.getCategory());
        qna.update(qnaRequest, category);
    }

    @Transactional
    public Long save(User user, Long id, QNACommentRequest commentRequest) {
        QNA qna = qnaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        QNAComment comment = commentRequest.toComment(user, qna);

        if (qna.isSecret()) {
            if (user.getRole() != Role.ROLE_ADMIN && !Objects.equals(qna.getAuthor().getId(), user.getId()))
                throw new BusinessException(ErrorCode.NO_AUTH);
        }

        if (commentRequest.getParent_id() != null) {
            QNAComment parent = qnaCommentRepository.findById(commentRequest.getParent_id()).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
            comment.setParent(parent);
        }

        return qnaCommentRepository.save(comment).getId();
    }

    @Transactional
    public void deleteComment(User user, Long id) {
        QNAComment comment = qnaCommentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (user.getRole() != Role.ROLE_ADMIN && !Objects.equals(comment.getAuthor().getId(), user.getId()))
            throw new BusinessException(ErrorCode.NO_AUTH);

        qnaCommentRepository.delete(comment);
    }

    @Transactional
    public void updateComment(User user, Long id, QNACommentRequest commentRequest) {
        QNAComment comment = qnaCommentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (user.getRole() != Role.ROLE_ADMIN && !Objects.equals(comment.getAuthor().getId(), user.getId()))
            throw new BusinessException(ErrorCode.NO_AUTH);

        comment.update(commentRequest);
    }
}
