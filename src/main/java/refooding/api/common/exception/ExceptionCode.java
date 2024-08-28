package refooding.api.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    // 400
    FILE_NOT_PROVIDED(HttpStatus.BAD_REQUEST, "전달된 파일이 없습니다"),
    BLANK_FILE_NAME(HttpStatus.BAD_REQUEST, "파일 이름은 비어있을 수 없습니다"),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "잘못된 파일 형식입니다"),
    UNSUPPORTED_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "지원하지않는 파일 확장자입니다"),
    MAX_IMAGE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "최대 업로드 이미지 수를 초과했습니다"),
    INVALID_S3_URL(HttpStatus.BAD_REQUEST, "S3 URL 형식이 올바르지 않습니다"),

    // 401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다"),

    // 404
    NOT_FOUND_EXCHANGE(HttpStatus.NOT_FOUND, "존재하지 않는 식재료 교환글 입니다"),
    NOT_FOUND_REGION(HttpStatus.NOT_FOUND, "존재하지 않는 지역입니다"),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다"),
    NOT_FOUND_CHAT_ROOM(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다"),
    NOT_FOUND_RECIPE(HttpStatus.NOT_FOUND, "존재하지 않는 레시피입니다."),
    NOT_FOUND_INGREDIENT(HttpStatus.NOT_FOUND, "냉장고에 존재하지 않는 재료입니다."),

    // 500
    FILE_CONVERSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 변환중 오류가 발생했습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다"),
    TEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 에러입니다");

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
