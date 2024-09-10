package refooding.api.domain.ingredient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.ingredient.entity.MemberIngredient;

import java.util.Optional;

public interface MemberIngredientRepository extends JpaRepository<MemberIngredient, Long>, MemberIngredientCustomRepository {
    @Query("select fi from MemberIngredient fi " +
            "where fi.id = :id " +
            "and fi.deletedAt is null")
    Optional<MemberIngredient> findMemberIngredientById(Long id);
}
