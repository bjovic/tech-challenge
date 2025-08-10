package group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch.document;

import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListing;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(
    indexName = "car-listings",
    createIndex = false
)
@Builder
@FieldNameConstants
public class CarListingDocument {

  @Id
  private String id;

  @Field(type = FieldType.Keyword, name = "make")
  private String make;

  @Field(type = FieldType.Keyword, name = "model")
  private String model;

  @Field(type = FieldType.Integer, name = "year")
  private Integer year;

  @Field(type = FieldType.Integer, name = "price")
  private Integer price;

  @Field(type = FieldType.Integer, name = "mileage")
  private Integer mileage;

  public static CarListingDocument toDocument(CarListing listing) {
    return CarListingDocument.builder()
        .id(listing.id())
        .make(listing.make())
        .model(listing.model())
        .year(listing.year())
        .price(listing.price())
        .mileage(listing.mileage())
        .build();
  }

  public CarListing toDomain() {
    return CarListing.builder()
        .id(this.id)
        .make(this.make)
        .model(this.model)
        .year(this.year)
        .price(this.price)
        .mileage(this.mileage)
        .build();
  }

}
