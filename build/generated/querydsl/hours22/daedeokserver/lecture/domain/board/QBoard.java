package hours22.daedeokserver.lecture.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 960802825L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final hours22.daedeokserver.common.domain.QBaseTime _super = new hours22.daedeokserver.common.domain.QBaseTime(this);

    public final hours22.daedeokserver.user.domain.QUser author;

    public final hours22.daedeokserver.category.domain.QBoardCategory category;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final hours22.daedeokserver.lecture.domain.lecture.QLecture lecture;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final NumberPath<Long> view = createNumber("view", Long.class);

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new hours22.daedeokserver.user.domain.QUser(forProperty("author"), inits.get("author")) : null;
        this.category = inits.isInitialized("category") ? new hours22.daedeokserver.category.domain.QBoardCategory(forProperty("category")) : null;
        this.lecture = inits.isInitialized("lecture") ? new hours22.daedeokserver.lecture.domain.lecture.QLecture(forProperty("lecture"), inits.get("lecture")) : null;
    }

}

