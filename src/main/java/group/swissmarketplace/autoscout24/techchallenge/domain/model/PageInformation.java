package group.swissmarketplace.autoscout24.techchallenge.domain.model;

import lombok.Builder;

@Builder
public record PageInformation(
    Integer page,
    Integer size,
    CarListingSortType sortType
) {
}
