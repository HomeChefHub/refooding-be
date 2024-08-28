package refooding.api.domain.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import refooding.api.common.exception.CustomException;
import refooding.api.common.exception.ExceptionCode;
import refooding.api.common.s3.S3Uploader;
import refooding.api.domain.exchange.dto.request.ExchangeCreateRequest;
import refooding.api.domain.exchange.dto.request.ExchangeUpdateRequest;
import refooding.api.domain.exchange.dto.response.ExchangeDetailResponse;
import refooding.api.domain.exchange.dto.response.ExchangeResponse;
import refooding.api.domain.exchange.entity.Exchange;
import refooding.api.domain.exchange.entity.ExchangeImage;
import refooding.api.domain.exchange.entity.ExchangeStatus;
import refooding.api.domain.exchange.entity.Region;
import refooding.api.domain.exchange.repository.ExchangeImageRepository;
import refooding.api.domain.exchange.repository.ExchangeRepository;
import refooding.api.domain.exchange.repository.ExchangeSearchCondition;
import refooding.api.domain.exchange.repository.RegionRepository;
import refooding.api.domain.member.entity.Member;
import refooding.api.domain.member.repository.MemberRepository;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService{

    private final ExchangeRepository exchangeRepository;
    private final ExchangeImageRepository exchangeImageRepository;
    private final RegionRepository regionRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    @Override
    public Slice<ExchangeResponse> getExchanges(String keyword, ExchangeStatus status, Long regionId, Long lastExchangeId, Pageable pageable) {
        return exchangeRepository.findExchangesByCondition(
                new ExchangeSearchCondition(keyword, status, regionId, lastExchangeId),
                pageable
        );
    }

    @Override
    public ExchangeDetailResponse getExchangeById(Long exchangeId) {
        Exchange findExchange = getById(exchangeId);
        List<ExchangeImage> findImages = getImageByExchangeId(findExchange.getId());
        List<String> imageUrls = findImages.stream()
                .map(ExchangeImage::getUrl)
                .toList();
        return ExchangeDetailResponse.from(findExchange, imageUrls);
    }

    @Override
    @Transactional
    public Long create(Long memberId, ExchangeCreateRequest request) {
        Member findMember = getMemberById(memberId);
        Region region = getRegionById(request.regionId());

        // S3 이미지 업로드
        List<ExchangeImage> exchangeImages = uploadExchangeImages(request.image());

        Exchange exchange = request.toExchange(region, findMember, exchangeImages);
        Exchange savedExchange = exchangeRepository.save(exchange);
        saveExchangeImages(savedExchange, exchangeImages);

        return savedExchange.getId();
    }

    @Override
    @Transactional
    public void update(Long memberId, Long exchangeId, ExchangeUpdateRequest request) {
        Member findMember = getMemberById(memberId);
        Exchange findExchange = getById(exchangeId);
        validateAuthor(findMember, findExchange);
        Region region = getRegionById(request.regionId());

        findExchange.updateExchange(request.title(), request.content(), request.status(), region);
        updateExchangeImages(findExchange, request);
    }

    @Override
    @Transactional
    public void delete(Long memberId, Long exchangeId) {
        Member findMember = getMemberById(memberId);
        Exchange findExchange = getById(exchangeId);
        validateAuthor(findMember, findExchange);

        deleteOldImages(findExchange.getId());
        findExchange.delete();
    }

    private List<ExchangeImage> uploadExchangeImages(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return Collections.emptyList();
        }
        String imageUrl = s3Uploader.uploadExchangeImage(image);
        return Collections.singletonList(new ExchangeImage(imageUrl));
    }

    private void updateExchangeImages(Exchange exchange, ExchangeUpdateRequest request) {
        List<ExchangeImage> newImages = uploadExchangeImages(request.image());
        boolean isNullOrEmpty = request.imageUrls() == null || request.imageUrls().isEmpty();

        // 기존 이미지가 없으면서, 새로운 이미지 없으면 모든 이미지 삭제
        if (isNullOrEmpty && newImages.isEmpty()) {
            deleteOldImages(exchange.getId());
            exchange.updateImages(newImages);
            return;
        }

        // 새로운 이미지가 있으면 기존 이미지 삭제 후 새로운 이미지 저장
        if (!newImages.isEmpty()) {
            deleteOldImages(exchange.getId());
            saveExchangeImages(exchange, newImages);
            exchange.updateImages(newImages);
        }
    }

    private void saveExchangeImages(Exchange exchange, List<ExchangeImage> images) {
        images.forEach(image -> {
            image.setExchange(exchange);
            exchangeImageRepository.save(image);
        });
    }

    private void deleteOldImages(Long exchangeId) {
        List<ExchangeImage> oldImages = getImageByExchangeId(exchangeId);
        oldImages.forEach(ExchangeImage::delete);
    }

    private Exchange getById(Long exchangeId) {
        return exchangeRepository.findExchangeById(exchangeId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_EXCHANGE));
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MEMBER));
    }

    private Region getRegionById(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_REGION));
    }

    private List<ExchangeImage> getImageByExchangeId(Long exchangeId) {
        return exchangeImageRepository.findAllByExchangeId(exchangeId);
    }

    private void validateAuthor(Member member, Exchange exchange) {
        if (!exchange.isAuthor(member)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED);
        }
    }

}
