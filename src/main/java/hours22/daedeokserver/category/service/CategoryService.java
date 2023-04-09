package hours22.daedeokserver.category.service;

import hours22.daedeokserver.category.domain.*;
import hours22.daedeokserver.category.dto.CategoryRequest;
import hours22.daedeokserver.category.dto.CategoryResponse;
import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.EntityNotFoundException;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final QNACategoryRepository qnaCategoryRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public CategoryResponse.List find(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        return new CategoryResponse.List(CategoryResponse.of(categoryPage.getContent()), categoryPage.getTotalElements(), categoryPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> find() {
        List<Category> categoryList = categoryRepository.findAll();

        return CategoryResponse.of(categoryList);
    }

    @Transactional(readOnly = true)
    public CategoryResponse findDetail(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        return CategoryResponse.of(category);
    }

    @Transactional
    public Long save(CategoryRequest categoryRequest) {
        fileService.uploadReal(CommonService.getUrl(categoryRequest.getContent(), FileType.LECTURE_CATEGORY));

        return categoryRepository.save(categoryRequest.toCategory()).getId();
    }

    @Transactional
    public void update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        category.update(request);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse.Summary> findQNACategory() {
        return CategoryResponse.Summary.ofQNA(qnaCategoryRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse.Summary> findBoardCategory() {
        return CategoryResponse.Summary.ofBoard(boardCategoryRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Category findByCategory(String category) {
        return categoryRepository.findByCategory(category).orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public QNACategory findQNACategory(String category) {
        return qnaCategoryRepository.findByCategory(category).orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public BoardCategory findBoardCategory(String category) {
        return boardCategoryRepository.findByCategory(category).orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
