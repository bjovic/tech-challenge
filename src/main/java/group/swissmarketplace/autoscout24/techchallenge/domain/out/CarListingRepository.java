package group.swissmarketplace.autoscout24.techchallenge.domain.out;

import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListing;

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
   * Deletes a car listing from the repository.
   *
   * @param listing the car listing to delete
   */
  void delete(CarListing listing);

}
