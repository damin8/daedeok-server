package hours22.daedeokserver.popup;

import hours22.daedeokserver.file.dto.FileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PopupRequest {
    private String url;
    private String link;

    public Popup toEntity() {
        url = url.replaceAll("dummy/", FileType.POPUP.directory());
        return new Popup(url, link);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        private List<Long> unchanged_popup_id_list;
        public List<PopupRequest> popup_list;
    }
}
