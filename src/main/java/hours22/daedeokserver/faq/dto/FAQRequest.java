package hours22.daedeokserver.faq.dto;

import hours22.daedeokserver.faq.domain.FAQ;
import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.file.dto.FileType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FAQRequest {
    private String title;
    private String content;
    private List<FileDTO> attachment_list;

    public FAQ toFAQ() {
        content = content.replaceAll("dummy/", FileType.FAQ.directory());
        return FAQ.builder()
                .title(title)
                .content(content)
                .build();
    }
}
