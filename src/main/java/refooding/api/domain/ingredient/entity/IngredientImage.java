package refooding.api.domain.ingredient.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IngredientImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberIngredient_id")
    private MemberIngredient memberIngredient;

    private String url;

    public IngredientImage(MemberIngredient memberIngredient, String url) {
        this.memberIngredient = memberIngredient;
        this.url = url;
    }

    public IngredientImage(String url) {
        this(null, url);
    }

    public void setIngredient(MemberIngredient memberIngredient) {
        this.memberIngredient = memberIngredient;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
