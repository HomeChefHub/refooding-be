package refooding.api.domain.refrigerator.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.recipe.entity.Ingredient;
import refooding.api.domain.refrigerator.entity.MemberIngredient;

import java.util.List;

public interface MemberIngredientRepository extends JpaRepository<MemberIngredient, Long> {

    @Query("select mi.ingredient from MemberIngredient mi where mi.member.id = :memberId and mi.deletedDate is null")
    List<Ingredient> findIngredientsByMemberId(Long memberId);
}
