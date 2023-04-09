package hours22.daedeokserver.qna.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QNAQueryDSL {
    @Autowired
    private final JPAQueryFactory jpaQueryFactory;
    private final QQNAComment comment = QQNAComment.qNAComment;

    public QNAQueryDSL(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<QNAComment> findCommentList(QNA qna) {
        return jpaQueryFactory.select(comment)
                .from(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.question.eq(qna))
                .distinct()
                .fetch();
    }
}
