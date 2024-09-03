package refooding.api.domain.exchange.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import refooding.api.common.config.QuerydslConfig;
import refooding.api.domain.exchange.entity.Region;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(QuerydslConfig.class)
@DataJpaTest
class RegionRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    // @Test
    void 식재료_교환_지역_목록_조회() {
        // when
        List<Region> parentRegions = regionRepository.findByParentIsNull();

        //then
        assertThat(parentRegions).isNotNull();
        assertThat(parentRegions.get(0).getParent()).isNull();
        assertThat(parentRegions.get(0).getName()).isNotNull();
        assertThat(parentRegions.get(0).getChildren()).isNotEmpty();
    }

}
