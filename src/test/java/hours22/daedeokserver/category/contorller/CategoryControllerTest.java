package hours22.daedeokserver.category.contorller;

import hours22.daedeokserver.category.controller.CategoryController;
import hours22.daedeokserver.category.document.CategoryDocumentation;
import hours22.daedeokserver.category.dto.CategoryRequest;
import hours22.daedeokserver.category.dto.CategoryResponse;
import hours22.daedeokserver.category.service.CategoryService;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest extends MvcDocumentation {
    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        super.setUp(webApplicationContext, restDocumentationContextProvider);
    }

    @Test
    void 리스트_조회() throws Exception {
        List<CategoryResponse> categoryList = new ArrayList<>();

        for (int i = 1; i <= 3; i++)
            categoryList.add(new CategoryResponse((long) i, "category", "content", LocalDateTime.now()));

        given(categoryService.find(any())).willReturn(new CategoryResponse.List(categoryList, 11L, 4));

        mockMvc.perform(get("/daedeok/category")
                        .param("keyword", "찾을 키워드")
                        .param("page", "0")
                        .param("required_count", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(CategoryDocumentation.list());
    }

    @Test
    void 카테고리_전체_조회() throws Exception {
        List<CategoryResponse> categoryList = new ArrayList<>();

        for (int i = 1; i <= 3; i++)
            categoryList.add(new CategoryResponse((long) i, "category", "content", LocalDateTime.now()));

        given(categoryService.find()).willReturn(categoryList);

        mockMvc.perform(get("/daedeok/category/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(CategoryDocumentation.all());
    }

    @Test
    void 카테고리_QNA_전체_조회() throws Exception {
        List<CategoryResponse.Summary> categoryList = new ArrayList<>();

        for (int i = 1; i <= 3; i++)
            categoryList.add(new CategoryResponse.Summary("category"));

        given(categoryService.findQNACategory()).willReturn(categoryList);

        mockMvc.perform(get("/daedeok/category/all/qna")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(CategoryDocumentation.allQ());
    }

    @Test
    void 카테고리_Board_전체_조회() throws Exception {
        List<CategoryResponse.Summary> categoryList = new ArrayList<>();

        for (int i = 1; i <= 3; i++)
            categoryList.add(new CategoryResponse.Summary("category"));

        given(categoryService.findBoardCategory()).willReturn(categoryList);

        mockMvc.perform(get("/daedeok/category/all/lecture-board")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(CategoryDocumentation.allB());
    }

    @Test
    void 카테고리_게시판_전체_조회() throws Exception {
        List<CategoryResponse> categoryList = new ArrayList<>();

        for (int i = 1; i <= 3; i++)
            categoryList.add(new CategoryResponse((long) i, "category", "content", LocalDateTime.now()));

        given(categoryService.find()).willReturn(categoryList);

        mockMvc.perform(get("/daedeok/category/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(CategoryDocumentation.all());
    }

    @Test
    void 카테고리_상세조회() throws Exception {
        CategoryResponse response = new CategoryResponse(1L, "title", "content", LocalDateTime.now());

        given(categoryService.findDetail(any())).willReturn(response);

        mockMvc.perform(get("/daedeok/category/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(CategoryDocumentation.findDetail());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 생성() throws Exception {
        CategoryRequest request = new CategoryRequest("category", "content");

        given(categoryService.save(any())).willReturn(1L);

        mockMvc.perform(post("/daedeok/category")
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(CategoryDocumentation.save());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 삭제() throws Exception {
        doNothing().when(categoryService).delete(any());

        mockMvc.perform(delete("/daedeok/category/{id}", 1L)
                        .header("authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(CategoryDocumentation.delete());
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_ADMIN)
    void 수정() throws Exception {
        CategoryRequest request = new CategoryRequest("category", "content");

        doNothing().when(categoryService).update(any(), any());

        mockMvc.perform(put("/daedeok/category/{id}", 1L)
                        .header("authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(CategoryDocumentation.update());
    }
}