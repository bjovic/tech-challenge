package group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch.query;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingSearchCriteria;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingSortType;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.PageInformation;
import group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch.document.CarListingDocument.Fields;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;

@UtilityClass
public class CarListingSearchQueryBuilder {

    public static NativeQuery buildQuery(CarListingSearchCriteria criteria, PageInformation pageInformation) {
        final var boolQuery = QueryBuilders.bool();

        if (criteria.make() != null && !criteria.make().trim().isEmpty()) {
            boolQuery.must(QueryBuilders.term(builder -> builder.field(Fields.make).value(criteria.make())));
        }

        if (criteria.model() != null && !criteria.model().trim().isEmpty()) {
            boolQuery.must(QueryBuilders.term(builder -> builder.field(Fields.model).value(criteria.model())));
        }

        if (criteria.yearFrom() != null || criteria.yearTo() != null) {
            final var rangeQuery = QueryBuilders.range();
            if (criteria.yearFrom() != null) {
                rangeQuery.number(builder -> builder.field(Fields.year).gte(criteria.yearFrom().doubleValue()));
            }
            if (criteria.yearTo() != null) {
                rangeQuery.number(builder -> builder.field(Fields.year).lte(criteria.yearTo().doubleValue()));

            }
            boolQuery.must(rangeQuery.build());
        }

        if (criteria.priceFrom() != null || criteria.priceTo() != null) {
            var rangeQuery = QueryBuilders.range();
            if (criteria.priceFrom() != null) {
                rangeQuery.number(builder -> builder.field(Fields.price).gte(criteria.priceFrom().doubleValue()));
            }
            if (criteria.priceTo() != null) {
                rangeQuery.number(builder -> builder.field(Fields.price).lte(criteria.priceTo().doubleValue()));
            }
            boolQuery.must(rangeQuery.build());
        }

        if (criteria.mileageFrom() != null || criteria.mileageTo() != null) {
            var rangeQuery = QueryBuilders.range();
            if (criteria.mileageFrom() != null) {
                rangeQuery.number(builder -> builder.field(Fields.mileage).gte(criteria.mileageFrom().doubleValue()));
            }
            if (criteria.mileageTo() != null) {
                rangeQuery.number(builder -> builder.field(Fields.mileage).lte(criteria.mileageTo().doubleValue()));
            }
            boolQuery.must(rangeQuery.build());
        }

        if (!boolQuery.hasClauses()) {
            boolQuery.must(QueryBuilders.matchAll().build());
        }

        return NativeQuery.builder()
            .withQuery(builder -> builder.bool(boolQuery.build()))
            .withPageable(buildPageable(pageInformation))
            .build();
    }

    private static Pageable buildPageable(PageInformation pageInformation) {
        return PageRequest.of(
            pageInformation.page(),
            pageInformation.size(),
            toSort(pageInformation.sortType())
        );
    }

    private static Sort toSort(CarListingSortType sortType) {
        return switch (sortType) {
            case CHEAPEST -> Sort.by(Sort.Direction.ASC, Fields.price);
            case MOST_EXPENSIVE -> Sort.by(Sort.Direction.DESC, Fields.price);
            case NEWEST -> Sort.by(Sort.Direction.DESC, Fields.year);
            case OLDEST -> Sort.by(Sort.Direction.ASC, Fields.year);
            case LOWEST_MILEAGE -> Sort.by(Sort.Direction.ASC, Fields.mileage);
            case HIGHEST_MILEAGE -> Sort.by(Sort.Direction.DESC, Fields.mileage);
        };
    }

}
