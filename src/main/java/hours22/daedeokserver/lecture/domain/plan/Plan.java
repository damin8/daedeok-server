package hours22.daedeokserver.lecture.domain.plan;

import hours22.daedeokserver.lecture.domain.Type;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.lecture.dto.plan.PlanRequest;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long week;
    private String title;
    private String tutor;
    private String location;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String link;
    private String introduce;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lecture lecture;

    @Builder
    public Plan(Long week, String title, String tutor, String location, LocalDateTime date, Type type, String link, String introduce, Lecture lecture) {
        this.week = week;
        this.title = title;
        this.tutor = tutor;
        this.location = location;
        this.date = date;
        this.type = type;
        this.link = link;
        this.introduce = introduce;
        this.lecture = lecture;
    }

    public void update(PlanRequest.Update request) {
        this.week = request.getWeek();
        this.title = request.getTitle();
        this.tutor = request.getTutor();

        if (!Type.valueOf(request.getType()).equals(Type.OFFLINE))
            this.location = request.getType();

        else this.location = request.getLocation();

        this.date = request.getRealDate();
        this.type = Type.valueOf(request.getType());
        this.link = (request.getZoom_link() == null) ? request.getVideo_link() : request.getZoom_link();
        this.introduce = request.getIntroduce();

    }

    public boolean validate() {
        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(date.minusMinutes(31)) && now.isBefore(date.plusHours(3).plusMinutes(1));
    }

    public boolean check() {
        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(date.minusMinutes(1L));
    }
}
