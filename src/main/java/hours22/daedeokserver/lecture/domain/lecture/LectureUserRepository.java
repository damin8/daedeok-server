package hours22.daedeokserver.lecture.domain.lecture;

import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureUserRepository extends JpaRepository<LectureUser, Long> {
    boolean existsByUserAndLecture(User user, Lecture lecture);
    Optional<List<LectureUser>> findLectureUsersByLecture_Id(Long id);
    List<LectureUser> findLectureUsersByUserAndLecture_Status(User user, Status status);
    Page<LectureUser> findLectureUsersByUserAndStatus(User user, Status status, Pageable pageable);
    Page<LectureUser> findLectureUsersByUserAndStatusAndLecture_Title(User user, Status status, String keyword, Pageable pageable);
    Page<LectureUser> findLectureUsersByUserAndLecture_CategoryAndStatus(User user, Category category, Status status, Pageable pageable);
    Page<LectureUser> findLectureUsersByUserAndLecture_CategoryAndStatusAndLecture_Title(User user, Category category, Status status, String keyword, Pageable pageable);
    List<LectureUser> findLectureUsersByLecture(Lecture lecture);
    List<LectureUser> findLectureUsersByUser_IdAndStatusIsNot(Long userId, Status status);
    Optional<LectureUser> findByUserIdAndLectureId(Long userId, Long lectureId);
    void deleteByUserIdAndLecture(Long user, Lecture lecture);
    Integer countLectureUsersByUserAndLecture_Status(User user, Status status);
    Long countLectureUsersByLecture(Lecture lecture);
}
