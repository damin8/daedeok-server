package hours22.daedeokserver.faq.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFAQ is a Querydsl query type for FAQ
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFAQ extends EntityPathBase<FAQ> {

    private static final long serialVersionUID = 2010808873L;

    public static final QFAQ fAQ = new QFAQ("fAQ");

    public final hours22.daedeokserver.common.domain.QBaseTime _super = new hours22.daedeokserver.common.domain.QBaseTime(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QFAQ(String variable) {
        super(FAQ.class, forVariable(variable));
    }

    public QFAQ(Path<? extends FAQ> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFAQ(PathMetadata metadata) {
        super(FAQ.class, metadata);
    }

}

