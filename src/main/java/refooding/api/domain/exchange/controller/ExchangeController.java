package refooding.api.domain.exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refooding.api.domain.exchange.dto.request.ExchangeCreateRequest;
import refooding.api.domain.exchange.dto.response.RegionResponse;
import refooding.api.domain.exchange.service.ExchangeService;
import refooding.api.domain.exchange.service.RegionService;

import java.util.List;

@Tag(name = "식재료 교환 API")
@RestController
@RequestMapping("/exchanges")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final RegionService regionService;

    @GetMapping("/regions")
    @Operation(
            summary = "식재료 교환 지역 목록 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "지역 목록 조회 성공",
                            content = @Content(schema = @Schema(implementation = RegionResponse.class))
                    )
            }
    )
    public ResponseEntity<List<RegionResponse>> regions() {
        List<RegionResponse> response = regionService.getRegionsWithChildren();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "식재료 교환글 등록",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "식재료 교환글 등록 성공"
                    )
            }
    )
    public ResponseEntity<Void> create(@RequestBody ExchangeCreateRequest request) {
        // TODO : 회원 추가
        exchangeService.create(request);
        return ResponseEntity.ok().build();
    }
}
