package refooding.api.domain.recipe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import refooding.api.common.domain.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Recipe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String tip;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL) // Recipe 엔티티 삭제시 Manual도 삭제
    private List<Manual> manualList = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL) // Recipe 엔티티 삭제시 해당 엔티티의 PK를 FK로 가지고 있는 recipeIngredient도 삭제
    private List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
}
