package hours22.daedeokserver.category.domain;

import hours22.daedeokserver.common.domain.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QNACategory extends BaseTime {
    @Id
    private String category;

    public QNACategory(String category, String content) {
        this.category = category;
    }
}
