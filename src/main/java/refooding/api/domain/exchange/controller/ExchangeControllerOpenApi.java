package refooding.api.domain.exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import refooding.api.domain.exchange.dto.request.ExchangeCreateRequest;
import refooding.api.domain.exchange.dto.request.ExchangeUpdateRequest;
import refooding.api.domain.exchange.dto.response.ExchangeDetailResponse;
import refooding.api.domain.exchange.dto.response.ExchangeResponse;
import refooding.api.domain.exchange.dto.response.RegionResponse;
import refooding.api.domain.exchange.entity.ExchangeStatus;

import java.util.List;

@Tag(name = "식재료 교환 API")
public interface ExchangeControllerOpenApi {

    @Operation(
            summary = "식재료 교환 목록 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "식재료 교환 목록 조회 성공"
                    )
            }
    )
    @Parameters(
            value = {
                    @Parameter(
                            name = "keyword",
                            description = "검색어"
                    ),
                    @Parameter(
                            name = "regionId",
                            description = "지역 아이디"
                    ),
                    @Parameter(
                            name = "status",
                            description = "식재료 교환 상태"
                    ),
                    @Parameter(
                            name = "size",
                            description = "요청 컨텐츠 수(0개 이상~50개 이하)"
                    ),
                    @Parameter(
                            name = "lastExchangeId",
                            description = "이전 요청 목록의 마지막 컨텐츠 아이디"
                    ),
            }
    )
    ResponseEntity<Slice<ExchangeResponse>> getExchanges(
            String keyword,
            Long regionId,
            ExchangeStatus status,
            int size,
            Long lastExchangeId
    );

    @Operation(
            summary = "식재료 교환 상세 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "식재료 교환 상세 조회 성공"
                    )
            }
    )
    @Parameter(
            name = "exchangeId",
            description = "게시글 아이디",
            required = true
    )
    ResponseEntity<ExchangeDetailResponse> getExchangeById(Long exchangeId);

    @Operation(
            summary = "식재료 교환글 등록",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "식재료 교환글 등록 성공"
                    )
            }
    )
    ResponseEntity<Void> create(ExchangeCreateRequest request);

    @Operation(
            summary = "식재료 교환글 수정",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "식재료 교환글 수정 성공"
                    )
            }
    )
    @Parameter(
            name = "exchangeId",
            description = "게시글 아이디",
            required = true
    )
    ResponseEntity<Void> update(Long exchangeId, ExchangeUpdateRequest request);

    @Operation(
            summary = "식재료 교환글 삭제",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "식재료 교환글 삭제 성공"
                    )
            }
    )
    @Parameter(
            name = "exchangeId",
            description = "게시글 아이디",
            required = true
    )
    ResponseEntity<Void> delete(Long exchangeId);

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
    ResponseEntity<List<RegionResponse>> regions();
}
