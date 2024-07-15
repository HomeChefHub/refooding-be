package refooding.api.domain.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import refooding.api.domain.common.BaseTimeEntity;

@Entity
@Getter
public class RecipeIngredient extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_ingredient_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private String quantity;

    private boolean isMainIngredient;
}
