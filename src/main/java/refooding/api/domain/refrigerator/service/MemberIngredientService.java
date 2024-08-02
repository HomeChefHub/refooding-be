package refooding.api.domain.refrigerator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
import refooding.api.domain.member.entity.Member;
import refooding.api.domain.member.repository.MemberRepository;
import refooding.api.domain.recipe.entity.Ingredient;
import refooding.api.domain.recipe.repository.IngredientRepository;
import refooding.api.domain.refrigerator.dto.MemberIngredientRequest;
import refooding.api.domain.refrigerator.entity.MemberIngredient;
import refooding.api.domain.refrigerator.repository.MemberIngredientRepository;

import java.awt.print.Pageable;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberIngredientService {

    private final IngredientRepository ingredientRepository;

    private final MemberIngredientRepository memberIngredientRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public Long saveMemberIngredient(MemberIngredientRequest memberIngredientRequest) {
        // 멤버 조회
        Member findMember = getMemberById(memberIngredientRequest.getMemberId());

        // 재료 조회 or 생성
        // 재료가 이미 ingredient 테이블에 있다면 조회해서 리턴
        // 재료가 ingredient 테이블에 없다면 새로 생성 + ingredient 테이블에 저장후 리턴
        Ingredient ingredient = ingredientRepository.findByName(memberIngredientRequest.getName())
                .orElseGet(() -> createAndSaveIngredient(memberIngredientRequest.getName()));

        // 멤버 재료 생성
        MemberIngredient memberIngredient = MemberIngredient.builder()
                .member(findMember)
                .ingredient(ingredient)
                .startDate(memberIngredientRequest.getStartDate())
                .endDate(memberIngredientRequest.getEndDate())
                .build();

        // 연관관계 설정
        memberIngredient.changeMember(findMember);
        memberIngredient.changeIngredient(ingredient);

        // 멤버 재료 저장
        memberIngredientRepository.save(memberIngredient);

        return memberIngredient.getId();

    }

    public List<Ingredient> getIngredientsByMemberId(Long memberId, Pageable pageable) {
        return memberIngredientRepository.findIngredientsByMemberId(memberId);
    }




    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER));
    }

    private Ingredient createAndSaveIngredient(String name) {
        Ingredient newIngredient = Ingredient.builder().name(name).build();
        ingredientRepository.save(newIngredient);
        return newIngredient;
    }

}
