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
public class MemberIngredientUpdateRequest {
    private Long requestMemberId; // 업데이트 요청하는 member의 id
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
