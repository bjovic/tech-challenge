package group.swissmarketplace.autoscout24.techchallenge.domain.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record CarListingMessage(
    MessageType type,
    CarListing listing
) {
}
