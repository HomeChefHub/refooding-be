package refooding.api.domain.ingredient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import refooding.api.domain.ingredient.dto.request.IngredientCreateRequest;
import refooding.api.domain.ingredient.dto.request.IngredientUpdateRequest;
import refooding.api.domain.ingredient.dto.response.IngredientResponse;

@Tag(name = "재료 API")
public interface IngredientControllerOpenApi {

    @Operation(summary = "재료 목록 조회")
    @Parameters(
            value = {
                    @Parameter(
                            name = "ingredientName",
                            description = "재료명",
                            example = "당근"
                    ),
                    @Parameter(name = "size", description = "요청 재료 갯수"),
                    @Parameter(name = "lastIngredientId", description = "이전 요청 목록의 마지막 재료 아이디"),
                    @Parameter(name = "daysUntilExpiration", description = "이전 요청 목록의 마지막 재료의 만료 일수")
            }
    )
    ResponseEntity<Slice<IngredientResponse>> getIngredients(String ingredientName,
                                                             int size,
                                                             Long lastIngredientId,
                                                             Integer daysUntilExpiration);

    @Operation(
            summary = "재료 추가",
            responses = {
                    @ApiResponse(responseCode = "200", description = "재료 추가 성공"),
            }
    )
    ResponseEntity<Void> add(IngredientCreateRequest request);

    @Operation(
            summary = "재료 수정",
            responses = {
                    @ApiResponse(responseCode = "200", description = "재료 수정 성공"),
                    @ApiResponse(responseCode = "404", description = "지정된 재료를 찾을 수 없음")
            }
    )
    ResponseEntity<Void> update(Long ingredientId, IngredientUpdateRequest request);

    @Operation(
            summary = "재료 삭제",
            responses = {
                    @ApiResponse(responseCode = "200", description = "재료 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "지정된 재료를 찾을 수 없음")
            }
    )
    ResponseEntity<Void> delete(Long ingredientId);

}
