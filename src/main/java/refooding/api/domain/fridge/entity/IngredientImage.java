package refooding.api.domain.fridge.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IngredientImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FridgeIngredient_id")
    private FridgeIngredient fridgeIngredient;

    private String url;

    public IngredientImage(FridgeIngredient fridgeIngredient, String url) {
        this.fridgeIngredient = fridgeIngredient;
        this.url = url;
    }

    public IngredientImage(String url) {
        this(null, url);
    }

    public void setIngredient(FridgeIngredient fridgeIngredient) {
        this.fridgeIngredient = fridgeIngredient;
    }
}
