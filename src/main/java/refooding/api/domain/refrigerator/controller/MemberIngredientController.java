package refooding.api.domain.refrigerator.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import refooding.api.domain.refrigerator.dto.MemberIngredientRequest;
import refooding.api.domain.refrigerator.service.MemberIngredientService;

@Tag(name = "냉장고 API")
@RestController
@RequestMapping("/refrigerator")
@RequiredArgsConstructor
public class MemberIngredientController {

    private final MemberIngredientService memberIngredientService;

    @PostMapping
    public ResponseEntity<String> addMemberIngredient(@RequestBody MemberIngredientRequest request) {
        Long savedId = memberIngredientService.saveMemberIngredient(request);

        return ResponseEntity.ok("MemberIngredient added with ID: " + savedId);
    }
}
