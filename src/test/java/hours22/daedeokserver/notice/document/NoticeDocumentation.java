package hours22.daedeokserver.notice.document;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentRequest;
import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class NoticeDocumentation {
    public static RestDocumentationResultHandler noticeList() {
        return document("notice/find",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("keyword").description("제목으로 찾을 키워드 (필수 x)"),
                        parameterWithName("page").description("페이지"),
                        parameterWithName("required_count").description("필요한 요소의 개수")
                )
        );
    }

    public static RestDocumentationResultHandler noticeDetail() {
        return document("notice/find/detail",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler save() {
        return document("notice/save",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler delete() {
        return document("notice/delete",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler update() {
        return document("notice/update",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler tutorNoticeList() {
        return document("notice/tutor/find",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("keyword").description("제목으로 찾을 키워드 (필수 x)"),
                        parameterWithName("page").description("페이지"),
                        parameterWithName("required_count").description("필요한 요소의 개수")
                )
        );
    }

    public static RestDocumentationResultHandler tutorNoticeDetail() {
        return document("notice/tutor/find/detail",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler tutorNoticeSave() {
        return document("notice/tutor/save",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler tutorNoticeDelete() {
        return document("notice/tutor/delete",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler tutorNoticeUpdate() {
        return document("notice/tutor/update",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler tutorNoticeSaveComment() {
        return document("notice/tutor/save/comment",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("notice_id").description("공지사항 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler tutorNoticeDeleteComment() {
        return document("notice/tutor/delete/comment",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler tutorNoticeUpdateComment() {
        return document("notice/tutor/update/comment",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }
}
