package refooding.api.domain.fridge.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import refooding.api.common.aws.S3Uploader;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
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

import java.util.ArrayList;
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

        // S3에 파일 업로드
        boolean isFileEmpty = isFileListEmpty(request.ingredientImages());
        List<MultipartFile> imageFiles = request.ingredientImages();
        List<IngredientImage> images = new ArrayList<>();

        if (!isFileEmpty) {
            List<String> ingredientImageUrls = s3Uploader.uploadFridgeIngredientImg(imageFiles);
            images = ingredientImageUrls.stream()
                    .map(IngredientImage::new)
                    .toList();
        }

        // 냉장고 재료 등록
        FridgeIngredient fridgeIngredient = new FridgeIngredient(findFridge, findIngredient, request.expirationDate(), images);
        fridgeIngredientRepository.save(fridgeIngredient);

        if (!isFileEmpty) {
            images.forEach(image -> image.setIngredient(fridgeIngredient));
            ingredientImageRepository.saveAll(images);
        }

        return fridgeIngredient.getId();
    }


    @Transactional
    @Override
    public void update(Long memberId, Long fridgeIngredientId, IngredientUpdateRequest request) {
        Member findMember = getMemberById(memberId);

        // TODO : 로그 작성
        Fridge findFridge = fridgeRepository.findFridgeWithMemberByMemberId(findMember.getId())
                .orElseThrow(() -> new RuntimeException("냉장고가 존재하지 않습니다"));
        if (!findFridge.validateMember(findMember.getId())) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED);
        }

        FridgeIngredient fridgeIngredient = fridgeIngredientRepository.findFridgeIngredientById(fridgeIngredientId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_INGREDIENT));

        Ingredient ingredient = getOrCreate(request.name());
        fridgeIngredient.updateFridgeIngredient(ingredient, request.expirationDate());
    }

    @Transactional
    @Override
    public void delete(Long memberId, Long fridgeIngredientId) {
        Member findMember = getMemberById(memberId);

        // TODO : 로그 작성
        Fridge findFridge = fridgeRepository.findFridgeWithMemberByMemberId(findMember.getId())
                .orElseThrow(() -> new RuntimeException("냉장고가 존재하지 않습니다"));
        if (!findFridge.validateMember(findMember.getId())) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED);
        }

        FridgeIngredient fridgeIngredient = fridgeIngredientRepository.findFridgeIngredientById(fridgeIngredientId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_INGREDIENT));

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

    private boolean isFileListEmpty(List<MultipartFile> imageFiles) {
        return imageFiles == null || imageFiles.isEmpty();
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findByIdAndDeletedDateIsNull(memberId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER));
    }

}
