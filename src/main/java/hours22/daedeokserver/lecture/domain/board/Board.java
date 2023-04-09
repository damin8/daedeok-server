package hours22.daedeokserver.lecture.domain.board;

import hours22.daedeokserver.category.domain.BoardCategory;
import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.lecture.dto.board.BoardRequest;
import hours22.daedeokserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BoardCategory category;
    @Lob
    private String content;
    private Long view;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lecture lecture;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @Builder
    public Board(String title, BoardCategory category, String content, Long view, Lecture lecture, User author) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.view = view;
        this.lecture = lecture;
        this.author = author;
    }

    public void increaseView() {
        view++;
    }

    public void update(BoardRequest request, BoardCategory category){
        this.title = request.getTitle();
        this.category = category;
        this.content = request.getContent();
        this.content = this.content.replaceAll("dummy/", FileType.LECTURE_BOARD.directory());
    }
}
