package refooding.api.domain.recipe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
import refooding.api.domain.recipe.dto.FavoriteRecipeToggleResponse;
import refooding.api.domain.recipe.dto.RecipeDetailResponse;
import refooding.api.domain.recipe.dto.RecipeResponse;
import refooding.api.domain.recipe.service.RecipeService;

import java.util.Arrays;
import java.util.List;

@Tag(name = "레시피 조회 API")
@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    @Operation(
            summary = "전체 레시피 목록 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "레시피 목록 조회 성공",
                            content = @Content(schema = @Schema(implementation = RecipeResponse.class))
                    )
            }
    )
    public ResponseEntity<Slice<RecipeResponse>> getRecipes(@RequestParam(required = false)
                                                                @Parameter(example = "감자,당근") String ingredientNames,
                                                           @RequestParam(required = false) String recipeName,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int size) {

        if (ingredientNames != null && !ingredientNames.isEmpty()) { // 주 재료명으로 조회하는 경우
            List<String> ingredientNameList = Arrays.asList(ingredientNames.split(",")); // ,을 기준으로 슬라이싱
            Slice<RecipeResponse> response = recipeService.getRecipesByIngredientNames(ingredientNameList, PageRequest.of(page, size));
            return ResponseEntity.ok(response);
        }
        if (recipeName != null) { // 레시피명으로 조회하는 경우
            Slice<RecipeResponse> response = recipeService.getRecipesByRecipeName(recipeName, PageRequest.of(page, size));
            return ResponseEntity.ok(response);
        }
        else { // 전체 조회
            Slice<RecipeResponse> response = recipeService.getRecipes(PageRequest.of(page, size));
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{recipeId}")
    @Operation(
            summary = "레시피 상세 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "레시피 상세 조회 성공",
                            content = @Content(schema = @Schema(implementation = RecipeDetailResponse.class))
                    )
            }
    )
    public ResponseEntity<RecipeDetailResponse> getRecipeDetailById(@PathVariable Long recipeId) {
        RecipeDetailResponse response = recipeService.getRecipeDetailById(recipeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/toggle-favorite/{memberId}/{recipeId}")
    @Operation(
            summary = "레시피 찜/찜 해제 토글",
            responses = {
                    @ApiResponse(responseCode = "200", description = "레시피 찜 상태 토글 성공"),
                    @ApiResponse(responseCode = "404", description = "레시피 또는 멤버를 찾을 수 없음")
            }
    )
    public ResponseEntity<?> toggleFavoriteRecipe(@PathVariable Long memberId, @PathVariable Long recipeId) {
        try {
            boolean isFavorited = recipeService.toggleFavoriteRecipe(memberId, recipeId);
            String message = isFavorited ? "찜 상태로 바뀌었습니다." : "찜 상태가 해제되었습니다.";
            return ResponseEntity.ok().body(FavoriteRecipeToggleResponse.builder().success(true).message(message).build());
        } catch (CustomException e) {
            return ResponseEntity.status(e.getExceptionCode().getStatus()).body(FavoriteRecipeToggleResponse.builder().success(false).message(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(FavoriteRecipeToggleResponse.builder().success(false).message(ExceptionCode.INTERNAL_SERVER_ERROR.getMessage()).build());
        }
    }

    @GetMapping("/members/{memberId}/favorites")
    @Operation(
            summary = "멤버별 찜한 레시피 목록 조회",
            responses = {
            @ApiResponse(responseCode = "200", description = "찜 목록 조회 성공")
    })
    public ResponseEntity<Slice<RecipeResponse>> getFavoriteRecipesByMemberId(@PathVariable Long memberId,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "5") int size) {
        Slice<RecipeResponse> favoriteRecipes = recipeService.getFavoriteRecipesByMemberId(memberId, PageRequest.of(page, size));
        return ResponseEntity.ok(favoriteRecipes);
    }

}
