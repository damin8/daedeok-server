package hours22.daedeokserver.common.service;

import hours22.daedeokserver.common.dto.LocalDateTimeDTO;
import hours22.daedeokserver.file.dto.FileRequest;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.lecture.dto.plan.PlanRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommonService {
    public static Pageable getPageable(int page, int required_count) {

        return PageRequest.of(page, required_count, Sort.by("id").descending());
    }

    public static LocalDateTimeDTO getDate(List<PlanRequest> planRequestList) {
        LocalDateTime startDate = planRequestList.stream()
                .map(PlanRequest::getRealDate)
                .min(LocalDateTime::compareTo)
                .get();

        LocalDateTime endDate = planRequestList.stream()
                .map(PlanRequest::getRealDate)
                .max(LocalDateTime::compareTo)
                .get();

        return new LocalDateTimeDTO(startDate, endDate);
    }

    public static LocalDateTimeDTO getUpdateDate(List<PlanRequest.Update> planRequestList) {
        LocalDateTime startDate = planRequestList.stream()
                .map(PlanRequest.Update::getRealDate)
                .min(LocalDateTime::compareTo)
                .get();

        LocalDateTime endDate = planRequestList.stream()
                .map(PlanRequest.Update::getRealDate)
                .max(LocalDateTime::compareTo)
                .get();

        return new LocalDateTimeDTO(startDate, endDate);
    }

    public static FileRequest getUrl(String content, FileType fileType) {
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
        Matcher matcher = pattern.matcher(content);

        List<String> urlList = new ArrayList<>();

        while (matcher.find()) {
            String url = matcher.group(1);
            urlList.add(url);
        }

        return new FileRequest(urlList, fileType.name());
    }

    public static String encrypt(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());

        return bytesToHex(md.digest());
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static String getParentId(FileType fileType, Long id) {
        return fileType.toString() + id;
    }
}
