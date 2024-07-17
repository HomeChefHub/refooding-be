package refooding.api.domain.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import refooding.api.common.domain.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Ingredient extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL) // Ingredient 엔티티 삭제시 해당 엔티티의 PK를 FK로 가지고 있는 recipeIngredient도 삭제
    private List<RecipeIngredient> recipIngredientList = new ArrayList<>();

}
