package hours22.daedeokserver.lecture.domain.board;

import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.lecture.dto.board.BoardCommentRequest;
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
public class BoardComment extends BaseTime {
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
    @JoinColumn(name = "board_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "parent")
    private List<BoardComment> children = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BoardComment parent;

    public void setParent(BoardComment comment){
        if(this.parent != null)
            this.parent.getChildren().remove(this);

        this.parent = comment;
        comment.getChildren().add(this);
    }

    @Builder
    public BoardComment(String content, User author, Board board) {
        this.content = content;
        this.author = author;
        this.board = board;
    }

    public void update(BoardCommentRequest request){
        this.content = request.getContent();
    }
}
