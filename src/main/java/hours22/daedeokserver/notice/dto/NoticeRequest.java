package hours22.daedeokserver.notice.dto;

import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NoticeRequest {
    private String title;
    private String content;
    private List<FileDTO> attachment_list;

    public Notice toNotice() {
        content = content.replaceAll("dummy/", FileType.GLOBAL_NOTICE.directory());
        return new Notice(title, content);
    }
}
