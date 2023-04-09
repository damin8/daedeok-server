package hours22.daedeokserver.lecture.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardCommentQueryDSL {
    @Autowired
    private final JPAQueryFactory jpaQueryFactory;
    private final QBoardComment comment = QBoardComment.boardComment;

    public BoardCommentQueryDSL(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<BoardComment> findCommentList(Board board) {
        return jpaQueryFactory.select(comment)
                .from(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.board.eq(board))
                .distinct()
                .fetch();
    }
}
