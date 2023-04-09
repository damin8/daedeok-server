package hours22.daedeokserver.popup.controller;

import hours22.daedeokserver.common.MvcDocumentation;
import hours22.daedeokserver.common.WithMockCustomUser;
import hours22.daedeokserver.popup.PopupController;
import hours22.daedeokserver.popup.PopupRequest;
import hours22.daedeokserver.popup.PopupResponse;
import hours22.daedeokserver.popup.PopupService;
import hours22.daedeokserver.popup.document.PopupDocumentation;
import hours22.daedeokserver.user.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PopupController.class)
class PopupControllerTest extends MvcDocumentation {
    @MockBean
    private PopupService service;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        super.setUp(webApplicationContext, restDocumentationContextProvider);
    }

    @Test
    @WithMockCustomUser
    void 팝업_찾기() throws Exception {
        List<PopupResponse.Summary> list = new ArrayList<>();
        list.add(new PopupResponse.Summary(1L, "https://aws.s3.com", "link"));
        list.add(new PopupResponse.Summary(2L, "https://aws.s4.com", "link"));
        list.add(new PopupResponse.Summary(3L, "https://aws.s5.com", "link"));

        given(service.find()).willReturn(new PopupResponse(list));

        mockMvc.perform(get("/daedeok/popup")
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(PopupDocumentation.list());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 팝업_생성() throws Exception {
        List<PopupRequest> list = new ArrayList<>();
        list.add(new PopupRequest("https://aws.s3.com", "link"));
        List<Long> idList = new ArrayList<>();
        idList.add(1L);


        doNothing().when(service).save(any());

        mockMvc.perform(put("/daedeok/popup")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(new PopupRequest.Update(idList, list)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(PopupDocumentation.save());
    }
}