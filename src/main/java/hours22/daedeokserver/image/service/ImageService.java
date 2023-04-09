package hours22.daedeokserver.image.service;

import hours22.daedeokserver.image.domain.Image;
import hours22.daedeokserver.image.domain.ImageRepository;
import hours22.daedeokserver.image.dto.ImageRequest;
import hours22.daedeokserver.image.dto.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public ImageResponse find() {
        List<Image> imageList = imageRepository.findAll();

        return new ImageResponse(ImageResponse.Summary.of(imageList));
    }

    @Transactional
    public void save(List<ImageRequest> request) {
        imageRepository.deleteAll();

        List<Image> imageList = new ArrayList<>();

        for (ImageRequest imageRequest : request) {
            imageList.add(imageRequest.toImage());
        }

        imageRepository.saveAll(imageList);
    }
}
