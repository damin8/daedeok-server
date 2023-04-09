package hours22.daedeokserver.lecture.dto.board;

import hours22.daedeokserver.category.domain.BoardCategory;
import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.lecture.domain.board.Board;
import hours22.daedeokserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private Long user_id;
    private String title;
    private String category;
    private String content;
    private String author;
    private LocalDateTime create_date;
    private Long view;
    private java.util.List<BoardCommentResponse> comment_list;
    private java.util.List<FileDTO> attachment_list;
    private Summary after;
    private Summary before;

    public static BoardResponse of(Board board, java.util.List<BoardCommentResponse> comment_list, java.util.List<FileDTO> attachment_list, Summary after, Summary before) {
        User user = board.getAuthor();
        BoardCategory boardCategory = board.getCategory();

        if (boardCategory == null)
            boardCategory = new BoardCategory(null);

        return new BoardResponse(board.getId(), user.getId(), board.getTitle(), boardCategory.getCategory(), board.getContent(), user.getName(), board.getCreateDate(), board.getView(), comment_list, attachment_list, after, before);
    }

    @Getter
    @AllArgsConstructor
    public static class List {
        private java.util.List<Summary> board_list;
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
        private LocalDateTime create_date;
        private Long view;

        public static java.util.List<Summary> of(java.util.List<Board> boardList) {
            java.util.List<Summary> summaryList = new ArrayList<>();

            for (Board board : boardList) {
                summaryList.add(of(board));
            }

            return summaryList;
        }

        public static Summary of(Board board) {
            if (board == null)
                return null;

            User user = board.getAuthor();
            BoardCategory boardCategory = board.getCategory();

            if (boardCategory == null)
                boardCategory = new BoardCategory(null);

            return new Summary(board.getId(), user.getId(), board.getTitle(), boardCategory.getCategory(), user.getName(), board.getCreateDate(), board.getView());
        }
    }
}
