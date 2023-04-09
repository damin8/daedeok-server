package hours22.daedeokserver.lecture.dto.board;

import hours22.daedeokserver.lecture.domain.board.Board;
import hours22.daedeokserver.lecture.domain.board.BoardComment;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardCommentRequest {
    private Long parent_id;
    private String content;

    public BoardComment toComment(User user, Board board) {
        return BoardComment.builder()
                .author(user)
                .board(board)
                .content(content)
                .build();
    }
}
