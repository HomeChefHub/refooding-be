package refooding.api.domain.recipe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refooding.api.domain.recipe.entity.Manual;
import refooding.api.domain.recipe.entity.Recipe;
import refooding.api.domain.recipe.entity.RecipeData;
import refooding.api.domain.recipe.repository.RecipeRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RecipeSetupService {

    private final RecipeRepository recipeRepository;

    @PostConstruct
    public void init() {
        importRecipes();
    }

    @Transactional
    public void importRecipes() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RecipeData recipeData = objectMapper.readValue(getClass().getClassLoader().getResourceAsStream("recipe-data.json"), RecipeData.class);

            List<Recipe> recipes = new ArrayList<>();

            int count = 0;
            for (RecipeData.Recipe recipeJson : recipeData.getRecipes()) {
                // Recipe 기본 정보 저장
                Recipe recipe = createRecipe(recipeJson);
                recipes.add(recipe);
                // Manual 엔티티 생성 및 저장
                List<Manual> manualList = createManuals(recipeJson);
                manualList.forEach(recipe::addManual);
                count++;
            }

            recipeRepository.saveAll(recipes);
            log.info("Recipes imported successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            log.info("Failed to import recipes={}", e.getMessage());
        }
    }

    private Recipe createRecipe(RecipeData.Recipe recipeJson) {
        return Recipe.builder()
                .name(recipeJson.getName())
                .tip(recipeJson.getTip())
                .mainIngredientName(recipeJson.getMainIngredientName())
                .thumbnail(recipeJson.getMainImage())
                .ingredients(recipeJson.getIngredientsDetails())
                .build();
    }

    private List<Manual> createManuals(RecipeData.Recipe recipeJson) {
        return recipeJson.getManuals().stream()
                .map(manualJson -> Manual.builder()
                        .seq(manualJson.getSeq())
                        .content(manualJson.getContent())
                        .imageUrl(manualJson.getImageUrl())
                        .build())
                .toList();
    }

}
