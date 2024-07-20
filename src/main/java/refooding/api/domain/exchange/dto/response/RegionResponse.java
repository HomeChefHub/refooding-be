package refooding.api.domain.exchange.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import refooding.api.domain.exchange.entity.Region;

import java.util.List;
@Getter
@AllArgsConstructor
public class RegionResponse{

    private Long id;
    private String name;
    private List<ChildRegion> childRegions;

    public static RegionResponse from(Region region){
        return new RegionResponse(
                region.getId(),
                region.getName(),
                region.getChildren().stream()
                        .map(ChildRegion::from)
                        .toList()
        );
    }

    @Getter
    @AllArgsConstructor
    public static class ChildRegion{
        private Long id;
        private String name;

        public static ChildRegion from(Region region) {
            return new ChildRegion(region.getId(), region.getName());
        }
    }

}

