package hours22.daedeokserver.notice.dto;

import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.notice.domain.TutorNotice;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TutorNoticeRequest {
    private String title;
    private String content;
    private List<FileDTO> attachment_list;

    public TutorNotice toTutorNotice(User user){
        content = content.replaceAll("dummy/", FileType.TUTOR_NOTICE.directory());
        return TutorNotice.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
