package hours22.daedeokserver.faq.controller;

import hours22.daedeokserver.common.MvcDocumentation;
import hours22.daedeokserver.common.WithMockCustomUser;
import hours22.daedeokserver.faq.document.FAQDocumentation;
import hours22.daedeokserver.faq.dto.FAQRequest;
import hours22.daedeokserver.faq.dto.FAQResponse;
import hours22.daedeokserver.faq.service.FAQService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FAQController.class)
class FAQControllerTest extends MvcDocumentation {
    @MockBean
    private FAQService faqService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        super.setUp(webApplicationContext, restDocumentationContextProvider);
    }

    @Test
    void 리스트_조회() throws Exception {
        List<FAQResponse.Summary> faqList = new ArrayList<>();

        for (int i = 1; i <= 3; i++)
            faqList.add(new FAQResponse.Summary((long) i, 1L, "title"));

        given(faqService.find(any(), any())).willReturn(new FAQResponse.List(faqList, 11L, 4));

        mockMvc.perform(get("/daedeok/faq")
                        .param("keyword", "찾을 키워드")
                        .param("page", "0")
                        .param("required_count", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(FAQDocumentation.faqList());
    }

    @Test
    void 상세_조회() throws Exception {
        FAQResponse.Summary before = new FAQResponse.Summary(1L, 1L, "title1");
        FAQResponse.Summary after = new FAQResponse.Summary(3L, 1L, "title3");

        given(faqService.find(anyLong())).willReturn(new FAQResponse(2L, 1L, "title2", "content2", null, after, before));

        mockMvc.perform(get("/daedeok/faq/{id}", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(FAQDocumentation.faqDetail());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 생성() throws Exception {
        FAQRequest faqRequest = new FAQRequest("title", "content", null);

        given(faqService.save(any())).willReturn(1L);

        mockMvc.perform(post("/daedeok/faq")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(faqRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(FAQDocumentation.save());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 수정() throws Exception {
        FAQRequest faqRequest = new FAQRequest("title", "content", null);

        doNothing().when(faqService).update(anyLong(), any());

        mockMvc.perform(put("/daedeok/faq/{id}", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(faqRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(FAQDocumentation.update());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 삭제() throws Exception {
        doNothing().when(faqService).delete(anyLong());

        mockMvc.perform(delete("/daedeok/faq/{id}", 1)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(FAQDocumentation.delete());
    }
}