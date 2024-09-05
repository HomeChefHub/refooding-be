package refooding.api.domain.ingredient.repository;

public record MemberIngredientSearchCondition(
        String ingredientName,
        Long memberId,
        Long lastIngredientId,
        Integer daysUntilExpiration
) {
}
