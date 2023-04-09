package hours22.daedeokserver.lecture.domain.handout;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHandout is a Querydsl query type for Handout
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QHandout extends EntityPathBase<Handout> {

    private static final long serialVersionUID = -1904181573L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHandout handout = new QHandout("handout");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final hours22.daedeokserver.lecture.domain.lecture.QLecture lecture;

    public final StringPath name = createString("name");

    public final StringPath url = createString("url");

    public QHandout(String variable) {
        this(Handout.class, forVariable(variable), INITS);
    }

    public QHandout(Path<? extends Handout> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHandout(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHandout(PathMetadata metadata, PathInits inits) {
        this(Handout.class, metadata, inits);
    }

    public QHandout(Class<? extends Handout> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.lecture = inits.isInitialized("lecture") ? new hours22.daedeokserver.lecture.domain.lecture.QLecture(forProperty("lecture"), inits.get("lecture")) : null;
    }

}

