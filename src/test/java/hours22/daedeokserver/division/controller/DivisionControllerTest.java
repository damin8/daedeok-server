package hours22.daedeokserver.division.controller;

import hours22.daedeokserver.common.MvcDocumentation;
import hours22.daedeokserver.common.WithMockCustomUser;
import hours22.daedeokserver.division.document.DivisionDocumentation;
import hours22.daedeokserver.division.dto.DivisionDTO;
import hours22.daedeokserver.division.dto.SecondDivisionDTO;
import hours22.daedeokserver.division.service.DivisionService;
import hours22.daedeokserver.user.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DivisionController.class)
class DivisionControllerTest extends MvcDocumentation {
    @MockBean
    private DivisionService service;
    private DivisionDTO dto;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        super.setUp(webApplicationContext, restDocumentationContextProvider);

        dto = new DivisionDTO("고등부", Arrays.asList("1학년", "2학년", "3학년"));
    }

    @Test
    @WithMockCustomUser
    void 소속_찾기() throws Exception {
        DivisionDTO temp = new DivisionDTO("소년부", Arrays.asList("1학년", "2학년", "3학년"));
        List<DivisionDTO> response = Arrays.asList(dto, temp);

        given(service.find()).willReturn(response);

        mockMvc.perform(get("/daedeok/division")
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(DivisionDocumentation.find());
    }

    @Test
    @WithMockCustomUser
    void 상위_소속_찾기() throws Exception {
        SecondDivisionDTO secondDivisionDTO = new SecondDivisionDTO(1L, "하위 소속");
        DivisionDTO.UpdateResponse response = new DivisionDTO.UpdateResponse("상위 소속", Arrays.asList(secondDivisionDTO));
        given(service.find(any())).willReturn(response);

        mockMvc.perform(get("/daedeok/division/detail")
                        .param("first_division", "고등부")
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(DivisionDocumentation.findDetail());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 소속_삭제() throws Exception {
        doNothing().when(service).delete(any());

        mockMvc.perform(delete("/daedeok/division")
                        .param("name", "first_division")
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(DivisionDocumentation.delete());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 소속_추가() throws Exception {
        doNothing().when(service).save(any());

        mockMvc.perform(post("/daedeok/division")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(DivisionDocumentation.save());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 소속_업데이트() throws Exception {
        SecondDivisionDTO secondDivisionDTO = new SecondDivisionDTO(1L, null);
        SecondDivisionDTO secondDivisionDTO2 = new SecondDivisionDTO(2L, null);
        SecondDivisionDTO secondDivisionDTO3 = new SecondDivisionDTO(3L, "3학년");
        SecondDivisionDTO secondDivisionDTO4 = new SecondDivisionDTO(null, "7학년");
        DivisionDTO.UpdateRequest request = new DivisionDTO.UpdateRequest("before", "after", Arrays.asList(secondDivisionDTO, secondDivisionDTO2), Arrays.asList(secondDivisionDTO3, secondDivisionDTO4));

        doNothing().when(service).update(any());

        mockMvc.perform(put("/daedeok/division")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(DivisionDocumentation.update());
    }
}