package hours22.daedeokserver.naver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class NaverDTO {

    private String type;
    private String contentType;
    private String countryCode;
    private String from;
    private String content;
    private List<Message> messages;

    @Getter
    @AllArgsConstructor
    public static class Message {
        private String to;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String statusCode;
        private String statusName;
        private String requestId;
        private LocalDateTime requestTime;
    }
}
