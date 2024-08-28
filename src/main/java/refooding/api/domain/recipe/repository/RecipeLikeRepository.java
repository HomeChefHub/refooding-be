package refooding.api.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.recipe.entity.RecipeLike;

import java.util.List;
import java.util.Optional;

public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long> {
    @Query("select rl from RecipeLike rl " +
            "where rl.recipe.id = :recipeId and rl.member.id = :memberId")
    Optional<RecipeLike> findByMemberIdAndRecipeId(Long memberId, Long recipeId);

    @Query("select rl.recipe.id from RecipeLike rl " +
            "where rl.member.id = :memberId and rl.deletedAt is null " +
            "order by rl.createdAt desc")
    List<Long> findRecipeIdsByMemberId(Long memberId);
}
