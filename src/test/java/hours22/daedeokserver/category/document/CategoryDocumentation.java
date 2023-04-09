package hours22.daedeokserver.category.document;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentRequest;
import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class CategoryDocumentation {
    public static RestDocumentationResultHandler list() {
        return document("category/find",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("keyword").description("제목으로 찾을 키워드 (필수 x)"),
                        parameterWithName("page").description("페이지"),
                        parameterWithName("required_count").description("필요한 요소의 개수")
                )
        );
    }

    public static RestDocumentationResultHandler all() {
        return document("category/find/all",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler allQ() {
        return document("category/find/all/qna",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler allB() {
        return document("category/find/all/board",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler findDetail() {
        return document("category/find/detail",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler save() {
        return document("category/save",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler delete() {
        return document("category/delete",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler update() {
        return document("category/update",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }
}

