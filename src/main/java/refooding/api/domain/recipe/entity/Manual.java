package refooding.api.domain.recipe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import refooding.api.common.domain.BaseTimeEntity;

@Entity
@Getter
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
}
