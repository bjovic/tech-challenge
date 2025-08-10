package group.swissmarketplace.autoscout24.techchallenge.application.service;

import group.swissmarketplace.autoscout24.techchallenge.domain.in.CarListingService;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListing;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingSearchCriteria;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.Page;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.PageInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationCarListingService implements CarListingService {

  private final CarListingService domainCarListingService;

  public void create(CarListing listing) {
    domainCarListingService.create(listing);
  }

  public Page<CarListing> search(CarListingSearchCriteria criteria, PageInformation pageInformation) {
    return domainCarListingService.search(criteria, pageInformation);
  }

  public void update(CarListing listing) {
    domainCarListingService.update(listing);
  }

  public void delete(CarListing listing) {
    domainCarListingService.delete(listing);
  }

}
