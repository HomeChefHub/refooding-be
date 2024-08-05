package refooding.api.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    // 404
    NOT_FOUND_EXCHANGE(HttpStatus.NOT_FOUND, "존재하지 않는 식재료 교환글 입니다"),
    NOT_FOUND_REGION(HttpStatus.NOT_FOUND, "존재하지 않는 지역입니다"),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다"),
    NOT_FOUND_CHAT_ROOM(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다"),
    NOT_FOUND_RECIPE(HttpStatus.NOT_FOUND, "존재하지 않는 레시피입니다."),
    NOT_FOUND_MEMBER_INGREDIENT(HttpStatus.NOT_FOUND, "해당 사용자의 냉장고에 존재하지 않는 재료입니다."),

    // 401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다"),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다"),
    TEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 에러입니");

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
