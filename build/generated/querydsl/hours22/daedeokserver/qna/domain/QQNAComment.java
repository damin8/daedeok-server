package hours22.daedeokserver.qna.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQNAComment is a Querydsl query type for QNAComment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QQNAComment extends EntityPathBase<QNAComment> {

    private static final long serialVersionUID = 1917909174L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQNAComment qNAComment = new QQNAComment("qNAComment");

    public final hours22.daedeokserver.common.domain.QBaseTime _super = new hours22.daedeokserver.common.domain.QBaseTime(this);

    public final hours22.daedeokserver.user.domain.QUser author;

    public final ListPath<QNAComment, QQNAComment> children = this.<QNAComment, QQNAComment>createList("children", QNAComment.class, QQNAComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QQNAComment parent;

    public final QQNA question;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QQNAComment(String variable) {
        this(QNAComment.class, forVariable(variable), INITS);
    }

    public QQNAComment(Path<? extends QNAComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQNAComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQNAComment(PathMetadata metadata, PathInits inits) {
        this(QNAComment.class, metadata, inits);
    }

    public QQNAComment(Class<? extends QNAComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new hours22.daedeokserver.user.domain.QUser(forProperty("author"), inits.get("author")) : null;
        this.parent = inits.isInitialized("parent") ? new QQNAComment(forProperty("parent"), inits.get("parent")) : null;
        this.question = inits.isInitialized("question") ? new QQNA(forProperty("question"), inits.get("question")) : null;
    }

}

