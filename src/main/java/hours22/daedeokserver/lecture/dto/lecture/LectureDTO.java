package hours22.daedeokserver.lecture.dto.lecture;

import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LectureDTO {
    private String title;
    private Status status;

    public static LectureDTO of(Lecture lecture) {
        return new LectureDTO(lecture.getTitle(), lecture.getStatus());
    }
}
