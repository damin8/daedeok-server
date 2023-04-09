package hours22.daedeokserver.academy.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAcademy is a Querydsl query type for Academy
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAcademy extends EntityPathBase<Academy> {

    private static final long serialVersionUID = -1986735255L;

    public static final QAcademy academy = new QAcademy("academy");

    public final hours22.daedeokserver.common.domain.QBaseTime _super = new hours22.daedeokserver.common.domain.QBaseTime(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QAcademy(String variable) {
        super(Academy.class, forVariable(variable));
    }

    public QAcademy(Path<? extends Academy> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAcademy(PathMetadata metadata) {
        super(Academy.class, metadata);
    }

}

