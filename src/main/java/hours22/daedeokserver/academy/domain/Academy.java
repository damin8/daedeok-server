package hours22.daedeokserver.academy.domain;

import hours22.daedeokserver.academy.dto.AcademyRequest;
import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.file.dto.FileType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Academy extends BaseTime {
    @Id
    private Long id;
    @Lob
    private String content;

    public Academy(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public void update(AcademyRequest request) {
        this.content = request.getContent();
        this.content = this.content.replaceAll("dummy/", FileType.AC_INFO.directory());
    }
}
