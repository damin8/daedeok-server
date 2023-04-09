package hours22.daedeokserver.qna.dto;

import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.qna.domain.QNA;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class QNAResponse {
    private Long id;
    private Long user_id;
    private String title;
    private String category;
    private String content;
    private String author;
    private boolean secret;
    private LocalDateTime create_date;
    private Long view;
    private java.util.List<QNACommentResponse> comment_list;
    private java.util.List<FileDTO> attachment_list;
    private Summary after;
    private Summary before;

    public static QNAResponse of(QNA qna, java.util.List<QNACommentResponse> comment_list, java.util.List<FileDTO> attachment_list, Summary after, Summary before) {
        User user = qna.getAuthor();
        String category = qna.getCategory() == null ? null : qna.getCategory().getCategory();

        return new QNAResponse(qna.getId(), user.getId(), qna.getTitle(), category, qna.getContent(), user.getName(), qna.isSecret(), qna.getCreateDate(), qna.getView(), comment_list, attachment_list, after, before);
    }

    @Getter
    @AllArgsConstructor
    public static class List {
        private java.util.List<Summary> qna_list;
        private Long total_count;
        private Integer total_page;
    }

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private Long id;
        private Long user_id;
        private String title;
        private String category;
        private String author;
        private boolean secret;
        private LocalDateTime create_date;
        private Long view;

        public static Summary of(QNA qna) {
            if (qna == null)
                return null;

            String category = qna.getCategory() == null ? null : qna.getCategory().getCategory();

            return new Summary(qna.getId(), qna.getAuthor().getId(), qna.getTitle(), category, qna.getAuthor().getName(), qna.isSecret(), qna.getCreateDate(), qna.getView());
        }

        public static java.util.List<Summary> of(java.util.List<QNA> qnaList) {
            java.util.List<Summary> summaryList = new ArrayList<>();

            for (QNA qna : qnaList) {
                summaryList.add(of(qna));
            }

            return summaryList;
        }
    }
}
