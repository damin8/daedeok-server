package hours22.daedeokserver.faq.dto;

import hours22.daedeokserver.faq.domain.FAQ;
import hours22.daedeokserver.file.dto.FileDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class FAQResponse {
    private Long id;
    private Long user_id;
    private String title;
    private String content;
    private java.util.List<FileDTO> attachment_list;
    private Summary after;
    private Summary before;

    public static FAQResponse of(FAQ faq, java.util.List<FileDTO> attachment_list, Summary after, Summary before) {
        return new FAQResponse(faq.getId(), 1L, faq.getTitle(), faq.getContent(), attachment_list, after, before);
    }

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private Long id;
        private Long user_id;
        private String title;

        public static java.util.List<Summary> of(java.util.List<FAQ> faqList) {
            java.util.List<Summary> summaryList = new ArrayList<>();

            for (FAQ faq : faqList) {
                summaryList.add(of(faq));
            }

            return summaryList;
        }

        public static Summary of(FAQ faq) {
            if (faq == null)
                return null;

            return new Summary(faq.getId(), 1L, faq.getTitle());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class List {
        private java.util.List<Summary> faq_list;
        private Long total_count;
        private Integer total_page;
    }
}
