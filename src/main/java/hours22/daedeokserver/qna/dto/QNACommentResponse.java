package hours22.daedeokserver.qna.dto;

import hours22.daedeokserver.qna.domain.QNAComment;
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
public class QNACommentResponse {
    private Long id;
    private Long user_id;
    private String author;
    private String content;
    private LocalDateTime create_date;
    private List<QNACommentResponse> children;

    public static List<QNACommentResponse> of(List<QNAComment> commentList) {
        List<QNACommentResponse> commentResponseList = new ArrayList<>();
        Map<Long, QNACommentResponse> map = new HashMap<>();
        commentList.forEach(comment -> {
            QNACommentResponse response = of(comment);
            map.put(response.getId(), response);

            if (comment.getParent() != null)
                map.get(comment.getParent().getId()).getChildren().add(response);

            else
                commentResponseList.add(response);
        });

        return commentResponseList;
    }

    private static QNACommentResponse of(QNAComment comment) {
        User user = comment.getAuthor();

        return new QNACommentResponse(comment.getId(), user.getId(), user.getName(), comment.getContent(), comment.getCreateDate(), new ArrayList<>());
    }
}
