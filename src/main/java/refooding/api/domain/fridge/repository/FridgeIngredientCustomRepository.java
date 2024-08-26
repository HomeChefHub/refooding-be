package refooding.api.domain.fridge.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import refooding.api.domain.fridge.dto.response.IngredientResponse;

public interface FridgeIngredientCustomRepository {
    Slice<IngredientResponse> findFridgeIngredientByCondition(FridgeIngredientSearchCondition condition, Pageable pageable);
}
