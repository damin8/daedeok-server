package hours22.daedeokserver.guide.domain;

import hours22.daedeokserver.guide.dto.GuideRequest;
import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.file.dto.FileType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Guide extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String content;

    @Builder
    public Guide(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public void update(GuideRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.content = this.content.replaceAll("dummy/", FileType.AC_INFO.directory());
    }
}
