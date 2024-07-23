package refooding.api.domain.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refooding.api.domain.exchange.dto.request.ExchangeCreateRequest;
import refooding.api.domain.exchange.dto.request.ExchangeUpdateRequest;
import refooding.api.domain.exchange.dto.response.ExchangeDetailResponse;
import refooding.api.domain.exchange.dto.response.RegionResponse;
import refooding.api.domain.exchange.service.ExchangeService;
import refooding.api.domain.exchange.service.RegionService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/exchanges")
@RequiredArgsConstructor
public class ExchangeController implements ExchangeControllerOpenApi{

    private final ExchangeService exchangeService;
    private final RegionService regionService;

    @GetMapping("/{exchangeId}")
    public ResponseEntity<ExchangeDetailResponse> getExchangeById(@PathVariable Long exchangeId) {
        ExchangeDetailResponse response = exchangeService.getExchangeById(exchangeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ExchangeCreateRequest request) {
        // TODO : 회원 추가
        Long exchangeId = exchangeService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/exchanges/" + exchangeId)).build();
    }

    @PatchMapping("/{exchangeId}")
    public ResponseEntity<Void> update(@PathVariable Long exchangeId, @RequestBody ExchangeUpdateRequest request){
        exchangeService.update(exchangeId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{exchangeId}")
    public ResponseEntity<Void> delete(@PathVariable Long exchangeId) {
        exchangeService.delete(exchangeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/regions")
    public ResponseEntity<List<RegionResponse>> regions() {
        List<RegionResponse> response = regionService.getRegionsWithChildren();
        return ResponseEntity.ok(response);
    }
}
