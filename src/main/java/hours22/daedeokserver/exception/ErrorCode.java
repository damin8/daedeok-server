package hours22.daedeokserver.exception;

public enum ErrorCode {

    ENTITY_NOT_FOUND(700, "22hours_000", "대상을 찾을 수 없습니다."),
    DIVISION_NOT_FOUND(700, "22hours_000", "소속을 확인해주세요."),
    INVALID_INPUT_VALUE(700, "22hours_000", "데이터 입력을 확인해주세요."),
    GUIDE_NOT_FOUND(700, "22hours_000", "해당 가이드는 존재하지 않습니다."),
    LECTUREUSER_NOT_FOUND(700, "22hours_000", "수강중인 데이터가 없습니다."),
    PLAN_NOT_FOUND(700, "22hours_000", "강의 계획을 찾을 수 없습니다."),
    HANDOUT_NOT_FOUND(700, "22hours_000", "강의 자료를 찾을 수 없습니다."),
    FAQ_NOT_FOUND(700, "22hours_000", "해당 자주묻는 질문을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(700, "22hours_000", "해당 카테고리를 찾을 수 없습니다."),
    LOGIN_FAILED(700, "22hours_000", "아이디 비밀번호를 확인해주세요."),
    AUTH_TIME_OUT(701, "22hours_001", "토큰이 만료 되었습니다."),
    AUTH_INVALID(702, "22hours_002", "토큰이 변조 되었습니다."),
    NO_AUTH(703, "22hours_003", "권한이 없습니다."),
    VALUE_CONFLICT(704, "22hours_004", "해당 전화번호는 이미 가입되었습니다."),
    VALUE_CONFLICT_DIVISION(704, "22hours_004", "중복된 상위 소속입니다."),
    INTERNAL_SERVER_ERROR(705, "22hours_005", "서버 내부적으로 문제가 있습니다."),
    FILE_INVALID(706, "22hours_006", "파일명에 부적합 문자가 포함되어 있습니다."),
    FILE_UPLOAD_FAIL(707, "22hours_007", "파일 업로드에 실패했습니다."),
    SAVE_FAIL(708, "22hours_008", "강의 개설에 실패했습니다. 정원을 확인해주세요."),
    JOIN_DIVISION(708, "22hours_008", "수강에 실패했습니다. 소속을 확인해주세요."),
    LECTURE_TIME_CHECK_FAIL(708, "22hours_008", "수강에 실패했습니다. 이미 동일한 시간의 수업을 듣고 있습니다."),
    LECTURE_TIMEOUT(708, "22hours_008", "수강에 실패했습니다. 첫번째 수업이 이미 시작했습니다."),
    JOIN_ALREADY(708, "22hours_008", "이미 수강중입니다."),
    JOIN_FAIL(708, "22hours_008", "수강에 실패했습니다. 정원을 확인해주세요."),
    ATTENDANCE_FAIL(708, "22hours_008", "출석을 할 수 없습니다. 강의 유형을 확인해주세요."),
    SAME_ROLE(708, "22hours_008", "이미 같은 역할입니다."),
    NOT_CERTIFICATE(708, "22hours_008", "강의가 수료되지 않았습니다."),
    TIME_MISMATCH(708, "22hours_008", "강의가 활성화 되지 않았습니다."),
    LECTURE_FINISHED(708, "22hours_008", "종료된 강의 입니다.");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
