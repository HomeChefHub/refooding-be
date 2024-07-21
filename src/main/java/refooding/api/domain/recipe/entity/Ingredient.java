package refooding.api.domain.recipe.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true) // Ingredient 엔티티 삭제시 해당 엔티티의 PK를 FK로 가지고 있는 recipeIngredient도 삭제
    private List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

    @Builder
    public Ingredient(String name) {
        this.name = name;
    }


    // == 연관관계 편의 메소드 == //

    /**
     * Ingredient, RecipeIngredient 연관관계 설정 메소드
     * @param recipeIngredient
     */
    public void makeToRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredientList.add(recipeIngredient);
        recipeIngredient.changeIngredient(this);
    }

}
