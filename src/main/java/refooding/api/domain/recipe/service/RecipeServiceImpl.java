package refooding.api.domain.recipe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
import refooding.api.domain.member.entity.Member;
import refooding.api.domain.member.repository.MemberRepository;
import refooding.api.domain.recipe.dto.response.RecipeDetailResponse;
import refooding.api.domain.recipe.dto.response.RecipeLikeResponse;
import refooding.api.domain.recipe.dto.response.RecipeResponse;
import refooding.api.domain.recipe.entity.Recipe;
import refooding.api.domain.recipe.entity.RecipeLike;
import refooding.api.domain.recipe.repository.RecipeLikeRepository;
import refooding.api.domain.recipe.repository.RecipeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;

    private final RecipeLikeRepository recipeLikeRepository;

    private final MemberRepository memberRepository;

    /**
     * 레시피 목록 조회
     */
    public Slice<RecipeResponse> getRecipes(String searchKeyword, Long lastRecipeId, Pageable pageable) {
        return recipeRepository.findRecipesByCondition(searchKeyword, lastRecipeId, pageable);
    }

    /**
     * 좋아요한 레시피 목록 조회
     */
    public Slice<RecipeResponse> getLikeRecipes(Long memberId, Long lastLikeRecipeId, Pageable pageable) {
        List<Long> likeRecipeIds = recipeLikeRepository.findRecipeIdsByMemberId(memberId);
        return recipeRepository.findLikeRecipes(likeRecipeIds, lastLikeRecipeId, pageable);
    }

    /**
     * 레시피 상세 조회
     */
    public RecipeDetailResponse getRecipeById(Long recipeId) {
        Recipe recipe = getById(recipeId);
        return RecipeDetailResponse.from(recipe);
    }

    /**
     * 레시피 좋아요 토글
     */
    @Transactional
    public RecipeLikeResponse toggleRecipeLike(Long memberId, Long recipeId) {
        Member findMember = getMemberById(memberId);
        Recipe findRecipe = getById(recipeId);
        return recipeLikeRepository.findByMemberIdAndRecipeId(findMember.getId(), findRecipe.getId())
                .map(this::toggleExistingLike)
                .orElseGet(() -> createNewRecipeLike(findMember, findRecipe));

    }

    private RecipeLikeResponse toggleExistingLike(RecipeLike existingLike) {
        if (existingLike.isLiked()) {
            existingLike.unlike();
            return new RecipeLikeResponse(false);
        } else {
            existingLike.like();
            return new RecipeLikeResponse(true);
        }
    }

    private RecipeLikeResponse createNewRecipeLike(Member member, Recipe recipe) {
        RecipeLike newRecipeLike = RecipeLike.builder()
                .member(member)
                .recipe(recipe)
                .build();
        newRecipeLike.like();
        recipeLikeRepository.save(newRecipeLike);
        return new RecipeLikeResponse(true);
    }

    private Recipe getById(Long recipeId) {
        return recipeRepository.findRecipeById(recipeId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_RECIPE));
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER));
    }

    // public Slice<RecipeResponse> getRandomRecipes(Pageable pageable) {
    //     // 총 레시피 수를 조회합니다.
    //     long totalRecipes = recipeRepository.count();
    //     // 페이지 크기에 따른 총 페이지 수를 계산합니다.
    //     int totalPages = (int) Math.ceil((double) totalRecipes / pageable.getPageSize());
    //
    //     // 랜덤 페이지 번호를 생성합니다.
    //     int randomPage = new Random().nextInt(totalPages);
    //     // 새로운 Pageable 객체를 생성합니다.
    //     Pageable randomPageable = PageRequest.of(randomPage, pageable.getPageSize(), pageable.getSort());
    //
    //     // 랜덤 페이지로 데이터를 조회합니다.
    //     Slice<Recipe> allRecipes = recipeRepository.findAllBySlice(randomPageable);
    //
    //     // Slice로 변환 후 반환
    //     return new SliceImpl<>(recipeResponses, randomPageable, allRecipes.hasNext());
    // }

    /**
     * Member가 가지고 있는 냉장고 재료 기준(유통기한 짧은 순) 추천 레시피 리스트 조회 기능
     * @param memberId
     * @param pageable
     * @return
     */
    // 리팩토링 필요
    // public Slice<RecipeResponse> getRecommendedRecipesByMemberId(Long memberId, Pageable pageable) {
    //     Member member = memberRepository.findById(memberId)
    //             .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER));
    //
    //     // 멤버의 유효한 재료들을 유통기한 순으로 조회
    //     List<FridgeIngredient> ingredients = memberIngredientRepository.findActiveIngredientsByMemberIdOrderedByEndDate(memberId);
    //
    //     // 재료 이름 추출 및 재료 ID와 최소 유통기한 매핑
    //     Map<String, LocalDateTime> ingredientMinEndDateMap = ingredients.stream()
    //             .collect(Collectors.toMap(
    //                     ingredient -> ingredient.getIngredient().getName(),
    //                     FridgeIngredient::getEndDate,
    //                     (existing, replacement) -> existing.isBefore(replacement) ? existing : replacement // 중복 키 처리
    //             ));
    //
    //     // 재료 이름을 기반으로 레시피 조회
    //     Slice<Recipe> findRecipes = recipeRepository.findByMainIngredientNames(new ArrayList<>(ingredientMinEndDateMap.keySet()), pageable);
    //
    //     // 조회된 레시피를 유통기한에 따라 정렬
    //     List<Recipe> sortedRecipes = new ArrayList<>(findRecipes.getContent());
    //     sortedRecipes.sort(Comparator.comparing(recipe -> ingredientMinEndDateMap.getOrDefault(recipe.getMainIngredientName(), LocalDateTime.MAX)));
    //
    //     // DTO 변환
    //     List<RecipeResponse> recipeResponses = sortedRecipes.stream()
    //             .map(recipe -> RecipeResponse.builder()
    //                     .id(recipe.getId())
    //                     .name(recipe.getName())
    //                     .imgSrc(recipe.getThumbnail())
    //                     .build())
    //             .collect(Collectors.toList());
    //
    //     // Slice로 변환 후 반환
    //     return new SliceImpl<>(recipeResponses, pageable, findRecipes.hasNext());
    // }

    /**
     * 재료명으로 레시피 목록 조회
     * @param ingredientNames
     * @param pageable
     * @return
     */
    // public Slice<RecipeResponse> getRecipesByIngredientNames(List<String> ingredientNames, Pageable pageable) {
    //     Slice<Recipe> findRecipes = recipeRepository.findByMainIngredientNames(ingredientNames, pageable);
    //
    //     // DTO 변환
    //     List<RecipeResponse> recipeResponses = findRecipes.getContent().stream()
    //             .map(recipe -> RecipeResponse.builder()
    //                     .id(recipe.getId())
    //                     .name(recipe.getName())
    //                     .imgSrc(recipe.getThumbnail())
    //                     .build())
    //             .toList();
    //
    //     // Slice로 변환 후 반환
    //     return new SliceImpl<>(recipeResponses, pageable, findRecipes.hasNext());
    // }

    /**
     * 레시피 이름으로 레시피 목록 조회
     * @param recipeName
     * @param pageable
     * @return
     */
    // public Slice<RecipeResponse> getRecipesByRecipeName(String recipeName, Pageable pageable) {
    //     Slice<Recipe> findRecipes = recipeRepository.findByNameContaining(recipeName, pageable);
    //
    //     // DTO 변환
    //     List<RecipeResponse> recipeResponses = findRecipes.getContent().stream()
    //             .map(recipe -> RecipeResponse.builder()
    //                     .id(recipe.getId())
    //                     .name(recipe.getName())
    //                     .imgSrc(recipe.getThumbnail())
    //                     .build())
    //             .toList();
    //
    //     // Slice로 변환 후 반환
    //     return new SliceImpl<>(recipeResponses, pageable, findRecipes.hasNext());
    //
    // }

    // private RecipeDetailResponse convertToRecipeDetailResponse(Recipe recipe) {
    //     List<ManualResponse> manualResponseList = recipe.getManuals().stream()
    //             .map(manual -> ManualResponse.builder()
    //                     .seq(manual.getSeq())
    //                     .content(manual.getContent())
    //                     .imgSrc(manual.getImageUrl())
    //                     .build())
    //             .toList();
    //
    //     List<RecipeIngredientResponse> riResponseList = recipe.getRecipeIngredients().stream()
    //             .map(ri -> RecipeIngredientResponse.builder()
    //                     .name(ri.getIngredient().getName())
    //                     .quantity(ri.getQuantity())
    //                     .build())
    //             .toList();
    //
    //     return RecipeDetailResponse.builder()
    //             .name(recipe.getName())
    //             .imgSrc(recipe.getThumbnail())
    //             .tip(recipe.getTip())
    //             .manualResponseList(manualResponseList)
    //             .recipeIngredientResponseList(riResponseList)
    //             .build();
    // }



}
