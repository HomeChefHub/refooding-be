package refooding.api.common.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
import refooding.api.common.util.ImageUtil;
import refooding.api.domain.fridge.entity.FridgeIngredient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    private final String INGREDIENT_URL = "fridge_ingredient/";

    public String uploadExchangeImage(MultipartFile file) {
        ImageUtil.validate(file);
        return uploadImage(file, EXCHANGE_URI);
    }

    public List<String> uploadExchangeImages(List<MultipartFile> files) {
        return files.stream()
                .map(this::uploadExchangeImage)
                .toList();
    }

    public String uploadIngredientImage(MultipartFile file) {
        ImageUtil.validate(file);
        return uploadImage(file, INGREDIENT_URL);
    }

    public List<String> uploadIngredientImages(List<MultipartFile> files) {
        return files.stream()
                .map(this::uploadIngredientImage)
                .toList();
    }

    public void deleteFiles(List<String> imageUrls) {
        List<String> keys = imageUrls.stream()
                .map(this::getObjectKeyFromUrl)
                .toList();
        keys.forEach(this::deleteS3);
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

    public void deleteS3(String key) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, key);
        amazonS3Client.deleteObject(deleteObjectRequest);
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

    private String getObjectKeyFromUrl(String imageUrl) {
        URI uri = createUri(imageUrl);
        String path = uri.getPath();

        if (path != null && path.startsWith("/")) {
            return path.substring(1);
        }

        throw new CustomException(ExceptionCode.INVALID_S3_URL);
    }

    private URI createUri(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new CustomException(ExceptionCode.INVALID_S3_URL);
        }
    }

}
