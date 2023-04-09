package hours22.daedeokserver.qna.controller;

import hours22.daedeokserver.common.MvcDocumentation;
import hours22.daedeokserver.common.WithMockCustomUser;
import hours22.daedeokserver.qna.document.QNADocumentation;
import hours22.daedeokserver.qna.dto.QNACommentRequest;
import hours22.daedeokserver.qna.dto.QNACommentResponse;
import hours22.daedeokserver.qna.dto.QNARequest;
import hours22.daedeokserver.qna.dto.QNAResponse;
import hours22.daedeokserver.qna.service.QNAService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = QNAController.class)
class QNAControllerTest extends MvcDocumentation {
    @MockBean
    private QNAService qnaService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        super.setUp(webApplicationContext, restDocumentationContextProvider);
    }

    @Test
    void 리스트_조회() throws Exception {
        List<QNAResponse.Summary> qnaList = new ArrayList<>();

        for (int i = 1; i <= 3; i++)
            qnaList.add(new QNAResponse.Summary((long) i, 1L, "title", "category", "author", true, LocalDateTime.now(), 77L));

        given(qnaService.find(any(String.class), any())).willReturn(new QNAResponse.List(qnaList, 11L, 4));

        mockMvc.perform(get("/daedeok/qna")
                        .param("keyword", "찾을 키워드")
                        .param("page", "0")
                        .param("required_count", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(QNADocumentation.qnaList());
    }

    @Test
    void 상세_조회() throws Exception {
        QNAResponse.Summary before = new QNAResponse.Summary(1L, 1L, "title1", "category", "author", false, LocalDateTime.now(), 77L);
        QNAResponse.Summary after = new QNAResponse.Summary(3L, 1L, "title3", "category", "author", true, LocalDateTime.now(), 77L);
        QNACommentResponse commentResponse2 = new QNACommentResponse(2L, 1L, "author", "content", LocalDateTime.now(), null);
        QNACommentResponse commentResponse3 = new QNACommentResponse(3L, 1L, "author", "content", LocalDateTime.now(), null);
        QNACommentResponse commentResponse1 = new QNACommentResponse(1L, 1L, "author", "content", LocalDateTime.now(), Arrays.asList(commentResponse2, commentResponse3));
        QNACommentResponse commentResponse5 = new QNACommentResponse(5L, 1L, "author", "content", LocalDateTime.now(), null);
        QNACommentResponse commentResponse6 = new QNACommentResponse(6L, 1L, "author", "content", LocalDateTime.now(), null);
        QNACommentResponse commentResponse4 = new QNACommentResponse(4L, 1L, "author", "content", LocalDateTime.now(), Arrays.asList(commentResponse5, commentResponse6));

        given(qnaService.find(any(), anyLong())).willReturn(new QNAResponse(2L, 1L, "title1", "category", "content", "author", false, LocalDateTime.now(), 77L, Arrays.asList(commentResponse1, commentResponse4), null, after, before));

        mockMvc.perform(get("/daedeok/qna/{id}", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(QNADocumentation.qnaDetail());
    }

    @Test
    @WithMockCustomUser
    void 생성() throws Exception {
        QNARequest qnaRequest = new QNARequest("title", "category", "content", null, true);

        given(qnaService.save(any(), any(QNARequest.class))).willReturn(1L);

        mockMvc.perform(post("/daedeok/qna")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(qnaRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(QNADocumentation.save());
    }

    @Test
    @WithMockCustomUser
    void 삭제() throws Exception {
        doNothing().when(qnaService).delete(any(), anyLong());

        mockMvc.perform(delete("/daedeok/qna/{id}", 1)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(QNADocumentation.delete());
    }

    @Test
    @WithMockCustomUser
    void 수정() throws Exception {
        QNARequest qnaRequest = new QNARequest("title", "category", "content", null, true);

        doNothing().when(qnaService).update(any(), anyLong(), any());

        mockMvc.perform(put("/daedeok/qna/{id}", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(qnaRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(QNADocumentation.update());
    }

    @Test
    @WithMockCustomUser
    void 댓글_생성() throws Exception {
        QNACommentRequest commentRequest = new QNACommentRequest("title", null);

        given(qnaService.save(any(), any(), any(QNACommentRequest.class))).willReturn(1L);

        mockMvc.perform(post("/daedeok/qna/{qna_id}/comment", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(QNADocumentation.saveComment());
    }

    @Test
    @WithMockCustomUser
    void 댓글_삭제() throws Exception {
        doNothing().when(qnaService).deleteComment(any(), anyLong());

        mockMvc.perform(delete("/daedeok/qna/comment/{id}", 1)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(QNADocumentation.deleteComment());
    }

    @Test
    @WithMockCustomUser
    void 댓글_수정() throws Exception {
        QNACommentRequest commentRequest = new QNACommentRequest("title", null);

        doNothing().when(qnaService).updateComment(any(), anyLong(), any());

        mockMvc.perform(put("/daedeok/qna/comment/{id}", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(QNADocumentation.updateComment());
    }
}