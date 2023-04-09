package hours22.daedeokserver.division.domain;

import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LectureDivision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lecture lecture;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Division division;

    @Builder
    public LectureDivision(Long id, Lecture lecture, Division division) {
        this.id = id;
        this.lecture = lecture;
        this.division = division;
    }

    public void setLecture(Lecture lecture) {
        if (this.lecture != null)
            this.lecture.getDivisionList().remove(this);

        this.lecture = lecture;
        lecture.getDivisionList().add(this);
    }

    public void updateDivision(Division division){
        this.division = division;
    }
}
