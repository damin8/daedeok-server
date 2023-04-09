package hours22.daedeokserver.user.dto;

import hours22.daedeokserver.division.domain.Division;
import hours22.daedeokserver.lecture.dto.plan.PlanRequest;
import hours22.daedeokserver.user.domain.Role;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class UserResponse {
    private List<Summary> user_list;
    private Long total_count;
    private Integer total_page;

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private Long id;
        private String name;
        private String duty;
        private Role role;
        private Integer lecture_num;
        private String first_division;
        private String second_division;
        private String phone_num;

        public static List<Summary> of(Map<User, Integer> map) {
            List<Summary> summaryList = new ArrayList<>();

            for (Map.Entry<User, Integer> entry : map.entrySet()) {
                Integer lectureNum = entry.getValue();
                summaryList.add(of(entry.getKey(), lectureNum));
            }

            summaryList.sort(new Comparator<Summary>() {
                @Override
                public int compare(Summary left, Summary right) {
                    if (left.getId() > right.getId()) {
                        return -1;
                    } else if (left.getId() < right.getId()) {
                        return 1;
                    }
                    return 0;
                }
            });

            return summaryList;
        }

        public static Summary of(User user, Integer lectureNum) {
            Division division = user.getDivision();

            if (user.getDivision() == null)
                division = new Division("", "");

            return new Summary(user.getId(), user.getName(), user.getDuty(), user.getRole(), lectureNum, division.getFirstDivision(), division.getSecondDivision(), user.getPhoneNum());
        }
    }
}
