package refooding.api.domain.exchange.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import refooding.api.common.qeurydsl.QuerydslRepositoryUtils;
import refooding.api.domain.exchange.dto.response.ExchangeResponse;
import refooding.api.domain.exchange.dto.response.QExchangeResponse;
import refooding.api.domain.exchange.entity.ExchangeStatus;
import refooding.api.domain.exchange.entity.QRegion;

import static refooding.api.domain.exchange.entity.QExchange.exchange;
import static refooding.api.domain.exchange.entity.QRegion.region;
import static refooding.api.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class ExchangeCustomRepositoryImpl implements ExchangeCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<ExchangeResponse> findExchangesByCondition(ExchangeSearchCondition condition, Pageable pageable) {

        QRegion parentRegion = new QRegion("parent");

        JPAQuery<ExchangeResponse> content = jpaQueryFactory
                .select(
                        new QExchangeResponse(
                                exchange.id,
                                exchange.title,
                                member.id,
                                member.name.as("username"),
                                parentRegion.name,
                                region.name,
                                exchange.status,
                                exchange.createdDate
                        )
                )
                .from(exchange)
                .leftJoin(exchange.member, member)
                .leftJoin(exchange.region, region)
                .leftJoin(parentRegion).on(parentRegion.id.eq(region.parent.id))
                .where(
                        keywordContains(condition.keyword()),
                        statusEq(condition.status()),
                        regionEq(condition.regionId()),
                        cursorCondition(condition.lastExchangeId()),
                        notDeleted()
                )
                .orderBy(exchange.id.desc());

        return QuerydslRepositoryUtils.fetchSlice(content, pageable);
    }

    private BooleanExpression keywordContains(String keyword) {
        return StringUtils.isNullOrEmpty(keyword) ? null : exchange.title.contains(keyword);
    }

    private BooleanExpression statusEq(ExchangeStatus status) {
        return status != null ? exchange.status.eq(status) : null;
    }

    private BooleanExpression regionEq(Long regionId) {
        return regionId != null ? exchange.region.id.eq(regionId) : null;
    }

    private BooleanExpression cursorCondition(Long lastExchangeId){
        return lastExchangeId == null ? null : exchange.id.lt(lastExchangeId);
    }

    private static BooleanExpression notDeleted() {
        return exchange.deletedDate.isNull();
    }


}
