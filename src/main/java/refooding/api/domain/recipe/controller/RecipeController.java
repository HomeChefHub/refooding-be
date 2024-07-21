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
    public ResponseEntity<List<RecipeResponse>> getRecipes() {
        List<RecipeResponse> response = recipeService.getRecipes();
        return ResponseEntity.ok(response);
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
//
//    @GetMapping()
//    @Operation(
//            summary = " 주재료 이름으로 레시피 목록 조회",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "레시피 목록 조회 성공",
//                            content = @Content(schema = @Schema(implementation = RecipeResponse.class))
//                    )
//            }
//    )
//    public ResponseEntity<List<RecipeResponse>> getRecipesByIngredientName(@RequestParam String ingredientName) {
//        List<RecipeResponse> response = recipeService.getRecipesByIngredientName(ingredientName);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping()
//    @Operation(
//            summary = "레시피 이름으로 레시피 목록 조회",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "레시피 목록 조회 성공",
//                            content = @Content(schema = @Schema(implementation = RecipeResponse.class))
//                    )
//            }
//    )
//    public ResponseEntity<List<RecipeResponse>> getRecipesByRecipeName(@RequestParam String recipeName) {
//        List<RecipeResponse> response = recipeService.getRecipesByRecipeName(recipeName);
//        return ResponseEntity.ok(response);
//    }

}
