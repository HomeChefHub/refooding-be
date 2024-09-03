package refooding.api.domain.exchange.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import refooding.api.common.config.QuerydslConfig;
import refooding.api.domain.exchange.entity.Exchange;
import refooding.api.domain.exchange.entity.ExchangeStatus;
import refooding.api.domain.exchange.entity.Region;
import refooding.api.domain.member.entity.Member;
import refooding.api.domain.member.repository.MemberRepository;
import refooding.api.utils.EntityManagerUtil;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class ExchangeRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ExchangeRepository exchangeRepository;
    @Autowired
    private MemberRepository memberRepository;

    // @Test
    void 식재료_교환글_상세조회() {
        //given
        Long exchangeId = 10L;
        Long writeMemberId = 1L;

        //when
        Exchange findExchange = exchangeRepository.findExchangeById(exchangeId).orElseThrow();

        //then
        assertThat(findExchange.getMember().getId()).isEqualTo(writeMemberId);
    }

    // @Test
    // void 식재료_교환글_저장(){
    //     //given
    //
    //     // 회원 생성
    //     Member member = Member.builder().name("토리").build();
    //     Member savedMember = memberRepository.save(member);
    //
    //     // 지역 조회
    //     long regionId = 17; // 강남구
    //     Region region = regionRepository.findById(regionId).orElseThrow();
    //
    //     // 식재료 교환
    //     String title = "당근 나눔합니당";
    //     String content = "맛있는 당근을 나눔합니다";
    //     Exchange exchange = new Exchange(title, content, region, member);
    //
    //     //when
    //     Exchange savedExchange = exchangeRepository.save(exchange);
    //     EntityManagerUtil.flushAndClearContext(testEntityManager);
    //     Exchange findExchange = exchangeRepository.findExchangeById(savedExchange.getId()).orElseThrow();
    //
    //     //then
    //     // 식재료 교환글 검증
    //     assertThat(findExchange).isNotNull();
    //     assertThat(findExchange.getId()).isEqualTo(savedExchange.getId());
    //     assertThat(findExchange.getStatus()).isEqualTo(ExchangeStatus.ACTIVE);
    //
    //     // 지역 검증
    //     assertThat(findExchange.getRegion().getId()).isEqualTo(17);
    //     assertThat(findExchange.getRegion().getName()).isEqualTo("강남구");
    //     assertThat(findExchange.getRegion().getParent().getName()).isEqualTo("서울특별시");
    //
    //     // 회원 검증
    //     assertThat(findExchange.getMember().getId()).isEqualTo(savedMember.getId());
    //     assertThat(findExchange.getMember().getName()).isEqualTo("토리");
    // }

}