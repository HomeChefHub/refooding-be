package refooding.api.domain.ingredient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
import refooding.api.common.s3.S3Uploader;
import refooding.api.domain.ingredient.dto.request.IngredientCreateRequest;
import refooding.api.domain.ingredient.dto.request.IngredientUpdateRequest;
import refooding.api.domain.ingredient.dto.response.IngredientResponse;
import refooding.api.domain.ingredient.entity.Ingredient;
import refooding.api.domain.ingredient.entity.IngredientImage;
import refooding.api.domain.ingredient.entity.MemberIngredient;
import refooding.api.domain.ingredient.repository.IngredientImageRepository;
import refooding.api.domain.ingredient.repository.IngredientRepository;
import refooding.api.domain.ingredient.repository.MemberIngredientRepository;
import refooding.api.domain.ingredient.repository.MemberIngredientSearchCondition;
import refooding.api.domain.member.entity.Member;
import refooding.api.domain.member.repository.MemberRepository;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService{

    private final IngredientRepository ingredientRepository;
    private final IngredientImageRepository ingredientImageRepository;
    private final MemberIngredientRepository memberIngredientRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    @Override
    public Slice<IngredientResponse> getIngredients(Long memberId, String ingredientName, Long lastIngredientId, Integer daysUntilExpiration, Pageable pageable) {
        Member findMember = getMemberById(memberId);

        return memberIngredientRepository.findMemberIngredientByCondition(
                new MemberIngredientSearchCondition(
                        ingredientName,
                        findMember.getId(),
                        lastIngredientId,
                        daysUntilExpiration
                ),
                pageable
        );
    }

    @Transactional
    @Override
    public Long create(Long memberId, IngredientCreateRequest request) {
        Member findMember = getMemberById(memberId);

        // 기존 등록된 재료가 없다면 생성
        Ingredient findIngredient = getOrCreate(request.name());

        // S3 이미지 업로드
        List<IngredientImage> ingredientImages = uploadIngredientImages(request.image());

        // 냉장고 재료 등록
        MemberIngredient memberIngredient = request.toFridgeIngredient(findMember, findIngredient, request.expirationDate(), ingredientImages);
        MemberIngredient savedIngredient = memberIngredientRepository.save(memberIngredient);
        saveIngredientImages(savedIngredient, ingredientImages);

        return memberIngredient.getId();
    }


    @Transactional
    @Override
    public void update(Long memberId, Long memberIngredientId, IngredientUpdateRequest request) {
        Member findMember = getMemberById(memberId);
        MemberIngredient memberIngredient = getMemberIngredient(memberIngredientId);
        validateAuthor(findMember, memberIngredient);

        // 재료 업데이트
        Ingredient ingredient = getOrCreate(request.name()); // 기존 등록된 재료가 없다면 생성
        memberIngredient.updateFridgeIngredient(ingredient, request.expirationDate());
        updateIngredientImages(memberIngredient, request);
    }

    @Transactional
    @Override
    public void delete(Long memberId, Long memberIngredientId) {
        Member findMember = getMemberById(memberId);
        MemberIngredient memberIngredient = getMemberIngredient(memberIngredientId);
        validateAuthor(findMember, memberIngredient);

        deleteOldImages(memberIngredientId);
        memberIngredient.delete();
    }

    private Ingredient getOrCreate(String name) {
        return ingredientRepository.findIngredientByName(name)
                .orElseGet(() -> {
                    Ingredient ingredient = new Ingredient(name);
                    ingredientRepository.save(ingredient);
                    return ingredient;
                });
    }

    private List<IngredientImage> uploadIngredientImages(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return Collections.emptyList();
        }
        String imageUrl = s3Uploader.uploadIngredientImage(image);
        return Collections.singletonList(new IngredientImage(imageUrl));
    }

    private void updateIngredientImages(MemberIngredient memberIngredient, IngredientUpdateRequest request) {
        List<IngredientImage> newImages = uploadIngredientImages(request.image());
        boolean isNullThumbnail = request.thumbnailUrl() == null;

        // 기존 이미지가 없으면서, 새로운 이미지 없으면 모든 이미지 삭제
        if (isNullThumbnail && newImages.isEmpty()) {
            deleteOldImages(memberIngredient.getId());
            memberIngredient.updateImages(newImages);
            return;
        }

        // 새로운 이미지가 있으면 기존 이미지 삭제 후 새로운 이미지 저장
        if (!newImages.isEmpty()) {
            deleteOldImages(memberIngredient.getId());
            saveIngredientImages(memberIngredient, newImages);
            memberIngredient.updateImages(newImages);
        }
    }

    private void saveIngredientImages(MemberIngredient memberIngredient, List<IngredientImage> images) {
        images.forEach(image -> {
            image.setIngredient(memberIngredient);
            ingredientImageRepository.save(image);
        });
    }

    private void deleteOldImages(Long fridgeIngredientId) {
        List<IngredientImage> oldImages = getImageByIngredientId(fridgeIngredientId);
        oldImages.forEach(IngredientImage::delete);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER));
    }

    private List<IngredientImage> getImageByIngredientId(Long ingredientId) {
        return ingredientImageRepository.findAllByIngredientId(ingredientId);
    }

    private MemberIngredient getMemberIngredient(Long memberIngredientId) {
        return memberIngredientRepository.findMemberIngredientById(memberIngredientId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_INGREDIENT));
    }

    private void validateAuthor(Member member, MemberIngredient ingredient) {
        if (!ingredient.isAuthor(member)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED);
        }
    }

}
