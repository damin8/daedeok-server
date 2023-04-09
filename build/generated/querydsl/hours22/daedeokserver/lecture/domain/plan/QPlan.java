package hours22.daedeokserver.lecture.domain.plan;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlan is a Querydsl query type for Plan
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPlan extends EntityPathBase<Plan> {

    private static final long serialVersionUID = -952143879L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlan plan = new QPlan("plan");

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduce = createString("introduce");

    public final hours22.daedeokserver.lecture.domain.lecture.QLecture lecture;

    public final StringPath link = createString("link");

    public final StringPath location = createString("location");

    public final StringPath title = createString("title");

    public final StringPath tutor = createString("tutor");

    public final EnumPath<hours22.daedeokserver.lecture.domain.Type> type = createEnum("type", hours22.daedeokserver.lecture.domain.Type.class);

    public final NumberPath<Long> week = createNumber("week", Long.class);

    public QPlan(String variable) {
        this(Plan.class, forVariable(variable), INITS);
    }

    public QPlan(Path<? extends Plan> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlan(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlan(PathMetadata metadata, PathInits inits) {
        this(Plan.class, metadata, inits);
    }

    public QPlan(Class<? extends Plan> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.lecture = inits.isInitialized("lecture") ? new hours22.daedeokserver.lecture.domain.lecture.QLecture(forProperty("lecture"), inits.get("lecture")) : null;
    }

}

