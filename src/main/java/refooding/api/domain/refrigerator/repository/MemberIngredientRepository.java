package refooding.api.domain.refrigerator.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.recipe.entity.Ingredient;
import refooding.api.domain.refrigerator.entity.MemberIngredient;

import java.util.List;
import java.util.Optional;

public interface MemberIngredientRepository extends JpaRepository<MemberIngredient, Long> {

    @Query("select mi.ingredient from MemberIngredient mi where mi.member.id = :memberId and mi.deletedDate is null")
    List<Ingredient> findIngredientsByMemberId(Long memberId);

    @Query("select mi from MemberIngredient mi join fetch mi.ingredient where mi.id = :id")
    Optional<MemberIngredient> findByIdWithIngredient(Long id);
}
