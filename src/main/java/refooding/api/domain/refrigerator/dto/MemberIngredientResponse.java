package refooding.api.domain.refrigerator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MemberIngredientResponse {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
