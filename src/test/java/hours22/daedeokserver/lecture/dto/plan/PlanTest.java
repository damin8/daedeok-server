package hours22.daedeokserver.lecture.dto.plan;

import hours22.daedeokserver.common.dto.LocalDateTimeDTO;
import hours22.daedeokserver.common.service.CommonService;
import hours22.daedeokserver.lecture.domain.Type;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.lecture.domain.plan.Plan;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class PlanTest {

    @Test
    void 날짜_최소_최대_구하기() {
        PlanRequest request1 = new PlanRequest(1L, 1L, "1주차", "tutor", "location", "2021-08-05", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        PlanRequest request2 = new PlanRequest(2L, 2L, "2주차", "tutor", "location", "2021-08-04", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        PlanRequest request3 = new PlanRequest(3L, 3L, "3주차", "tutor", "location", "2021-08-03", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        List<PlanRequest> requestList = Arrays.asList(request1, request2, request3);

        LocalDateTimeDTO dateTimeDTO = CommonService.getDate(requestList);

        String temp = "2021-08-05" + " 10:55";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime today = LocalDateTime.parse(temp, formatter);

        assertThat(today).isEqualTo(dateTimeDTO.getEndDate());
        assertThat(dateTimeDTO.getStartDate()).isEqualTo(today.minusDays(2));
    }

    @Test
    void 타입_중복_제거() {
        Plan plan1 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.OFFLINE, "link", "introduce", null);
        Plan plan2 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.ONLINE, "link", "introduce", null);
        Plan plan3 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.ZOOM, "link", "introduce", null);
        Plan plan4 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.ZOOM, "link", "introduce", null);
        Plan plan5 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.ONLINE, "link", "introduce", null);
        Plan plan6 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.OFFLINE, "link", "introduce", null);
        Plan plan7 = new Plan(1L, "title", "tutor", "location", LocalDateTime.now(), Type.OFFLINE, "link", "introduce", null);

        List<Type> typeList = PlanResponse.getType(Arrays.asList(plan1, plan2, plan3, plan4, plan5, plan6, plan7));

        assertThat(typeList.size()).isEqualTo(3);
        assertThat(typeList).contains(Type.ZOOM, Type.OFFLINE, Type.ONLINE);
    }

    @Test
    void 시간_합치기() {
        String date = "2021-08-03";
        String time = "10:55";
        String temp = date + " " + time;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(temp, formatter);

        assertThat(dateTime).isEqualTo(LocalDateTime.of(2021, 8, 3, 10, 55));
    }

    @Test
    void 시간_분할하기() {
        String date = "2021-08-03";
        String time = "10:55";
        String temp = date + " " + time;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(temp, formatter);

        temp = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String[] arr = temp.split(" ");
        date = arr[0];
        time = arr[1];
        assertThat(date).isEqualTo("2021-08-03");
        assertThat(time).isEqualTo("10:55");
    }

    @Test
    void 문자_자르기() {
        /*String str = "1![toasteditorimagee](https://daedeok.s3.ap-northeast-2.amazonaws.com/dummy/nsC7jcAZp0DrI0z1gO48.png)2![toasteditorimagee](https://daedeok.s3.ap-northeast-2.amazonaws.com/dummy/nsC7jcAZp0DrI0z1gO48.png)3![toasteditorimagee](https://daedeok.s3.ap-northeast-2.amazonaws.com/dummy/nsC7jcAZp0DrI0z1gO48.png)4![toasteditorimagee](https://daedeok.s3.ap-northeast-2.amazonaws.com/dummy/nsC7jcAZp0DrI0z1gO48.png)";
        Pattern pattern = Pattern.compile("\\((.*?)\\)");*/
        String str = "<p><img src=\"https://daedeok.s3.ap-northeast-2.amazonaws.com/qna/l7XqixpCgSlC9uLOuXMF.png\" width=\"506\" style=\"\"></p><p>안녕안녕</p>";
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");

        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    @Test
    void 첫번째_수업_확인() {
        Lecture lecture = Lecture.builder()
                .startDate(LocalDateTime.now().minusMinutes(1))
                .build();

        assertThat(lecture.validate()).isFalse();
    }

    @Test
    void 주차별로_정렬하기() {
        PlanRequest request1 = new PlanRequest(1L, 3L, "1주차", "tutor", "location", "2021-08-05", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        PlanRequest request2 = new PlanRequest(2L, 2L, "2주차", "tutor", "location", "2021-08-04", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        PlanRequest request3 = new PlanRequest(3L, 1L, "3주차", "tutor", "location", "2021-08-03", "10:55", "ZOOM", "줌 링크", "비디오 링크", "설명");
        List<PlanRequest> requestList = Arrays.asList(request1, request2, request3);

        int i = 3;

        for (PlanRequest request : requestList) {
            assertThat(request.getWeek()).isEqualTo(i);
            i--;
        }

        requestList.sort(new Comparator<PlanRequest>() {
            @Override
            public int compare(PlanRequest left, PlanRequest right) {
                if (left.getWeek() < right.getWeek()) {
                    return -1;
                } else if (left.getWeek() > right.getWeek()) {
                    return 1;
                }
                return 0;
            }
        });

        i++;

        for (PlanRequest request : requestList) {
            assertThat(request.getWeek()).isEqualTo(i);
            i++;
        }
    }

    @Test
    void 강의_시간_계산() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lecture = now.plusHours(3);
        boolean flag = lecture.isAfter(now.minusMinutes(31)) && lecture.isBefore(now.plusHours(3).plusMinutes(1));

        assertThat(flag).isTrue();
    }
}
