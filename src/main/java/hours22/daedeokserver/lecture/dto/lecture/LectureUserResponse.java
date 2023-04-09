package hours22.daedeokserver.lecture.dto.lecture;

import hours22.daedeokserver.division.domain.Division;
import hours22.daedeokserver.lecture.domain.lecture.LectureUser;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class LectureUserResponse {
    private List<Summary> user_list;
    private Long student_num;

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
        private String fileUrl;

        public static List<Summary> of(List<LectureUser> userList) {
            List<Summary> summaryList = new ArrayList<>();

            for (LectureUser user : userList) {
                summaryList.add(of(user));
            }

            return summaryList;
        }

        private static Summary of(LectureUser lectureUser) {
            User user = lectureUser.getUser();
            Division division = user.getDivision();

            if (user.getDivision() == null)
                division = new Division("", "");

            return new Summary(user.getId(), user.getName(), user.getDuty(), division.getFirstDivision(), division.getSecondDivision(), user.getPhoneNum(), lectureUser.getStatus(), lectureUser.getFileUrl());
        }
    }
}
