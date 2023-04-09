package hours22.daedeokserver.qna.domain;

import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.qna.dto.QNACommentRequest;
import hours22.daedeokserver.user.domain.User;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QNAComment extends BaseTime {
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
    @JoinColumn(name = "question_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private QNA question;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "parent")
    private List<QNAComment> children = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private QNAComment parent;

    public void setParent(QNAComment comment){
        if(this.parent != null)
            this.parent.getChildren().remove(this);

        this.parent = comment;
        comment.getChildren().add(this);
    }

    @Builder
    public QNAComment(String content, User author, QNA question) {
        this.content = content;
        this.author = author;
        this.question = question;
    }

    public void update(QNACommentRequest commentRequest){
        this.content = commentRequest.getContent();
    }
}
