package hours22.daedeokserver.lecture.dto.handout;

import hours22.daedeokserver.lecture.domain.handout.Handout;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class HandoutRequest {
    private String name;
    private String url;

    public Handout toHandout(Lecture lecture, String url) {
        return Handout.builder()
                .lecture(lecture)
                .name(name)
                .url(url)
                .build();
    }

    public static List<HandoutRequest> of(List<Handout> handoutList) {
        List<HandoutRequest> handoutRequestList = new ArrayList<>();

        for (Handout handout : handoutList) {
            handoutRequestList.add(of(handout));
        }

        return handoutRequestList;
    }

    private static HandoutRequest of(Handout handout) {
        return new HandoutRequest(handout.getName(), handout.getUrl());
    }

    @Getter
    @AllArgsConstructor
    public static class Update {
        private List<HandoutRequest> new_file_list;
        private List<HandoutRequest> delete_file_list;
    }
}
