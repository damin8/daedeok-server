package hours22.daedeokserver.lecture.domain.handout;

import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HandoutRepository extends JpaRepository<Handout, Long> {
    Optional<List<Handout>> findHandoutsByLecture_Id(Long id);
    List<Handout> findHandoutsByLectureAndUrlIn(Lecture lecture, List<String> urlList);
    void deleteHandoutsByLectureAndUrlIn(Lecture lecture, List<String> urlList);
}
