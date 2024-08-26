package refooding.api.domain.fridge.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import refooding.api.common.domain.BaseTimeEntity;
import refooding.api.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fridge extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fridge")
    private List<FridgeIngredient> ingredients = new ArrayList<>();

    public Fridge(Member member) {
        this.member = member;
    }

    public boolean validateMember(Long memberId) {
        return getMember().getId().equals(memberId);
    }
}
