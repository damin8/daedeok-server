package hours22.daedeokserver.qna.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQNA is a Querydsl query type for QNA
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QQNA extends EntityPathBase<QNA> {

    private static final long serialVersionUID = -839953047L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQNA qNA = new QQNA("qNA");

    public final hours22.daedeokserver.common.domain.QBaseTime _super = new hours22.daedeokserver.common.domain.QBaseTime(this);

    public final hours22.daedeokserver.user.domain.QUser author;

    public final hours22.daedeokserver.category.domain.QQNACategory category;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath secret = createBoolean("secret");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final NumberPath<Long> view = createNumber("view", Long.class);

    public QQNA(String variable) {
        this(QNA.class, forVariable(variable), INITS);
    }

    public QQNA(Path<? extends QNA> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQNA(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQNA(PathMetadata metadata, PathInits inits) {
        this(QNA.class, metadata, inits);
    }

    public QQNA(Class<? extends QNA> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new hours22.daedeokserver.user.domain.QUser(forProperty("author"), inits.get("author")) : null;
        this.category = inits.isInitialized("category") ? new hours22.daedeokserver.category.domain.QQNACategory(forProperty("category")) : null;
    }

}

