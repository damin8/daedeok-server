package hours22.daedeokserver.guide.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QGuide is a Querydsl query type for Guide
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGuide extends EntityPathBase<Guide> {

    private static final long serialVersionUID = -1371499031L;

    public static final QGuide guide = new QGuide("guide");

    public final hours22.daedeokserver.common.domain.QBaseTime _super = new hours22.daedeokserver.common.domain.QBaseTime(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QGuide(String variable) {
        super(Guide.class, forVariable(variable));
    }

    public QGuide(Path<? extends Guide> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGuide(PathMetadata metadata) {
        super(Guide.class, metadata);
    }

}

