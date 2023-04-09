package hours22.daedeokserver.qna.document;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentRequest;
import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class QNADocumentation {
    public static RestDocumentationResultHandler qnaList() {
        return document("qna/find",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("keyword").description("제목으로 찾을 키워드 (필수 x)"),
                        parameterWithName("page").description("페이지"),
                        parameterWithName("required_count").description("필요한 요소의 개수")
                )
        );
    }

    public static RestDocumentationResultHandler qnaDetail() {
        return document("qna/find/detail",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler save() {
        return document("qna/save",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler delete() {
        return document("qna/delete",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler update() {
        return document("qna/update",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler saveComment() {
        return document("qna/save/comment",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("qna_id").description("질문과 답변 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler deleteComment() {
        return document("qna/delete/comment",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler updateComment() {
        return document("qna/update/comment",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }
}
