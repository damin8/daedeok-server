package hours22.daedeokserver.lecture.domain.lecture;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLectureUser is a Querydsl query type for LectureUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QLectureUser extends EntityPathBase<LectureUser> {

    private static final long serialVersionUID = 712195364L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLectureUser lectureUser = new QLectureUser("lectureUser");

    public final hours22.daedeokserver.common.domain.QBaseTime _super = new hours22.daedeokserver.common.domain.QBaseTime(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath fileUrl = createString("fileUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QLecture lecture;

    public final EnumPath<hours22.daedeokserver.lecture.domain.Status> status = createEnum("status", hours22.daedeokserver.lecture.domain.Status.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final hours22.daedeokserver.user.domain.QUser user;

    public QLectureUser(String variable) {
        this(LectureUser.class, forVariable(variable), INITS);
    }

    public QLectureUser(Path<? extends LectureUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLectureUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLectureUser(PathMetadata metadata, PathInits inits) {
        this(LectureUser.class, metadata, inits);
    }

    public QLectureUser(Class<? extends LectureUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.lecture = inits.isInitialized("lecture") ? new QLecture(forProperty("lecture"), inits.get("lecture")) : null;
        this.user = inits.isInitialized("user") ? new hours22.daedeokserver.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

