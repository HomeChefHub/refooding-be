package refooding.api.domain.fridge.repository;

public record FridgeIngredientSearchCondition(
        String ingredientName,
        Long fridgeId,
        Long lastIngredientId,
        Integer daysUntilExpiration
) {
}
