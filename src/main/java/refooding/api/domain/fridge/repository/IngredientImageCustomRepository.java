package refooding.api.domain.fridge.repository;

import refooding.api.domain.fridge.entity.IngredientImage;

import java.util.List;

public interface IngredientImageCustomRepository {
    void saveAll(List<IngredientImage> images);
}
