package hours22.daedeokserver.user.dto;


import hours22.daedeokserver.division.domain.Division;
import hours22.daedeokserver.user.domain.Role;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long user_id;
    private String name;
    private Role role;
    private String duty;
    private Integer lecture_num;
    private String first_division;
    private String second_division;
    private String phone_num;
    private String access_token;
    private String refresh_token;

    public static LoginResponse of(User user, Integer lectureNum, String accessToken, String refreshToken) {
        Division division = user.getDivision();

        if (division == null)
            division = new Division("", "");

        return new LoginResponse(user.getId(), user.getName(), user.getRole(), user.getDuty(), lectureNum, division.getFirstDivision(), division.getSecondDivision(), user.getPhoneNum(), accessToken, refreshToken);
    }

    @Getter
    @AllArgsConstructor
    public static class Info {
        private Long user_id;
        private String name;
        private Role role;
        private String duty;
        private Integer lecture_num;
        private String first_division;
        private String second_division;
        private String phone_num;

        public static Info of(User user, Integer lectureNum) {
            Division division = user.getDivision();

            if (user.getDivision() == null)
                division = new Division("", "");

            return new Info(user.getId(), user.getName(), user.getRole(), user.getDuty(), lectureNum, division.getFirstDivision(), division.getSecondDivision(), user.getPhoneNum());
        }
    }
}
