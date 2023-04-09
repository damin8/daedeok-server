package hours22.daedeokserver.guide.controller;

import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.guide.dto.GuideRequest;
import hours22.daedeokserver.guide.dto.GuideResponse;
import hours22.daedeokserver.guide.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/daedeok/guide")
@RequiredArgsConstructor
public class GuideController {
    private final GuideService guideService;

    @GetMapping
    public ResponseEntity<GuideResponse.List> find(@RequestParam(required = false) String keyword,
                                                   @RequestParam int page,
                                                   @RequestParam int required_count) {
        if (keyword == null)
            return ResponseEntity.ok(guideService.find(CommonService.getPageable(page, required_count)));

        return ResponseEntity.ok(guideService.find(keyword, CommonService.getPageable(page, required_count)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuideResponse> findDetail(@PathVariable Long id) {

        return ResponseEntity.ok(guideService.find(id));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody GuideRequest request) {
        Long id = guideService.save(request);

        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody GuideRequest request) {
        guideService.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        guideService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
