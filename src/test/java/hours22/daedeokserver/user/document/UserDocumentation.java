package hours22.daedeokserver.user.document;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentRequest;
import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class UserDocumentation {
    public static RestDocumentationResultHandler login() {
        return document("user/login",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler reissue() {
        return document("user/reissue",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler check() {
        return document("user/check",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler find() {
        return document("user/find",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler findDuty() {
        return document("user/find/duty",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler findMember() {
        return document("user/find/member",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler checkFail() {
        return document("user/check-fail",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler create() {
        return document("user/create",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler updatePassword() {
        return document("user/update/password",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler update() {
        return document("user/update",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler updateAdmin() {
        return document("user/update/admin",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler adminPassword() {
        return document("user/admin/update",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler updateRole() {
        return document("user/admin/update/role",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler resetPassword() {
        return document("user/admin/reset/password",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler adminDelete() {
        return document("user/admin/delete",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler delete() {
        return document("user/delete",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }
}
