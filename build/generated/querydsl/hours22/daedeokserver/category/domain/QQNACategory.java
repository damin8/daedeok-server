package hours22.daedeokserver.category.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QQNACategory is a Querydsl query type for QNACategory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QQNACategory extends EntityPathBase<QNACategory> {

    private static final long serialVersionUID = -2100496829L;

    public static final QQNACategory qNACategory = new QQNACategory("qNACategory");

    public final hours22.daedeokserver.common.domain.QBaseTime _super = new hours22.daedeokserver.common.domain.QBaseTime(this);

    public final StringPath category = createString("category");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QQNACategory(String variable) {
        super(QNACategory.class, forVariable(variable));
    }

    public QQNACategory(Path<? extends QNACategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQNACategory(PathMetadata metadata) {
        super(QNACategory.class, metadata);
    }

}

