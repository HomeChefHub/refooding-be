package refooding.api.domain.recipe.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import refooding.api.common.qeurydsl.QuerydslRepositoryUtils;
import refooding.api.domain.recipe.dto.response.QRecipeResponse;
import refooding.api.domain.recipe.dto.response.RecipeResponse;

import java.util.List;

import static refooding.api.domain.recipe.entity.QRecipe.recipe;

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
                        // recipeNameEq(condition.recipeName()),
                        cursorCondition(lastRecipeId)
                )
                .orderBy(recipe.id.desc());
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
        return StringUtils.isNullOrEmpty(ingredientName) ? null : recipe.ingredients.contains(ingredientName);
    }

    // private BooleanExpression recipeNameEq(String recipeName) {
    //     return StringUtils.isNullOrEmpty(recipeName) ? null : recipe.name.eq(recipeName);
    // }

    private BooleanExpression cursorCondition(Long lastExchangeId){
        return lastExchangeId == null ? null : recipe.id.lt(lastExchangeId);
    }
}
