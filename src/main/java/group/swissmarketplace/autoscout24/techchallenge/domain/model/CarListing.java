package group.swissmarketplace.autoscout24.techchallenge.domain.model;

import lombok.Builder;

@Builder
public record CarListing(
    String id,
    String make,
    String model,
    Integer year,
    Integer price,
    Integer mileage
) {
}
