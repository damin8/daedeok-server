package hours22.daedeokserver.notice.dto;

import hours22.daedeokserver.notice.domain.TutorNoticeComment;
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
public class TutorNoticeCommentResponse {
    private Long id;
    private Long user_id;
    private String author;
    private String content;
    private LocalDateTime create_date;
    private List<TutorNoticeCommentResponse> children;

    public static List<TutorNoticeCommentResponse> of(List<TutorNoticeComment> commentList) {
        List<TutorNoticeCommentResponse> commentResponseList = new ArrayList<>();
        Map<Long, TutorNoticeCommentResponse> map = new HashMap<>();
        commentList.forEach(comment -> {
            TutorNoticeCommentResponse response = of(comment);
            map.put(response.getId(), response);

            if (comment.getParent() != null)
                map.get(comment.getParent().getId()).getChildren().add(response);

            else
                commentResponseList.add(response);
        });

        return commentResponseList;
    }

    private static TutorNoticeCommentResponse of(TutorNoticeComment comment) {
        User user = comment.getAuthor();

        return new TutorNoticeCommentResponse(comment.getId(), user.getId(), user.getName(), comment.getContent(), comment.getCreateDate(), new ArrayList<>());
    }
}
