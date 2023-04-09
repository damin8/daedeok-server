package hours22.daedeokserver.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FileRequest {
    private List<String> file_list;
    private String to_path;

    @Getter
    @AllArgsConstructor
    public static class Update {
        private List<String> new_file_list;
        private List<String> delete_file_list;
        private String to_path;
    }
}
