package refooding.api.domain.recipe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import refooding.api.domain.recipe.dto.request.RecipeLikeRequest;
import refooding.api.domain.recipe.dto.response.RecipeDetailResponse;
import refooding.api.domain.recipe.dto.response.RecipeLikeResponse;
import refooding.api.domain.recipe.dto.response.RecipeResponse;

@Tag(name = "레시피 API")
public interface RecipeControllerOpenApi {

    @Operation(
            summary = "전체 레시피 목록 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "레시피 목록 조회 성공")
            }
    )
    @Parameters(
            value = {
                    @Parameter(
                            name = "searchKeyword",
                            description = "레시피명 또는 재료명",
                            example = "김치찌개(레시피명), 감자(재료명)"
                    ),
                    @Parameter(name = "size", description = "요청 레시피 수(0개 이상~50개 이하)"),
                    @Parameter(name = "lastExchangeId", description = "이전 요청 목록의 마지막 레시피 아이디"),
            }
    )
    ResponseEntity<Slice<RecipeResponse>> getRecipes(String searchKeyword, int size, Long lastRecipeId);

    @Operation(
            summary = "레시피 상세 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "레시피 상세 조회 성공")
            }
    )
    @Parameter(name = "recipeId", description = "레시피 아이디")
    ResponseEntity<RecipeDetailResponse> getRecipeById(Long recipeId);

    @Operation(
            summary = "레시피 좋아요 토글",
            responses = {
                    @ApiResponse(responseCode = "200", description = "레시피 찜 상태 업데이트 성공"),
                    @ApiResponse(responseCode = "404", description = "레시피 또는 회원을 찾을 수 없음")
            }
    )
    public ResponseEntity<RecipeLikeResponse> toggleRecipeLike(RecipeLikeRequest request);

    @Operation(
            summary = "좋아요한 레시피 목록 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "레시피 좋아요 목록 조회 성공")
            })
    ResponseEntity<Slice<RecipeResponse>> getLikeRecipes(int size, Long lastLikeRecipeId);

}
