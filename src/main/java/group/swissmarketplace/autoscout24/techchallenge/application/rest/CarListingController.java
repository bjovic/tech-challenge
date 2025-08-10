package group.swissmarketplace.autoscout24.techchallenge.application.rest;

import group.swissmarketplace.autoscout24.techchallenge.application.service.ApplicationCarListingService;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListing;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingSearchCriteria;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingSortType;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.Page;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.PageInformation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car-listings")
public class CarListingController {

  private final ApplicationCarListingService applicationCarListingService;

  @Operation(
      summary = "Search car listings",
      description = "Search for car listings based on various criteria such as make, model, year, price, and mileage."
  )
  @ApiResponse(
      responseCode = "200",
      description = "Returns a paginated list of car listings matching the search criteria."
  )
  @ApiResponse(
      responseCode = "400",
      description = "Invalid search criteria provided. Ensure all parameters meet the validation requirements."
  )
  @ApiResponse(
      responseCode = "500",
      description = "Internal server error. An unexpected error occurred while processing the request."
  )
  @GetMapping
  public Page<CarListing> searchCarListings(
      @Parameter(description = "The manufacturer of the car.")
      @RequestParam(required = false) @Pattern(regexp = ".*\\S.*", message = "must not be blank") String make,
      @Parameter(description = "The model of the car.")
      @RequestParam(required = false) @Pattern(regexp = ".*\\S.*", message = "must not be blank") String model,
      @Parameter(description = "The minimum manufacturing year.")
      @RequestParam(required = false) @Min(0) Integer yearFrom,
      @Parameter(description = "The maximum manufacturing year.")
      @RequestParam(required = false) @Min(0) Integer yearTo,
      @Parameter(description = "The minimum price.")
      @RequestParam(required = false) @Min(0) Integer priceFrom,
      @Parameter(description = "The maximum price.")
      @RequestParam(required = false) @Min(0) Integer priceTo,
      @Parameter(description = "The minimum mileage.")
      @RequestParam(required = false) @Min(0) Integer mileageFrom,
      @Parameter(description = "The maximum mileage.")
      @RequestParam(required = false) @Min(0) Integer mileageTo,
      @Parameter(description = "The page number of the results.")
      @RequestParam(defaultValue = "0") @Min(0) Integer page,
      @Parameter(description = "The number of results per page.")
      @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size,
      @Parameter(description = "The sorting criteria for the results.")
      @RequestParam(defaultValue = "CHEAPEST") CarListingSortType sortBy
  ) {
    return applicationCarListingService.search(
        CarListingSearchCriteria.builder()
            .make(make)
            .model(model)
            .yearFrom(yearFrom)
            .yearTo(yearTo)
            .priceFrom(priceFrom)
            .priceTo(priceTo)
            .mileageFrom(mileageFrom)
            .mileageTo(mileageTo)
            .build(),
        PageInformation.builder()
            .page(page)
            .size(size)
            .sortType(sortBy)
            .build()
    );
  }
}
