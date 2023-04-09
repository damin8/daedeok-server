package hours22.daedeokserver.notice.domain;

import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.notice.dto.NoticeRequest;
import hours22.daedeokserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String content;

    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(NoticeRequest request){
        this.title = request.getTitle();
        this.content = request.getContent();
        this.content = this.content.replaceAll("dummy/", FileType.GLOBAL_NOTICE.directory());
    }
}
