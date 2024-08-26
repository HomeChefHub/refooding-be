package refooding.api.domain.fridge.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import refooding.api.common.qeurydsl.QuerydslRepositoryUtils;
import refooding.api.domain.fridge.dto.response.IngredientResponse;
import refooding.api.domain.fridge.dto.response.QIngredientResponse;
import refooding.api.domain.fridge.entity.QFridgeIngredient;

import static refooding.api.domain.fridge.entity.QFridgeIngredient.fridgeIngredient;
import static refooding.api.domain.fridge.entity.QIngredient.ingredient;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FridgeIngredientCustomRepositoryImpl implements FridgeIngredientCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<IngredientResponse> findFridgeIngredientByCondition(FridgeIngredientSearchCondition condition, Pageable pageable) {

        NumberTemplate<Integer> daysUntilExpiration = calculateDaysUntilExpiration();

        JPAQuery<IngredientResponse> content = jpaQueryFactory
                .select(
                        new QIngredientResponse(
                                fridgeIngredient.id,
                                ingredient.name,
                                daysUntilExpiration,
                                fridgeIngredient.storageStartDate,
                                fridgeIngredient.expirationDate,
                                fridgeIngredient.thumbnailUrl
                        )
                )
                .from(fridgeIngredient)
                .leftJoin(fridgeIngredient.ingredient, ingredient)
                .where(
                        fridgeIdEq(condition.fridgeId()),
                        ingredientNameContains(condition.ingredientName()),
                        cursorCondition(daysUntilExpiration, condition.daysUntilExpiration(), condition.lastIngredientId()),
                        notDeleted()

                )
                .orderBy(daysUntilExpiration.asc(),
                        fridgeIngredient.createdDate.desc(),
                        fridgeIngredient.id.asc());

        return QuerydslRepositoryUtils.fetchSlice(content, pageable);

    }

    private BooleanExpression fridgeIdEq(Long fridgeId) {
        return fridgeId != null ? QFridgeIngredient.fridgeIngredient.fridge.id.eq(fridgeId) : null;
    }

    private BooleanExpression ingredientNameContains(String ingredientName) {
        return StringUtils.hasText(ingredientName) ? ingredient.name.contains(ingredientName) : null;
    }

    /**
     * 1. 재료 만료일수가 lastDaysUntilExpiration 보다 큰 재료이거나
     * 2. 재료 만료일수가 lastDaysUntilExpiration와 같으면서 lastIngredientId 보다 작은 id 값을 조회한다
     */
    private BooleanExpression cursorCondition(NumberTemplate<Integer> daysUntilExpiration,
                                              Integer lastDaysUntilExpiration,
                                              Long lastIngredientId) {
        if (lastDaysUntilExpiration == null || lastIngredientId == null) {
            return null;
        }
        return daysUntilExpirationGt(daysUntilExpiration, lastDaysUntilExpiration)
                .or(daysUntilExpirationEq(daysUntilExpiration, lastDaysUntilExpiration)
                        .and(idLt(lastIngredientId)));
    }

    public static BooleanExpression daysUntilExpirationGt(NumberTemplate<Integer> daysUntilExpiration, Integer lastExpirationCountdown) {
        return lastExpirationCountdown != null ? daysUntilExpiration.gt(lastExpirationCountdown) : null;
    }

    public static BooleanExpression daysUntilExpirationEq(NumberTemplate<Integer> daysUntilExpiration, Integer lastExpirationCountdown) {
        return lastExpirationCountdown != null ? daysUntilExpiration.eq(lastExpirationCountdown) : null;
    }

    public static BooleanExpression idLt(Long lastIngredientId) {
        return lastIngredientId != null ? fridgeIngredient.id.lt(lastIngredientId) : null;
    }

    private static BooleanExpression notDeleted() {
        return fridgeIngredient.deletedDate.isNull();
    }

    private NumberTemplate<Integer> calculateDaysUntilExpiration() {
        return Expressions.numberTemplate(Integer.class,
                "TIMESTAMPDIFF(DAY, {0}, {1})",
                fridgeIngredient.storageStartDate,
                fridgeIngredient.expirationDate
        );
    }

}
