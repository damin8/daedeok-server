package hours22.daedeokserver.lecture.dto.board;

import hours22.daedeokserver.lecture.domain.board.BoardComment;
import hours22.daedeokserver.qna.domain.QNAComment;
import hours22.daedeokserver.qna.dto.QNACommentResponse;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class BoardCommentResponse {
    private Long id;
    private Long user_id;
    private String author;
    private String content;
    private LocalDateTime create_date;
    private List<BoardCommentResponse> children;

    public static List<BoardCommentResponse> of(List<BoardComment> commentList) {
        List<BoardCommentResponse> commentResponseList = new ArrayList<>();
        Map<Long, BoardCommentResponse> map = new HashMap<>();
        commentList.forEach(comment -> {
            BoardCommentResponse response = of(comment);
            map.put(response.getId(), response);

            if (comment.getParent() != null)
                map.get(comment.getParent().getId()).getChildren().add(response);

            else
                commentResponseList.add(response);
        });

        return commentResponseList;
    }

    private static BoardCommentResponse of(BoardComment comment) {
        User user = comment.getAuthor();

        return new BoardCommentResponse(comment.getId(), user.getId(), user.getName(), comment.getContent(), comment.getCreateDate(), new ArrayList<>());
    }
}
