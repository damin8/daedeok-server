package hours22.daedeokserver.guide.dto;

import hours22.daedeokserver.guide.domain.Guide;
import hours22.daedeokserver.file.dto.FileType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GuideRequest {
    private String title;
    private String content;

    public Guide toEntity() {
        content = content.replaceAll("dummy/", FileType.AC_INFO.directory());
        return Guide.builder()
                .title(title)
                .content(content)
                .build();
    }
}
