package hours22.daedeokserver.lecture.controller;

import hours22.daedeokserver.category.domain.BoardCategory;
import hours22.daedeokserver.common.MvcDocumentation;
import hours22.daedeokserver.common.WithMockCustomUser;
import hours22.daedeokserver.division.dto.DivisionDTO;
import hours22.daedeokserver.lecture.document.LectureDocumentation;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.domain.Type;
import hours22.daedeokserver.lecture.domain.board.Board;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.lecture.domain.plan.Plan;
import hours22.daedeokserver.lecture.dto.board.BoardCommentRequest;
import hours22.daedeokserver.lecture.dto.board.BoardCommentResponse;
import hours22.daedeokserver.lecture.dto.board.BoardRequest;
import hours22.daedeokserver.lecture.dto.board.BoardResponse;
import hours22.daedeokserver.lecture.dto.handout.HandoutRequest;
import hours22.daedeokserver.lecture.dto.handout.HandoutResponse;
import hours22.daedeokserver.lecture.dto.lecture.*;
import hours22.daedeokserver.lecture.dto.plan.*;
import hours22.daedeokserver.lecture.service.LectureService;
import hours22.daedeokserver.lecture.service.LectureUserService;
import hours22.daedeokserver.lecture.service.PlanService;
import hours22.daedeokserver.user.domain.Role;
import hours22.daedeokserver.user.domain.User;
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

@WebMvcTest(controllers = LectureController.class)
class LectureControllerTest extends MvcDocumentation {
    @MockBean
    private LectureService lectureService;
    @MockBean
    private LectureUserService userService;
    @MockBean
    private PlanService planService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        super.setUp(webApplicationContext, restDocumentationContextProvider);
    }

    @Test
    void 리스트_조회() throws Exception {
        List<LectureResponse.Summary> list = new ArrayList<>();

        long studentLimit = -1L;
        for (int i = 1; i <= 3; i++, studentLimit += 20)
            list.add(new LectureResponse.Summary((long) i, "title", "category", "day", "time", LocalDateTime.now(), LocalDateTime.now(), studentLimit, 30L));

        given(lectureService.findCategory(any(Status.class), any(), any(), any())).willReturn(new LectureResponse.List(list, 11L, 4));

        mockMvc.perform(get("/daedeok/lecture")
                        .param("category", "category")
                        .param("status", "OPEN")
                        .param("keyword", "찾을 키워드")
                        .param("page", "0")
                        .param("required_count", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.list());
    }

    @Test
    @WithMockCustomUser
    void 수료_리스트_조회() throws Exception {
        List<LectureResponse.SummaryWithCertificate> list = new ArrayList<>();

        list.add(new LectureResponse.SummaryWithCertificate((long) 1, "title1", "day", "time", LocalDateTime.now(), LocalDateTime.now(), Status.COMPLETE, "http://aws.s3.com"));
        list.add(new LectureResponse.SummaryWithCertificate((long) 2, "title2", "day", "time", LocalDateTime.now(), LocalDateTime.now(), null, null));
        list.add(new LectureResponse.SummaryWithCertificate((long) 3, "title3", "day", "time", LocalDateTime.now(), LocalDateTime.now(), null, null));

        given(userService.findComplete(any(), any(), any())).willReturn(new LectureResponse.CertificateList(list, 11L, 4));

        mockMvc.perform(get("/daedeok/lecture/complete")
                        .param("keyword", "키워드")
                        .param("page", "0")
                        .param("required_count", "10")
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.completeList());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 종료_리스트_조회() throws Exception {
        List<LectureResponse.Summary> list = new ArrayList<>();

        long studentLimit = -1L;
        for (int i = 1; i <= 3; i++, studentLimit += 20)
            list.add(new LectureResponse.Summary((long) i, "title", "category", "day", "time", LocalDateTime.now(), LocalDateTime.now(), studentLimit, 30L));

        given(lectureService.findFinishCategory(any(), any(), any(), any())).willReturn(new LectureResponse.List(list, 11L, 4));

        mockMvc.perform(get("/daedeok/lecture/finish")
                        .param("category", "category")
                        .param("keyword", "찾을 키워드")
                        .param("page", "0")
                        .param("required_count", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.finish_list());
    }

    @Test
    @WithMockCustomUser
    void 메인_조회() throws Exception {
        Plan plan1 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.OFFLINE, "link", "introduce", null);
        Plan plan2 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.ONLINE, "link", "introduce", null);
        Plan plan3 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.ZOOM, "link", "introduce", null);
        Plan plan4 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.ZOOM, "link", "introduce", null);
        Plan plan5 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.ONLINE, "link", "introduce", null);
        Plan plan6 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.OFFLINE, "link", "introduce", null);
        Plan plan7 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.OFFLINE, "link", "introduce", null);
        List<Plan> planList = Arrays.asList(plan1, plan2, plan3, plan4, plan5, plan6, plan7);

        Lecture lecture = Lecture.builder()
                .title("title")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .studentLimit(10L)
                .build();

        Board board = Board.builder()
                .title("title")
                .category(new BoardCategory("category"))
                .build();

        MainResponse.LectureMain lectureMain = MainResponse.LectureMain.of(1, lecture, planList, 1L);
        MainResponse.BoardList boardList = MainResponse.BoardList.of(lecture, Arrays.asList(board));
        MainResponse.NoticeSummary noticeSummary = new MainResponse.NoticeSummary(1L, "title", LocalDateTime.now());
        MainResponse mainResponse = new MainResponse(Arrays.asList(lectureMain, lectureMain), Arrays.asList(boardList, boardList), Arrays.asList(noticeSummary, noticeSummary));

        given(lectureService.findMain(any())).willReturn(mainResponse);

        mockMvc.perform(get("/daedeok/lecture/main")
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.main());
    }

    @Test
    @WithMockCustomUser
    void 사이드바_조회() throws Exception {
        List<LectureResponse.Sidebar> sidebarList = new ArrayList<>();

        sidebarList.add(new LectureResponse.Sidebar(1L, "title1"));
        sidebarList.add(new LectureResponse.Sidebar(2L, "title2"));

        given(lectureService.findSidebar(any())).willReturn(sidebarList);

        mockMvc.perform(get("/daedeok/lecture/sidebar")
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.sidebar());
    }

    @Test
    @WithMockCustomUser
    void 수강신청_가능한_강의() throws Exception {
        List<LectureResponse.SummaryWithStatus> list = new ArrayList<>();

        list.add(new LectureResponse.SummaryWithStatus((long) 1, "title", "category", "day", "time", LocalDateTime.now(), LocalDateTime.now(), -1L, 30L, Status.IMPOSSIBLE));
        list.add(new LectureResponse.SummaryWithStatus((long) 2, "title", "category", "day", "time", LocalDateTime.now(), LocalDateTime.now(), 20L, 30L, Status.POSSIBLE));
        list.add(new LectureResponse.SummaryWithStatus((long) 3, "title", "category", "day", "time", LocalDateTime.now(), LocalDateTime.now(), 30L, 30L, Status.ING));

        given(lectureService.findCategory(any(User.class), any(String.class), any(String.class), any(Integer.class), any(Integer.class))).willReturn(new LectureResponse.PossibleList(list, 11L, 4));

        mockMvc.perform(get("/daedeok/lecture/possible")
                        .header("authorization", accessToken)
                        .param("category", "카테고리")
                        .param("keyword", "찾을 키워드")
                        .param("page", "0")
                        .param("required_count", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.possibleList());
    }

    @Test
    @WithMockCustomUser
    void 강의_게시판_리스트_조회() throws Exception {
        List<BoardResponse.Summary> list = new ArrayList<>();

        for (int i = 1; i <= 3; i++)
            list.add(new BoardResponse.Summary((long) i, 1L, "title", "category", "author", LocalDateTime.now(), 30L));

        given(lectureService.find(anyLong(), any())).willReturn(new BoardResponse.List(list, 11L, 4));

        mockMvc.perform(get("/daedeok/lecture/{lecture_id}/board", 1)
                        .header("authorization", accessToken)
                        .param("category", "카테고리")
                        .param("page", "0")
                        .param("required_count", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.boardList());
    }

    @Test
    @WithMockCustomUser
    void 출석_관리_강의_계획_리스트_조회() throws Exception {
        List<PlanResponse.Summary> list = new ArrayList<>();

        for (int i = 1; i <= 3; i++)
            list.add(new PlanResponse.Summary((long) i, (long) i, "title", LocalDateTime.now(), "day", Type.ZOOM, "link"));

        given(lectureService.findPlan(anyLong())).willReturn(list);

        mockMvc.perform(get("/daedeok/lecture/{lecture_id}/plan", 1)
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.planList());
    }

    @Test
    @WithMockCustomUser
    void 강의_출석_조회_학생() throws Exception {
        List<PlanResponse.Attendance> list = new ArrayList<>();

        list.add(new PlanResponse.Attendance((long) 1, (long) 1, "title", null));
        list.add(new PlanResponse.Attendance((long) 2, (long) 2, "title", null));
        list.add(new PlanResponse.Attendance((long) 3, (long) 3, "title", Status.COMPLETE));

        given(planService.findAttendance(any(), anyLong())).willReturn(list);

        mockMvc.perform(get("/daedeok/lecture/{lecture_id}/attendance", 1)
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.lectureAttendanceList());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 출석_관리_학생_리스트_조회() throws Exception {
        List<PlanUserResponse.Summary> list = new ArrayList<>();

        list.add(new PlanUserResponse.Summary((long) 1, "name1", "duty", "first_division", "second_division", "010-9011-7518", Status.COMPLETE));
        list.add(new PlanUserResponse.Summary((long) 2, "name2", "duty", "first_division", "second_division", "010-9011-7518", null));
        list.add(new PlanUserResponse.Summary((long) 3, "name3", "duty", "first_division", "second_division", "010-9011-7518", null));

        given(planService.find(anyLong())).willReturn(new PlanUserResponse(11L, Type.ZOOM, 1L, list));

        mockMvc.perform(get("/daedeok/lecture/plan/{plan_id}/user", 1)
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.planUserList());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강의_학생_관리() throws Exception {
        List<LectureUserResponse.Summary> list = new ArrayList<>();

        list.add(new LectureUserResponse.Summary((long) 1, "name1", "duty", "first_division", "second_division", "010-9011-7518", Status.FINISH, "http://naver.com"));
        list.add(new LectureUserResponse.Summary((long) 2, "name2", "duty", "first_division", "second_division", "010-9011-7518", Status.ING, null));
        list.add(new LectureUserResponse.Summary((long) 3, "name3", "duty", "first_division", "second_division", "010-9011-7518", Status.FINISH, null));

        given(userService.find(any())).willReturn(new LectureUserResponse(list, 11L));

        mockMvc.perform(get("/daedeok/lecture/{lecture_id}/user", 1)
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.lectureUserList());
    }

    @Test
    @WithMockCustomUser
    void 상세_조회() throws Exception {
        DivisionDTO temp = new DivisionDTO("소년부", Arrays.asList("1학년", "2학년", "3학년"));
        DivisionDTO dto = new DivisionDTO("소년부", Arrays.asList("1학년", "2학년", "3학년"));
        List<DivisionDTO> divisionList = Arrays.asList(temp, dto);

        PlanResponse planResponse1 = new PlanResponse(1L, "1주차", "tutor", "location", LocalDateTime.now());
        PlanResponse planResponse2 = new PlanResponse(2L, "2주차", "tutor", "location", LocalDateTime.now());
        PlanResponse planResponse3 = new PlanResponse(3L, "3주차", "tutor", "location", LocalDateTime.now());
        List<PlanResponse> planList = Arrays.asList(planResponse1, planResponse2, planResponse3);

        given(lectureService.find(anyLong())).willReturn(new LectureResponse(2L, 1L, "title", "category", "day", "time", LocalDateTime.now(), LocalDateTime.now(), 30L, 21L, "content", "이소명 강사님", Arrays.asList(Type.ZOOM, Type.OFFLINE, Type.ONLINE), divisionList, "참고자료", planList));

        mockMvc.perform(get("/daedeok/lecture/{id}", 2)
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.detail());
    }

    @Test
    @WithMockCustomUser
    void 강의제목_상태_조회() throws Exception {
        given(lectureService.findCustomSummary(anyLong())).willReturn(new LectureDTO("title", Status.OPEN));

        mockMvc.perform(get("/daedeok/lecture/{lecture_id}/custom", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.custom());
    }

    @Test
    @WithMockCustomUser
    void 주차_온라인_상세_조회() throws Exception {
        given(planService.findOnline(any(), anyLong())).willReturn(new OnlineResponse((long) 1, 1L, "title", "link", "introduce", 71.6F));

        mockMvc.perform(get("/daedeok/lecture/plan/{plan_id}/online", 1)
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.planOnline());
    }

    @Test
    @WithMockCustomUser
    void 상세_정보_조회() throws Exception {
        PlanResponse.SummaryWithDate planResponse1 = new PlanResponse.SummaryWithDate(1L, 1L, "title", "tutor", "location", Type.ZOOM, LocalDateTime.now());
        PlanResponse.SummaryWithDate planResponse2 = new PlanResponse.SummaryWithDate(2L, 2L, "title", "tutor", "location", Type.OFFLINE, LocalDateTime.now());
        PlanResponse.SummaryWithDate planResponse3 = new PlanResponse.SummaryWithDate(3L, 3L, "title", "tutor", "location", Type.ONLINE, LocalDateTime.now());
        List<PlanResponse.SummaryWithDate> planList = Arrays.asList(planResponse1, planResponse2, planResponse3);

        HandoutResponse handoutResponse1 = new HandoutResponse(1L, "file_name", "file_url");
        HandoutResponse handoutResponse2 = new HandoutResponse(2L, "file_name", "file_url");
        HandoutResponse handoutResponse3 = new HandoutResponse(3L, "file_name", "file_url");
        List<HandoutResponse> handoutList = Arrays.asList(handoutResponse1, handoutResponse2, handoutResponse3);

        given(lectureService.findDetailInfo(anyLong())).willReturn(new LectureResponse.WithHandout(handoutList, planList));

        mockMvc.perform(get("/daedeok/lecture/{id}/info", 2)
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.detailInfo());
    }

    @Test
    @WithMockCustomUser
    void 게시판_상세_조회() throws Exception {
        BoardResponse.Summary before = new BoardResponse.Summary(1L, 1L, "title1", "category", "author", LocalDateTime.now(), 11L);
        BoardResponse.Summary after = new BoardResponse.Summary(3L, 1L, "title3", "category", "author", LocalDateTime.now(), 11L);
        BoardCommentResponse commentResponse2 = new BoardCommentResponse(2L, 1L, "author", "content", LocalDateTime.now(), null);
        BoardCommentResponse commentResponse3 = new BoardCommentResponse(3L, 1L, "author", "content", LocalDateTime.now(), null);
        BoardCommentResponse commentResponse1 = new BoardCommentResponse(1L, 1L, "author", "content", LocalDateTime.now(), Arrays.asList(commentResponse2, commentResponse3));
        BoardCommentResponse commentResponse5 = new BoardCommentResponse(5L, 1L, "author", "content", LocalDateTime.now(), null);
        BoardCommentResponse commentResponse6 = new BoardCommentResponse(6L, 1L, "author", "content", LocalDateTime.now(), null);
        BoardCommentResponse commentResponse4 = new BoardCommentResponse(4L, 1L, "author", "content", LocalDateTime.now(), Arrays.asList(commentResponse5, commentResponse6));

        given(lectureService.findBoard(any(), anyLong())).willReturn(new BoardResponse(2L, 1L, "title", "category", "content", "author", LocalDateTime.now(), 30L, Arrays.asList(commentResponse1, commentResponse4), null, after, before));

        mockMvc.perform(get("/daedeok/lecture/board/{id}", 2)
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.boardDetail());
    }

    @Test
    @WithMockCustomUser
    void 생성() throws Exception {
        DivisionDTO temp = new DivisionDTO("소년부", Arrays.asList("1학년", "2학년", "3학년"));
        DivisionDTO dto = new DivisionDTO("소년부", Arrays.asList("1학년", "2학년", "3학년"));
        List<DivisionDTO> divisionList = Arrays.asList(temp, dto);

        PlanRequest request1 = new PlanRequest(1L, 1L, "1주차", "tutor", "location", "2021-08-03", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        PlanRequest request2 = new PlanRequest(2L, 2L, "1주차", "tutor", "location", "2021-08-03", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        PlanRequest request3 = new PlanRequest(3L, 3L, "1주차", "tutor", "location", "2021-08-03", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        List<PlanRequest> planList = Arrays.asList(request1, request2, request3);

        HandoutRequest handoutRequest1 = new HandoutRequest("파일 이름1", "파일 경로1");
        HandoutRequest handoutRequest2 = new HandoutRequest("파일 이름2", "파일 경로2");
        List<HandoutRequest> handoutList = Arrays.asList(handoutRequest1, handoutRequest2);

        LectureRequest request = new LectureRequest("title", "content", "category", "day", "time", divisionList, -1L, "참고자료", handoutList, planList);

        given(lectureService.save(any(), any(LectureRequest.class))).willReturn(1L);

        mockMvc.perform(post("/daedeok/lecture")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(LectureDocumentation.save());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강의_철회() throws Exception {

        doNothing().when(lectureService).cancel(any(), anyLong(), any());

        mockMvc.perform(delete("/daedeok/lecture/cancel/{user_id}/{lecture_id}", 1L, 2L)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.cancel());
    }

    @Test
    @WithMockCustomUser
    void 강의_철회_학생() throws Exception {

        doNothing().when(lectureService).cancel(any(), any());

        mockMvc.perform(delete("/daedeok/lecture/cancel/{lecture_id}", 2L)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.cancelMember());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 수정용Get() throws Exception {
        DivisionDTO temp = new DivisionDTO("소년부", Arrays.asList("1학년", "2학년", "3학년"));
        DivisionDTO dto = new DivisionDTO("소년부", Arrays.asList("1학년", "2학년", "3학년"));
        List<DivisionDTO> divisionList = Arrays.asList(temp, dto);

        PlanRequest request1 = new PlanRequest(1L, 1L, "1주차", "tutor", "location", "2021-08-03", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        PlanRequest request2 = new PlanRequest(2L, 2L, "1주차", "tutor", "location", "2021-08-03", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        PlanRequest request3 = new PlanRequest(3L, 3L, "1주차", "tutor", "location", "2021-08-03", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        List<PlanRequest> planList = Arrays.asList(request1, request2, request3);

        HandoutRequest handoutRequest1 = new HandoutRequest("파일 이름1", "파일 경로1");
        HandoutRequest handoutRequest2 = new HandoutRequest("파일 이름2", "파일 경로2");
        List<HandoutRequest> handoutList = Arrays.asList(handoutRequest1, handoutRequest2);

        LectureRequest request = new LectureRequest("title", "content", "category", "day", "time", divisionList, -1L, "참고자료", handoutList, planList);

        given(lectureService.find(any(User.class), anyLong())).willReturn(request);

        mockMvc.perform(get("/daedeok/lecture/{id}/update", 1)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LectureDocumentation.findUpdate());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 수정() throws Exception {
        DivisionDTO temp = new DivisionDTO("소년부", Arrays.asList("1학년", "2학년", "3학년"));
        DivisionDTO dto = new DivisionDTO("소년부", Arrays.asList("1학년", "2학년", "3학년"));
        List<DivisionDTO> divisionList = Arrays.asList(temp, dto);

        PlanRequest.Update request1 = new PlanRequest.Update(1L, 1L, "1주차", "tutor", "location", "2021-08-03", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        PlanRequest.Update request2 = new PlanRequest.Update(2L, 2L, "1주차", "tutor", "location", "2021-08-03", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        PlanRequest.Update request3 = new PlanRequest.Update(3L, 3L, "1주차", "tutor", "location", "2021-08-03", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        List<PlanRequest.Update> planList = Arrays.asList(request1, request2, request3);

        HandoutRequest handoutRequest1 = new HandoutRequest("파일 이름1", "파일 경로1");
        HandoutRequest handoutRequest2 = new HandoutRequest("파일 이름2", "파일 경로2");
        List<HandoutRequest> newList = Arrays.asList(handoutRequest1, handoutRequest2);
        List<HandoutRequest> deleteList = Arrays.asList(handoutRequest1, handoutRequest2);
        HandoutRequest.Update handoutList = new HandoutRequest.Update(newList, deleteList);
        LectureRequest.Update request = new LectureRequest.Update("title", "content", "day", "time", "category", divisionList, -1L, "참고자료", handoutList, Arrays.asList(1L, 2L), planList);

        doNothing().when(lectureService).update(any(), any(), any(LectureRequest.Update.class));

        mockMvc.perform(put("/daedeok/lecture/{id}", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.update());
    }

    @Test
    @WithMockCustomUser
    void 수강신청() throws Exception {
        doNothing().when(lectureService).join(any(), anyLong());

        mockMvc.perform(post("/daedeok/lecture/join/{lecture_id}", 1)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.join());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강의_종료() throws Exception {
        doNothing().when(lectureService).finish(any(), anyLong());

        mockMvc.perform(post("/daedeok/lecture/finish/{lecture_id}", 1)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.finish());
    }

    @Test
    @WithMockCustomUser
    void 게시판_생성() throws Exception {
        BoardRequest request = new BoardRequest("title", "category", "content", null);

        given(lectureService.save(any(), anyLong(), any(BoardRequest.class))).willReturn(1L);

        mockMvc.perform(post("/daedeok/lecture/{lecture_id}/board", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(LectureDocumentation.saveBoard());
    }

    @Test
    @WithMockCustomUser
    void 게시판_삭제() throws Exception {

        doNothing().when(lectureService).deleteBoard(any(), any());

        mockMvc.perform(delete("/daedeok/lecture/board/{id}", 1)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.deleteBoard());
    }

    @Test
    @WithMockCustomUser
    void 게시판_수정() throws Exception {
        BoardRequest request = new BoardRequest("title", "category", "content", null);

        doNothing().when(lectureService).update(any(), anyLong(), any(BoardRequest.class));

        mockMvc.perform(put("/daedeok/lecture/board/{id}", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.updateBoard());
    }

    @Test
    @WithMockCustomUser
    void 사용자_영상_저장() throws Exception {
        DurationRequest request = new DurationRequest(21.72F);

        doNothing().when(planService).saveDuration(any(), anyLong(), any());

        mockMvc.perform(post("/daedeok/lecture/plan/{plan_id}/online", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.saveDuration());
    }

    @Test
    @WithMockCustomUser
    void 게시판_댓글_생성() throws Exception {
        BoardCommentRequest request = new BoardCommentRequest(1L, "content");

        given(lectureService.save(any(), anyLong(), any(BoardCommentRequest.class))).willReturn(1L);

        mockMvc.perform(post("/daedeok/lecture/board/{board_id}/comment", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(LectureDocumentation.saveBoardComment());
    }

    @Test
    @WithMockCustomUser
    void 게시판_댓글_삭제() throws Exception {

        doNothing().when(lectureService).deleteComment(any(), any());

        mockMvc.perform(delete("/daedeok/lecture/board/comment/{id}", 1)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.deleteBoardComment());
    }

    @Test
    @WithMockCustomUser
    void 게시판_댓글_수정() throws Exception {
        BoardCommentRequest request = new BoardCommentRequest(1L, "content");

        doNothing().when(lectureService).updateComment(any(), anyLong(), any());

        mockMvc.perform(put("/daedeok/lecture/board/comment/{id}", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.updateBoardComment());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강의_학생_출석() throws Exception {
        doNothing().when(planService).attendance(anyLong(), any());

        mockMvc.perform(post("/daedeok/lecture/plan/{plan_id}/user", 1)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(new AttendanceRequest(-1L)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.savePlanUser());
    }

    @Test
    @WithMockCustomUser
    void 출석_학생() throws Exception {
        doNothing().when(planService).attendance(any(User.class), anyLong());

        mockMvc.perform(post("/daedeok/lecture/plan/{plan_id}/attendance", 1)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.memberAttendance());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void 강의_삭제() throws Exception {
        doNothing().when(lectureService).delete(any(), any());

        mockMvc.perform(delete("/daedeok/lecture/{id}", 1)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LectureDocumentation.delete());
    }
}