package refooding.api.domain.exchange.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import refooding.api.domain.exchange.entity.Region;

import java.util.List;

public record RegionResponse(
        Long id,
        String name,
        @Schema(description = "해당 지역에 대한 하위 지역 목록")
        List<ChildRegion> childRegions) {

    public static RegionResponse of(Region region) {
        return new RegionResponse(
                region.getId(),
                region.getName(),
                region.getChildren().stream()
                        .map(ChildRegion::of)
                        .toList()
        );
    }

    private record ChildRegion(Long id, String name) {
        public static ChildRegion of(Region region) {
            return new ChildRegion(region.getId(), region.getName());
        }
    }

}