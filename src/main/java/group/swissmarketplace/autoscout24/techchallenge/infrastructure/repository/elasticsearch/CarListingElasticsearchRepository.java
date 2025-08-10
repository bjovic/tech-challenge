package group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch;

import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListing;
import group.swissmarketplace.autoscout24.techchallenge.domain.out.CarListingRepository;
import group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch.document.CarListingDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CarListingElasticsearchRepository implements CarListingRepository {

  private final ElasticsearchOperations elasticsearchOperations;

  public void save(CarListing listing) {
    elasticsearchOperations.save(CarListingDocument.toDocument(listing));
  }

  public void update(CarListing listing) {
    elasticsearchOperations.update(CarListingDocument.toDocument(listing));
  }

  public void delete(CarListing listing) {
    elasticsearchOperations.delete(listing.id(), CarListingDocument.class);
  }

}
