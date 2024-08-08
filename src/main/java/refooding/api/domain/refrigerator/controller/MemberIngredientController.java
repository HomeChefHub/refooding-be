package refooding.api.domain.refrigerator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refooding.api.domain.refrigerator.dto.MemberIngredientCreateRequest;
import refooding.api.domain.refrigerator.dto.MemberIngredientDeleteRequest;
import refooding.api.domain.refrigerator.dto.MemberIngredientResponse;
import refooding.api.domain.refrigerator.dto.MemberIngredientUpdateRequest;
import refooding.api.domain.refrigerator.service.MemberIngredientService;

import java.util.List;

@Tag(name = "냉장고 API")
@RestController
@RequestMapping("/refrigerator")
@RequiredArgsConstructor
public class MemberIngredientController {

    private final MemberIngredientService memberIngredientService;

    @PostMapping
    @Operation(
            summary = "냉장고 재료 추가",
            responses = {
                    @ApiResponse(responseCode = "200", description = "재료 추가 성공"),
            }
    )
    public ResponseEntity<String> addMemberIngredient(@RequestBody MemberIngredientCreateRequest request) {
        Long savedId = memberIngredientService.saveMemberIngredient(request);

        return ResponseEntity.ok("MemberIngredient added with ID: " + savedId);
    }

    @PatchMapping("/{memberIngredientId}")
    @Operation(
            summary = "냉장고 재료 수정",
            responses = {
                    @ApiResponse(responseCode = "200", description = "재료 수정 성공"),
                    @ApiResponse(responseCode = "404", description = "지정된 재료를 찾을 수 없음")
            }
    )
    public ResponseEntity<String> updateMemberIngredient(@PathVariable Long memberIngredientId,
                                                         @RequestBody MemberIngredientUpdateRequest request) {
        memberIngredientService.updateMemberIngredient(memberIngredientId, request);

        return ResponseEntity.ok("MemberIngredient updated with ID: " + memberIngredientId);
    }

    @DeleteMapping("/{memberIngredientId}")
    @Operation(
            summary = "냉장고 재료 삭제",
            responses = {
                    @ApiResponse(responseCode = "200", description = "재료 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "지정된 재료를 찾을 수 없음")
            }
    )
    public ResponseEntity<String> deleteMemberIngredient(@PathVariable Long memberIngredientId,
                                                       @RequestBody MemberIngredientDeleteRequest request) {
        memberIngredientService.deleteMemberIngredient(memberIngredientId, request);

        return ResponseEntity.ok("MemberIngredient deleted with ID: " + memberIngredientId);
    }

    @GetMapping("/{memberIngredientId}")
    @Operation(
            summary = "냉장고 재료 단건 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "재료 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "지정된 재료를 찾을 수 없음")
            }
    )
    public ResponseEntity<MemberIngredientResponse> getMemberIngredient(@PathVariable Long memberIngredientId) {
        MemberIngredientResponse response = memberIngredientService.getMemberIngredient(memberIngredientId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/members/{memberId}")
    @Operation(
            summary = "멤버별 냉장고 재료 목록 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "재료 목록 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "멤버를 찾을 수 없음")
            }
    )
    public ResponseEntity<List<MemberIngredientResponse>> getMemberIngredients(@PathVariable Long memberId) {
        List<MemberIngredientResponse> responses = memberIngredientService.getMemberIngredients(memberId);
        return ResponseEntity.ok(responses);
    }


}
