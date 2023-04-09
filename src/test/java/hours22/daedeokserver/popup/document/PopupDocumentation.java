package hours22.daedeokserver.popup.document;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentRequest;
import static hours22.daedeokserver.util.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class PopupDocumentation {
    public static RestDocumentationResultHandler list() {
        return document("popup/find",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler save() {
        return document("popup/save",
                getDocumentRequest(),
                getDocumentResponse()
        );
    }
}
