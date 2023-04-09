package hours22.daedeokserver.category.dto;

import hours22.daedeokserver.category.domain.BoardCategory;
import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.category.domain.QNACategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String category;
    private String content;
    private LocalDateTime create_date;

    public static java.util.List<CategoryResponse> of(java.util.List<Category> categoryList) {
        java.util.List<CategoryResponse> categoryResponseList = new ArrayList<>();

        for (Category category : categoryList) {
            categoryResponseList.add(of(category));
        }

        return categoryResponseList;
    }

    public static CategoryResponse of(Category category) {
        return new CategoryResponse(category.getId(), category.getCategory(), category.getContent(), category.getCreateDate());
    }

    @Getter
    @AllArgsConstructor
    public static class List {
        private java.util.List<CategoryResponse> category_list;
        private Long total_count;
        private Integer total_page;
    }

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private String category;

        public static java.util.List<Summary> ofQNA(java.util.List<QNACategory> categoryList) {
            java.util.List<Summary> summaryList = new ArrayList<>();

            for (QNACategory category : categoryList) {
                summaryList.add(of(category));
            }

            return summaryList;
        }

        private static Summary of(QNACategory category) {
            return new Summary(category.getCategory());
        }

        public static java.util.List<Summary> ofBoard(java.util.List<BoardCategory> categoryList) {
            java.util.List<Summary> summaryList = new ArrayList<>();

            for (BoardCategory category : categoryList) {
                summaryList.add(of(category));
            }

            return summaryList;
        }

        private static Summary of(BoardCategory category) {
            return new Summary(category.getCategory());
        }
    }
}
