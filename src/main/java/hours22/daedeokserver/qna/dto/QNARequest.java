package hours22.daedeokserver.qna.dto;

import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.category.domain.QNACategory;
import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.qna.domain.QNA;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QNARequest {
    private String title;
    private String category;
    private String content;
    private List<FileDTO> attachment_list;
    private boolean secret;

    public QNA toQNA(User user, QNACategory category) {
        content = content.replaceAll("dummy/", FileType.QNA.directory());
        return QNA.builder()
                .author(user)
                .title(title)
                .category(category)
                .content(content)
                .secret(secret)
                .view(0L)
                .build();
    }
}
