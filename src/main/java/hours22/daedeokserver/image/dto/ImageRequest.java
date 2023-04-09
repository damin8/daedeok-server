package hours22.daedeokserver.image.dto;

import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.image.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageRequest {
    private String url;

    public Image toImage() {
        url = url.replaceAll("dummy/", FileType.MAIN_IMAGE.directory());
        return new Image(url);
    }
}
