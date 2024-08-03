package refooding.api.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import refooding.api.domain.chat.dto.request.RoomCreateRequest;
import refooding.api.domain.chat.dto.response.RoomResponse;

@Tag(name = "채팅 API")
public interface RoomControllerOpenApi {

    @Operation(
            summary = "채팅방 생성",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "채팅방 생성 성공"
                    )
            }
    )
    ResponseEntity<Void> getOrCreate(RoomCreateRequest request);

}
