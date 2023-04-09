package hours22.daedeokserver.faq.document;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentRequest;
import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class FAQDocumentation {
    public static RestDocumentationResultHandler faqList() {
        return document("faq/find",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("keyword").description("제목으로 찾을 키워드 (필수 x)"),
                        parameterWithName("page").description("페이지"),
                        parameterWithName("required_count").description("필요한 요소의 개수")
                )
        );
    }

    public static RestDocumentationResultHandler faqDetail() {
        return document("faq/find/detail",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler save() {
        return document("faq/save",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler update() {
        return document("faq/update",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler delete() {
        return document("faq/delete",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }
}
