package hours22.daedeokserver.qna.dto;

import hours22.daedeokserver.qna.domain.QNA;
import hours22.daedeokserver.qna.domain.QNAComment;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QNACommentRequest {
    private String content;
    private Long parent_id;

    public QNAComment toComment(User user, QNA qna) {
        return QNAComment.builder()
                .content(content)
                .author(user)
                .question(qna)
                .build();
    }
}
