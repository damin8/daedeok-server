package hours22.daedeokserver.notice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTutorNoticeComment is a Querydsl query type for TutorNoticeComment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTutorNoticeComment extends EntityPathBase<TutorNoticeComment> {

    private static final long serialVersionUID = -1208343286L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTutorNoticeComment tutorNoticeComment = new QTutorNoticeComment("tutorNoticeComment");

    public final hours22.daedeokserver.common.domain.QBaseTime _super = new hours22.daedeokserver.common.domain.QBaseTime(this);

    public final hours22.daedeokserver.user.domain.QUser author;

    public final ListPath<TutorNoticeComment, QTutorNoticeComment> children = this.<TutorNoticeComment, QTutorNoticeComment>createList("children", TutorNoticeComment.class, QTutorNoticeComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QTutorNotice notice;

    public final QTutorNoticeComment parent;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QTutorNoticeComment(String variable) {
        this(TutorNoticeComment.class, forVariable(variable), INITS);
    }

    public QTutorNoticeComment(Path<? extends TutorNoticeComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTutorNoticeComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTutorNoticeComment(PathMetadata metadata, PathInits inits) {
        this(TutorNoticeComment.class, metadata, inits);
    }

    public QTutorNoticeComment(Class<? extends TutorNoticeComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new hours22.daedeokserver.user.domain.QUser(forProperty("author"), inits.get("author")) : null;
        this.notice = inits.isInitialized("notice") ? new QTutorNotice(forProperty("notice"), inits.get("notice")) : null;
        this.parent = inits.isInitialized("parent") ? new QTutorNoticeComment(forProperty("parent"), inits.get("parent")) : null;
    }

}

