package refooding.api.domain.chat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import refooding.api.domain.chat.dto.request.RoomCreateRequest;
import refooding.api.domain.chat.service.RoomService;

import java.net.URI;

@Controller
@RequestMapping("/chat/rooms")
@RequiredArgsConstructor
public class RoomController implements RoomControllerOpenApi{

    private final RoomService roomService;

    @Override
    @PostMapping
    public ResponseEntity<Void> getOrCreate(@Valid @RequestBody RoomCreateRequest request) {
        // TODO : 인증 추가
        Long roomId = roomService.getOrCreate(request);
        return ResponseEntity.created(URI.create("/chat/rooms/" + roomId)).build();
    }

}
