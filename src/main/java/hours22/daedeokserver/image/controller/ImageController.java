package hours22.daedeokserver.image.controller;

import hours22.daedeokserver.image.dto.ImageRequest;
import hours22.daedeokserver.image.dto.ImageResponse;
import hours22.daedeokserver.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/daedeok/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService service;

    @GetMapping
    public ResponseEntity<ImageResponse> find() {
        return ResponseEntity.ok(service.find());
    }

    @PutMapping
    public ResponseEntity<Void> save(@RequestBody List<ImageRequest> request) {
        service.save(request);

        return ResponseEntity.noContent().build();
    }
}
