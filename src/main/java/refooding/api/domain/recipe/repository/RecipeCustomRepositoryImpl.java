package refooding.api.domain.recipe.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import refooding.api.common.qeurydsl.QuerydslRepositoryUtils;
import refooding.api.domain.recipe.dto.response.QRecipeResponse;
import refooding.api.domain.recipe.dto.response.QRecommendRecipeResponse;
import refooding.api.domain.recipe.dto.response.RecipeResponse;
import refooding.api.domain.recipe.dto.response.RecommendRecipeResponse;

import java.time.LocalDateTime;
import java.util.List;

import static refooding.api.domain.ingredient.entity.QIngredient.ingredient;
import static refooding.api.domain.ingredient.entity.QMemberIngredient.memberIngredient;
import static refooding.api.domain.recipe.entity.QRecipe.recipe;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RecipeCustomRepositoryImpl implements RecipeCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<RecipeResponse> findRecipesByCondition(String searchKeyword, Long lastRecipeId, Pageable pageable) {
        JPAQuery<RecipeResponse> content = jpaQueryFactory
                .select(
                        new QRecipeResponse(
                                recipe.id,
                                recipe.name,
                                recipe.thumbnail
                        )
                )
                .from(recipe)
                .where(
                        ingredientNameContains(searchKeyword),
                        cursorCondition(lastRecipeId)
                )
                .orderBy(recipe.id.desc());
        return QuerydslRepositoryUtils.fetchSlice(content, pageable);
    }

    @Override
    public Slice<RecommendRecipeResponse> findRecipesSortedByEarliestExpiration(RecommendRecipeCondition condition, Pageable pageable) {
        JPAQuery<RecommendRecipeResponse> content = jpaQueryFactory
                .select(
                        new QRecommendRecipeResponse(
                                recipe.id,
                                recipe.name,
                                recipe.thumbnail,
                                memberIngredient.expirationDate.min()
                        )
                )
                .from(recipe)
                .leftJoin(memberIngredient)
                .on(memberIngredient.member.id.eq(condition.memberId()))
                .where(
                        recipe.ingredients.contains(ingredient.name),
                        memberIngredient.expirationDate.after(LocalDateTime.now()),
                        EarliestExpirationCursorCondition(condition.lastRecipeId(), condition.expirationDate()),
                        ingredientNameContains(condition.ingredientName()),
                        ingredientNotDeleted()
                )
                .groupBy(recipe)
                .orderBy(memberIngredient.expirationDate.min().asc(), recipe.id.asc());
        return QuerydslRepositoryUtils.fetchSlice(content, pageable);
    }

    @Override
    public Slice<RecipeResponse> findLikeRecipes(List<Long> recipeIds, Long lastLikeRecipeId, Pageable pageable) {
        JPAQuery<RecipeResponse> content = jpaQueryFactory
                .select(
                        new QRecipeResponse(
                                recipe.id,
                                recipe.name,
                                recipe.thumbnail
                        )
                )
                .from(recipe)
                .where(
                        recipe.id.in(recipeIds),
                        cursorCondition(lastLikeRecipeId)
                )
                .orderBy(recipe.id.desc());

        return QuerydslRepositoryUtils.fetchSlice(content, pageable);
    }

    private BooleanExpression ingredientNameContains(String ingredientName) {
        return StringUtils.isNullOrEmpty(ingredientName) ? null : recipe.ingredients.like("%" + ingredientName + "%");
    }

    private BooleanExpression cursorCondition(Long lastRecipeId){
        return lastRecipeId == null ? null : recipe.id.lt(lastRecipeId);
    }


    /**
     * 1. expirationDate 보다 크거나
     * 2. expirationDate가 같으면서 lastRecipeId 보다 큰 id 값을 조회한다
     */
    private BooleanExpression EarliestExpirationCursorCondition(Long lastRecipeId, LocalDateTime expirationDate) {
        if (lastRecipeId == null || expirationDate == null) {
            return null;
        }

        return memberIngredient.expirationDate.gt(expirationDate)
                .or(memberIngredient.expirationDate.eq(expirationDate)
                    .and(recipe.id.gt(lastRecipeId)));
    }

    private static BooleanExpression ingredientNotDeleted() {
        return memberIngredient.deletedAt.isNull();
    }
}
