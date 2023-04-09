package hours22.daedeokserver.category.controller;

import hours22.daedeokserver.category.dto.CategoryRequest;
import hours22.daedeokserver.category.dto.CategoryResponse;
import hours22.daedeokserver.category.service.CategoryService;
import hours22.daedeokserver.common.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/daedeok/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CategoryResponse.List> find(@RequestParam int page,
                                                      @RequestParam int required_count) {
        return ResponseEntity.ok(categoryService.find(CommonService.getPageable(page, required_count)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponse>> findAll() {

        return ResponseEntity.ok(categoryService.find());
    }

    @GetMapping("/all/qna")
    public ResponseEntity<List<CategoryResponse.Summary>> findAllQNA() {

        return ResponseEntity.ok(categoryService.findQNACategory());
    }

    @GetMapping("/all/lecture-board")
    public ResponseEntity<List<CategoryResponse.Summary>> findAllBoard() {

        return ResponseEntity.ok(categoryService.findBoardCategory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findDetail(@PathVariable Long id) {

        return ResponseEntity.ok(categoryService.findDetail(id));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CategoryRequest categoryRequest) {
        Long id = categoryService.save(categoryRequest);

        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody CategoryRequest request) {
        categoryService.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
