package hours22.daedeokserver.notice.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TutorNoticeQueryDSL {
    @Autowired
    private final JPAQueryFactory jpaQueryFactory;
    private final QTutorNoticeComment comment = QTutorNoticeComment.tutorNoticeComment;

    public TutorNoticeQueryDSL(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<TutorNoticeComment> findCommentList(TutorNotice notice) {
        return jpaQueryFactory.select(comment)
                .from(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.notice.eq(notice))
                .distinct()
                .fetch();
    }
}
