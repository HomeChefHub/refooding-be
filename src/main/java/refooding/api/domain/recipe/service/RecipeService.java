package refooding.api.domain.recipe.service;

import lombok.RequiredArgsConstructor;
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

    public List<RecipeResponse> getRecipes() {
        List<Recipe> allRecipes = recipeRepository.findAll();

        // DTO 변환
        return allRecipes.stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .imgSrc(recipe.getMainImgSrc())
                        .build())
                .toList();
    }

    public RecipeDetailResponse getRecipeDetailById(Long recipeId) {
        return recipeRepository.findByIdWithDetails(recipeId)
                .map(this::convertToRecipeDetailResponse) // Recipe가 존재하면 DTO 변환
                .orElse(null); // Recipe가 존재하지 않으면 null 반환

    }

    public List<RecipeResponse> getRecipesByIngredientName(String ingredientName) {
        List<Recipe> findRecipes = recipeRepository.findByMainIngredientName(ingredientName);

        // DTO 변환
        return findRecipes.stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .imgSrc(recipe.getMainImgSrc())
                        .build())
                .toList();
    }

    public List<RecipeResponse> getRecipesByIngredientNames(List<String> ingredientNames) {
        List<Recipe> findRecipes = recipeRepository.findByMainIngredientNames(ingredientNames);

        // DTO 변환
        return findRecipes.stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .imgSrc(recipe.getMainImgSrc())
                        .build())
                .toList();
    }
    public List<RecipeResponse> getRecipesByRecipeName(String recipeName) {
        List<Recipe> findRecipes = recipeRepository.findByNameContaining(recipeName);

        // DTO 변환
        return findRecipes.stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .imgSrc(recipe.getMainImgSrc())
                        .build())
                .toList();

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
