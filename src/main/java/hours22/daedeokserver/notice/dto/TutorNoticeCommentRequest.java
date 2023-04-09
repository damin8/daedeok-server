package hours22.daedeokserver.notice.dto;

import hours22.daedeokserver.notice.domain.TutorNotice;
import hours22.daedeokserver.notice.domain.TutorNoticeComment;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TutorNoticeCommentRequest {
    private Long parent_id;
    private String content;

    public TutorNoticeComment toTutorNoticeComment(User user, TutorNotice tutorNotice){
        return TutorNoticeComment.builder()
                .author(user)
                .content(content)
                .notice(tutorNotice)
                .build();
    }
}
