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
import refooding.api.domain.refrigerator.dto.MemberIngredientCreateRequest;
import refooding.api.domain.refrigerator.dto.MemberIngredientDeleteRequest;
import refooding.api.domain.refrigerator.dto.MemberIngredientUpdateRequest;
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
    public Long saveMemberIngredient(MemberIngredientCreateRequest memberIngredientRequest) {
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

    @Transactional
    public void updateMemberIngredient(Long memberIngredientId, MemberIngredientUpdateRequest request) {
        MemberIngredient memberIngredient = memberIngredientRepository.findById(memberIngredientId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER_INGREDIENT));

        // 업데이트 요청 memberId와 MemberIngredient의 memberId가 일치하는지 검증
        if (!memberIngredient.getMember().getId().equals(request.getRequestMemberId())) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED);
        }

        // 재료 조회 or 생성 및 업데이트
        Ingredient ingredient = ingredientRepository.findByName(request.getName())
                .orElseGet(() -> createAndSaveIngredient(request.getName()));

        // 멤버 재료 정보 업데이트
        memberIngredient.changeIngredient(ingredient);
        memberIngredient.changeStartDate(request.getStartDate());
        memberIngredient.changeEndDate(request.getEndDate());

    }

    @Transactional
    public void deleteMemberIngredient(Long memberIngredientId, MemberIngredientDeleteRequest request) {
        MemberIngredient memberIngredient = memberIngredientRepository.findById(memberIngredientId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER_INGREDIENT));

        // 업데이트 요청 memberId와 MemberIngredient의 memberId가 일치하는지 검증
        if (!memberIngredient.getMember().getId().equals(request.getRequestMemberId())) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED);
        }
        // 논리 삭제 실행
        memberIngredient.delete();
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
