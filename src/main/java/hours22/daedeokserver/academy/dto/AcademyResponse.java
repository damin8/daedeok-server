package hours22.daedeokserver.academy.dto;

import hours22.daedeokserver.academy.domain.Academy;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AcademyResponse {
    private String content;
    private LocalDateTime update_date;

    public static AcademyResponse of(Academy academy) {
        if (academy == null)
            return new AcademyResponse("", LocalDateTime.now());

        return new AcademyResponse(academy.getContent(), academy.getUpdateDate());
    }
}
