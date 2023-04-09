package hours22.daedeokserver.category.dto;

import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.file.dto.FileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private String category;
    private String content;

    public Category toCategory() {
        content = content.replaceAll("dummy/", FileType.LECTURE_CATEGORY.directory());
        return new Category(category, content);
    }
}
