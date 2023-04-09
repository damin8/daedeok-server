package hours22.daedeokserver.notice.controller;

import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.notice.dto.*;
import hours22.daedeokserver.notice.service.NoticeService;
import hours22.daedeokserver.security.CurrentUser;
import hours22.daedeokserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/daedeok/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<NoticeResponse.List> find(@RequestParam(required = false) String keyword,
                                                    @RequestParam int page,
                                                    @RequestParam int required_count) {
        if (keyword == null)
            return ResponseEntity.ok(noticeService.find(CommonService.getPageable(page, required_count)));

        return ResponseEntity.ok(noticeService.find(keyword, CommonService.getPageable(page, required_count)));
    }

    @GetMapping("/tutor")
    public ResponseEntity<TutorNoticeResponse.List> findNotice(@RequestParam(required = false) String keyword,
                                                               @RequestParam int page,
                                                               @RequestParam int required_count) {
        if (keyword == null)
            return ResponseEntity.ok(noticeService.findTutorNotice(CommonService.getPageable(page, required_count)));

        return ResponseEntity.ok(noticeService.findTutorNotice(keyword, CommonService.getPageable(page, required_count)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> findDetail(@PathVariable Long id) {

        return ResponseEntity.ok(noticeService.find(id));
    }

    @GetMapping("/tutor/{id}")
    public ResponseEntity<TutorNoticeResponse> findNoticeDetail(@PathVariable Long id) {

        return ResponseEntity.ok(noticeService.findTutorNotice(id));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody NoticeRequest noticeRequest) {
        Long id = noticeService.save(noticeRequest);

        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noticeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody NoticeRequest noticeRequest) {
        noticeService.update(id, noticeRequest);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/tutor")
    public ResponseEntity<Void> saveNotice(@CurrentUser User user,
                                           @RequestBody TutorNoticeRequest request) {
        Long id = noticeService.save(user, request);

        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }

    @DeleteMapping("/tutor/{id}")
    public ResponseEntity<Void> deleteTutorNotice(@CurrentUser User user,
                                                  @PathVariable Long id) {
        noticeService.deleteTutorNotice(user, id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/tutor/{id}")
    public ResponseEntity<Void> updateNotice(@CurrentUser User user,
                                             @PathVariable Long id,
                                             @RequestBody TutorNoticeRequest request) {
        noticeService.update(user, id, request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/tutor/{notice_id}/comment")
    public ResponseEntity<Void> saveNoticeComment(@CurrentUser User user,
                                                  @PathVariable Long notice_id,
                                                  @RequestBody TutorNoticeCommentRequest request) {

        return ResponseEntity.created(URI.create(String.valueOf(noticeService.save(user, notice_id, request)))).build();
    }

    @DeleteMapping("/tutor/comment/{id}")
    public ResponseEntity<Void> deleteTutorNoticeComment(@CurrentUser User user,
                                                         @PathVariable Long id) {
        noticeService.deleteTutorNoticeComment(user, id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/tutor/comment/{id}")
    public ResponseEntity<Void> updateNoticeComment(@CurrentUser User user,
                                                    @PathVariable Long id,
                                                    @RequestBody TutorNoticeCommentRequest request) {
        noticeService.updateComment(user, id, request);

        return ResponseEntity.noContent().build();
    }
}
