package hours22.daedeokserver.lecture.dto.plan;

import hours22.daedeokserver.lecture.domain.plan.Plan;
import hours22.daedeokserver.lecture.domain.plan.PlanUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OnlineResponse {
    private Long id;
    private Long week;
    private String title;
    private String link;
    private String introduce;
    private Float duration;

    public static OnlineResponse of(PlanUser planUser) {
        Plan plan = planUser.getPlan();

        return new OnlineResponse(planUser.getId(), plan.getWeek(), plan.getTitle(), plan.getLink(), plan.getIntroduce(), planUser.getDuration());
    }
}
