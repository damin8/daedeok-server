package hours22.daedeokserver.lecture.domain.board;

import hours22.daedeokserver.category.domain.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByLectureId(Long lectureId, Pageable pageable);
    Page<Board> findAllByLectureIdAndCategory(Long lecture_id, BoardCategory category, Pageable pageable);
    Optional<Board> findFirstByIdGreaterThanAndLectureId(Long id, Long lectureId);
    Page<Board> findByIdLessThanAndLectureId(Long id, Long lectureId, Pageable pageable);
}
