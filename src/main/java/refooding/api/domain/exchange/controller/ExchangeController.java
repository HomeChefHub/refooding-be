package refooding.api.domain.exchange.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import refooding.api.domain.exchange.dto.request.ExchangeCreateRequest;
import refooding.api.domain.exchange.dto.request.ExchangeUpdateRequest;
import refooding.api.domain.exchange.dto.response.ExchangeDetailResponse;
import refooding.api.domain.exchange.dto.response.ExchangeResponse;
import refooding.api.domain.exchange.dto.response.RegionResponse;
import refooding.api.domain.exchange.entity.ExchangeStatus;
import refooding.api.domain.exchange.service.ExchangeService;
import refooding.api.domain.exchange.service.RegionService;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/exchanges")
@RequiredArgsConstructor
public class ExchangeController implements ExchangeControllerOpenApi{

    private final ExchangeService exchangeService;
    private final RegionService regionService;

    @Override
    @GetMapping
    public ResponseEntity<Slice<ExchangeResponse>> getExchanges(
            @RequestParam(required = false)  String keyword,
            @RequestParam(required = false)  Long regionId,
            @RequestParam(required = false)  ExchangeStatus status,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) Long lastExchangeId
            ) {

        Slice<ExchangeResponse> response = exchangeService.getExchanges(keyword, status, regionId, lastExchangeId, PageRequest.ofSize(size));
        return ResponseEntity.ok(response);
    }
    @Override
    @GetMapping("/{exchangeId}")
    public ResponseEntity<ExchangeDetailResponse> getExchangeById(@PathVariable Long exchangeId) {
        ExchangeDetailResponse response = exchangeService.getExchangeById(exchangeId);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> create(@ModelAttribute @Valid ExchangeCreateRequest request) {
        // TODO : 인증 추가
        // 임시 회원 아이디
        Long memberId = 1L;

        Long exchangeId = exchangeService.create(memberId, request);
        return ResponseEntity.created(URI.create("/api/v1/exchanges/" + exchangeId)).build();
    }

    @Override
    @PatchMapping("/{exchangeId}")
    public ResponseEntity<Void> update(@PathVariable Long exchangeId, @ModelAttribute @Valid ExchangeUpdateRequest request){
        // TODO : 인증 추가
        // 임시 회원 아이디
        Long memberId = 1L;

        exchangeService.update(memberId, exchangeId, request);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/{exchangeId}")
    public ResponseEntity<Void> delete(@PathVariable Long exchangeId) {
        // TODO : 인증 추가
        // 임시 회원 아이디
        Long memberId = 1L;

        exchangeService.delete(memberId, exchangeId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/regions")
    public ResponseEntity<List<RegionResponse>> regions() {
        List<RegionResponse> response = regionService.getRegionsWithChildren();
        return ResponseEntity.ok(response);
    }
}
