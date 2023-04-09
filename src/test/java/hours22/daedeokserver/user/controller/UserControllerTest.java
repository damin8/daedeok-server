package hours22.daedeokserver.user.controller;

import hours22.daedeokserver.common.MvcDocumentation;
import hours22.daedeokserver.common.WithMockCustomUser;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.ConflictException;
import hours22.daedeokserver.user.document.UserDocumentation;
import hours22.daedeokserver.user.domain.Role;
import hours22.daedeokserver.user.dto.*;
import hours22.daedeokserver.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest extends MvcDocumentation {
    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        super.setUp(webApplicationContext, restDocumentationContextProvider);
    }

    @Test
    void 로그인() throws Exception {
        LoginRequest loginRequest = new LoginRequest("01090117518", "password");
        LoginResponse loginResponse = new LoginResponse(1L, "김보성", Role.ROLE_MEMBER, "목사", 3, "first_division", "second_division", "phone_num", "access_token", "refresh_toke");

        given(userService.login(any())).willReturn(loginResponse);

        mockMvc.perform(post("/daedeok/user/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(UserDocumentation.login());
    }

    @Test
    void 토큰_재발급() throws Exception {
        TokenDto.Request request = new TokenDto.Request("accessToken", "refreshToken");
        TokenDto.Response response = new TokenDto.Response("accessToken", 1111111L);

        given(userService.reissue(any())).willReturn(response);

        mockMvc.perform(post("/daedeok/user/reissue")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(UserDocumentation.reissue());
    }

    @Test
    void 휴대폰_중복_확인() throws Exception {
        doNothing().when(userService).check(any());

        mockMvc.perform(get("/daedeok/user/check")
                        .param("phone_num", "01090117518")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(UserDocumentation.check());

        doThrow(new ConflictException(ErrorCode.VALUE_CONFLICT)).when(userService).check(any());

        mockMvc.perform(get("/daedeok/user/check")
                        .param("phone_num", "01090117518")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(UserDocumentation.checkFail());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 회원관리() throws Exception {
        UserResponse.Summary user1 = new UserResponse.Summary((long) 1, "name1", "duty", Role.ROLE_MEMBER, 1, "first_division", "second_division", "010-9011-7518");
        UserResponse.Summary user2 = new UserResponse.Summary((long) 2, "name2", "duty", Role.ROLE_MEMBER, 1, "first_division", "second_division", "010-9011-7518");
        UserResponse.Summary user3 = new UserResponse.Summary((long) 3, "name3", "duty", Role.ROLE_MEMBER, 1, "first_division", "second_division", "010-9011-7518");

        given(userService.find(any(), any())).willReturn(new UserResponse(Arrays.asList(user1, user2, user3), 11L, 4));

        mockMvc.perform(get("/daedeok/user/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("keyword", "찾을 키워드")
                        .param("page", "0")
                        .param("required_count", "3"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(UserDocumentation.find());
    }

    @Test
    void 직분찾기() throws Exception {

        given(userService.findDuty()).willReturn(new ArrayList<String>() {{
            add("목사");
            add("전도사");
            add("장로");
            add("권사");
            add("집사");
            add("청년");
            add("학생");
        }});

        mockMvc.perform(get("/daedeok/user/duty")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(UserDocumentation.findDuty());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 회원찾기() throws Exception {
        UserResponse.Summary user1 = new UserResponse.Summary((long) 1, "name1", "duty", Role.ROLE_MEMBER, 1, "first_division", "second_division", "010-9011-7518");

        given(userService.findSummary(any())).willReturn(user1);

        mockMvc.perform(get("/daedeok/user/admin/member/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(UserDocumentation.findMember());
    }

    @Test
    void 회원가입() throws Exception {
        UserRequest userRequest = new UserRequest("01090117517", "name", "password", "직분", "상위 소속", "하위 소속");

        given(userService.create(any())).willReturn(1L);

        mockMvc.perform(post("/daedeok/user")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(UserDocumentation.create());
    }

    @Test
    @WithMockCustomUser
    void 비밀번호_변경() throws Exception {
        PasswordRequest passwordRequest = new PasswordRequest("1234");

        doNothing().when(userService).updatePassword(any(), any());

        mockMvc.perform(put("/daedeok/user/password")
                        .content(objectMapper.writeValueAsString(passwordRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(UserDocumentation.updatePassword());
    }

    @Test
    @WithMockCustomUser
    void 정보_수정() throws Exception {
        UserRequest.Update updateRequest = new UserRequest.Update("김보성", "목사");

        doNothing().when(userService).update(any(), any());

        mockMvc.perform(put("/daedeok/user")
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(UserDocumentation.update());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 정보_수정_관리자() throws Exception {
        UserRequest.UpdateAdmin updateRequest = new UserRequest.UpdateAdmin("김보성", "목사", "1차 소속", "2차 소속");

        doNothing().when(userService).update(any(), any());

        mockMvc.perform(put("/daedeok/user/admin/member/{id}", 1L)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(UserDocumentation.updateAdmin());
    }

    @Test
    @WithMockCustomUser
    void 관리자_역할_수정() throws Exception {
        UserRequest.Update updateRequest = new UserRequest.Update("김보성", "목사");

        doNothing().when(userService).update(any(), any());

        mockMvc.perform(put("/daedeok/user")
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(UserDocumentation.update());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 비밀번호_초기화() throws Exception {
        doNothing().when(userService).updatePassword(any(Long.class));

        mockMvc.perform(put("/daedeok/user/admin/password/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(UserDocumentation.adminPassword());
    }

    @Test
    @WithMockCustomUser
    void 비밀번호찾기() throws Exception {
        PasswordRequest.Reset request = new PasswordRequest.Reset("01012345678", "newpassword");

        doNothing().when(userService).updatePassword(any(PasswordRequest.Reset.class));

        mockMvc.perform(put("/daedeok/user/password/reset")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(UserDocumentation.resetPassword());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 역할_바꾸기() throws Exception {
        UserRequest.UpdateRole request = new UserRequest.UpdateRole(1L, "ROLE_MEMBER");

        doNothing().when(userService).updateRole(any());

        mockMvc.perform(put("/daedeok/user/admin/role")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(UserDocumentation.updateRole());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 회원_탈퇴_관리자() throws Exception {
        doNothing().when(userService).deleteAdmin(any());

        mockMvc.perform(delete("/daedeok/user/admin/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(UserDocumentation.adminDelete());
    }

    @Test
    @WithMockCustomUser
    void 회원_탈퇴() throws Exception {
        doNothing().when(userService).deleteAdmin(any());

        mockMvc.perform(delete("/daedeok/user", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(UserDocumentation.delete());
    }
}