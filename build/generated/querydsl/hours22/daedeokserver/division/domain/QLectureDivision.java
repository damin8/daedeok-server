package hours22.daedeokserver.division.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLectureDivision is a Querydsl query type for LectureDivision
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QLectureDivision extends EntityPathBase<LectureDivision> {

    private static final long serialVersionUID = 1034345085L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLectureDivision lectureDivision = new QLectureDivision("lectureDivision");

    public final QDivision division;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final hours22.daedeokserver.lecture.domain.lecture.QLecture lecture;

    public QLectureDivision(String variable) {
        this(LectureDivision.class, forVariable(variable), INITS);
    }

    public QLectureDivision(Path<? extends LectureDivision> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLectureDivision(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLectureDivision(PathMetadata metadata, PathInits inits) {
        this(LectureDivision.class, metadata, inits);
    }

    public QLectureDivision(Class<? extends LectureDivision> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.division = inits.isInitialized("division") ? new QDivision(forProperty("division")) : null;
        this.lecture = inits.isInitialized("lecture") ? new hours22.daedeokserver.lecture.domain.lecture.QLecture(forProperty("lecture"), inits.get("lecture")) : null;
    }

}

