package refooding.api.domain.recipe.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String tip;

    private String mainIngredientName;

    private String mainImgSrc;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true) // Recipe 엔티티 삭제시 Manual도 삭제
    @OrderBy("seq ASC") // seq를 기준으로 오름차순 정렬
    private Set<Manual> manualList = new HashSet<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true) // Recipe 엔티티 삭제시 해당 엔티티의 PK를 FK로 가지고 있는 recipeIngredient도 삭제
    private Set<RecipeIngredient> recipeIngredientList = new HashSet<>();

    @Builder
    public Recipe(String name, String tip, String mainIngredientName, String mainImgSrc) {
        this.name = name;
        this.tip = tip;
        this.mainIngredientName = mainIngredientName;
        this.mainImgSrc = mainImgSrc;
    }

    // == 연관관계 편의 메소드 == //

    /**
     * Recipe, Manual 연관관계 설정 메소드
     * @param manual
     */
    public void addManual(Manual manual) {
        manualList.add(manual);
        manual.changeRecipe(this);
    }

    /**
     * Recipe, ReciepeIngredient 연관관계 설정 메소드
     * @param recipeIngredient
     */
    public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredientList.add(recipeIngredient);
        recipeIngredient.changeRecipe(this);
    }
}
