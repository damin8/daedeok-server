package hours22.daedeokserver.academy.controller;

import hours22.daedeokserver.academy.document.AcademyDocumentation;
import hours22.daedeokserver.academy.dto.AcademyRequest;
import hours22.daedeokserver.academy.dto.AcademyResponse;
import hours22.daedeokserver.academy.service.AcademyService;
import hours22.daedeokserver.common.MvcDocumentation;
import hours22.daedeokserver.common.WithMockCustomUser;
import hours22.daedeokserver.user.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AcademyController.class)
class AcademyControllerTest extends MvcDocumentation {
    @MockBean
    private AcademyService academyService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        super.setUp(webApplicationContext, restDocumentationContextProvider);
    }

    @Test
    void 비전_상세조회() throws Exception {
        AcademyResponse response = new AcademyResponse("content", LocalDateTime.now());

        given(academyService.findEduvision()).willReturn(response);

        mockMvc.perform(get("/daedeok/acinfo/eduvision")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(AcademyDocumentation.findVisionDetail());
    }

    @Test
    void 상세조회() throws Exception {
        AcademyResponse response = new AcademyResponse("content", LocalDateTime.now());

        given(academyService.find()).willReturn(response);

        mockMvc.perform(get("/daedeok/acinfo/introduce")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(AcademyDocumentation.findDetail());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 수정() throws Exception {
        AcademyRequest request = new AcademyRequest("content");

        doNothing().when(academyService).update(any());

        mockMvc.perform(put("/daedeok/acinfo/introduce")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(AcademyDocumentation.update());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 비젼_수정() throws Exception {
        AcademyRequest request = new AcademyRequest("content");

        doNothing().when(academyService).updateEduvision(any());

        mockMvc.perform(put("/daedeok/acinfo/eduvision")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(AcademyDocumentation.updateVision());
    }
}