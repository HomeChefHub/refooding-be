package refooding.api.domain.exchange.dto.request;

import refooding.api.domain.exchange.entity.Exchange;
import refooding.api.domain.exchange.entity.Region;
import refooding.api.domain.member.entity.Member;

public record ExchangeCreateRequest(
        String title,
        String content,
        long regionId
) {
    public Exchange toExchange(Region region, Member member) {
        return new Exchange(title, content, region, member);
    }
}
