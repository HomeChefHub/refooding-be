package refooding.api.domain.recipe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
import refooding.api.domain.ingredient.repository.MemberIngredientRepository;
import refooding.api.domain.member.entity.Member;
import refooding.api.domain.member.repository.MemberRepository;
import refooding.api.domain.recipe.dto.response.RecipeDetailResponse;
import refooding.api.domain.recipe.dto.response.RecipeLikeResponse;
import refooding.api.domain.recipe.dto.response.RecipeResponse;
import refooding.api.domain.recipe.dto.response.RecommendRecipeResponse;
import refooding.api.domain.recipe.entity.Recipe;
import refooding.api.domain.recipe.entity.RecipeLike;
import refooding.api.domain.recipe.repository.RecipeLikeRepository;
import refooding.api.domain.recipe.repository.RecipeRepository;
import refooding.api.domain.recipe.repository.RecommendRecipeCondition;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

    @Override
    public Slice<RecommendRecipeResponse> getRecommendedRecipes(Long memberId, String ingredientName, Long lastRecipeId, LocalDateTime expirationDate, Pageable pageable) {
        Member findMember = getMemberById(memberId);

        return recipeRepository.findRecipesSortedByEarliestExpiration(
                new RecommendRecipeCondition(findMember.getId(), ingredientName, lastRecipeId, expirationDate),
                pageable);
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

    private LocalDateTime findEarliestExpiration(Recipe recipe, Map<String, LocalDateTime> expirationMap) {
        String ingredients = recipe.getIngredients();
        return expirationMap.entrySet().stream()
                /**
                 * \\b을 통해 재료 이름이 독립된 단어로 존재하는 경우만 매칭되도록 한다.
                 * 예를 들어, '파'라는 재료가 '파프리카'와 매칭되지않고 '파' 독립된 단어로 매칭한다.
                 */
                .filter(entry -> Pattern.compile("\\b" + Pattern.quote(entry.getKey()) + "\\b").matcher(ingredients).find())
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(LocalDateTime.MAX);
    }

}
