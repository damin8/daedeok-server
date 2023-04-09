package hours22.daedeokserver.lecture.dto.lecture;

import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.division.dto.DivisionDTO;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.domain.Type;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.lecture.domain.lecture.LectureUser;
import hours22.daedeokserver.lecture.domain.plan.Plan;
import hours22.daedeokserver.lecture.dto.handout.HandoutResponse;
import hours22.daedeokserver.lecture.dto.plan.PlanResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

@Getter
@AllArgsConstructor
public class LectureResponse {
    private Long id;
    private Long user_id;
    private String title;
    private String category;
    private String day;
    private String time;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private Long student_limit;
    private Long student_num;
    private String content;
    private String tutor;
    private java.util.List<Type> type;
    private java.util.List<DivisionDTO> division_list;
    private String reference;
    private java.util.List<PlanResponse> lecture_plan;

    public static LectureResponse of(Lecture lecture, java.util.List<Plan> planList, Long studentNum) {
        Category category = lecture.getCategory();

        if (category == null)
            category = new Category(null, null);

        return new LectureResponse(lecture.getId(), lecture.getTutor().getId(), lecture.getTitle(), category.getCategory(), lecture.getDay(), lecture.getTime(), lecture.getStartDate(), lecture.getEndDate(), lecture.getStudentLimit(), studentNum, lecture.getContent(), lecture.getTutor().getName(), PlanResponse.getType(planList), DivisionDTO.ofLectureDivision(lecture.getDivisionList()), lecture.getReference(), PlanResponse.of(planList));
    }


    @Getter
    @AllArgsConstructor
    public static class List {
        private java.util.List<Summary> lecture_list;
        private Long total_count;
        private Integer total_page;

        public static List of(Map<Lecture, Long> map, Page<Lecture> lecturePage) {
            return new List(Summary.of(map), lecturePage.getTotalElements(), lecturePage.getTotalPages());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Sidebar {
        private Long lecture_id;
        private String title;

        public static java.util.List<Sidebar> of(java.util.List<Lecture> lectureList) {
            java.util.List<Sidebar> sidebarList = new ArrayList<>();

            for (Lecture lecture : lectureList) {
                sidebarList.add(of(lecture));
            }

            return sidebarList;
        }

        private static Sidebar of(Lecture lecture) {
            return new Sidebar(lecture.getId(), lecture.getTitle());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CertificateList {
        private java.util.List<SummaryWithCertificate> lecture_list;
        private Long total_count;
        private Integer total_page;
    }

    @Getter
    @AllArgsConstructor
    public static class PossibleList {
        private java.util.List<SummaryWithStatus> lecture_list;
        private Long total_count;
        private Integer total_page;
    }

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private Long id;
        private String title;
        private String category;
        private String day;
        private String time;
        private LocalDateTime start_date;
        private LocalDateTime end_date;
        private Long student_limit;
        private Long student_num;

        private static Summary of(Lecture lecture, Long studentNum) {
            Category category = lecture.getCategory();

            if (category == null)
                category = new Category(null, null);

            return new Summary(lecture.getId(), lecture.getTitle(), category.getCategory(), lecture.getDay(), lecture.getTime(), lecture.getStartDate(), lecture.getEndDate(), lecture.getStudentLimit(), studentNum);
        }

        public static java.util.List<Summary> of(Map<Lecture, Long> map) {
            java.util.List<Summary> summaryList = new ArrayList<>();

            for (Map.Entry<Lecture, Long> entry : map.entrySet()) {
                Long studentNum = entry.getValue();
                summaryList.add(of(entry.getKey(), studentNum));
            }

            summaryList.sort(new Comparator<Summary>() {
                @Override
                public int compare(Summary left, Summary right) {
                    if (left.getId() > right.getId()) {
                        return -1;
                    } else if (left.getId() < right.getId()) {
                        return 1;
                    }
                    return 0;
                }
            });

            return summaryList;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class SummaryWithStatus {
        private Long id;
        private String title;
        private String category;
        private String day;
        private String time;
        private LocalDateTime start_date;
        private LocalDateTime end_date;
        private Long student_limit;
        private Long student_num;
        private Status status;

        public static SummaryWithStatus of(Lecture lecture, Status status, Long studentNum) {
            Category category = lecture.getCategory();

            if (category == null)
                category = new Category(null, null);

            return new SummaryWithStatus(lecture.getId(), lecture.getTitle(), category.getCategory(), lecture.getDay(), lecture.getTime(), lecture.getStartDate(), lecture.getEndDate(), lecture.getStudentLimit(), studentNum, status);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class WithHandout {
        private java.util.List<HandoutResponse> handout_list;
        private java.util.List<PlanResponse.SummaryWithDate> lecture_plan_list;
    }

    @Getter
    @AllArgsConstructor
    public static class SummaryWithCertificate {
        private Long id;
        private String title;
        private String day;
        private String time;
        private LocalDateTime start_date;
        private LocalDateTime end_date;
        private Status status;
        private String url;

        private static SummaryWithCertificate of(LectureUser lectureUser) {
            Lecture lecture = lectureUser.getLecture();

            return new SummaryWithCertificate(lecture.getId(), lecture.getTitle(), lecture.getDay(), lecture.getTime(), lecture.getStartDate(), lecture.getEndDate(), lectureUser.getStatus(), lectureUser.getFileUrl());
        }

        public static java.util.List<SummaryWithCertificate> of(java.util.List<LectureUser> lectureList) {
            java.util.List<SummaryWithCertificate> summaryList = new ArrayList<>();

            for (LectureUser lectureUser : lectureList) {
                summaryList.add(of(lectureUser));
            }

            return summaryList;
        }
    }
}
