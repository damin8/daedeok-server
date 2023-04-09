package hours22.daedeokserver.notice.controller;

import hours22.daedeokserver.common.MvcDocumentation;
import hours22.daedeokserver.common.WithMockCustomUser;
import hours22.daedeokserver.notice.document.NoticeDocumentation;
import hours22.daedeokserver.notice.dto.*;
import hours22.daedeokserver.notice.service.NoticeService;
import hours22.daedeokserver.user.domain.Role;
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

@WebMvcTest(controllers = NoticeController.class)
class NoticeControllerTest extends MvcDocumentation {
    @MockBean
    private NoticeService noticeService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        super.setUp(webApplicationContext, restDocumentationContextProvider);
    }

    @Test
    void 리스트_조회() throws Exception {
        List<NoticeResponse.Summary> noticeList = new ArrayList<>();

        for (int i = 1; i <= 3; i++)
            noticeList.add(new NoticeResponse.Summary((long) i, 1L, "title", LocalDateTime.now()));

        given(noticeService.find(any(), any())).willReturn(new NoticeResponse.List(noticeList, 11L, 4));

        mockMvc.perform(get("/daedeok/notice")
                        .param("keyword", "찾을 키워드")
                        .param("page", "0")
                        .param("required_count", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(NoticeDocumentation.noticeList());
    }

    @Test
    void 상세_조회() throws Exception {
        NoticeResponse.Summary before = new NoticeResponse.Summary(1L, 1L, "title1", LocalDateTime.now());
        NoticeResponse.Summary after = new NoticeResponse.Summary(3L, 1L, "title3", LocalDateTime.now());

        given(noticeService.find(anyLong())).willReturn(new NoticeResponse(2L, 1L, "title2", "content2", LocalDateTime.now(), null, after, before));

        mockMvc.perform(get("/daedeok/notice/{id}", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(NoticeDocumentation.noticeDetail());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강사_공지_리스트_조회() throws Exception {
        List<TutorNoticeResponse.Summary> list = new ArrayList<>();

        for (int i = 1; i <= 3; i++)
            list.add(new TutorNoticeResponse.Summary((long) i, 1L, "title", "name", LocalDateTime.now()));

        given(noticeService.findTutorNotice(any(), any())).willReturn(new TutorNoticeResponse.List(list, 11L, 4));

        mockMvc.perform(get("/daedeok/notice/tutor")
                        .header("authorization", accessToken)
                        .param("keyword", "찾을 키워드")
                        .param("page", "0")
                        .param("required_count", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(NoticeDocumentation.tutorNoticeList());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강사_공지_상세_조회() throws Exception {
        TutorNoticeResponse.Summary before = new TutorNoticeResponse.Summary(1L, 1L, "title1", "name", LocalDateTime.now());
        TutorNoticeResponse.Summary after = new TutorNoticeResponse.Summary(3L, 1L, "title3", "name", LocalDateTime.now());
        TutorNoticeCommentResponse commentResponse2 = new TutorNoticeCommentResponse(2L, 1L, "author", "content", LocalDateTime.now(), null);
        TutorNoticeCommentResponse commentResponse3 = new TutorNoticeCommentResponse(3L, 1L, "author", "content", LocalDateTime.now(), null);
        TutorNoticeCommentResponse commentResponse1 = new TutorNoticeCommentResponse(1L, 1L, "author", "content", LocalDateTime.now(), Arrays.asList(commentResponse2, commentResponse3));
        TutorNoticeCommentResponse commentResponse5 = new TutorNoticeCommentResponse(5L, 1L, "author", "content", LocalDateTime.now(), null);
        TutorNoticeCommentResponse commentResponse6 = new TutorNoticeCommentResponse(6L, 1L, "author", "content", LocalDateTime.now(), null);
        TutorNoticeCommentResponse commentResponse4 = new TutorNoticeCommentResponse(4L, 1L, "author", "content", LocalDateTime.now(), Arrays.asList(commentResponse5, commentResponse6));

        given(noticeService.findTutorNotice(anyLong())).willReturn(new TutorNoticeResponse(2L, 1L, "title1", "content", "name", LocalDateTime.now(), Arrays.asList(commentResponse1, commentResponse4), null, after, before));

        mockMvc.perform(get("/daedeok/notice/tutor/{notice_id}", 2)
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(NoticeDocumentation.tutorNoticeDetail());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 생성() throws Exception {
        NoticeRequest noticeRequest = new NoticeRequest("title", "content", null);

        given(noticeService.save(any(NoticeRequest.class))).willReturn(1L);

        mockMvc.perform(post("/daedeok/notice")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(noticeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(NoticeDocumentation.save());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 삭제() throws Exception {
        doNothing().when(noticeService).delete(anyLong());

        mockMvc.perform(delete("/daedeok/notice/{id}", 1L)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(NoticeDocumentation.delete());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 수정() throws Exception {
        NoticeRequest noticeRequest = new NoticeRequest("title", "content", null);

        doNothing().when(noticeService).update(anyLong(), any(NoticeRequest.class));
        given(noticeService.save(any(NoticeRequest.class))).willReturn(1L);

        mockMvc.perform(put("/daedeok/notice/{id}", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(noticeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(NoticeDocumentation.update());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강사_공지_생성() throws Exception {
        TutorNoticeRequest request = new TutorNoticeRequest("title", "content", null);

        given(noticeService.save(any(), any(TutorNoticeRequest.class))).willReturn(1L);

        mockMvc.perform(post("/daedeok/notice/tutor")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(NoticeDocumentation.tutorNoticeSave());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강사_공지_삭제() throws Exception {
        doNothing().when(noticeService).deleteTutorNotice(any(), anyLong());

        mockMvc.perform(delete("/daedeok/notice/tutor/{id}", 1L)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(NoticeDocumentation.tutorNoticeDelete());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강사_공지_수정() throws Exception {
        TutorNoticeRequest request = new TutorNoticeRequest("title", "content", null);

        doNothing().when(noticeService).update(any(), anyLong(), any(TutorNoticeRequest.class));

        mockMvc.perform(put("/daedeok/notice/tutor/{id}", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(NoticeDocumentation.tutorNoticeUpdate());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강사_공지_댓글_생성() throws Exception {
        TutorNoticeCommentRequest request = new TutorNoticeCommentRequest(1L, "content");

        given(noticeService.save(any(), anyLong(), any(TutorNoticeCommentRequest.class))).willReturn(1L);

        mockMvc.perform(post("/daedeok/notice/tutor/{notice_id}/comment", 2L)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(NoticeDocumentation.tutorNoticeSaveComment());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강사_공지_댓글_삭제() throws Exception {
        doNothing().when(noticeService).deleteTutorNoticeComment(any(), anyLong());

        mockMvc.perform(delete("/daedeok/notice/tutor/comment/{id}", 1L)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(NoticeDocumentation.tutorNoticeDeleteComment());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강사_공지_댓글_수정() throws Exception {
        TutorNoticeCommentRequest request = new TutorNoticeCommentRequest(1L, "content");

        doNothing().when(noticeService).updateComment(any(), anyLong(), any());

        mockMvc.perform(put("/daedeok/notice/tutor/comment/{id}", 2L)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(NoticeDocumentation.tutorNoticeUpdateComment());
    }
}