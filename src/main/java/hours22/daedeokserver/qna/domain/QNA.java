package hours22.daedeokserver.qna.domain;

import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.category.domain.QNACategory;
import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.qna.dto.QNARequest;
import hours22.daedeokserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QNA extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private QNACategory category;
    @Lob
    private String content;
    private boolean secret;
    private Long view;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @Builder
    public QNA(String title, QNACategory category, String content, boolean secret, Long view, User author) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.secret = secret;
        this.view = view;
        this.author = author;
    }

    public void increaseView() {
        view++;
    }

    public void update(QNARequest request, QNACategory category){
        this.title = request.getTitle();
        this.category = category;
        this.content = request.getContent();
        this.secret = request.isSecret();
        this.content = this.content.replaceAll("dummy/", FileType.QNA.directory());
    }
}
