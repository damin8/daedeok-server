package hours22.daedeokserver.image.dto;

import hours22.daedeokserver.image.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {
    public List<Summary> image_list;

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private Long id;
        private String url;

        public static List<Summary> of(List<Image> imageList) {
            List<Summary> summaryList = new ArrayList<>();

            for (Image image : imageList) {
                summaryList.add(of(image));
            }

            return summaryList;
        }

        private static Summary of(Image image) {
            return new Summary(image.getId(), image.getUrl());
        }
    }
}
