package refooding.api.domain.ingredient.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import refooding.api.domain.ingredient.dto.response.IngredientResponse;
import refooding.api.domain.ingredient.entity.MemberIngredient;

import java.util.List;

public interface MemberIngredientCustomRepository {

    /**
     * 유통기한 짧은 순 정렬하여 조회
     */
    Slice<IngredientResponse> findMemberIngredientByCondition(MemberIngredientSearchCondition condition, Pageable pageable);

}
