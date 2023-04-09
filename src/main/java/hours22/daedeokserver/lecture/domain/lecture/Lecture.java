package hours22.daedeokserver.lecture.domain.lecture;

import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.division.domain.LectureDivision;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.dto.lecture.LectureRequest;
import hours22.daedeokserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "lecture")
    private List<LectureDivision> divisionList = new ArrayList<>();
    @Lob
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User tutor;
    private Long view;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Long studentLimit;
    private String reference;
    private String day;
    private String time;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public Lecture(String title, Category category, String content, User tutor, Long view, Status status, Long studentLimit, String reference, String day, String time, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.tutor = tutor;
        this.view = view;
        this.status = status;
        this.studentLimit = studentLimit;
        this.reference = reference;
        this.day = day;
        this.time = time;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void finishLecture() {
        this.status = Status.FINISH;
    }

    public void update(LectureRequest.Update request, Category category, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = request.getTitle();
        this.category = category;
        this.content = request.getContent();
        this.studentLimit = request.getStudent_limit();
        this.reference = request.getReference();
        this.day = request.getDay();
        this.time = request.getTime();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean validate() { // 첫 번째 수업 전 (startDate) 확인
        LocalDateTime now = LocalDateTime.now();

        return now.isBefore(startDate);
    }

    public boolean checkTime(List<LectureUser> lectureUserList) { // 중복 시간 확인
        String date = day + time;

        for(LectureUser lectureUser : lectureUserList){
            String temp = lectureUser.getLecture().getDay() + lectureUser.getLecture().getTime();

            if(date.equals(temp))
                return false;
        }

        return true;
    }
}
