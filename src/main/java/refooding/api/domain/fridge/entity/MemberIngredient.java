package refooding.api.domain.fridge.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;
import refooding.api.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberIngredient extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private String thumbnailUrl;

    @Column(nullable = false)
    private LocalDateTime storageStartDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "memberIngredient")
    private List<IngredientImage> images = new ArrayList<>();

    public MemberIngredient(Member member, Ingredient ingredient, LocalDateTime expirationDate, List<IngredientImage> images) {
        this.member = member;
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
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isAuthor(Member member) {
        return this.member.equals(member);
    }

    private static String getThumbnailUrl(List<IngredientImage> images) {
        return images.isEmpty() ? null : images.get(0).getUrl();
    }

}
