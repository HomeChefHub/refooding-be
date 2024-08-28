package refooding.api.domain.fridge.service;

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
import refooding.api.domain.fridge.dto.request.IngredientCreateRequest;
import refooding.api.domain.fridge.dto.request.IngredientUpdateRequest;
import refooding.api.domain.fridge.dto.response.IngredientResponse;
import refooding.api.domain.fridge.entity.Fridge;
import refooding.api.domain.fridge.entity.FridgeIngredient;
import refooding.api.domain.fridge.entity.Ingredient;
import refooding.api.domain.fridge.entity.IngredientImage;
import refooding.api.domain.fridge.repository.*;
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
    private final FridgeRepository fridgeRepository;
    private final FridgeIngredientRepository fridgeIngredientRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    @Override
    public Slice<IngredientResponse> getIngredients(Long memberId, String ingredientName, Long lastIngredientId, Integer daysUntilExpiration, Pageable pageable) {
        Member findMember = getMemberById(memberId);

        // 기존 생성된 냉장고가 없다면 생성
        Fridge findFridge = fridgeRepository.findFridgeByMemberId(memberId)
                .orElseGet(()->{
                    Fridge fridge = new Fridge(findMember);
                    fridgeRepository.save(fridge);
                    return fridge;
                });

        return fridgeIngredientRepository.findFridgeIngredientByCondition(
                new FridgeIngredientSearchCondition(
                        ingredientName,
                        findFridge.getId(),
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
        // TODO : 로그 작성
        Fridge findFridge = fridgeRepository.findFridgeByMemberId(findMember.getId())
                .orElseThrow(() -> new RuntimeException("냉장고를 찾을 수 없습니다"));

        // 기존 등록된 재료가 없다면 생성
        Ingredient findIngredient = getOrCreate(request.name());

        // S3 이미지 업로드
        List<IngredientImage> ingredientImages = uploadIngredientImages(request.image());

        // 냉장고 재료 등록
        FridgeIngredient fridgeIngredient = request.toFridgeIngredient(findFridge, findIngredient, request.expirationDate(), ingredientImages);
        FridgeIngredient savedIngredient = fridgeIngredientRepository.save(fridgeIngredient);
        saveIngredientImages(savedIngredient, ingredientImages);

        return fridgeIngredient.getId();
    }


    @Transactional
    @Override
    public void update(Long memberId, Long fridgeIngredientId, IngredientUpdateRequest request) {
        Member findMember = getMemberById(memberId);
        // TODO : 로그 작성
        Fridge findFridge = getFridgeWithMemberByMemberId(findMember.getId());
        validateAuthor(findMember, findFridge);

        // 재료 업데이트
        FridgeIngredient fridgeIngredient = getFridgeIngredient(fridgeIngredientId);
        Ingredient ingredient = getOrCreate(request.name()); // 기존 등록된 재료가 없다면 생성
        fridgeIngredient.updateFridgeIngredient(ingredient, request.expirationDate());
        updateIngredientImages(fridgeIngredient, request);
    }

    @Transactional
    @Override
    public void delete(Long memberId, Long fridgeIngredientId) {
        Member findMember = getMemberById(memberId);
        // TODO : 로그 작성
        Fridge findFridge = getFridgeWithMemberByMemberId(findMember.getId());
        validateAuthor(findMember, findFridge);
        FridgeIngredient fridgeIngredient = getFridgeIngredient(fridgeIngredientId);

        deleteOldImages(fridgeIngredientId);
        fridgeIngredient.delete();
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

    private void updateIngredientImages(FridgeIngredient fridgeIngredient, IngredientUpdateRequest request) {
        List<IngredientImage> newImages = uploadIngredientImages(request.image());
        boolean isNullThumbnail = request.thumbnailUrl() == null;

        // 기존 이미지가 없으면서, 새로운 이미지 없으면 모든 이미지 삭제
        if (isNullThumbnail && newImages.isEmpty()) {
            deleteOldImages(fridgeIngredient.getId());
            fridgeIngredient.updateImages(newImages);
            return;
        }

        // 새로운 이미지가 있으면 기존 이미지 삭제 후 새로운 이미지 저장
        if (!newImages.isEmpty()) {
            deleteOldImages(fridgeIngredient.getId());
            saveIngredientImages(fridgeIngredient, newImages);
            fridgeIngredient.updateImages(newImages);
        }
    }

    private void saveIngredientImages(FridgeIngredient fridgeIngredient, List<IngredientImage> images) {
        images.forEach(image -> {
            image.setIngredient(fridgeIngredient);
            ingredientImageRepository.save(image);
        });
    }

    private void deleteOldImages(Long fridgeIngredientId) {
        List<IngredientImage> oldImages = getImageByIngredientId(fridgeIngredientId);
        oldImages.forEach(IngredientImage::delete);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findByIdAndDeletedDateIsNull(memberId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER));
    }

    private List<IngredientImage> getImageByIngredientId(Long ingredientId) {
        return ingredientImageRepository.findAllByIngredientId(ingredientId);
    }

    private Fridge getFridgeWithMemberByMemberId(Long memberId) {
        return fridgeRepository.findFridgeWithMemberByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("냉장고가 존재하지 않습니다"));
    }

    private FridgeIngredient getFridgeIngredient(Long fridgeIngredientId) {
        return fridgeIngredientRepository.findFridgeIngredientById(fridgeIngredientId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_INGREDIENT));
    }

    private void validateAuthor(Member member, Fridge fridge) {
        if (!fridge.isAuthor(member)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED);
        }
    }

}
