package hours22.daedeokserver.lecture.domain.plan;

import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PlanUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    @ManyToOne
    @JoinColumn(name = "plan_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Plan plan;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Float duration;

    public PlanUser(User user, Plan plan, Status status) {
        this.user = user;
        this.plan = plan;
        this.status = status;
        this.duration = 0f;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updateDuration(Float duration) {
        if (duration >= 90) {
            status = Status.COMPLETE;
            duration = 100f;
        }

        this.duration = duration;
    }
}
