package hours22.daedeokserver.popup;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/daedeok/popup")
@RequiredArgsConstructor
public class PopupController {
    private final PopupService service;

    @GetMapping
    public ResponseEntity<PopupResponse> find() {
        return ResponseEntity.ok(service.find());
    }

    @PutMapping
    public ResponseEntity<Void> save(@RequestBody PopupRequest.Update request) {
        service.save(request);

        return ResponseEntity.noContent().build();
    }
}
