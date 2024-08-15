package refooding.api.common.util;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;

import java.util.List;

@Getter
public class ImageUtil {

    private static final List<String> WHITE_LIST = List.of("jpg", "jpeg", "png", "webp", "heic");

    public static String getOriginalFilename(MultipartFile file) {
        return file.getOriginalFilename();
    }

    public static void validate(MultipartFile file) {
        validateNotNull(file);
        String fileName = getOriginalFilename(file);
        validateFileNameNotBlank(fileName);
        validateExtension(fileName);
    }

    private static void validateNotNull(MultipartFile file) {
        if (file == null) {
            throw new CustomException(ExceptionCode.FILE_NOT_PROVIDED);
        }
    }

    private static void validateFileNameNotBlank(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new CustomException(ExceptionCode.BLANK_FILE_NAME);
        }
    }

    private static void validateExtension(String fileName) {
        int extensionIndex = fileName.lastIndexOf(".");
        if (extensionIndex == -1 || fileName.endsWith(".")) {
            throw new CustomException(ExceptionCode.INVALID_FILE_EXTENSION);
        }
        String extension = fileName.substring(extensionIndex + 1);
        if (!WHITE_LIST.contains(extension.toLowerCase())) {
            throw new CustomException(ExceptionCode.UNSUPPORTED_FILE_EXTENSION);
        }
    }
}
