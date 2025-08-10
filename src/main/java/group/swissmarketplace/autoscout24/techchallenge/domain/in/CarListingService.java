package group.swissmarketplace.autoscout24.techchallenge.domain.in;

import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListing;

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
