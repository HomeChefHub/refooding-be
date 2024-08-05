package refooding.api.domain.exchange.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
import refooding.api.domain.exchange.dto.request.ExchangeCreateRequest;
import refooding.api.domain.exchange.dto.request.ExchangeUpdateRequest;
import refooding.api.domain.exchange.dto.response.ExchangeDetailResponse;
import refooding.api.domain.exchange.dto.response.ExchangeResponse;
import refooding.api.domain.exchange.entity.Exchange;
import refooding.api.domain.exchange.entity.ExchangeStatus;
import refooding.api.domain.exchange.entity.Region;
import refooding.api.domain.exchange.repository.ExchangeRepository;
import refooding.api.domain.exchange.repository.ExchangeSearchCondition;
import refooding.api.domain.exchange.repository.RegionRepository;
import refooding.api.domain.member.entity.Member;
import refooding.api.domain.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService{

    private final ExchangeRepository exchangeRepository;
    private final RegionRepository regionRepository;
    private final MemberRepository memberRepository;

    @Override
    public Slice<ExchangeResponse> getExchanges(String keyword, ExchangeStatus status, Long regionId, Long lastExchangeId, Pageable pageable) {
        return exchangeRepository.findExchangeByCondition(
                new ExchangeSearchCondition(keyword, status, regionId, lastExchangeId),
                pageable
        );
    }

    @Override
    public ExchangeDetailResponse getExchangeById(Long exchangeId) {
        Exchange exchange = getById(exchangeId);
        return ExchangeDetailResponse.from(exchange);
    }

    @Override
    @Transactional
    public Long create(ExchangeCreateRequest request) {
        // TODO : 회원 도메인 구현시 적용
        // 임시 회원 아이디
        Long memberId = 1L;
        Member findMember = getMemberById(memberId);
        Region region = getRegionById(request.regionId());

        Exchange exchange = request.toExchange(region, findMember);
        exchangeRepository.save(exchange);

        return exchange.getId();
    }



    @Override
    @Transactional
    public void update(Long exchangeId, ExchangeUpdateRequest request) {
        // TODO : 회원 도메인 구현시 적용
        // 임시 회원 아이디
        Long memberId = 1L;
        Member findMember = getMemberById(memberId);

        Exchange exchange = getById(exchangeId);
        if (exchange.validateMember(findMember.getId())) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED);
        }
        Region region = getRegionById(request.regionId());

        exchange.updateExchange(
                request.title(),
                request.content(),
                request.status(),
                region
        );
    }

    @Override
    @Transactional
    public void delete(Long exchangeId) {
        // TODO : 회원 도메인 구현시 적용
        // 임시 회원 아이디
        Long memberId = 1L;
        Member findMember = getMemberById(memberId);

        Exchange exchange = getById(exchangeId);
        if (exchange.validateMember(findMember.getId())) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED);
        }

        exchange.delete();
    }

    private Exchange getById(Long exchangeId) {
        return exchangeRepository.findExchangeById(exchangeId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_EXCHANGE));
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findByIdAndDeletedDateIsNull(memberId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER));
    }

    private Region getRegionById(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_REGION));
    }
}
