package hours22.daedeokserver.category.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardCategory is a Querydsl query type for BoardCategory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBoardCategory extends EntityPathBase<BoardCategory> {

    private static final long serialVersionUID = -1704246011L;

    public static final QBoardCategory boardCategory = new QBoardCategory("boardCategory");

    public final hours22.daedeokserver.common.domain.QBaseTime _super = new hours22.daedeokserver.common.domain.QBaseTime(this);

    public final StringPath category = createString("category");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QBoardCategory(String variable) {
        super(BoardCategory.class, forVariable(variable));
    }

    public QBoardCategory(Path<? extends BoardCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardCategory(PathMetadata metadata) {
        super(BoardCategory.class, metadata);
    }

}

