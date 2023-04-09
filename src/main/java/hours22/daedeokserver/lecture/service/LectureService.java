package hours22.daedeokserver.lecture.service;

import hours22.daedeokserver.category.domain.BoardCategory;
import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.category.service.CategoryService;
import hours22.daedeokserver.common.dto.LocalDateTimeDTO;
import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.division.domain.Division;
import hours22.daedeokserver.division.dto.DivisionDTO;
import hours22.daedeokserver.division.service.DivisionService;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.BusinessException;
import hours22.daedeokserver.exception.business.EntityNotFoundException;
import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.file.service.FileService;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.domain.board.*;
import hours22.daedeokserver.lecture.domain.handout.Handout;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.lecture.domain.lecture.LectureQueryDSL;
import hours22.daedeokserver.lecture.domain.lecture.LectureRepository;
import hours22.daedeokserver.lecture.domain.lecture.LectureUser;
import hours22.daedeokserver.lecture.domain.plan.Plan;
import hours22.daedeokserver.lecture.domain.plan.PlanUser;
import hours22.daedeokserver.lecture.dto.board.BoardCommentRequest;
import hours22.daedeokserver.lecture.dto.board.BoardCommentResponse;
import hours22.daedeokserver.lecture.dto.board.BoardRequest;
import hours22.daedeokserver.lecture.dto.board.BoardResponse;
import hours22.daedeokserver.lecture.dto.handout.HandoutRequest;
import hours22.daedeokserver.lecture.dto.handout.HandoutResponse;
import hours22.daedeokserver.lecture.dto.lecture.LectureDTO;
import hours22.daedeokserver.lecture.dto.lecture.LectureRequest;
import hours22.daedeokserver.lecture.dto.lecture.LectureResponse;
import hours22.daedeokserver.lecture.dto.lecture.MainResponse;
import hours22.daedeokserver.lecture.dto.plan.PlanRequest;
import hours22.daedeokserver.lecture.dto.plan.PlanResponse;
import hours22.daedeokserver.notice.domain.TutorNotice;
import hours22.daedeokserver.notice.domain.TutorNoticeRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureUserService lectureUserService;
    private final PlanService planService;
    private final FileService fileService;
    private final CategoryService categoryService;
    private final DivisionService divisionService;
    private final LectureRepository lectureRepository;
    private final LectureQueryDSL lectureQueryDSL;
    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final TutorNoticeRepository tutorNoticeRepository;
    private final BoardCommentQueryDSL boardCommentQueryDSL;
    private final RedisUtil redis;

    @Transactional(readOnly = true)
    public LectureResponse.List find(Status status, Pageable pageable) {
        Page<Lecture> lecturePage = lectureRepository.findAllByStatus(status, pageable);

        return LectureResponse.List.of(lectureMapping(lecturePage), lecturePage);
    }

    @Transactional(readOnly = true)
    public LectureResponse.List find(Status status, String keyword, Pageable pageable) {
        Page<Lecture> lecturePage = lectureRepository.findAllByStatusAndTitleContains(status, keyword, pageable);

        return LectureResponse.List.of(lectureMapping(lecturePage), lecturePage);
    }

    @Transactional(readOnly = true)
    public LectureResponse.List findFinish(User user, Pageable pageable) {
        Page<Lecture> lecturePage = lectureRepository.findAllByTutorAndStatus(user, Status.FINISH, pageable);

        return LectureResponse.List.of(lectureMapping(lecturePage), lecturePage);
    }

    @Transactional(readOnly = true)
    public LectureResponse.List findFinish(User user, String keyword, Pageable pageable) {
        Page<Lecture> lecturePage = lectureRepository.findAllByTutorAndStatusAndTitleContains(user, Status.FINISH, keyword, pageable);

        return LectureResponse.List.of(lectureMapping(lecturePage), lecturePage);
    }

    @Transactional(readOnly = true)
    public LectureResponse.List findCategory(Status status, String category, Pageable pageable) {
        Category entity = categoryService.findByCategory(category);
        Page<Lecture> lecturePage = lectureRepository.findAllByStatusAndCategory(status, entity, pageable);

        return LectureResponse.List.of(lectureMapping(lecturePage), lecturePage);
    }

    @Transactional(readOnly = true)
    public LectureResponse.List findCategory(Status status, String category, String keyword, Pageable pageable) {
        Category entity = categoryService.findByCategory(category);
        Page<Lecture> lecturePage = lectureRepository.findAllByStatusAndCategoryAndTitleContains(status, entity, keyword, pageable);

        return LectureResponse.List.of(lectureMapping(lecturePage), lecturePage);
    }

    @Transactional(readOnly = true)
    public LectureResponse.List findFinishCategory(User user, String category, Pageable pageable) {
        Category entity = categoryService.findByCategory(category);
        Page<Lecture> lecturePage = lectureRepository.findAllByTutorAndStatusAndCategory(user, Status.FINISH, entity, pageable);

        return LectureResponse.List.of(lectureMapping(lecturePage), lecturePage);
    }

    @Transactional(readOnly = true)
    public LectureResponse.List findFinishCategory(User user, String category, String keyword, Pageable pageable) {
        Category entity = categoryService.findByCategory(category);
        Page<Lecture> lecturePage = lectureRepository.findAllByTutorAndStatusAndCategoryAndTitleContains(user, Status.FINISH, entity, keyword, pageable);

        return LectureResponse.List.of(lectureMapping(lecturePage), lecturePage);
    }

    @Transactional(readOnly = true)
    public LectureResponse.PossibleList find(User user, int page, int requiredCount) {
        Page<Lecture> lecturePage = lectureQueryDSL.findLecture(user.getDivision(), Status.OPEN, page, requiredCount);

        return getList(user, lecturePage);
    }

    @Transactional(readOnly = true)
    public LectureResponse.PossibleList find(User user, String keyword, int page, int requiredCount) {
        Page<Lecture> lecturePage = lectureQueryDSL.findLecture(user.getDivision(), Status.OPEN, keyword, page, requiredCount);

        return getList(user, lecturePage);
    }

    @Transactional(readOnly = true)
    public LectureResponse.PossibleList findCategory(User user, String category, int page, int requiredCount) {
        Category entity = categoryService.findByCategory(category);
        Page<Lecture> lecturePage = lectureQueryDSL.findLecture(user.getDivision(), Status.OPEN, entity, page, requiredCount);

        return getList(user, lecturePage);
    }

    @Transactional(readOnly = true)
    public LectureResponse.PossibleList findCategory(User user, String category, String title, int page, int requiredCount) {
        Category entity = categoryService.findByCategory(category);
        Page<Lecture> lecturePage = lectureQueryDSL.findLecture(user.getDivision(), Status.OPEN, entity, title, page, requiredCount);

        return getList(user, lecturePage);
    }

    @Transactional(readOnly = true)
    public BoardResponse.List find(Long lecture_id, Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAllByLectureId(lecture_id, pageable);

        return new BoardResponse.List(BoardResponse.Summary.of(boardPage.getContent()), boardPage.getTotalElements(), boardPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public BoardResponse.List find(Long lecture_id, String category, Pageable pageable) {
        BoardCategory entity = categoryService.findBoardCategory(category);
        Page<Board> boardPage = boardRepository.findAllByLectureIdAndCategory(lecture_id, entity, pageable);

        return new BoardResponse.List(BoardResponse.Summary.of(boardPage.getContent()), boardPage.getTotalElements(), boardPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public List<PlanResponse.Summary> findPlan(Long id) {
        List<Plan> planList = planService.findPlans(id);

        return PlanResponse.Summary.of(planList);
    }

    @Transactional(readOnly = true)
    public LectureResponse find(Long id) {
        Lecture lecture = lectureQueryDSL.findLecture(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        List<Plan> planList = planService.findPlans(id);

        return LectureResponse.of(lecture, planList, lectureUserService.countLectureUser(lecture));
    }

    @Transactional(readOnly = true)
    public LectureRequest find(User user, Long id) {
        Lecture lecture = lectureQueryDSL.findLecture(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (!user.getId().equals(lecture.getTutor().getId()))
            throw new BusinessException(ErrorCode.NO_AUTH);

        List<DivisionDTO> divisionDTO = DivisionDTO.ofLectureDivision(lecture.getDivisionList());
        List<Handout> handoutList = fileService.findHandoutsById(id);
        List<Plan> planList = planService.findPlans(id);

        return LectureRequest.of(lecture, divisionDTO, handoutList, planList);
    }

    @Transactional(readOnly = true)
    public LectureResponse.WithHandout findDetailInfo(Long id) {
        List<Handout> handoutList = fileService.findHandoutsById(id);
        List<Plan> planList = planService.findPlans(id);

        return new LectureResponse.WithHandout(HandoutResponse.of(handoutList), PlanResponse.SummaryWithDate.of(planList));
    }

    @Transactional(readOnly = true)
    public LectureDTO findCustomSummary(Long id) {
        Lecture lecture = findById(id);

        return LectureDTO.of(lecture);
    }

    @Transactional(readOnly = true)
    public MainResponse findMain(User user) {
        if (user.getRole().equals(Role.ROLE_MEMBER))
            return lectureUserService.findMain(user);

        List<Lecture> lectureList = lectureRepository.findAllByTutorAndStatusNotIn(user, Arrays.asList(Status.FINISH, Status.COMPLETE));
        List<TutorNotice> tutorNoticeList = tutorNoticeRepository.findAll(CommonService.getPageable(0, 4)).getContent();
        List<MainResponse.NoticeSummary> noticeList = MainResponse.NoticeSummary.of(tutorNoticeList);

        lectureList.sort(new Comparator<Lecture>() {
            @Override
            public int compare(Lecture left, Lecture right) { // 내림차순
                if (left.getId() < right.getId()) {
                    return 1;
                } else if (left.getId() > right.getId()) {
                    return -1;
                }
                return 0;
            }
        });

        return lectureUserService.findMain(lectureList, noticeList);
    }

    @Transactional(readOnly = true)
    public List<LectureResponse.Sidebar> findSidebar(User user) {
        if (user.getRole().equals(Role.ROLE_MEMBER))
            return lectureUserService.findSidebar(user);

        List<Lecture> lectureList = lectureRepository.findAllByTutorAndStatusNotIn(user, Arrays.asList(Status.FINISH, Status.COMPLETE));
        return LectureResponse.Sidebar.of(lectureList);
    }

    @Transactional(readOnly = true)
    public BoardResponse findBoard(User user, Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        Board after = boardRepository.findFirstByIdGreaterThanAndLectureId(id, board.getLecture().getId()).orElse(null);
        Page<Board> beforePage = boardRepository.findByIdLessThanAndLectureId(id, board.getLecture().getId(), PageRequest.of(0, 1, Sort.by("id").descending()));
        List<Board> beforeList = beforePage.getContent();
        Board before = beforeList.size() == 0 ? null : beforeList.get(0);
        List<BoardComment> boardCommentList = boardCommentQueryDSL.findCommentList(board);

        if (user != null) {
            String viewKey = "board_view_user_" + user.getId() + "_board_" + id;
            if (redis.getData(viewKey) == null) {
                board.increaseView();
                redis.setDataExpire(viewKey, viewKey, 60 * 60 * 24);
            }
        }

        String parentId = CommonService.getParentId(FileType.LECTURE_BOARD, id);
        List<FileDTO> fileList = fileService.find(parentId);

        return BoardResponse.of(board, BoardCommentResponse.of(boardCommentList), fileList, BoardResponse.Summary.of(after), BoardResponse.Summary.of(before));
    }

    @Transactional
    public Long save(User user, LectureRequest request) {
        if (request.getStudent_limit() == 0)
            throw new BusinessException(ErrorCode.SAVE_FAIL);

        List<PlanRequest> planRequestList = request.getPlan_list();
        LocalDateTimeDTO dateTimeDTO = CommonService.getDate(planRequestList);
        Category category = categoryService.findByCategory(request.getCategory());

        Lecture lecture = lectureRepository.save(request.toLecture(user, category, dateTimeDTO.getStartDate(), dateTimeDTO.getEndDate()));

        List<DivisionDTO> divisionList = request.getDivision_list();
        divisionList = Optional.ofNullable(divisionList).orElseGet(Collections::emptyList)
                .stream()
                .distinct()
                .collect(Collectors.toList());

        divisionService.saveLectureDivision(lecture, divisionList);

        if (request.getHandout_list() != null)
            fileService.saveHandoutList(lecture, request.getHandout_list());


        List<Plan> planList = request.getPlan_list()
                .stream()
                .map(planRequest -> planRequest.toPlan(lecture))
                .collect(Collectors.toList());

        planService.save(planList);

        return lecture.getId();
    }

    @Transactional
    public void cancel(User user, Long userId, Long id) {
        Lecture lecture = findById(id);

        if (!Objects.equals(lecture.getTutor().getId(), user.getId()))
            throw new BusinessException(ErrorCode.NO_AUTH);

        LectureUser lectureUser = lectureUserService.findLectureUser(userId, id);

        if (lectureUser.getStatus().equals(Status.COMPLETE))
            fileService.deleteCertificate(userId, id);

        lectureUserService.delete(userId, lecture);
        planService.delete(userId, lecture);
    }

    @Transactional
    public void cancel(User user, Long id) {
        Lecture lecture = findById(id);
        Long userId = user.getId();
        LectureUser lectureUser = lectureUserService.findLectureUser(userId, id);

        if (lectureUser.getStatus().equals(Status.COMPLETE))
            fileService.deleteCertificate(userId, id);

        lectureUserService.delete(userId, lecture);
        planService.delete(userId, lecture);
    }

    @Transactional
    public void update(User user, Long id, LectureRequest.Update request) {
        Lecture lecture = findById(id);
        Category category = categoryService.findByCategory(request.getCategory());

        if (!Objects.equals(lecture.getTutor().getId(), user.getId()))
            throw new BusinessException(ErrorCode.NO_AUTH);

        if (lecture.getStatus().equals(Status.FINISH))
            throw new BusinessException(ErrorCode.LECTURE_FINISHED);

        List<PlanRequest.Update> planRequestList = request.getPlan_list();
        LocalDateTimeDTO dateTimeDTO = CommonService.getUpdateDate(planRequestList);

        lecture.update(request, category, dateTimeDTO.getStartDate(), dateTimeDTO.getEndDate());
        List<DivisionDTO> divisionList = request.getDivision_list();

        if (request.getHandout_list() != null) {
            List<HandoutRequest> newList = request.getHandout_list().getNew_file_list();
            List<HandoutRequest> deleteList = request.getHandout_list().getDelete_file_list();

            fileService.saveHandoutList(lecture, newList);
            fileService.deleteHandoutList(lecture, deleteList);
        }
        List<User> userList = lectureUserService.findUserByLecture(lecture);
        planService.update(lecture, request.getDelete_plan_list(), userList, request.getPlan_list());
        divisionService.updateLectureDivision(lecture, divisionList);
    }

    @Transactional
    public Long save(User user, Long lectureId, BoardRequest request) {
        Lecture lecture = findById(lectureId);
        BoardCategory category = categoryService.findBoardCategory(request.getCategory());
        fileService.uploadReal(CommonService.getUrl(request.getContent(), FileType.LECTURE_BOARD));

        Long id = boardRepository.save(request.toBoard(user, lecture, category)).getId();
        String parentId = CommonService.getParentId(FileType.LECTURE_BOARD, id);
        fileService.save(parentId, request.getAttachment_list(), FileType.LECTURE_BOARD);

        return id;
    }

    @Transactional
    public void deleteBoard(User user, Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (!Objects.equals(board.getAuthor().getId(), user.getId()))
            throw new BusinessException(ErrorCode.NO_AUTH);

        boardRepository.delete(board);
        String parentId = CommonService.getParentId(FileType.LECTURE_BOARD, id);
        fileService.delete(parentId);
    }

    @Transactional
    public void update(User user, Long boardId, BoardRequest request) {
        Board board = findBoardById(boardId);
        BoardCategory category = categoryService.findBoardCategory(request.getCategory());

        if (!Objects.equals(board.getAuthor().getId(), user.getId()))
            throw new BusinessException(ErrorCode.NO_AUTH);

        String parentId = CommonService.getParentId(FileType.LECTURE_BOARD, boardId);
        fileService.delete(parentId);
        fileService.save(parentId, request.getAttachment_list(), FileType.LECTURE_BOARD);
        board.update(request, category);
    }

    @Transactional
    public Long save(User user, Long boardId, BoardCommentRequest request) {
        Board board = findBoardById(boardId);
        BoardComment comment = request.toComment(user, board);

        if (request.getParent_id() != null) {
            BoardComment parent = findBoardCommentById(request.getParent_id());
            comment.setParent(parent);
        }

        return boardCommentRepository.save(comment).getId();
    }

    @Transactional
    public void deleteComment(User user, Long boardCommentId) {
        BoardComment comment = findBoardCommentById(boardCommentId);

        if (!Objects.equals(comment.getAuthor().getId(), user.getId()))
            throw new BusinessException(ErrorCode.NO_AUTH);

        boardCommentRepository.delete(comment);
    }

    @Transactional
    public void updateComment(User user, Long boardCommentId, BoardCommentRequest request) {
        BoardComment comment = findBoardCommentById(boardCommentId);

        if (!Objects.equals(comment.getAuthor().getId(), user.getId()))
            throw new BusinessException(ErrorCode.NO_AUTH);

        comment.update(request);
    }

    @Transactional
    public void join(User user, Long id) {
        Lecture lecture = lectureQueryDSL.findLecture(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        Long studentNum = lectureUserService.countLectureUser(lecture);
        List<DivisionDTO> divisionDTO = DivisionDTO.ofLectureDivision(lecture.getDivisionList());
        Division temp = user.getDivision();

        if (user.getDivision() == null)
            temp = new Division(null, null);

        if (!DivisionDTO.checkDivision(temp.getFirstDivision(), temp.getSecondDivision(), divisionDTO))
            throw new BusinessException(ErrorCode.JOIN_DIVISION);

        if (lecture.getStatus().equals(Status.FINISH))
            throw new BusinessException(ErrorCode.LECTURE_FINISHED);

        if (lectureUserService.exist(user, lecture))
            throw new BusinessException(ErrorCode.JOIN_ALREADY);

        if (!lecture.validate())
            throw new BusinessException(ErrorCode.LECTURE_TIMEOUT);

        List<LectureUser> lectureUserList = lectureUserService.findLectureUserByUser(user.getId());
        if (!lecture.checkTime(lectureUserList))
            throw new BusinessException(ErrorCode.LECTURE_TIME_CHECK_FAIL);

        List<Plan> planList = planService.findPlans(id);
        List<PlanUser> planUserList = new ArrayList<>();

        if (lecture.getStudentLimit() == -1 || lecture.getStudentLimit() > studentNum) {
            LectureUser lectureUser = LectureUser.builder()
                    .lecture(lecture)
                    .user(user)
                    .status(Status.ING)
                    .build();

            lectureUserService.save(lectureUser);

            for (Plan plan : planList)
                planUserList.add(new PlanUser(user, plan, null));

            planService.savePlanUser(planUserList);
        } else throw new BusinessException(ErrorCode.JOIN_FAIL);
    }

    @Transactional
    public void finish(User user, Long id) {
        Lecture lecture = findById(id);

        if (lecture.getTutor().getId().equals(user.getId())) {

            List<LectureUser> userList = lectureUserService.findLectureUserByLecture(lecture);

            for (LectureUser temp : userList) {
                temp.updateStatus(Status.COMPLETE);
            }

            lecture.finishLecture();

        } else
            throw new BusinessException(ErrorCode.NO_AUTH);
    }

    @Transactional
    public void delete(User user, Long id) {
        Lecture lecture = findById(id);

        if (!Objects.equals(lecture.getTutor().getId(), user.getId()))
            throw new BusinessException(ErrorCode.NO_AUTH);

        List<LectureUser> lectureUserList = lectureUserService.findLectureUserByLecture(lecture);
        List<String> fileList = new ArrayList<>();

        for (LectureUser lectureUser : lectureUserList) {
            if (lectureUser.getStatus().equals(Status.COMPLETE) && lectureUser.getFileUrl() != null)
                fileList.add(lectureUser.getFileUrl());
        }

        fileService.deleteCertificate(fileList);
        lectureRepository.delete(lecture);
    }

    @Transactional(readOnly = true)
    public Integer count(User user, Status status) {
        return lectureRepository.countLecturesByTutorAndStatus(user, status);
    }

    private Lecture findById(Long id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private Board findBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private BoardComment findBoardCommentById(Long id) {
        return boardCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private LectureResponse.PossibleList getList(User user, Page<Lecture> lecturePage) {
        List<Lecture> lectureList = lecturePage.getContent();
        List<LectureResponse.SummaryWithStatus> summaryWithStatusList = new ArrayList<>();

        for (Lecture lecture : lectureList) {
            Long studentNum = lectureUserService.countLectureUser(lecture);
            if (lectureUserService.exist(user, lecture))
                summaryWithStatusList.add(LectureResponse.SummaryWithStatus.of(lecture, Status.ING, studentNum));

            else {
                if (lecture.getStudentLimit() == -1 || lecture.getStudentLimit() > studentNum)
                    summaryWithStatusList.add(LectureResponse.SummaryWithStatus.of(lecture, Status.POSSIBLE, studentNum));

                else
                    summaryWithStatusList.add(LectureResponse.SummaryWithStatus.of(lecture, Status.IMPOSSIBLE, studentNum));
            }
        }

        return new LectureResponse.PossibleList(summaryWithStatusList, lecturePage.getTotalElements(), lecturePage.getTotalPages());
    }

    private Map<Lecture, Long> lectureMapping(Page<Lecture> page) {
        Map<Lecture, Long> map = new HashMap<>();
        List<Lecture> lectureList = page.getContent();

        for (Lecture lecture : lectureList) {
            Long lectureNum = lectureUserService.countLectureUser(lecture);
            map.put(lecture, lectureNum);
        }

        return map;
    }
}

