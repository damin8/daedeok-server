package hours22.daedeokserver.user.controller;

import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.security.CurrentUser;
import hours22.daedeokserver.user.domain.User;
import hours22.daedeokserver.user.dto.*;
import hours22.daedeokserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/daedeok/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/check")
    public ResponseEntity<Void> check(@RequestParam String phone_num) {
        userService.check(phone_num);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin")
    public ResponseEntity<UserResponse> find(@RequestParam(required = false) String keyword,
                                             @RequestParam int page,
                                             @RequestParam int required_count) {

        if (keyword == null)
            return ResponseEntity.ok(userService.find(CommonService.getPageable(page, required_count)));

        return ResponseEntity.ok(userService.find(keyword, CommonService.getPageable(page, required_count)));
    }

    @GetMapping("/duty")
    public ResponseEntity<List<String>> findDuty() {
        return ResponseEntity.ok(userService.findDuty());
    }

    @GetMapping("/admin/member/{id}")
    public ResponseEntity<UserResponse.Summary> findMember(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findSummary(id));
    }

    @GetMapping("/info")
    public ResponseEntity<LoginResponse.Info> findInfo(@CurrentUser User user) {

        return ResponseEntity.ok(userService.findInfo(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto.Response> reissue(@RequestBody TokenDto.Request request) {
        return ResponseEntity.ok(userService.reissue(request));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UserRequest userRequest) {
        Long id = userService.create(userRequest);

        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@CurrentUser User user,
                                               @RequestBody PasswordRequest passwordRequest) {
        userService.updatePassword(user, passwordRequest);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@CurrentUser User user,
                                       @RequestBody UserRequest.Update updateRequest) {
        userService.update(user, updateRequest);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/member/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody UserRequest.UpdateAdmin request) {
        userService.updateAdmin(id, request);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password/reset")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordRequest.Reset request) {
        userService.updatePassword(request);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/role")
    public ResponseEntity<Void> updateRole(@RequestBody UserRequest.UpdateRole request) {
        userService.updateRole(request);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/password/{id}")
    public ResponseEntity<Void> changePassword(@PathVariable Long id) {
        userService.updatePassword(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        userService.deleteAdmin(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@CurrentUser User user) {
        userService.delete(user);

        return ResponseEntity.noContent().build();
    }
}
