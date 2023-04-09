package hours22.daedeokserver.lecture.dto.plan;

import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.domain.Type;
import hours22.daedeokserver.lecture.domain.plan.Plan;
import hours22.daedeokserver.lecture.domain.plan.PlanUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PlanResponse {
    private Long week;
    private String title;
    private String tutor;
    private String location;
    private LocalDateTime date;

    public static java.util.List<Type> getType(java.util.List<Plan> planList) {
        java.util.List<Type> typeList = new ArrayList<>();

        for (Plan plan : planList) {
            typeList.add(plan.getType());
        }

        return typeList.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private static PlanResponse of(Plan plan) {
        return new PlanResponse(plan.getWeek(), plan.getTitle(), plan.getTutor(), plan.getLocation(), plan.getDate());
    }

    public static java.util.List<PlanResponse> of(java.util.List<Plan> planList) {
        java.util.List<PlanResponse> planResponseList = new ArrayList<>();

        for (Plan plan : planList) {
            planResponseList.add(of(plan));
        }

        planResponseList.sort(new Comparator<PlanResponse>() {
            @Override
            public int compare(PlanResponse left, PlanResponse right) {
                if (left.getWeek() < right.getWeek()) {
                    return -1;
                } else if (left.getWeek() > right.getWeek()) {
                    return 1;
                }
                return 0;
            }
        });

        return planResponseList;
    }

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private Long id;
        private Long week;
        private String title;
        private LocalDateTime date;
        private String day;
        private Type type;
        private String link;

        private static Summary of(Plan plan) {
            DayOfWeek dayOfWeek = plan.getDate().getDayOfWeek();
            return new Summary(plan.getId(), plan.getWeek(), plan.getTitle(), plan.getDate(), dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN), plan.getType(), plan.getLink());
        }

        public static java.util.List<Summary> of(java.util.List<Plan> planList) {
            java.util.List<Summary> planResponseList = new ArrayList<>();

            for (Plan plan : planList) {
                planResponseList.add(of(plan));
            }

            planResponseList.sort(new Comparator<Summary>() {
                @Override
                public int compare(Summary left, Summary right) {
                    if (left.getWeek() < right.getWeek()) {
                        return -1;
                    } else if (left.getWeek() > right.getWeek()) {
                        return 1;
                    }
                    return 0;
                }
            });

            return planResponseList;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class SummaryWithDate {
        private Long id;
        private Long week;
        private String title;
        private String tutor;
        private String location;
        private Type type;
        private LocalDateTime date;

        private static SummaryWithDate of(Plan plan) {
            return new SummaryWithDate(plan.getId(), plan.getWeek(), plan.getTitle(), plan.getTutor(), plan.getLocation(), plan.getType(), plan.getDate());
        }

        public static java.util.List<SummaryWithDate> of(java.util.List<Plan> planList) {
            java.util.List<SummaryWithDate> planSummaryWithDateList = new ArrayList<>();

            for (Plan plan : planList) {
                planSummaryWithDateList.add(of(plan));
            }

            planSummaryWithDateList.sort(new Comparator<SummaryWithDate>() {
                @Override
                public int compare(SummaryWithDate left, SummaryWithDate right) {
                    if (left.getWeek() < right.getWeek()) {
                        return -1;
                    } else if (left.getWeek() > right.getWeek()) {
                        return 1;
                    }
                    return 0;
                }
            });

            return planSummaryWithDateList;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Attendance {
        private Long id;
        private Long week;
        private String title;
        private Status status;

        public static Attendance of(Plan plan, PlanUser planUser) {
            return new Attendance(plan.getId(), plan.getWeek(), plan.getTitle(), planUser.getStatus());
        }
    }
}
