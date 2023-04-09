package hours22.daedeokserver.notice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTutorNotice is a Querydsl query type for TutorNotice
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTutorNotice extends EntityPathBase<TutorNotice> {

    private static final long serialVersionUID = 1284677525L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTutorNotice tutorNotice = new QTutorNotice("tutorNotice");

    public final hours22.daedeokserver.common.domain.QBaseTime _super = new hours22.daedeokserver.common.domain.QBaseTime(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final hours22.daedeokserver.user.domain.QUser user;

    public QTutorNotice(String variable) {
        this(TutorNotice.class, forVariable(variable), INITS);
    }

    public QTutorNotice(Path<? extends TutorNotice> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTutorNotice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTutorNotice(PathMetadata metadata, PathInits inits) {
        this(TutorNotice.class, metadata, inits);
    }

    public QTutorNotice(Class<? extends TutorNotice> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new hours22.daedeokserver.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

