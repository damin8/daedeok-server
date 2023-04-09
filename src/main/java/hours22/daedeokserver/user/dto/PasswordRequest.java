package hours22.daedeokserver.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {
    private String password;

    @Getter
    @AllArgsConstructor
    public static class Reset {
        private String phone_num;
        private String password;
    }
}
