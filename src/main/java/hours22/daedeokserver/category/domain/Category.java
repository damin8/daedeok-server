package hours22.daedeokserver.category.domain;

import hours22.daedeokserver.category.dto.CategoryRequest;
import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.file.dto.FileType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private String content;

    public Category(String category, String content) {
        this.category = category;
        this.content = content;
    }

    public void update(CategoryRequest request){
        this.category = request.getCategory();
        this.content = request.getContent();
        this.content = this.content.replaceAll("dummy/", FileType.LECTURE_CATEGORY.directory());
    }
}
