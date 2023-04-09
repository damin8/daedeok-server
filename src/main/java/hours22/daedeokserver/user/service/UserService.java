package hours22.daedeokserver.user.service;

import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.common.service.RandMaker;
import hours22.daedeokserver.division.domain.Division;
import hours22.daedeokserver.division.service.DivisionService;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.BusinessException;
import hours22.daedeokserver.exception.business.ConflictException;
import hours22.daedeokserver.exception.business.EntityNotFoundException;
import hours22.daedeokserver.exception.token.TokenExpiredException;
import hours22.daedeokserver.exception.token.TokenInvalidException;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.service.LectureService;
import hours22.daedeokserver.lecture.service.LectureUserService;
import hours22.daedeokserver.naver.service.NaverService;
import hours22.daedeokserver.security.JwtTokenProvider;
import hours22.daedeokserver.user.domain.Role;
import hours22.daedeokserver.user.domain.User;
import hours22.daedeokserver.user.domain.UserRepository;
import hours22.daedeokserver.user.dto.*;
import hours22.daedeokserver.user.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final List<String> dutyList = new ArrayList<String>() {{
        add("성도");
        add("집사");
        add("권사");
        add("장로");
        add("전도사");
        add("목사");
    }};
    private final UserRepository userRepository;
    private final RedisUtil redis;
    private final JwtTokenProvider jwtTokenProvider;
    private final NaverService naverService;
    private final DivisionService divisionService;
    private final LectureUserService lectureUserService;
    private final LectureService lectureService;

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findUserByPhoneNumAndPassword(loginRequest.getId(), loginRequest.getPassword())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.LOGIN_FAILED));

        String id = String.valueOf(user.getId());
        TokenDto tokenDto = jwtTokenProvider.createToken(id, Role.ROLE_MEMBER);
        redis.setDataExpire(id, tokenDto.getRefresh_token(), tokenDto.getExpire_time());
        Integer lectureNum = user.getRole().equals(Role.ROLE_MEMBER) ? lectureUserService.countLectureUser(user) : lectureService.count(user, Status.OPEN);

        return LoginResponse.of(user, lectureNum, tokenDto.getAccess_token(), tokenDto.getRefresh_token());
    }

    @Transactional
    public TokenDto.Response reissue(TokenDto.Request request) {

        if (!jwtTokenProvider.validateToken(request.getRefresh_token())) {
            throw new TokenExpiredException(ErrorCode.AUTH_TIME_OUT);
        }

        String id = jwtTokenProvider.getSubject(request.getAccess_token());
        String refreshToken = redis.getData(id);

        if (refreshToken == null || !refreshToken.equals(request.getRefresh_token())) {
            throw new TokenInvalidException(ErrorCode.AUTH_INVALID);
        }

        return jwtTokenProvider.createAccessToken(id, Role.ROLE_MEMBER);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void save(List<User> userList) {
        userRepository.saveAll(userList);
    }

    @Transactional
    public User find(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
    }

    public List<String> findDuty() {
        return dutyList;
    }

    @Transactional(readOnly = true)
    public void check(String phoneNum) {
        if (userRepository.existsByPhoneNum(phoneNum))
            throw new ConflictException(ErrorCode.VALUE_CONFLICT);
    }

    @Transactional(readOnly = true)
    public UserResponse find(Pageable pageable) {
        Page<User> userPage = userRepository.findAllByRoleIsNot(Role.ROLE_ADMIN, pageable);

        return new UserResponse(UserResponse.Summary.of(userMapping(userPage)), userPage.getTotalElements(), userPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public UserResponse.Summary findSummary(Long id) {
        return UserResponse.Summary.of(find(id), 0);
    }

    @Transactional(readOnly = true)
    public UserResponse find(String keyword, Pageable pageable) {
        Page<User> userPage = userRepository.findAllByNameContainsAndRoleIsNot(keyword, Role.ROLE_ADMIN, pageable);

        return new UserResponse(UserResponse.Summary.of(userMapping(userPage)), userPage.getTotalElements(), userPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public LoginResponse.Info findInfo(User user) {
        Integer lectureNum = user.getRole().equals(Role.ROLE_MEMBER) ? lectureUserService.countLectureUser(user) : lectureService.count(user, Status.OPEN);

        return LoginResponse.Info.of(user, lectureNum);
    }

    @Transactional
    public Long create(UserRequest userRequest) {
        check(userRequest.getPhone_num());
        Division division = divisionService.find(userRequest.getFirst_division(), userRequest.getSecond_division());

        return userRepository.save(userRequest.toUser(division)).getId();
    }

    @Transactional
    public void updatePassword(User user, PasswordRequest passwordRequest) {
        user.updatePassword(passwordRequest.getPassword());
        save(user);
    }

    @Transactional
    public void updatePassword(PasswordRequest.Reset request) {
        User user = userRepository.findByPhoneNum(request.getPhone_num()).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        user.updatePassword(request.getPassword());
        save(user);
    }

    @Transactional
    public void updatePassword(Long userId) {
        User user = find(userId);
        String newPassword = RandMaker.generateKey(false, 8);

        try {
            naverService.sendSms(user.getPhoneNum(), "대덕바이블에서 보냅니다.\n임시 비밀번호 : "
                    + newPassword + "\n로그인 후에 비밀번호를 바꿔주시기 바랍니다.\n감사합니다.");
            user.updatePassword(CommonService.encrypt(newPassword));
        } catch (Exception e) {
            logger.error("문자 보내기 실패 !");
        }
    }

    @Transactional
    public void update(User user, UserRequest.Update updateRequest) {
        user.update(updateRequest);
        save(user);
    }

    @Transactional
    public void updateAdmin(Long userId, UserRequest.UpdateAdmin request) {
        Division division = divisionService.find(request.getFirst_division(), request.getSecond_division());

        User user = find(userId);
        user.update(request, division);
    }

    @Transactional
    public void updateRole(UserRequest.UpdateRole request) {
        User user = find(request.getId());
        Role role = Role.valueOf(request.getRole());

        if (role.equals(user.getRole()))
            throw new BusinessException(ErrorCode.SAME_ROLE);

        userRepository.delete(user);
        userRepository.flush();
        user.updateRole(role);
        save(user);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        User user = find(id);

        userRepository.delete(user);
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    private Map<User, Integer> userMapping(Page<User> userPage) {
        Map<User, Integer> map = new HashMap<>();
        List<User> userList = userPage.getContent();

        for (User user : userList) {
            Integer lectureNum = lectureUserService.countLectureUser(user);
            map.put(user, lectureNum);
        }

        return map;
    }
}
