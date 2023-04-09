package hours22.daedeokserver.division.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDivision is a Querydsl query type for Division
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDivision extends EntityPathBase<Division> {

    private static final long serialVersionUID = -6926661L;

    public static final QDivision division = new QDivision("division");

    public final StringPath firstDivision = createString("firstDivision");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath secondDivision = createString("secondDivision");

    public QDivision(String variable) {
        super(Division.class, forVariable(variable));
    }

    public QDivision(Path<? extends Division> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDivision(PathMetadata metadata) {
        super(Division.class, metadata);
    }

}

