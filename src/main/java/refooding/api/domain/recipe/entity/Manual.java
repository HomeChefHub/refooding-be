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
public class Manual extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private int seq;

    private String content;

    private String imageSrc;

    @Builder
    public Manual(int seq, String content, String imageSrc) {
        this.seq = seq;
        this.content = content;
        this.imageSrc = imageSrc;
    }

    // == 연관관계 편의 메소드 설정 == //

    public void changeRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
