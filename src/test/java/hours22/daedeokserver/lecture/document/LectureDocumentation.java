package hours22.daedeokserver.lecture.document;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentRequest;
import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class LectureDocumentation {
    public static RestDocumentationResultHandler list() {
        return document("lecture/find",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("category").description("수업 카테고리 설정"),
                        parameterWithName("status").description("강의 상태 (진행 : OPEN, 종료 : FINISH, 수료 : COMPLETE)"),
                        parameterWithName("keyword").description("제목으로 찾을 키워드 (필수 x)"),
                        parameterWithName("page").description("페이지"),
                        parameterWithName("required_count").description("필요한 요소의 개수")
                )
        );
    }

    public static RestDocumentationResultHandler finish_list() {
        return document("lecture/find/finish",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("category").description("수업 카테고리 설정"),
                        parameterWithName("keyword").description("제목으로 찾을 키워드 (필수 x)"),
                        parameterWithName("page").description("페이지"),
                        parameterWithName("required_count").description("필요한 요소의 개수")
                )
        );
    }

    public static RestDocumentationResultHandler custom() {
        return document("lecture/custom",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lecture_id").description("강의 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler completeList() {
        return document("lecture/find/complete",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("keyword").description("제목으로 찾을 키워드 (필수 x)"),
                        parameterWithName("page").description("페이지"),
                        parameterWithName("required_count").description("필요한 요소의 개수")
                )
        );
    }

    public static RestDocumentationResultHandler main() {
        return document("lecture/main",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler sidebar() {
        return document("lecture/find/sidebar",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler possibleList() {
        return document("lecture/find/possible",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("category").description("카테고리 (필수 x)"),
                        parameterWithName("keyword").description("제목으로 찾을 키워드 (필수 x)"),
                        parameterWithName("page").description("페이지"),
                        parameterWithName("required_count").description("필요한 요소의 개수")
                )
        );
    }

    public static RestDocumentationResultHandler boardList() {
        return document("lecture/find/board",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lecture_id").description("강의 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler planList() {
        return document("lecture/find/plan",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lecture_id").description("강의 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler lectureAttendanceList() {
        return document("lecture/find/attendance",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lecture_id").description("강의 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler planUserList() {
        return document("lecture/find/plan/user",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("plan_id").description("주차 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler planOnline() {
        return document("lecture/find/plan/online",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("plan_id").description("주차 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler lectureUserList() {
        return document("lecture/find/user",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lecture_id").description("강의 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler detail() {
        return document("lecture/find/detail",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler findUpdate() {
        return document("lecture/find/update",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler detailInfo() {
        return document("lecture/find/detail/info",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler boardDetail() {
        return document("lecture/find/detail/board",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler save() {
        return document("lecture/save",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler cancel() {
        return document("lecture/cancel",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("user_id").description("사용자 고유 번호"),
                        parameterWithName("lecture_id").description("강의 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler cancelMember() {
        return document("lecture/cancel/member",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lecture_id").description("강의 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler update() {
        return document("lecture/update",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler join() {
        return document("lecture/join",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lecture_id").description("강의 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler finish() {
        return document("lecture/finish",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lecture_id").description("강의 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler saveBoard() {
        return document("lecture/save/board",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lecture_id").description("강의 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler deleteBoard() {
        return document("lecture/delete/board",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler updateBoard() {
        return document("lecture/update/board",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("게시글 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler saveDuration() {
        return document("lecture/save/duration",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("plan_id").description("주차 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler savePlanUser() {
        return document("lecture/save/plan/user",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("plan_id").description("주차 고유 번호")
                ),
                requestFields(
                        fieldWithPath("user_id").description("사용자 고유 번호 (전체일 경우 -1)")
                )
        );
    }

    public static RestDocumentationResultHandler memberAttendance() {
        return document("lecture/save/plan/user/attendance",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("plan_id").description("주차 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler saveBoardComment() {
        return document("lecture/save/board/comment",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("board_id").description("게시판 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler deleteBoardComment() {
        return document("lecture/delete/board/comment",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler updateBoardComment() {
        return document("lecture/update/board/comment",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("게시판 댓글 고유 번호")
                )
        );
    }

    public static RestDocumentationResultHandler delete() {
        return document("lecture/delete",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }
}
