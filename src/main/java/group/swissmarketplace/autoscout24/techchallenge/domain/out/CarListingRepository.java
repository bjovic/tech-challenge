package group.swissmarketplace.autoscout24.techchallenge.domain.out;

import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListing;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingSearchCriteria;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.Page;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.PageInformation;

/**
 * Repository interface for managing car listings.
 * This interface defines methods for saving, updating, searching, and deleting car listings.
 */
public interface CarListingRepository {

  /**
   * Saves a car listing to the repository.
   *
   * @param listing the car listing to save
   */
  void save(CarListing listing);

  /**
   * Updates an existing car listing in the repository.
   *
   * @param listing the car listing to update
   */
  void update(CarListing listing);

  /**
   * Searches for car listings based on the provided criteria and pagination information.
   *
   * @param criteria the search criteria
   * @param pageInformation the pagination information
   * @return a page of car listings matching the search criteria
   */
  Page<CarListing> search(CarListingSearchCriteria criteria, PageInformation pageInformation);

  /**
   * Deletes a car listing from the repository.
   *
   * @param listing the car listing to delete
   */
  void delete(CarListing listing);

}
