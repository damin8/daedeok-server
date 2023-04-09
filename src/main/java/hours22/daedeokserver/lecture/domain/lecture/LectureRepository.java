package hours22.daedeokserver.lecture.domain.lecture;

import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Page<Lecture> findAllByStatus(Status status, Pageable pageable);
    Page<Lecture> findAllByStatusAndTitleContains(Status status, String keyword, Pageable pageable);
    Page<Lecture> findAllByTutorAndStatus(User user, Status status, Pageable pageable);
    Page<Lecture> findAllByTutorAndStatusAndTitleContains(User user, Status status, String keyword, Pageable pageable);
    Page<Lecture> findAllByStatusAndCategory(Status status, Category category, Pageable pageable);
    Page<Lecture> findAllByStatusAndCategoryAndTitleContains(Status status, Category category, String title, Pageable pageable);
    Page<Lecture> findAllByTutorAndStatusAndCategory(User tutor, Status status, Category category, Pageable pageable);
    Page<Lecture> findAllByTutorAndStatusAndCategoryAndTitleContains(User tutor, Status status, Category category, String title, Pageable pageable);
    List<Lecture> findAllByTutorAndStatusNotIn(User user, List<Status> statusList);
    Integer countLecturesByTutorAndStatus(User user, Status status);
}
