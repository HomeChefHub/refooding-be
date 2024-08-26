package refooding.api.common.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
import refooding.api.common.util.ImageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final String IMAGE_BASE_URI = "images/";
    private final String EXCHANGE_URI = "exchange/";
    private final String REFRIGERATOR_URI = "refrigerator/";

    public List<String> uploadExchangeImg(List<MultipartFile> files) {
        files.forEach(ImageUtil::validate);
        return files.stream()
                .map(file -> uploadImage(file, EXCHANGE_URI))
                .toList();
    }

    public List<String> uploadFridgeIngredientImg(List<MultipartFile> files) {
        files.forEach(ImageUtil::validate);
        return files.stream()
                .map(file -> uploadImage(file, REFRIGERATOR_URI))
                .toList();
    }

    private String uploadImage(MultipartFile file, String subDir) {
        String newFileName = createNewFileName(ImageUtil.getOriginalFilename(file));
        String filePath = IMAGE_BASE_URI + subDir + newFileName;
        return uploadFile(file, filePath);
    }

    private String createNewFileName(String fileName) {
        return UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
    }

    private String uploadFile(MultipartFile file, String filename) {
        File newFile = convertMultipartFileToFile(file)
                .orElseThrow(() -> new CustomException(ExceptionCode.FILE_CONVERSION_ERROR));
        String uploadImageUrl = putS3(newFile, filename);
        deleteLocalFile(newFile);
        return uploadImageUrl;
    }

    private Optional<File> convertMultipartFileToFile(MultipartFile file) {
        File convertFile = new File(ImageUtil.getOriginalFilename(file));
        try {
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            throw new CustomException(ExceptionCode.FILE_CONVERSION_ERROR);
        }
        return Optional.empty();
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     *  convertMultipartFileToFile 함수로 만들어진 File 로컬에서 제거
     */
    private void deleteLocalFile(File targetFile) {
        if (targetFile.delete()) {
            return;
        }
        log.info("File delete fail, fileName = {}", targetFile.getName());
    }

}
