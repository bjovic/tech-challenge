package group.swissmarketplace.autoscout24.techchallenge.application.messaging;

import group.swissmarketplace.autoscout24.techchallenge.application.service.ApplicationCarListingService;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingMessage;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CarListingsConsumer {

  private final ApplicationCarListingService applicationCarListingService;

  @Bean
  public Consumer<CarListingMessage> carListings() {
    return carListing -> {
      log.info("Received new car <message={}>", carListing);
      switch (carListing.type()) {
        case CREATE -> applicationCarListingService.create(carListing.listing());
        case UPDATE -> applicationCarListingService.update(carListing.listing());
        case DELETE -> applicationCarListingService.delete(carListing.listing());
        default -> throw new IllegalArgumentException("Unknown message type: " + carListing.type());
      }
    };
  }

}
