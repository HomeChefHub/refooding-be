package refooding.api.domain.ingredient.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import refooding.api.domain.ingredient.dto.request.IngredientCreateRequest;
import refooding.api.domain.ingredient.dto.request.IngredientUpdateRequest;
import refooding.api.domain.ingredient.dto.response.IngredientResponse;

public interface IngredientService {
    Slice<IngredientResponse> getIngredients(Long memberId, String ingredientName, Long lastIngredientId, Integer daysUntilExpiration, Pageable pageable);
    Long create(Long memberId, IngredientCreateRequest request);
    void update(Long memberId, Long fridgeIngredientId, IngredientUpdateRequest request);
    void delete(Long memberID, Long fridgeIngredientId);

}
