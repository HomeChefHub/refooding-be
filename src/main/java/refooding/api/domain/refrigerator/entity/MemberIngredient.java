package refooding.api.domain.refrigerator.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;
import refooding.api.domain.member.entity.Member;
import refooding.api.domain.recipe.entity.Ingredient;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Builder
    public MemberIngredient(Member member, Ingredient ingredient, LocalDateTime startDate, LocalDateTime endDate) {
        this.member = member;
        this.ingredient = ingredient;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void delete() {
        this.deletedDate = LocalDateTime.now();
    }

    // == 연관관계 편의 메소드 == //
    public void changeMember(Member member) {
        this.member = member;
    }

    public void changeIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    // 유통기한 변경 메소드
    public void changeStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void changeEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
