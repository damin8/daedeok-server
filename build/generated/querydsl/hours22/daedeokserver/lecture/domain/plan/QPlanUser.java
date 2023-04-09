package hours22.daedeokserver.lecture.domain.plan;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanUser is a Querydsl query type for PlanUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPlanUser extends EntityPathBase<PlanUser> {

    private static final long serialVersionUID = 969747300L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanUser planUser = new QPlanUser("planUser");

    public final NumberPath<Float> duration = createNumber("duration", Float.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPlan plan;

    public final EnumPath<hours22.daedeokserver.lecture.domain.Status> status = createEnum("status", hours22.daedeokserver.lecture.domain.Status.class);

    public final hours22.daedeokserver.user.domain.QUser user;

    public QPlanUser(String variable) {
        this(PlanUser.class, forVariable(variable), INITS);
    }

    public QPlanUser(Path<? extends PlanUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanUser(PathMetadata metadata, PathInits inits) {
        this(PlanUser.class, metadata, inits);
    }

    public QPlanUser(Class<? extends PlanUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plan = inits.isInitialized("plan") ? new QPlan(forProperty("plan"), inits.get("plan")) : null;
        this.user = inits.isInitialized("user") ? new hours22.daedeokserver.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

