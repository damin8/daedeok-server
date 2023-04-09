package hours22.daedeokserver.faq.controller;

import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.faq.dto.FAQRequest;
import hours22.daedeokserver.faq.dto.FAQResponse;
import hours22.daedeokserver.faq.service.FAQService;
import hours22.daedeokserver.security.CurrentUser;
import hours22.daedeokserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/daedeok/faq")
@RequiredArgsConstructor
public class FAQController {
    private final FAQService faqService;

    @GetMapping
    public ResponseEntity<FAQResponse.List> find(@RequestParam(required = false) String keyword,
                                                 @RequestParam int page,
                                                 @RequestParam int required_count) {
        if (keyword == null)
            return ResponseEntity.ok(faqService.find(CommonService.getPageable(page, required_count)));

        return ResponseEntity.ok(faqService.find(keyword, CommonService.getPageable(page, required_count)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FAQResponse> findDetail(@PathVariable Long id) {

        return ResponseEntity.ok(faqService.find(id));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody FAQRequest faqRequest) {
        Long id = faqService.save(faqRequest);

        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody FAQRequest faqRequest) {
        faqService.update(id, faqRequest);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        faqService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
