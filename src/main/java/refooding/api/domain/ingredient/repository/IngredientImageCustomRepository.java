package refooding.api.domain.ingredient.repository;

import refooding.api.domain.ingredient.entity.IngredientImage;

import java.util.List;

public interface IngredientImageCustomRepository {
    void saveAll(List<IngredientImage> images);
}
