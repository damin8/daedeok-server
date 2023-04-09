package hours22.daedeokserver.lecture.domain;

public enum Status {
    OPEN, FINISH, COMPLETE, // 진행 중인 강의(전체), 종료된 강의(강사), 수료한 강의(학생)
    POSSIBLE, IMPOSSIBLE, ING // 수업 신청 가능, 수업 신청 마감, 수업 듣는 중
}
