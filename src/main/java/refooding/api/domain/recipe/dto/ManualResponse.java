package refooding.api.domain.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ManualResponse {
    private int seq;
    private String content;
    private String imgSrc;
}
