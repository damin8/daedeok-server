package hours22.daedeokserver.lecture.dto.board;

import hours22.daedeokserver.category.domain.BoardCategory;
import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.lecture.domain.board.Board;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardRequest {
    private String title;
    private String category;
    private String content;
    private List<FileDTO> attachment_list;

    public Board toBoard(User user, Lecture lecture, BoardCategory category) {
        content = content.replaceAll("dummy/", FileType.LECTURE_BOARD.directory());
        return Board.builder()
                .view(0L)
                .title(title)
                .category(category)
                .content(content)
                .author(user)
                .lecture(lecture)
                .build();
    }
}
