package refooding.api.domain.fridge.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FridgeIngredient extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private String thumbnailUrl;

    @Column(nullable = false)
    private LocalDateTime storageStartDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fridgeIngredient")
    private List<IngredientImage> images = new ArrayList<>();

    public FridgeIngredient(Fridge fridge, Ingredient ingredient, LocalDateTime expirationDate, List<IngredientImage> images) {
        this.fridge = fridge;
        this.ingredient = ingredient;
        this.storageStartDate = LocalDateTime.now();
        this.expirationDate = expirationDate;
        this.images.addAll(images);
        this.thumbnailUrl = !images.isEmpty() ? images.get(0).getUrl() : null;
    }

    // TODO : 이미지 추가 필요
    public void updateFridgeIngredient(Ingredient ingredient, LocalDateTime expirationDate) {
        this.ingredient = ingredient;
        this.expirationDate = expirationDate;
    }

    public void updateImages(List<IngredientImage> newImages) {
        this.images.addAll(newImages);
        this.thumbnailUrl = getThumbnailUrl(newImages);
        newImages.forEach(image -> image.setIngredient(this));
    }

    public void delete() {
        this.deletedDate = LocalDateTime.now();
    }

    private static String getThumbnailUrl(List<IngredientImage> images) {
        return images.isEmpty() ? null : images.get(0).getUrl();
    }

}
