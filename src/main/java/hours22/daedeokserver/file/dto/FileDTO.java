package hours22.daedeokserver.file.dto;

import hours22.daedeokserver.file.domain.File;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class FileDTO {
    private String name;
    private String url;

    public static List<FileDTO> of(List<File> fileList) {
        List<FileDTO> fileDTOList = new ArrayList<>();
        for (File file : fileList)
            fileDTOList.add(of(file));

        return fileDTOList;
    }

    private static FileDTO of(File file) {
        return new FileDTO(file.getName(), file.getUrl());
    }
}
