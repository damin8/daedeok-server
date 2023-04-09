package hours22.daedeokserver.lecture.domain.lecture;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLecture is a Querydsl query type for Lecture
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QLecture extends EntityPathBase<Lecture> {

    private static final long serialVersionUID = -2093719111L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLecture lecture = new QLecture("lecture");

    public final hours22.daedeokserver.common.domain.QBaseTime _super = new hours22.daedeokserver.common.domain.QBaseTime(this);

    public final hours22.daedeokserver.category.domain.QCategory category;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath day = createString("day");

    public final ListPath<hours22.daedeokserver.division.domain.LectureDivision, hours22.daedeokserver.division.domain.QLectureDivision> divisionList = this.<hours22.daedeokserver.division.domain.LectureDivision, hours22.daedeokserver.division.domain.QLectureDivision>createList("divisionList", hours22.daedeokserver.division.domain.LectureDivision.class, hours22.daedeokserver.division.domain.QLectureDivision.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath reference = createString("reference");

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final EnumPath<hours22.daedeokserver.lecture.domain.Status> status = createEnum("status", hours22.daedeokserver.lecture.domain.Status.class);

    public final NumberPath<Long> studentLimit = createNumber("studentLimit", Long.class);

    public final StringPath time = createString("time");

    public final StringPath title = createString("title");

    public final hours22.daedeokserver.user.domain.QUser tutor;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final NumberPath<Long> view = createNumber("view", Long.class);

    public QLecture(String variable) {
        this(Lecture.class, forVariable(variable), INITS);
    }

    public QLecture(Path<? extends Lecture> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLecture(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLecture(PathMetadata metadata, PathInits inits) {
        this(Lecture.class, metadata, inits);
    }

    public QLecture(Class<? extends Lecture> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new hours22.daedeokserver.category.domain.QCategory(forProperty("category")) : null;
        this.tutor = inits.isInitialized("tutor") ? new hours22.daedeokserver.user.domain.QUser(forProperty("tutor"), inits.get("tutor")) : null;
    }

}

