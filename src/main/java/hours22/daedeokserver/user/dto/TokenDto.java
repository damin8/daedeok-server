package hours22.daedeokserver.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDto {
    private String access_token;
    private String refresh_token;
    private Long expire_time;

    @Getter
    @AllArgsConstructor
    public static class Request {
        private String access_token;
        private String refresh_token;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private String access_token;
        private Long expire_time;
    }
}
