package group.swissmarketplace.autoscout24.techchallenge.domain.model;

import lombok.Builder;

@Builder
public record CarListingSearchCriteria(
    String make,
    String model,
    Integer yearFrom,
    Integer yearTo,
    Integer priceFrom,
    Integer priceTo,
    Integer mileageFrom,
    Integer mileageTo
) {

}
