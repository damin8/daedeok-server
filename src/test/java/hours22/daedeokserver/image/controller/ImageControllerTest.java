package hours22.daedeokserver.image.controller;

import hours22.daedeokserver.common.MvcDocumentation;
import hours22.daedeokserver.common.WithMockCustomUser;
import hours22.daedeokserver.image.document.ImageDocumentation;
import hours22.daedeokserver.image.dto.ImageRequest;
import hours22.daedeokserver.image.dto.ImageResponse;
import hours22.daedeokserver.image.service.ImageService;
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

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ImageController.class)
class ImageControllerTest extends MvcDocumentation {
    @MockBean
    private ImageService service;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        super.setUp(webApplicationContext, restDocumentationContextProvider);
    }

    @Test
    @WithMockCustomUser
    void 이미지_찾기() throws Exception {
        List<ImageResponse.Summary> list = new ArrayList<>();
        list.add(new ImageResponse.Summary(1L, "https://aws.s3.com"));
        list.add(new ImageResponse.Summary(2L, "https://aws.s4.com"));
        list.add(new ImageResponse.Summary(3L, "https://aws.s5.com"));

        given(service.find()).willReturn(new ImageResponse(list));

        mockMvc.perform(get("/daedeok/image")
                        .header("authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(ImageDocumentation.list());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 이미지_생성() throws Exception {
        List<ImageRequest> list = new ArrayList<>();
        list.add(new ImageRequest("https://aws.s3.com"));

        doNothing().when(service).save(anyList());

        mockMvc.perform(put("/daedeok/image")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(list))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(ImageDocumentation.save());
    }
}