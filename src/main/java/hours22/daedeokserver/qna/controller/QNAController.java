package hours22.daedeokserver.qna.controller;

import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.qna.dto.QNACommentRequest;
import hours22.daedeokserver.qna.dto.QNARequest;
import hours22.daedeokserver.qna.dto.QNAResponse;
import hours22.daedeokserver.qna.service.QNAService;
import hours22.daedeokserver.security.CurrentUser;
import hours22.daedeokserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/daedeok/qna")
@RequiredArgsConstructor
public class QNAController {
    private final QNAService qnaService;

    @GetMapping
    public ResponseEntity<QNAResponse.List> find(@RequestParam(required = false) String category,
                                                 @RequestParam(required = false) String keyword,
                                                 @RequestParam int page,
                                                 @RequestParam int required_count) {
        if (category == null) {
            if (keyword == null)
                return ResponseEntity.ok(qnaService.find(CommonService.getPageable(page, required_count)));

            return ResponseEntity.ok(qnaService.find(keyword, CommonService.getPageable(page, required_count)));
        }

        if (keyword == null)
            return ResponseEntity.ok(qnaService.findCategory(category, CommonService.getPageable(page, required_count)));

        return ResponseEntity.ok(qnaService.findCategory(category, keyword, CommonService.getPageable(page, required_count)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QNAResponse> findDetail(@CurrentUser User user, @PathVariable Long id) {

        return ResponseEntity.ok(qnaService.find(user, id));
    }

    @PostMapping
    public ResponseEntity<Void> save(@CurrentUser User user,
                                     @RequestBody QNARequest qnaRequest) {
        Long id = qnaService.save(user, qnaRequest);

        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@CurrentUser User user,
                                       @PathVariable Long id) {
        qnaService.delete(user, id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@CurrentUser User user,
                                       @PathVariable Long id,
                                       @RequestBody QNARequest qnaRequest) {
        qnaService.update(user, id, qnaRequest);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{qna_id}/comment")
    public ResponseEntity<Void> saveComment(@CurrentUser User user,
                                            @PathVariable Long qna_id,
                                            @RequestBody QNACommentRequest commentRequest) {
        Long id = qnaService.save(user, qna_id, commentRequest);

        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Void> deleteComment(@CurrentUser User user,
                                              @PathVariable Long id) {
        qnaService.deleteComment(user, id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<Void> updateComment(@CurrentUser User user,
                                              @PathVariable Long id,
                                              @RequestBody QNACommentRequest commentRequest) {
        qnaService.updateComment(user, id, commentRequest);

        return ResponseEntity.noContent().build();
    }
}
