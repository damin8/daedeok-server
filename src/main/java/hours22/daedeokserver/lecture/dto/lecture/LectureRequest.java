package hours22.daedeokserver.lecture.dto.lecture;

import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.division.dto.DivisionDTO;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.domain.handout.Handout;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.lecture.domain.plan.Plan;
import hours22.daedeokserver.lecture.dto.handout.HandoutRequest;
import hours22.daedeokserver.lecture.dto.plan.PlanRequest;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class LectureRequest {
    private String title;
    private String content;
    private String category;
    @NotBlank(message = "요일을 입력해주세요")
    private String day;
    @NotBlank(message = "시간을 입력해주세요")
    private String time;
    private List<DivisionDTO> division_list;
    private Long student_limit;
    private String reference;
    private List<HandoutRequest> handout_list;
    @NotEmpty(message = "강의 계획은 최소 1개 이상의 주차를 포함해야 합니다")
    private List<PlanRequest> plan_list;

    public Lecture toLecture(User user, Category category, LocalDateTime startDate, LocalDateTime endDate) {
        return Lecture.builder()
                .title(title)
                .category(category)
                .content(content)
                .tutor(user)
                .view(0L)
                .day(day)
                .time(time)
                .status(Status.OPEN)
                .studentLimit(student_limit)
                .startDate(startDate)
                .endDate(endDate)
                .reference(reference)
                .build();
    }

    public static LectureRequest of(Lecture lecture, List<DivisionDTO> divisionList, List<Handout> handoutList, List<Plan> planList) {
        Category category = lecture.getCategory();

        if (category == null)
            category = new Category(null, null);

        return new LectureRequest(lecture.getTitle(), lecture.getContent(), category.getCategory(), lecture.getDay(), lecture.getTime(), divisionList, lecture.getStudentLimit(), lecture.getReference(), HandoutRequest.of(handoutList), PlanRequest.of(planList));
    }

    @Getter
    @AllArgsConstructor
    public static class Update {
        private String title;
        private String content;
        private String day;
        private String time;
        private String category;
        private List<DivisionDTO> division_list;
        private Long student_limit;
        private String reference;
        private HandoutRequest.Update handout_list;
        private List<Long> delete_plan_list;
        private List<PlanRequest.Update> plan_list;
    }
}
