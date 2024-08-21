package refooding.api.domain.recipe.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String tip;

    private String mainIngredientName;

    private String thumbnail;

    @Column(length = 500)
    private String ingredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true) // Recipe 엔티티 삭제시 Manual도 삭제
    private Set<Manual> manuals = new HashSet<>();

    @Builder
    public Recipe(String name, String tip, String mainIngredientName, String thumbnail, String ingredients) {
        this.name = name;
        this.tip = tip;
        this.mainIngredientName = mainIngredientName;
        this.thumbnail = thumbnail;
        this.ingredients = ingredients;
    }

    // == 연관관계 편의 메소드 == //
    /**
     * Recipe, Manual 연관관계 설정 메소드
     * @param manual
     */
    public void addManual(Manual manual) {
        manuals.add(manual);
        manual.changeRecipe(this);
    }

}
