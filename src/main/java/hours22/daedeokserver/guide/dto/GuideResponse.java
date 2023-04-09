package hours22.daedeokserver.guide.dto;

import hours22.daedeokserver.guide.domain.Guide;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class GuideResponse {
    private Long id;
    private Long user_id;
    private String title;
    private String content;
    private Summary after;
    private Summary before;

    public static GuideResponse of(Guide guide, Summary after, Summary before) {
        return new GuideResponse(guide.getId(), 1L, guide.getTitle(), guide.getContent(), after, before);
    }

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private Long id;
        private Long user_id;
        private String title;

        public static java.util.List<Summary> of(java.util.List<Guide> guideList) {
            java.util.List<Summary> summaryList = new ArrayList<>();

            for (Guide guide : guideList) {
                summaryList.add(of(guide));
            }

            return summaryList;
        }

        public static Summary of(Guide guide) {
            if (guide == null)
                return null;

            return new Summary(guide.getId(), 1L, guide.getTitle());
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
