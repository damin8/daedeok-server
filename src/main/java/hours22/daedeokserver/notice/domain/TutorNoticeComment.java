package hours22.daedeokserver.notice.domain;

import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.notice.dto.TutorNoticeCommentRequest;
import hours22.daedeokserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TutorNoticeComment extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TutorNotice notice;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "parent")
    private List<TutorNoticeComment> children = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TutorNoticeComment parent;

    public void setParent(TutorNoticeComment comment) {
        if (this.parent != null)
            this.parent.getChildren().remove(this);

        this.parent = comment;
        comment.getChildren().add(this);
    }

    @Builder
    public TutorNoticeComment(String content, User author, TutorNotice notice) {
        this.content = content;
        this.author = author;
        this.notice = notice;
    }

    public void update(TutorNoticeCommentRequest request){
        this.content = request.getContent();
    }
}
