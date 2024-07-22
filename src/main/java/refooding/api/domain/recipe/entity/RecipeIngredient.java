package refooding.api.domain.recipe.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeIngredient extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private String quantity;

    private boolean isMainIngredient;

    @Builder
    public RecipeIngredient(Ingredient ingredient, String quantity, boolean isMainIngredient) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.isMainIngredient = isMainIngredient;
    }


    // == 연관관계 편의 메소드 == //
    public void changeRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void changeIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
