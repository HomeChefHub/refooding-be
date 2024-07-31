package refooding.api.domain.recipe.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import refooding.api.domain.recipe.entity.FavoriteRecipe;

import java.util.List;
import java.util.Optional;

public interface FavoriteRecipeRepository extends JpaRepository<FavoriteRecipe, Long> {
    Optional<FavoriteRecipe> findByMemberIdAndRecipeId(Long memberId, Long recipeId);

    @Query("SELECT fr.recipe.id FROM FavoriteRecipe fr WHERE fr.member.id = :memberId AND fr.deletedDate IS NULL")
    List<Long> findRecipeIdsByMemberId(@Param("memberId") Long memberId);
}
