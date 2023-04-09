package hours22.daedeokserver.lecture.dto.lecture;

import hours22.daedeokserver.category.domain.BoardCategory;
import hours22.daedeokserver.lecture.domain.Type;
import hours22.daedeokserver.lecture.domain.board.Board;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.lecture.domain.plan.Plan;
import hours22.daedeokserver.lecture.dto.plan.PlanResponse;
import hours22.daedeokserver.notice.domain.TutorNotice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainResponse {
    private List<LectureMain> lecture_list;
    private List<BoardList> board_list;
    private List<NoticeSummary> notice_list;

    @Getter
    @AllArgsConstructor
    public static class LectureMain {
        private int seq_num;
        private Long lecture_id;
        private String title;
        private java.util.List<Type> type;
        private LocalDateTime start_date;
        private LocalDateTime end_date;
        private Long student_limit;
        private Long student_num;

        public static LectureMain of(int seqNum, Lecture lecture, List<Plan> planList, Long studentNum) {
            return new LectureMain(seqNum, lecture.getId(), lecture.getTitle(), PlanResponse.getType(planList), lecture.getStartDate(), lecture.getEndDate(), lecture.getStudentLimit(), studentNum);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class BoardList {
        private Long lecture_id;
        private String lecture_title;
        private List<BoardSummary> boardSummaryList;

        public static BoardList of(Lecture lecture, List<Board> boardList) {
            return new BoardList(lecture.getId(), lecture.getTitle(), BoardSummary.of(boardList));
        }
    }

    @Getter
    @AllArgsConstructor
    public static class BoardSummary {
        private Long board_id;
        private String title;
        private String category;
        private LocalDateTime create_date;

        private static List<BoardSummary> of(List<Board> boardList) {
            List<BoardSummary> boardSummaryList = new ArrayList<>();

            for (Board board : boardList) {
                boardSummaryList.add(of(board));
            }

            return boardSummaryList;
        }

        private static BoardSummary of(Board board) {
            BoardCategory boardCategory = board.getCategory();

            if (boardCategory == null)
                boardCategory = new BoardCategory(null);

            return new BoardSummary(board.getId(), board.getTitle(), boardCategory.getCategory(), board.getCreateDate());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class NoticeSummary {
        private Long notice_id;
        private String title;
        private LocalDateTime create_date;

        public static List<NoticeSummary> of(List<TutorNotice> noticeList) {
            List<NoticeSummary> noticeSummaryList = new ArrayList<>();

            for (TutorNotice notice : noticeList) {
                noticeSummaryList.add(of(notice));
            }

            return noticeSummaryList;
        }

        private static NoticeSummary of(TutorNotice notice) {
            return new NoticeSummary(notice.getId(), notice.getTitle(), notice.getCreateDate());
        }
    }
}
