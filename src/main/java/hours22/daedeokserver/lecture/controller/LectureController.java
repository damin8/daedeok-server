package hours22.daedeokserver.lecture.controller;

import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.dto.board.BoardCommentRequest;
import hours22.daedeokserver.lecture.dto.board.BoardRequest;
import hours22.daedeokserver.lecture.dto.board.BoardResponse;
import hours22.daedeokserver.lecture.dto.lecture.*;
import hours22.daedeokserver.lecture.dto.plan.*;
import hours22.daedeokserver.lecture.service.LectureService;
import hours22.daedeokserver.lecture.service.LectureUserService;
import hours22.daedeokserver.lecture.service.PlanService;
import hours22.daedeokserver.security.CurrentUser;
import hours22.daedeokserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/daedeok/lecture")
@RequiredArgsConstructor
public class LectureController {
    private final LectureService service;
    private final LectureUserService userService;
    private final PlanService planService;

    @GetMapping
    public ResponseEntity<LectureResponse.List> find(@RequestParam(required = false) String category,
                                                     @RequestParam String status,
                                                     @RequestParam(required = false) String keyword,
                                                     @RequestParam int page,
                                                     @RequestParam int required_count) {
        if (category == null) {
            if (keyword == null)
                return ResponseEntity.ok(service.find(Status.valueOf(status), CommonService.getPageable(page, required_count)));

            return ResponseEntity.ok(service.find(Status.valueOf(status), keyword, CommonService.getPageable(page, required_count)));
        }

        if (keyword == null)
            return ResponseEntity.ok(service.findCategory(Status.valueOf(status), category, CommonService.getPageable(page, required_count)));

        return ResponseEntity.ok(service.findCategory(Status.valueOf(status), category, keyword, CommonService.getPageable(page, required_count)));
    }

    @GetMapping("/finish")
    public ResponseEntity<LectureResponse.List> findFinish(@CurrentUser User user,
                                                           @RequestParam(required = false) String category,
                                                           @RequestParam(required = false) String keyword,
                                                           @RequestParam int page,
                                                           @RequestParam int required_count) {
        if (category == null) {
            if (keyword == null)
                return ResponseEntity.ok(service.findFinish(user, CommonService.getPageable(page, required_count)));

            return ResponseEntity.ok(service.findFinish(user, keyword, CommonService.getPageable(page, required_count)));
        }

        if (keyword == null)
            return ResponseEntity.ok(service.findFinishCategory(user, category, CommonService.getPageable(page, required_count)));

        return ResponseEntity.ok(service.findFinishCategory(user, category, keyword, CommonService.getPageable(page, required_count)));
    }

    @GetMapping("/complete")
    public ResponseEntity<LectureResponse.CertificateList> findComplete(@CurrentUser User user,
                                                                        @RequestParam(required = false) String category,
                                                                        @RequestParam(required = false) String keyword,
                                                                        @RequestParam int page,
                                                                        @RequestParam int required_count) {
        if(category==null) {
            if (keyword == null)
                return ResponseEntity.ok(userService.findComplete(user, CommonService.getPageable(page, required_count)));

            return ResponseEntity.ok(userService.findComplete(user, keyword, CommonService.getPageable(page, required_count)));
        }

        if (keyword == null)
            return ResponseEntity.ok(userService.findCompleteCategory(user, category, CommonService.getPageable(page, required_count)));

        return ResponseEntity.ok(userService.findCompleteCategory(user, category, keyword, CommonService.getPageable(page, required_count)));
    }

    @GetMapping("/main")
    public ResponseEntity<MainResponse> findMain(@CurrentUser User user) {

        return ResponseEntity.ok(service.findMain(user));
    }

    @GetMapping("/sidebar")
    public ResponseEntity<List<LectureResponse.Sidebar>> findSidebar(@CurrentUser User user) {

        return ResponseEntity.ok(service.findSidebar(user));
    }

    @GetMapping("/possible")
    public ResponseEntity<LectureResponse.PossibleList> find(@CurrentUser User user,
                                                             @RequestParam(required = false) String category,
                                                             @RequestParam(required = false) String keyword,
                                                             @RequestParam int page,
                                                             @RequestParam int required_count) {
        if (category == null) {
            if (keyword == null)
                return ResponseEntity.ok(service.find(user, page, required_count));

            return ResponseEntity.ok(service.find(user, keyword, page, required_count));
        }

        if (keyword == null)
            return ResponseEntity.ok(service.findCategory(user, category, page, required_count));

        return ResponseEntity.ok(service.findCategory(user, category, keyword, page, required_count));
    }

    @GetMapping("/{lecture_id}/board")
    public ResponseEntity<BoardResponse.List> find(@PathVariable Long lecture_id,
                                                   @RequestParam(required = false) String category,
                                                   @RequestParam int page,
                                                   @RequestParam int required_count) {
        if (category == null)
            return ResponseEntity.ok(service.find(lecture_id, CommonService.getPageable(page, required_count)));

        return ResponseEntity.ok(service.find(lecture_id, category, CommonService.getPageable(page, required_count)));
    }

    @GetMapping("/{lecture_id}/plan")
    public ResponseEntity<List<PlanResponse.Summary>> find(@PathVariable Long lecture_id) {
        return ResponseEntity.ok(service.findPlan(lecture_id));
    }

    @GetMapping("/{lecture_id}/attendance")
    public ResponseEntity<List<PlanResponse.Attendance>> findAttendance(@CurrentUser User user, @PathVariable Long lecture_id) {
        return ResponseEntity.ok(planService.findAttendance(user, lecture_id));
    }

    @GetMapping("/plan/{plan_id}/user")
    public ResponseEntity<PlanUserResponse> findPlanUser(@PathVariable Long plan_id) {
        return ResponseEntity.ok(planService.find(plan_id));
    }

    @GetMapping("/plan/{plan_id}/online")
    public ResponseEntity<OnlineResponse> findPlanOnline(@CurrentUser User user, @PathVariable Long plan_id) {
        return ResponseEntity.ok(planService.findOnline(user, plan_id));
    }

    @PostMapping("/plan/{plan_id}/online")
    public ResponseEntity<Void> saveDuration(@CurrentUser User user,
                                             @PathVariable Long plan_id,
                                             @RequestBody DurationRequest request) {
        planService.saveDuration(user, plan_id, request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LectureResponse> findDetail(@PathVariable Long id) {

        return ResponseEntity.ok(service.find(id));
    }

    @GetMapping("/{id}/update")
    public ResponseEntity<LectureRequest> findDetail(@CurrentUser User user, @PathVariable Long id) {

        return ResponseEntity.ok(service.find(user, id));
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<LectureResponse.WithHandout> findDetailInfo(@PathVariable Long id) {

        return ResponseEntity.ok(service.findDetailInfo(id));
    }

    @GetMapping("/{lecture_id}/custom")
    public ResponseEntity<LectureDTO> findCustomSummary(@PathVariable Long lecture_id) {

        return ResponseEntity.ok(service.findCustomSummary(lecture_id));
    }


    @GetMapping("/board/{id}")
    public ResponseEntity<BoardResponse> findBoardDetail(@CurrentUser User user,
                                                         @PathVariable Long id) {

        return ResponseEntity.ok(service.findBoard(user, id));
    }

    @GetMapping("/{lecture_id}/user")
    public ResponseEntity<LectureUserResponse> findLectureUser(@PathVariable Long lecture_id) {

        return ResponseEntity.ok(userService.find(lecture_id));
    }

    @PostMapping
    public ResponseEntity<Void> save(@CurrentUser User user,
                                     @Valid @RequestBody LectureRequest request) {
        Long id = service.save(user, request);

        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }

    @DeleteMapping("/cancel/{user_id}/{lecture_id}")
    public ResponseEntity<Void> cancel(@CurrentUser User user,
                                       @PathVariable Long user_id,
                                       @PathVariable Long lecture_id) {
        service.cancel(user, user_id, lecture_id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cancel/{lecture_id}")
    public ResponseEntity<Void> cancel(@CurrentUser User user,
                                       @PathVariable Long lecture_id) {
        service.cancel(user, lecture_id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@CurrentUser User user,
                                       @PathVariable Long id,
                                       @Valid @RequestBody LectureRequest.Update request) {
        service.update(user, id, request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/join/{lecture_id}")
    public ResponseEntity<Void> join(@CurrentUser User user, @PathVariable Long lecture_id) {
        service.join(user, lecture_id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/finish/{lecture_id}")
    public ResponseEntity<Void> finish(@CurrentUser User user, @PathVariable Long lecture_id) {
        service.finish(user, lecture_id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lecture_id}/board")
    public ResponseEntity<Void> save(@CurrentUser User user,
                                     @PathVariable Long lecture_id,
                                     @RequestBody BoardRequest request) {
        Long id = service.save(user, lecture_id, request);

        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<Void> deleteBoard(@CurrentUser User user,
                                            @PathVariable Long id) {
        service.deleteBoard(user, id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/board/{id}")
    public ResponseEntity<Void> update(@CurrentUser User user,
                                       @PathVariable Long id,
                                       @RequestBody BoardRequest request) {
        service.update(user, id, request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/board/{board_id}/comment")
    public ResponseEntity<Void> save(@CurrentUser User user,
                                     @PathVariable Long board_id,
                                     @RequestBody BoardCommentRequest request) {
        Long id = service.save(user, board_id, request);

        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }

    @DeleteMapping("/board/comment/{id}")
    public ResponseEntity<Void> deleteBoardComment(@CurrentUser User user,
                                                   @PathVariable Long id) {
        service.deleteComment(user, id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/board/comment/{id}")
    public ResponseEntity<Void> updateComment(@CurrentUser User user,
                                              @PathVariable Long id,
                                              @RequestBody BoardCommentRequest request) {
        service.updateComment(user, id, request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/plan/{plan_id}/user")
    public ResponseEntity<Void> save(@PathVariable Long plan_id,
                                     @RequestBody AttendanceRequest request) {
        Long userId = request.getUser_id();

        if (userId == -1L)
            planService.attendance(plan_id);

        else planService.attendance(plan_id, request.getUser_id());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/plan/{plan_id}/attendance")
    public ResponseEntity<Void> save(@CurrentUser User user,
                                     @PathVariable Long plan_id) {
        planService.attendance(user, plan_id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@CurrentUser User user,
                                       @PathVariable Long id) {
        service.delete(user, id);

        return ResponseEntity.noContent().build();
    }
}
