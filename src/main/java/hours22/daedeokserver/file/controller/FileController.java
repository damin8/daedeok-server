package hours22.daedeokserver.file.controller;

import hours22.daedeokserver.file.dto.FileRequest;
import hours22.daedeokserver.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/daedeok/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping
    public ResponseEntity<List<String>> uploadDummy(@RequestParam("file_list") List<MultipartFile> fileList) {

        return ResponseEntity.ok(fileService.uploadDummy(fileList));
    }

    @PostMapping("/real")
    public ResponseEntity<Void> uploadReal(@RequestBody FileRequest fileRequest) {
        fileService.uploadReal(fileRequest);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateFile(@RequestBody FileRequest.Update request) {
        fileService.update(request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/certificate/{user_id}/{lecture_id}")
    public ResponseEntity<String> uploadCertificate(@PathVariable Long user_id,
                                                    @PathVariable Long lecture_id,
                                                    @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(fileService.uploadCertificate(user_id, lecture_id, file));
    }

    @DeleteMapping("/certificate/{user_id}/{lecture_id}")
    public ResponseEntity<String> deleteCertificate(@PathVariable Long user_id,
                                                    @PathVariable Long lecture_id) {
        fileService.deleteCertificate(user_id, lecture_id);

        return ResponseEntity.noContent().build();
    }
}
