package refooding.api.domain.recipe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refooding.api.domain.recipe.dto.request.RecipeLikeRequest;
import refooding.api.domain.recipe.dto.response.RecipeDetailResponse;
import refooding.api.domain.recipe.dto.response.RecipeLikeResponse;
import refooding.api.domain.recipe.dto.response.RecipeResponse;
import refooding.api.domain.recipe.service.RecipeService;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController implements RecipeControllerOpenApi{

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<Slice<RecipeResponse>> getRecipes(
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false) Long lastRecipeId) {

        Slice<RecipeResponse> response = recipeService.getRecipes(searchKeyword, lastRecipeId, PageRequest.ofSize(size));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDetailResponse> getRecipeById(@PathVariable Long recipeId) {
        RecipeDetailResponse response = recipeService.getRecipeById(recipeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/likes")
    public ResponseEntity<Slice<RecipeResponse>> getLikeRecipes(
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Long lastLikeRecipeId) {

        // TODO : 인증 추가
        // 임시 회원 아이디
        Long memberId = 1L;

        Slice<RecipeResponse> favoriteRecipes = recipeService.getLikeRecipes(memberId, lastLikeRecipeId, PageRequest.ofSize(size));
        return ResponseEntity.ok(favoriteRecipes);
    }

    @PostMapping("/likes")
    public ResponseEntity<RecipeLikeResponse> toggleRecipeLike(@Valid @RequestBody RecipeLikeRequest request) {
        //TODO : 인증 추가
        // 임시 회원 아이디
        Long memberId = 1L;

        RecipeLikeResponse response = recipeService.toggleRecipeLike(memberId, request.recipeId());
        return ResponseEntity.ok(response);
    }

    // @GetMapping("/random")
    // @Operation(
    //         summary = "랜덤 레시피 목록 조회",
    //         responses = {
    //                 @ApiResponse(responseCode = "200", description = "추천 목록 조회 성공")
    //         })
    // public ResponseEntity<Slice<RecipeResponse>> getRandomRecipes(
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "5") int size) {
    //
    //     PageRequest pageable = PageRequest.of(page, size);
    //     Slice<RecipeResponse> randomRecipes = recipeService.getRandomRecipes(pageable);
    //
    //     return ResponseEntity.ok(randomRecipes);
    // }


}
