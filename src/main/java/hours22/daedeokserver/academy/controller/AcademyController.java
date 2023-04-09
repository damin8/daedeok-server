package hours22.daedeokserver.academy.controller;

import hours22.daedeokserver.academy.dto.AcademyRequest;
import hours22.daedeokserver.academy.dto.AcademyResponse;
import hours22.daedeokserver.academy.service.AcademyService;
import hours22.daedeokserver.common.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/daedeok/acinfo")
@RequiredArgsConstructor
public class AcademyController {
    private final AcademyService academyService;

    @GetMapping("/introduce")
    public ResponseEntity<AcademyResponse> find() {

        return ResponseEntity.ok(academyService.find());
    }

    @GetMapping("/eduvision")
    public ResponseEntity<AcademyResponse> findEduvision() {

        return ResponseEntity.ok(academyService.findEduvision());
    }

    @PutMapping("/introduce")
    public ResponseEntity<Void> update(@RequestBody AcademyRequest request) {
        academyService.update(request);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/eduvision")
    public ResponseEntity<Void> updateEduvision(@RequestBody AcademyRequest request) {
        academyService.updateEduvision(request);

        return ResponseEntity.noContent().build();
    }
}
