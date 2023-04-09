package hours22.daedeokserver.file.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import hours22.daedeokserver.common.service.RandMaker;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.BusinessException;
import hours22.daedeokserver.exception.business.EntityNotFoundException;
import hours22.daedeokserver.exception.business.FileUploadException;
import hours22.daedeokserver.file.domain.File;
import hours22.daedeokserver.file.domain.FileRepository;
import hours22.daedeokserver.file.dto.FileDTO;
import hours22.daedeokserver.file.dto.FileRequest;
import hours22.daedeokserver.file.dto.FileType;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.domain.handout.Handout;
import hours22.daedeokserver.lecture.domain.handout.HandoutRepository;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.lecture.domain.lecture.LectureUser;
import hours22.daedeokserver.lecture.dto.handout.HandoutRequest;
import hours22.daedeokserver.lecture.service.LectureUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private AmazonS3 s3Client;
    private final List<String> whiteList = Arrays.asList(".hwp", ".pdf", ".ppt", ".pptx", ".doc", ".docx", ".gif", ".jpg", ".jpeg", ".png", ".bmp", ".zip", ".HWP", ".PDF", ".PPT", ".PPTX", ".DOC", ".DOCX", ".GIF", ".JPG", ".JPEG", ".PNG", ".BMP", ".ZIP");
    private final HandoutRepository handoutRepository;
    private final FileRepository fileRepository;
    private final LectureUserService lectureUserService;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public List<String> uploadDummy(List<MultipartFile> fileList) {
        List<String> urls = new ArrayList<>();
        FileType fileType = FileType.DUMMY;

        for (MultipartFile multipartFile : fileList) {
            String url = upload(fileType, multipartFile);
            urls.add(url);
        }

        return urls;
    }

    @Transactional
    public void save(String parentId, List<FileDTO> fileDTOList, FileType fileType) {
        List<File> fileList = new ArrayList<>();

        for (FileDTO fileDTO : fileDTOList) {
            String url = fileDTO.getUrl();
            updateDummyToReal(url, fileType);
            url = url.replaceAll("dummy/", fileType.directory());
            fileList.add(new File(fileDTO.getName(), parentId, url));
        }

        fileRepository.saveAll(fileList);
    }

    @Transactional
    public void delete(String parentId) {
        fileRepository.deleteAllByParentId(parentId);
    }

    @Transactional(readOnly = true)
    public List<FileDTO> find(String parentId) {
        List<File> fileList = fileRepository.findAllByParentId(parentId);

        return FileDTO.of(fileList);
    }

    @Transactional
    public String uploadCertificate(Long userId, Long lectureId, MultipartFile file) {
        LectureUser lectureUser = lectureUserService.findLectureUser(userId, lectureId);

        if (!lectureUser.getStatus().equals(Status.COMPLETE))
            throw new BusinessException(ErrorCode.NOT_CERTIFICATE);

        String url = upload(FileType.CERTIFICATE, file);

        lectureUser.updateUrl(url);
        lectureUserService.save(lectureUser);
        return url;
    }

    @Transactional
    public void deleteCertificate(Long userId, Long lectureId) {
        LectureUser lectureUser = lectureUserService.findLectureUser(userId, lectureId);
        ArrayList<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
        String url = lectureUser.getFileUrl();

        lectureUser.updateUrl(null);
        lectureUserService.save(lectureUser);

        if (url == null)
            return;

        String fileName = url.substring(url.lastIndexOf('/') + 1);
        fileName = FileType.CERTIFICATE.directory() + fileName;
        keys.add(new DeleteObjectsRequest.KeyVersion(fileName));

        DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucket)
                .withKeys(keys)
                .withQuiet(false);

        s3Client.deleteObjects(multiObjectDeleteRequest);
    }

    public void deleteCertificate(List<String> fileList) {

        if (fileList.size() == 0)
            return;

        ArrayList<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();

        for (String url : fileList) {
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            fileName = FileType.CERTIFICATE.directory() + fileName;
            keys.add(new DeleteObjectsRequest.KeyVersion(fileName));
        }

        DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucket)
                .withKeys(keys)
                .withQuiet(false);

        s3Client.deleteObjects(multiObjectDeleteRequest);
    }

    private String upload(FileType fileType, MultipartFile file) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            objectMetadata.setContentLength(bytes.length);

            String fileName = file.getOriginalFilename();
            String rand = RandMaker.generateKey(false, 20);

            if (fileName.contains("..") || fileName.contains("/"))
                throw new FileUploadException(ErrorCode.FILE_INVALID);

            String ext = fileName.substring(fileName.lastIndexOf('.'));
            String directory = fileType.directory();

            if (!whiteList.contains(ext))
                throw new FileUploadException(ErrorCode.FILE_INVALID);

            String ranTitle = directory + rand + ext;

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            s3Client.putObject(new PutObjectRequest(bucket, ranTitle, byteArrayInputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return s3Client.getUrl(bucket, ranTitle).toString();
        } catch (IOException e) {
            throw new FileUploadException(ErrorCode.FILE_UPLOAD_FAIL);
        }
    }

    private void updateDummyToReal(String url, FileType fileType) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        String from = FileType.DUMMY.directory() + fileName;
        String to = fileType.directory() + fileName;

        if (s3Client.doesObjectExist(bucket, from)) {
            s3Client.copyObject(bucket, from, bucket, to);
            s3Client.setObjectAcl(bucket, to, CannedAccessControlList.PublicRead);
            s3Client.deleteObject(bucket, from);
        }
    }

    public void uploadReal(FileRequest request) {
        List<String> fileList = request.getFile_list();
        FileType fileType = FileType.valueOf(request.getTo_path());

        for (String url : fileList)
            updateDummyToReal(url, fileType);
    }

    public void update(FileRequest.Update request) {
        FileRequest newFileList = new FileRequest(request.getNew_file_list(), null);
        FileType fileType = FileType.valueOf(request.getTo_path());

        for (String url : newFileList.getFile_list())
            updateDummyToReal(url, fileType);

        ArrayList<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();

        for (String url : request.getDelete_file_list()) {
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            fileName = fileType.directory() + fileName;
            keys.add(new DeleteObjectsRequest.KeyVersion(fileName));
        }

        if (!keys.isEmpty()) {
            DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucket)
                    .withKeys(keys)
                    .withQuiet(false);

            s3Client.deleteObjects(multiObjectDeleteRequest);
        }
    }

    @Transactional
    public void saveHandoutList(Lecture lecture, List<HandoutRequest> fileRequestList) {
        if (fileRequestList == null || fileRequestList.size() == 0)
            return;

        List<Handout> files = new ArrayList<>();

        for (HandoutRequest fileRequest : fileRequestList) {
            String url = fileRequest.getUrl();
            FileType fileType = FileType.HANDOUT;
            updateDummyToReal(url, fileType);
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            String to = fileType.directory() + fileName;
            url = s3Client.getUrl(bucket, to).toString();

            Handout file = fileRequest.toHandout(lecture, url);
            files.add(file);
        }

        handoutRepository.saveAll(files);
    }

    @Transactional
    public void deleteHandoutList(Lecture lecture, List<HandoutRequest> fileRequestList) {
        if (fileRequestList == null || fileRequestList.size() == 0)
            return;

        List<String> urlList = fileRequestList.stream()
                .map(HandoutRequest::getUrl)
                .collect(Collectors.toList());

        List<Handout> handoutList = handoutRepository.findHandoutsByLectureAndUrlIn(lecture, urlList);
        handoutRepository.deleteHandoutsByLectureAndUrlIn(lecture, urlList);
        ArrayList<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();

        for (Handout handout : handoutList) {
            String url = handout.getUrl();
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            fileName = FileType.HANDOUT + fileName;
            keys.add(new DeleteObjectsRequest.KeyVersion(fileName));
        }

        if (!keys.isEmpty()) {
            DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucket)
                    .withKeys(keys)
                    .withQuiet(false);

            s3Client.deleteObjects(multiObjectDeleteRequest);
        }
    }

    public List<Handout> findHandoutsById(Long id) {
        return handoutRepository.findHandoutsByLecture_Id(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.HANDOUT_NOT_FOUND));
    }
}
