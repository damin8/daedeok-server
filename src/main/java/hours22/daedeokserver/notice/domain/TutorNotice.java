package hours22.daedeokserver.notice.domain;

import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.notice.dto.TutorNoticeRequest;
import hours22.daedeokserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TutorNotice extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public TutorNotice(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void update(TutorNoticeRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.content = this.content.replaceAll("dummy/", FileType.TUTOR_NOTICE.directory());
    }
}
