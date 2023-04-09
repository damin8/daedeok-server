package hours22.daedeokserver.user.dto;

import hours22.daedeokserver.division.domain.Division;
import hours22.daedeokserver.user.domain.Role;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class UserRequest {
    @Pattern(regexp = "^[0-9]*?", message = "핸드폰 번호는 숫자만 올 수 있습니다.")
    private String phone_num;
    private String name;
    private String password;
    private String duty;
    private String first_division;
    private String second_division;

    public User toUser(Division division) {
        return User.builder()
                .phoneNum(phone_num)
                .name(name)
                .password(password)
                .role(Role.ROLE_MEMBER)
                .duty(duty)
                .division(division)
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class Update {
        private String name;
        private String duty;
    }

    @Getter
    @AllArgsConstructor
    public static class UpdateAdmin {
        private String name;
        private String duty;
        private String first_division;
        private String second_division;
    }

    @Getter
    @AllArgsConstructor
    public static class UpdateRole {
        private Long id;
        private String role;
    }
}
