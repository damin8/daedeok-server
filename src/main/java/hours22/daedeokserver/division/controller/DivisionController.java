package hours22.daedeokserver.division.controller;

import hours22.daedeokserver.division.dto.DivisionDTO;
import hours22.daedeokserver.division.service.DivisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/daedeok/division")
@RequiredArgsConstructor
public class DivisionController {
    private final DivisionService service;

    @GetMapping
    public ResponseEntity<List<DivisionDTO>> find() {
        return ResponseEntity.ok(service.find());
    }

    @GetMapping("/detail")
    public ResponseEntity<DivisionDTO.UpdateResponse> find(@RequestParam("first_division") String firstDivision) {
        return ResponseEntity.ok(service.find(firstDivision));
    }

    @DeleteMapping
    public ResponseEntity<List<DivisionDTO>> delete(@RequestParam String name) {
        service.delete(name);

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody DivisionDTO request) {
        service.save(request);

        return ResponseEntity.created(URI.create("")).build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody DivisionDTO.UpdateRequest request) {
        service.update(request);

        return ResponseEntity.noContent().build();
    }
}
