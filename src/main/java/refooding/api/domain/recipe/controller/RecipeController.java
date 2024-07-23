package refooding.api.domain.recipe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refooding.api.domain.recipe.dto.RecipeDetailResponse;
import refooding.api.domain.recipe.dto.RecipeResponse;
import refooding.api.domain.recipe.service.RecipeService;

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
    public ResponseEntity<List<RecipeResponse>> getRecipes(@RequestParam(required = false) List<String> ingredientNames,
                                                           @RequestParam(required = false) String recipeName) {

        if (ingredientNames != null && !ingredientNames.isEmpty()) { // 주 재료명으로 조회하는 경우
            List<RecipeResponse> response = recipeService.getRecipesByIngredientNames(ingredientNames);
            return ResponseEntity.ok(response);
        }
        if (recipeName != null) { // 레시피명으로 조회하는 경우
            List<RecipeResponse> response = recipeService.getRecipesByRecipeName(recipeName);
            return ResponseEntity.ok(response);
        }
        else { // 전체 조회
            List<RecipeResponse> response = recipeService.getRecipes();
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

}
