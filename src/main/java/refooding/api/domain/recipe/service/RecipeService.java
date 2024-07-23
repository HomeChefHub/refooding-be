package refooding.api.domain.recipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refooding.api.domain.recipe.dto.ManualResponse;
import refooding.api.domain.recipe.dto.RecipeDetailResponse;
import refooding.api.domain.recipe.dto.RecipeIngredientResponse;
import refooding.api.domain.recipe.dto.RecipeResponse;
import refooding.api.domain.recipe.entity.Recipe;
import refooding.api.domain.recipe.repository.RecipeRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    /**
     * 레시피 목록 조회
     * @param pageable
     * @return
     */
    public Slice<RecipeResponse> getRecipes(Pageable pageable) {
        Slice<Recipe> allRecipes = recipeRepository.findAllBySlice(pageable);

        // DTO 변환
        List<RecipeResponse> recipeResponses = allRecipes.getContent().stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .imgSrc(recipe.getMainImgSrc())
                        .build())
                .toList();

        // Slice로 변환 후 반환
        return new SliceImpl<>(recipeResponses, pageable, allRecipes.hasNext());
    }


    /**
     * 재료명으로 레시피 목록 조회
     * @param ingredientNames
     * @param pageable
     * @return
     */
    public Slice<RecipeResponse> getRecipesByIngredientNames(List<String> ingredientNames, Pageable pageable) {
        Slice<Recipe> findRecipes = recipeRepository.findByMainIngredientNames(ingredientNames, pageable);

        // DTO 변환
        List<RecipeResponse> recipeResponses = findRecipes.getContent().stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .imgSrc(recipe.getMainImgSrc())
                        .build())
                .toList();

        // Slice로 변환 후 반환
        return new SliceImpl<>(recipeResponses, pageable, findRecipes.hasNext());
    }

    /**
     * 레시피 이름으로 레시피 목록 조회
     * @param recipeName
     * @param pageable
     * @return
     */
    public Slice<RecipeResponse> getRecipesByRecipeName(String recipeName, Pageable pageable) {
        Slice<Recipe> findRecipes = recipeRepository.findByNameContaining(recipeName, pageable);

        // DTO 변환
        List<RecipeResponse> recipeResponses = findRecipes.getContent().stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .imgSrc(recipe.getMainImgSrc())
                        .build())
                .toList();

        // Slice로 변환 후 반환
        return new SliceImpl<>(recipeResponses, pageable, findRecipes.hasNext());

    }

    /**
     * 레시피 상세 조회
     * @param recipeId
     * @return
     */
    public RecipeDetailResponse getRecipeDetailById(Long recipeId) {
        return recipeRepository.findByIdWithDetails(recipeId)
                .map(this::convertToRecipeDetailResponse) // Recipe가 존재하면 DTO 변환
                .orElse(null); // Recipe가 존재하지 않으면 null 반환

    }

    private RecipeDetailResponse convertToRecipeDetailResponse(Recipe recipe) {
        List<ManualResponse> manualResponseList = recipe.getManualList().stream()
                .map(manual -> ManualResponse.builder()
                        .seq(manual.getSeq())
                        .content(manual.getContent())
                        .imgSrc(manual.getImageSrc())
                        .build())
                .toList();

        List<RecipeIngredientResponse> riResponseList = recipe.getRecipeIngredientList().stream()
                .map(ri -> RecipeIngredientResponse.builder()
                        .name(ri.getIngredient().getName())
                        .quantity(ri.getQuantity())
                        .build())
                .toList();

        return RecipeDetailResponse.builder()
                .name(recipe.getName())
                .imgSrc(recipe.getMainImgSrc())
                .tip(recipe.getTip())
                .manualResponseList(manualResponseList)
                .recipeIngredientResponseList(riResponseList)
                .build();
    }



}
