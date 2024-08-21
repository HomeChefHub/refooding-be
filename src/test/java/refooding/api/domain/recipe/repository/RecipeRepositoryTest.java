package refooding.api.domain.recipe.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional(readOnly = true)
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ManualRepository manualRepository;

    // @Test
//     @Transactional
//     void 레시피_추가_테스트() {
//         //given
//
//         // 레시피 생성
//         Recipe recipe = Recipe.builder()
//                 .name("새우김치볶음")
//                 .tip("이런 요리는 없으니 해먹지 마세요!")
//                 .mainIngredientName("새우")
//                 .mainImgSrc("www.naver.com")
//                 .build();
//
//
//         Manual step1 = Manual.builder()
//                 .seq(1)
//                 .content("새우를 손질하세요!")
//                 .imageSrc("www.naver.com")
//                 .build();
//
//         Manual step2 = Manual.builder()
//                 .seq(2)
//                 .content("김치에 소금을 버무려서 넣으세요!")
//                 .imageSrc("www.naver.com")
//                 .build();
//
//         recipe.addManual(step1);
//         recipe.addManual(step2);
//
//         RecipeIngredient riSalt = RecipeIngredient.builder()
//                 .ingredient(salt)
//                 .quantity("75g")
//                 .build();
//
//         RecipeIngredient riKimchi = RecipeIngredient.builder()
//                 .ingredient(kimchi)
//                 .quantity("한 포기")
//                 .build();
//
//         RecipeIngredient riShrimp = RecipeIngredient.builder()
//                 .ingredient(shrimp)
//                 .quantity("100마리")
//                 .build();
//
//         recipe.addRecipeIngredient(riSalt);
//         recipe.addRecipeIngredient(riKimchi);
//         recipe.addRecipeIngredient(riShrimp);
//
//         salt.makeToRecipeIngredient(riSalt);
//         kimchi.makeToRecipeIngredient(riKimchi);
//         shrimp.makeToRecipeIngredient(riShrimp);
//
//         recipeRepository.save(recipe);
//
//         // when
// //        Optional<Recipe> findRecipe = recipeRepository.findById(1L);
//         Optional<Recipe> findRecipe = recipeRepository.findByIdWithDetails(1L);
//
//
//         // then
//         Assertions.assertThat(findRecipe).isPresent();
//         Assertions.assertThat(findRecipe.get().getName()).isEqualTo("새우김치볶음");
//         Assertions.assertThat(findRecipe.get().getManualList()).hasSize(2);
//         Assertions.assertThat(findRecipe.get().getRecipeIngredientList()).hasSize(3);
//
//
//
//
//     }
//
//     // @Test
//     void 지연_로딩_테스트() {
//         // given
//
//         // when
//         // Id로 조회
//         Optional<Recipe> recipeOptional = recipeRepository.findById(4L);
//
//         // then
//         recipeOptional.ifPresent(recipe -> {  // Optional을 사용하여 값의 존재 유무 확인
//             System.out.println("============= Recipe만 조회하는 쿼리가 나옴==========");
//
//             System.out.println("=============== Manual도 조회=======");
//             recipe.getManualList().forEach(manual ->
//                     System.out.println("manual.getContent() = " + manual.getContent())
//             );
//
//             System.out.println("=============== Recipe_ingredient 조회=======");
//             recipe.getRecipeIngredientList().forEach(recipeIngredient ->
//                     System.out.println("recipeIngredient.getQuantity() = " + recipeIngredient.getQuantity())
//             );
//         });
//     }
//
//     // @Test
//     void 전체_목록_조회_테스트() {
//         List<Recipe> all = recipeRepository.findAll();
//
//         for (Recipe recipe : all) {
//             System.out.println(recipe.getId());
//             System.out.println(recipe.getName());
//             System.out.println(recipe.getMainImgSrc());
//         }
//     }

}
