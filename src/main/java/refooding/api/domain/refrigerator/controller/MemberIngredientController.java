package refooding.api.domain.refrigerator.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refooding.api.domain.refrigerator.dto.MemberIngredientCreateRequest;
import refooding.api.domain.refrigerator.dto.MemberIngredientUpdateRequest;
import refooding.api.domain.refrigerator.service.MemberIngredientService;

@Tag(name = "냉장고 API")
@RestController
@RequestMapping("/refrigerator")
@RequiredArgsConstructor
public class MemberIngredientController {

    private final MemberIngredientService memberIngredientService;

    @PostMapping
    public ResponseEntity<String> addMemberIngredient(@RequestBody MemberIngredientCreateRequest request) {
        Long savedId = memberIngredientService.saveMemberIngredient(request);

        return ResponseEntity.ok("MemberIngredient added with ID: " + savedId);
    }

    @PatchMapping("/{memberIngredientId}")
    public ResponseEntity<String> updateMemberIngredient(@PathVariable Long memberIngredientId,
                                                         @RequestBody MemberIngredientUpdateRequest request) {
        memberIngredientService.updateMemberIngredient(memberIngredientId, request);

        return ResponseEntity.ok("MemberIngredient updated with ID: " + memberIngredientId);
    }

}
