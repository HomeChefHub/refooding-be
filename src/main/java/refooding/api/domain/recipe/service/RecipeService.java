package refooding.api.domain.recipe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
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
import refooding.api.domain.recipe.entity.*;
import refooding.api.domain.recipe.repository.IngredientRepository;
import refooding.api.domain.recipe.repository.RecipeRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngredientRepository ingredientRepository;

    @PostConstruct
    public void init() {
        importRecipes();
    }

    @Transactional
    public void importRecipes() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RecipeData recipeData = objectMapper.readValue(getClass().getClassLoader().getResourceAsStream("recipe-data.json"), RecipeData.class);
            for (RecipeData.Recipe recipe : recipeData.getRecipes()) {
                saveRecipe(recipe);
            }
            System.out.println("Recipes imported successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to import recipes: " + e.getMessage());

        }
    }


    @Transactional
    public void saveRecipe(RecipeData.Recipe recipeJson) {
        // Recipe 기본 정보 저장
        Recipe recipe = Recipe.builder()
                .name(recipeJson.getName())
                .tip(recipeJson.getTip())
                .mainIngredientName(recipeJson.getHashTag())
                .mainImgSrc(recipeJson.getMainImage())
                .build();


        // Manual 엔티티 생성 및 저장
        List<RecipeData.Manual> manualList = recipeJson.getManuals();
        for (int i = 0; i < manualList.size(); i ++) {
            RecipeData.Manual manualJson = manualList.get(i);
            Manual manual = Manual.builder()
                    .seq(i + 1)
                    .content(manualJson.getContent())
                    .imageSrc(manualJson.getImageSrc())
                    .build();

            // Manual 엔티티 저장
            recipe.addManual(manual);
        }

        // Ingredient, RecipeIngredient 엔티티 생성 및 저장
        String[] ingredientDetails = recipeJson.getIngredientsDetails().split(",");
        Pattern pattern = Pattern.compile("^(.*?)(\\d+\\.?\\d*\\s?[a-zA-Z가-힣]+|[가-힣]+)$");
        for (String ingredientDetail : ingredientDetails) {
            Matcher matcher = pattern.matcher(ingredientDetail.trim());
            if (matcher.find()) {
                String ingredientName = matcher.group(1).trim();
                String quantity = matcher.group(2).trim();

                Optional<Ingredient> optionalIngredient = ingredientRepository.findByName(ingredientName);
                Ingredient ingredient;
                if (optionalIngredient.isPresent()) {
                    ingredient = optionalIngredient.get();
                } else {
                    ingredient = Ingredient.builder()
                            .name(ingredientName).build();

                    // Ingredient 엔티티 저장
                    ingredientRepository.save(ingredient);
                }

                RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                        .ingredient(ingredient)
                        .quantity(quantity)
                        .build();

                // RecipeIngredient 엔티티 저장
                recipe.addRecipeIngredient(recipeIngredient);
            }
        }


        // Recipe 엔티티 저장
        recipeRepository.save(recipe);

    }


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
