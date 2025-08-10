package group.swissmarketplace.autoscout24.techchallenge.domain.service;

import group.swissmarketplace.autoscout24.techchallenge.domain.in.CarListingService;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListing;
import group.swissmarketplace.autoscout24.techchallenge.domain.out.CarListingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DomainCarListingService implements CarListingService {

  private final CarListingRepository carListingRepository;

  @Override
  public void create(CarListing listing) {
    log.debug("Creating listing: <car={}>", listing);
    carListingRepository.save(listing);
    log.debug("Created listing: <id={}>", listing.id());
  }

  @Override
  public void update(CarListing listing) {
    log.debug("Updating listing: <car={}>", listing);
    carListingRepository.update(listing);
    log.debug("Updated listing: <id={}>", listing.id());
  }

  @Override
  public void delete(CarListing listing) {
    log.debug("Deleting listing: <car={}>", listing);
    carListingRepository.delete(listing);
    log.debug("Deleted listing: <id={}>", listing.id());
  }

}
