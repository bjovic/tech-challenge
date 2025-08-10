package group.swissmarketplace.autoscout24.techchallenge.domain.in;

import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListing;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingSearchCriteria;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.Page;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.PageInformation;

/**
 * Service interface for managing car listings.
 * This interface defines the operations that can be performed on car listings,
 * such as creating, searching, updating, and deleting listings.
 */
public interface CarListingService {

  /**
   * Creates a new car listing.
   *
   * @param listing the car listing to create
   */
  void create(CarListing listing);

  /**
   * Searches for car listings based on the provided criteria and pagination information.
   *
   * @param criteria the search criteria
   * @param pageInformation the pagination information
   * @return a page of car listings matching the search criteria
   */
  Page<CarListing> search(CarListingSearchCriteria criteria, PageInformation pageInformation);

  /**
   * Updates an existing car listing.
   *
   * @param listing the car listing to update
   */
  void update(CarListing listing);

  /**
   * Deletes a car listing.
   *
   * @param listing the car listing to delete
   */
  void delete(CarListing listing);

}
