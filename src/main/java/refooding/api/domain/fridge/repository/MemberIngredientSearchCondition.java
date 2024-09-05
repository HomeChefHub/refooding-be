package refooding.api.domain.fridge.repository;

public record MemberIngredientSearchCondition(
        String ingredientName,
        Long memberId,
        Long lastIngredientId,
        Integer daysUntilExpiration
) {
}
