package refooding.api.domain.fridge.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import refooding.api.domain.fridge.entity.IngredientImage;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientImageCustomRepositoryImpl implements IngredientImageCustomRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<IngredientImage> images) {
        String sql = """
                    INSERT INTO ingredient_image (fridge_ingredient_id, url, created_date, modified_date) 
                    VALUES (:fridgeIngredientId, :url, :createdDate, :modifiedDate)
                """;
        namedParameterJdbcTemplate.batchUpdate(sql, getFridgeIngredientImageToSqlParameterSources(images));
    }

    private MapSqlParameterSource[] getFridgeIngredientImageToSqlParameterSources(List<IngredientImage> images) {
        return images.stream()
                .map(this::getFridgeIngredientImageToSqlParameterSource)
                .toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource getFridgeIngredientImageToSqlParameterSource(IngredientImage image) {
        final LocalDateTime now = LocalDateTime.now();
        return new MapSqlParameterSource()
                .addValue("fridgeIngredientId", image.getFridgeIngredient().getId())
                .addValue("url", image.getUrl())
                .addValue("createdDate", now)
                .addValue("modifiedDate", now);
    }
}
