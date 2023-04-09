package hours22.daedeokserver.lecture.dto.plan;

import hours22.daedeokserver.division.domain.Division;
import hours22.daedeokserver.lecture.domain.plan.PlanUser;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.domain.Type;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PlanUserResponse {

    private Long total_student;
    private Type type;
    private Long week;
    private List<Summary> student_list;

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private Long user_id;
        private String name;
        private String duty;
        private String first_division;
        private String second_division;
        private String phone_num;
        private Status status;

        public static List<Summary> of(List<PlanUser> planUserList) {
            List<Summary> summaryList = new ArrayList<>();

            for (PlanUser planUser : planUserList) {
                summaryList.add(of(planUser));
            }

            return summaryList;
        }

        private static Summary of(PlanUser planUser) {
            User user = planUser.getUser();
            Division division = user.getDivision();

            if (user.getDivision() == null)
                division = new Division("", "");

            return new Summary(user.getId(), user.getName(), user.getDuty(), division.getFirstDivision(), division.getSecondDivision(), user.getPhoneNum(), planUser.getStatus());
        }
    }
}
