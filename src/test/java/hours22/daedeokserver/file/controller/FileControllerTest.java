package hours22.daedeokserver.file.controller;

import hours22.daedeokserver.common.MvcDocumentation;
import hours22.daedeokserver.common.WithMockCustomUser;
import hours22.daedeokserver.file.document.FileDocumentation;
import hours22.daedeokserver.file.dto.FileRequest;
import hours22.daedeokserver.file.service.FileService;
import hours22.daedeokserver.user.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FileController.class)
class FileControllerTest extends MvcDocumentation {
    @MockBean
    private FileService fileService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        super.setUp(webApplicationContext, restDocumentationContextProvider);
    }

    @Test
    @WithMockCustomUser
    void uploadDummy() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file_list", new byte[1]);
        MockMultipartFile file2 = new MockMultipartFile("file_list", new byte[1]);

        given(fileService.uploadDummy(anyList())).willReturn(Arrays.asList("http://s3.com", "http://naver.com"));

        mockMvc.perform(multipart("/daedeok/file")
                        .file(file)
                        .file(file2)
                        .header("authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(FileDocumentation.upload());
    }

    @Test
    @WithMockCustomUser
    void uploadReal() throws Exception {
        FileRequest fileRequest = new FileRequest(Arrays.asList("naver.com", "naver.com"), "BOARD");
        doNothing().when(fileService).uploadReal(any());

        mockMvc.perform(post("/daedeok/file/real")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(fileRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(FileDocumentation.uploadReal());
    }

    @Test
    @WithMockCustomUser
    void update() throws Exception {
        FileRequest.Update fileRequest = new FileRequest.Update(Arrays.asList("naver.com", "naver.com"), Arrays.asList("naver.com", "naver.com"), "QNA");
        doNothing().when(fileService).update(any());

        mockMvc.perform(put("/daedeok/file")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(fileRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(FileDocumentation.update());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void uploadCertificate() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", new byte[1]);

        given(fileService.uploadCertificate(any(), any(), any())).willReturn("http://s3.com");

        mockMvc.perform(multipart("/daedeok/file/certificate/{user_id}/{lecture_id}", 1L, 2L)
                        .file(file)
                        .header("authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(FileDocumentation.uploadCertificate());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_TUTOR)
    void deleteCertificate() throws Exception {
        doNothing().when(fileService).deleteCertificate(any(), any());

        mockMvc.perform(delete("/daedeok/file/certificate/{user_id}/{lecture_id}", 1L, 2L)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(FileDocumentation.deletecertificate());
    }
}