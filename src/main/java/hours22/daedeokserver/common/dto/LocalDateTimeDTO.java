package hours22.daedeokserver.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LocalDateTimeDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
