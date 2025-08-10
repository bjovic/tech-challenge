package group.swissmarketplace.autoscout24.techchallenge.integration;

import group.swissmarketplace.autoscout24.techchallenge.application.messaging.CarListingsConsumer;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListing;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingMessage;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.MessageType;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

class CarListingIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private CarListingsConsumer carListingsConsumer;

  @Test
  void test_createUpdateDeleteCarListing_successfullyCreatedUpdatedDeleted() {

    final var createCar = new CarListing(
        "1",
        "audi",
        "a4",
        2022,
        50000,
        15000
    );
    final var createMessage = new CarListingMessage(
        MessageType.CREATE,
        createCar
    );

    carListingsConsumer.carListings().accept(createMessage);

    await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
        webTestClient.get().uri("/api/car-listings?make=audi&model=a4")
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<Page<CarListing>>() {
            })
            .value(page -> {
              assertThat(page.content()).hasSize(1);
              assertThat(page.content().getFirst()).isEqualTo(createCar);
            })
    );

    final var updateCar = new CarListing(
        "1",
        "audi",
        "a4",
        2022,
        45000,
        15000
    );
    final var updateMessage = new CarListingMessage(
        MessageType.UPDATE,
        updateCar
    );

    carListingsConsumer.carListings().accept(updateMessage);

    await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
        webTestClient.get().uri("/api/car-listings?make=audi&model=a4")
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<Page<CarListing>>() {
            })
            .value(page -> {
              assertThat(page.content()).hasSize(1);
              assertThat(page.content().getFirst()).isEqualTo(updateCar);
            })
    );

    final var deleteCar = new CarListing(
        "1",
        "audi",
        "a4",
        2022,
        45000,
        15000
    );
    final var deleteMessage = new CarListingMessage(
        MessageType.DELETE,
        deleteCar
    );

    carListingsConsumer.carListings().accept(deleteMessage);

    await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
        webTestClient.get().uri("/api/car-listings?make=audi&model=a4")
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<Page<CarListing>>() {
            })
            .value(page -> {
              assertThat(page.content()).isEmpty();
            })
    );

  }
}
