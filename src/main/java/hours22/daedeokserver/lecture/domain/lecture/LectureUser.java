package hours22.daedeokserver.lecture.domain.lecture;

import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.user.domain.User;
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
public class LectureUser extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    @ManyToOne
    @JoinColumn(name = "lecture_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lecture lecture;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String fileUrl;

    @Builder
    public LectureUser(Long id, User user, Lecture lecture, Status status, String fileUrl) {
        this.id = id;
        this.user = user;
        this.lecture = lecture;
        this.status = status;
        this.fileUrl = fileUrl;
    }

    public void updateUrl(String url) {
        this.fileUrl = url;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}
