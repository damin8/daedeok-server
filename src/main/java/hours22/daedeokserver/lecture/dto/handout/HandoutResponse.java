package hours22.daedeokserver.lecture.dto.handout;

import hours22.daedeokserver.lecture.domain.handout.Handout;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class HandoutResponse {
    private Long id;
    private String name;
    private String url;

    public static List<HandoutResponse> of(List<Handout> handoutList) {
        List<HandoutResponse> handoutResponseList = new ArrayList<>();

        for (Handout handout : handoutList) {
            handoutResponseList.add(of(handout));
        }

        return handoutResponseList;
    }

    private static HandoutResponse of(Handout handout) {
        return new HandoutResponse(handout.getId(), handout.getName(), handout.getUrl());
    }
}
