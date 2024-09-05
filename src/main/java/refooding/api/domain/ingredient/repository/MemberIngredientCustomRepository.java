package refooding.api.domain.ingredient.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import refooding.api.domain.ingredient.dto.response.IngredientResponse;

public interface MemberIngredientCustomRepository {
    Slice<IngredientResponse> findMemberIngredientByCondition(MemberIngredientSearchCondition condition, Pageable pageable);
}
