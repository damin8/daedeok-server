package hours22.daedeokserver.popup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PopupResponse {
    public List<Summary> popup_list;

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private Long id;
        private String url;
        private String link;

        public static List<Summary> of(List<Popup> list) {
            List<Summary> summaryList = new ArrayList<>();

            for (Popup popup : list) {
                summaryList.add(of(popup));
            }

            return summaryList;
        }

        private static Summary of(Popup popup) {
            return new Summary(popup.getId(), popup.getUrl(), popup.getLink());
        }
    }
}
