package refooding.api.domain.ingredient.repository;

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
import refooding.api.domain.ingredient.dto.response.IngredientResponse;
import refooding.api.domain.ingredient.dto.response.QIngredientResponse;

import static refooding.api.domain.ingredient.entity.QIngredient.ingredient;
import static refooding.api.domain.ingredient.entity.QMemberIngredient.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberIngredientCustomRepositoryImpl implements MemberIngredientCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<IngredientResponse> findMemberIngredientByCondition(MemberIngredientSearchCondition condition, Pageable pageable) {

        NumberTemplate<Integer> daysUntilExpiration = calculateDaysUntilExpiration();


        JPAQuery<IngredientResponse> content = jpaQueryFactory
                .select(
                        new QIngredientResponse(
                                memberIngredient.id,
                                ingredient.name,
                                daysUntilExpiration,
                                memberIngredient.storageStartDate,
                                memberIngredient.expirationDate,
                                memberIngredient.thumbnailUrl
                        )
                )
                .from(memberIngredient)
                .leftJoin(memberIngredient.ingredient, ingredient)
                .where(
                        memberIdEq(condition.memberId()),
                        ingredientNameContains(condition.ingredientName()),
                        cursorCondition(daysUntilExpiration, condition.daysUntilExpiration(), condition.lastIngredientId()),
                        notDeleted()

                )
                .orderBy(daysUntilExpiration.asc(),
                        memberIngredient.createdAt.desc(),
                        memberIngredient.id.asc());

        return QuerydslRepositoryUtils.fetchSlice(content, pageable);

    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? memberIngredient.member.id.eq(memberId) : null;
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
        return lastIngredientId != null ? memberIngredient.id.lt(lastIngredientId) : null;
    }

    private static BooleanExpression notDeleted() {
        return memberIngredient.deletedAt.isNull();
    }

    private NumberTemplate<Integer> calculateDaysUntilExpiration() {
        return Expressions.numberTemplate(Integer.class,
                "TIMESTAMPDIFF(DAY, {0}, {1})",
                Expressions.currentDate(),
                memberIngredient.expirationDate
        );
    }

}
