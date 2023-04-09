package hours22.daedeokserver.faq.domain;

import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.faq.dto.FAQRequest;
import hours22.daedeokserver.file.dto.FileType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FAQ extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String content;

    @Builder
    public FAQ(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(FAQRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.content = this.content.replaceAll("dummy/", FileType.FAQ.directory());
    }
}
