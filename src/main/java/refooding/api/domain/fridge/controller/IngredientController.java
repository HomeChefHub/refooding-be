package refooding.api.domain.fridge.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refooding.api.domain.fridge.dto.request.IngredientCreateRequest;
import refooding.api.domain.fridge.dto.request.IngredientUpdateRequest;
import refooding.api.domain.fridge.dto.response.IngredientResponse;
import refooding.api.domain.fridge.service.IngredientService;

import java.net.URI;

@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController implements IngredientControllerOpenApi{

    private final IngredientService ingredientService;

    @GetMapping
    public ResponseEntity<Slice<IngredientResponse>> getIngredients(
            @RequestParam(required = false) String ingredientName,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Long lastIngredientId,
            @RequestParam(required = false) Integer daysUntilExpiration) {

        // TODO : 인증 추가
        // 임시 회원 아이디
        Long memberId = 1L;

        Slice<IngredientResponse> response = ingredientService.getIngredients(memberId, ingredientName, lastIngredientId, daysUntilExpiration, PageRequest.ofSize(size));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> add(@Valid @ModelAttribute IngredientCreateRequest request) {

        // TODO : 인증 추가
        // 임시 회원 아이디
        Long memberId = 1L;

        Long ingredientId = ingredientService.create(memberId, request);
        return ResponseEntity.created(URI.create("/api/v1/ingredient/" + ingredientId)).build();
    }

    @PatchMapping("/{ingredientId}")
    public ResponseEntity<Void> update(
            @PathVariable Long ingredientId,
            @ModelAttribute IngredientUpdateRequest request) {

        // TODO : 인증 추가
        // 임시 회원 아이디
        Long memberId = 1L;
        ingredientService.update(memberId, ingredientId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Void> delete(@PathVariable Long ingredientId) {

        // TODO : 인증 추가
        // 임시 회원 아이디
        Long memberId = 1L;
        ingredientService.delete(memberId, ingredientId);

        return ResponseEntity.noContent().build();
    }

}
