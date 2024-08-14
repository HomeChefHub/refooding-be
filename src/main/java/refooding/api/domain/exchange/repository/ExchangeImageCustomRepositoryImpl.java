package refooding.api.domain.exchange.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import refooding.api.domain.exchange.entity.ExchangeImage;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExchangeImageCustomRepositoryImpl implements ExchangeImageCustomRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<ExchangeImage> images) {
        String sql = """
                    INSERT INTO exchange_image (exchange_id, url, created_date, modified_date) 
                    VALUES (:exchangeId, :url, :createdDate, :modifiedDate)
                """;
        namedParameterJdbcTemplate.batchUpdate(sql, getExchangeImageToSqlParameterSources(images));
    }

    private MapSqlParameterSource[] getExchangeImageToSqlParameterSources(List<ExchangeImage> images) {
        return images.stream()
                .map(this::getExchangeImageToSqlParameterSource)
                .toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource getExchangeImageToSqlParameterSource(ExchangeImage image) {
        final LocalDateTime now = LocalDateTime.now();
        return new MapSqlParameterSource()
                .addValue("exchangeId", image.getExchange().getId())
                .addValue("url", image.getUrl())
                .addValue("createdDate", now)
                .addValue("modifiedDate", now);
    }
}
