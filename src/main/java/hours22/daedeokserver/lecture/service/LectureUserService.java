package hours22.daedeokserver.lecture.service;

import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.category.domain.CategoryRepository;
import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.EntityNotFoundException;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.domain.board.Board;
import hours22.daedeokserver.lecture.domain.board.BoardRepository;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.lecture.domain.lecture.LectureUser;
import hours22.daedeokserver.lecture.domain.lecture.LectureUserRepository;
import hours22.daedeokserver.lecture.domain.plan.Plan;
import hours22.daedeokserver.lecture.dto.lecture.LectureResponse;
import hours22.daedeokserver.lecture.dto.lecture.LectureUserResponse;
import hours22.daedeokserver.lecture.dto.lecture.MainResponse;
import hours22.daedeokserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureUserService {

    private final LectureUserRepository lectureUserRepository;
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final PlanService planService;

    @Transactional(readOnly = true)
    public LectureUserResponse find(Long lectureId) {
        List<LectureUser> lectureUserList = lectureUserRepository.findLectureUsersByLecture_Id(lectureId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        long count = lectureUserList.size();
        return new LectureUserResponse(LectureUserResponse.Summary.of(lectureUserList), count);
    }

    @Transactional(readOnly = true)
    public LectureResponse.CertificateList findComplete(User user, Pageable pageable) {
        Page<LectureUser> lectureUserPage = lectureUserRepository.findLectureUsersByUserAndStatus(user, Status.COMPLETE, pageable);

        return new LectureResponse.CertificateList(LectureResponse.SummaryWithCertificate.of(lectureUserPage.getContent()), lectureUserPage.getTotalElements(), lectureUserPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public LectureResponse.CertificateList findComplete(User user, String keyword, Pageable pageable) {
        Page<LectureUser> lectureUserPage = lectureUserRepository.findLectureUsersByUserAndStatusAndLecture_Title(user, Status.COMPLETE, keyword, pageable);

        return new LectureResponse.CertificateList(LectureResponse.SummaryWithCertificate.of(lectureUserPage.getContent()), lectureUserPage.getTotalElements(), lectureUserPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public LectureResponse.CertificateList findCompleteCategory(User user, String category, Pageable pageable) {
        Category entity = categoryRepository.findByCategory(category).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        Page<LectureUser> lectureUserPage = lectureUserRepository.findLectureUsersByUserAndLecture_CategoryAndStatus(user, entity, Status.COMPLETE, pageable);

        return new LectureResponse.CertificateList(LectureResponse.SummaryWithCertificate.of(lectureUserPage.getContent()), lectureUserPage.getTotalElements(), lectureUserPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public LectureResponse.CertificateList findCompleteCategory(User user, String category, String keyword, Pageable pageable) {
        Category entity = categoryRepository.findByCategory(category).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        Page<LectureUser> lectureUserPage = lectureUserRepository.findLectureUsersByUserAndLecture_CategoryAndStatusAndLecture_Title(user, entity, Status.COMPLETE, keyword, pageable);

        return new LectureResponse.CertificateList(LectureResponse.SummaryWithCertificate.of(lectureUserPage.getContent()), lectureUserPage.getTotalElements(), lectureUserPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public List<LectureResponse.Sidebar> findSidebar(User user) {
        List<LectureUser> lectureUserList = findLectureUser(user);

        List<Lecture> lectureList = lectureUserList.stream()
                .map(LectureUser::getLecture)
                .collect(Collectors.toList());

        return LectureResponse.Sidebar.of(lectureList);

    }

    @Transactional(readOnly = true)
    public MainResponse findMain(User user) {
        List<LectureUser> lectureUserList = findLectureUser(user);

        List<Lecture> lectureList = lectureUserList.stream()
                .map(LectureUser::getLecture)
                .collect(Collectors.toList());

        return findMain(lectureList, new ArrayList<>());

    }

    @Transactional(readOnly = true)
    public MainResponse findMain(List<Lecture> lectureList, List<MainResponse.NoticeSummary> noticeList) {
        List<MainResponse.LectureMain> lectureMainList = new ArrayList<>();
        List<MainResponse.BoardList> board_list = new ArrayList<>();
        int size = lectureList.size();

        for (Lecture lecture : lectureList) {
            Long lectureId = lecture.getId();
            List<Plan> planList = planService.findPlans(lecture.getId());
            MainResponse.LectureMain lectureMain = MainResponse.LectureMain.of(size--, lecture, planList, countLectureUser(lecture));
            lectureMainList.add(lectureMain);

            Page<Board> boardPage = boardRepository.findAllByLectureId(lectureId, CommonService.getPageable(0, 3));
            board_list.add(MainResponse.BoardList.of(lecture, boardPage.getContent()));
        }

        return new MainResponse(lectureMainList, board_list, noticeList);
    }

    @Transactional(readOnly = true)
    public List<User> findUserByLecture(Lecture lecture) {
        List<LectureUser> lectureUserList = lectureUserRepository.findLectureUsersByLecture(lecture);

        return lectureUserList.stream()
                .map(LectureUser::getUser)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LectureUser> findLectureUserByUser(Long userId) {
        return lectureUserRepository.findLectureUsersByUser_IdAndStatusIsNot(userId, Status.COMPLETE);
    }

    @Transactional(readOnly = true)
    public List<LectureUser> findLectureUserByLecture(Lecture lecture) {
        return lectureUserRepository.findLectureUsersByLecture(lecture);
    }

    @Transactional
    public LectureUser findLectureUser(Long userId, Long lectureId) {
        return lectureUserRepository.findByUserIdAndLectureId(userId, lectureId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public boolean exist(User user, Lecture lecture) {
        return lectureUserRepository.existsByUserAndLecture(user, lecture);
    }

    @Transactional
    public void save(LectureUser lectureUser) {
        lectureUserRepository.save(lectureUser);
    }

    @Transactional
    public void delete(Long id, Lecture lecture) {
        lectureUserRepository.deleteByUserIdAndLecture(id, lecture);
    }

    @Transactional(readOnly = true)
    public Integer countLectureUser(User user) {
        return lectureUserRepository.countLectureUsersByUserAndLecture_Status(user, Status.OPEN);
    }

    @Transactional(readOnly = true)
    public Long countLectureUser(Lecture lecture) {
        return lectureUserRepository.countLectureUsersByLecture(lecture);
    }

    private List<LectureUser> findLectureUser(User user) {
        List<LectureUser> lectureUserList = lectureUserRepository.findLectureUsersByUserAndLecture_Status(user, Status.OPEN);

        lectureUserList.sort(new Comparator<LectureUser>() {
            @Override
            public int compare(LectureUser left, LectureUser right) {
                if (left.getId() < right.getId()) {
                    return 1;
                } else if (left.getId() > right.getId()) {
                    return -1;
                }
                return 0;
            }
        });

        return lectureUserList;
    }
}
