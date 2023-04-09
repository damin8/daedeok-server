package hours22.daedeokserver.division.domain;

import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureDivisionRepository extends JpaRepository<LectureDivision, Long> {
    List<LectureDivision> findAllByDivision(Division division);
    void deleteAllByLecture(Lecture lecture);
}
