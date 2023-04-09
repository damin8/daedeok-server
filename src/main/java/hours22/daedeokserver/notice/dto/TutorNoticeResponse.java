package hours22.daedeokserver.notice.dto;

import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.notice.domain.TutorNotice;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class TutorNoticeResponse {
    private Long id;
    private Long user_id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime create_date;
    private java.util.List<TutorNoticeCommentResponse> comment_list;
    private java.util.List<FileDTO> attachment_list;
    private Summary after;
    private Summary before;

    public static TutorNoticeResponse of(TutorNotice notice, java.util.List<TutorNoticeCommentResponse> comment_list, java.util.List<FileDTO> attachment_list, Summary after, Summary before) {
        return new TutorNoticeResponse(notice.getId(), notice.getUser().getId(), notice.getTitle(), notice.getContent(), notice.getUser().getName(), notice.getCreateDate(), comment_list, attachment_list, after, before);
    }

    @Getter
    @AllArgsConstructor
    public static class List {
        private java.util.List<Summary> notice_list;
        private Long total_count;
        private Integer total_page;
    }

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private Long id;
        private Long user_id;
        private String title;
        private String author;
        private LocalDateTime create_date;

        public static java.util.List<Summary> of(java.util.List<TutorNotice> noticeList) {
            java.util.List<Summary> summaryList = new ArrayList<>();

            for (TutorNotice notice : noticeList) {
                summaryList.add(of(notice));
            }

            return summaryList;
        }

        public static Summary of(TutorNotice notice) {
            if (notice == null)
                return null;

            return new Summary(notice.getId(), notice.getUser().getId(), notice.getTitle(), notice.getUser().getName(), notice.getCreateDate());
        }
    }
}
