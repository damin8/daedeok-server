package hours22.daedeokserver.lecture.dto.plan;

import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.BusinessException;
import hours22.daedeokserver.lecture.domain.Type;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.lecture.domain.plan.Plan;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
public class PlanRequest {
    private Long id;
    private Long week;
    private String title;
    private String tutor;
    private String location;
    @NotBlank(message = "날짜를 입력해주세요")
    private String date;
    @NotBlank(message = "시간을 입력해주세요")
    private String time;
    private String type;
    private String zoom_link;
    private String video_link;
    private String introduce;

    public Plan toPlan(Lecture lecture) {

        if (!Type.valueOf(type).equals(Type.OFFLINE))
            location = type;

        String link = (zoom_link == null) ? video_link : zoom_link;

        return Plan.builder()
                .lecture(lecture)
                .week(week)
                .title(title)
                .tutor(tutor)
                .location(location)
                .date(getRealDate())
                .type(Type.valueOf(type))
                .link(link)
                .introduce(introduce)
                .build();
    }

    public static List<PlanRequest> of(List<Plan> planList) {
        List<PlanRequest> planRequestList = new ArrayList<>();

        for (Plan plan : planList) {
            planRequestList.add(of(plan));
        }

        planRequestList.sort(new Comparator<PlanRequest>() {
            @Override
            public int compare(PlanRequest left, PlanRequest right) {
                if (left.getWeek() < right.getWeek()) {
                    return -1;
                } else if (left.getWeek() > right.getWeek()) {
                    return 1;
                }
                return 0;
            }
        });

        return planRequestList;
    }

    private static PlanRequest of(Plan plan) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = plan.getDate();
        String[] arr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).split(" ");

        return new PlanRequest(plan.getId(), plan.getWeek(), plan.getTitle(), plan.getTutor(), plan.getLocation(), arr[0], arr[1], plan.getType().name(), plan.getLink(), plan.getLink(), plan.getIntroduce());
    }

    public LocalDateTime getRealDate() {
        try {
            String temp = date + " " + time;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(temp, formatter);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Update {
        private Long id;
        private Long week;
        private String title;
        private String tutor;
        private String location;
        @NotBlank
        private String date;
        @NotBlank
        private String time;
        private String type;
        private String zoom_link;
        private String video_link;
        private String introduce;

        public LocalDateTime getRealDate() {
            try {
                String temp = date + " " + time;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                return LocalDateTime.parse(temp, formatter);
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
            }
        }

        public Plan toPlan(Lecture lecture) {

            if (!Type.valueOf(type).equals(Type.OFFLINE))
                location = type;

            String link = (zoom_link == null) ? video_link : zoom_link;

            return Plan.builder()
                    .lecture(lecture)
                    .week(week)
                    .title(title)
                    .tutor(tutor)
                    .location(location)
                    .date(getRealDate())
                    .type(Type.valueOf(type))
                    .link(link)
                    .introduce(introduce)
                    .build();
        }
    }
}
