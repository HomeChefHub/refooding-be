package refooding.api.domain.chat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refooding.api.domain.chat.dto.request.RoomCreateRequest;
import refooding.api.domain.chat.dto.response.RoomResponse;
import refooding.api.domain.chat.service.RoomService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/chat/rooms")
@RequiredArgsConstructor
public class RoomController implements RoomControllerOpenApi{

    private final RoomService roomService;

    /**
     * 채팅방 생성
     */
    @Override
    @PostMapping
    public ResponseEntity<Void> getOrCreate(@Valid @RequestBody RoomCreateRequest request) {
        // TODO : 인증 추가
        Long roomId = roomService.getOrCreate(request);
        return ResponseEntity.created(URI.create("/chat/rooms/" + roomId)).build();
    }

    /**
     * 채팅방 목록 조회
     */
    @Override
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getJoinRooms() {
        // TODO : 인증 추가
        List<RoomResponse> response = roomService.getJoinRoomsByMemberId();
        return ResponseEntity.ok(response);
    }

    /**
     * 채팅방 나가기
     */
    @Override
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> exitRoom(@PathVariable Long roomId) {
        roomService.exitRoom(roomId);
        return ResponseEntity.noContent().build();
    }


}
