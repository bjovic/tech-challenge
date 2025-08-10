package group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch;

import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListing;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingSearchCriteria;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.Page;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.PageInformation;
import group.swissmarketplace.autoscout24.techchallenge.domain.out.CarListingRepository;
import group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch.document.CarListingDocument;
import group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch.query.CarListingSearchQueryBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CarListingElasticsearchRepository implements CarListingRepository {

  private final ElasticsearchOperations elasticsearchOperations;

  public void save(CarListing listing) {
    elasticsearchOperations.save(CarListingDocument.toDocument(listing));
  }

  public Page<CarListing> search(
      CarListingSearchCriteria criteria,
      PageInformation pageInformation
  ) {
    final var query = CarListingSearchQueryBuilder.buildQuery(criteria, pageInformation);

    final var searchHits = elasticsearchOperations.search(query, CarListingDocument.class);

    return new Page<>(
        pageInformation,
        searchHits.getTotalHits(),
        searchHits.stream().map(SearchHit::getContent).map(CarListingDocument::toDomain).toList()
    );
  }

  public void update(CarListing listing) {
    elasticsearchOperations.update(CarListingDocument.toDocument(listing));
  }

  public void delete(CarListing listing) {
    elasticsearchOperations.delete(listing.id(), CarListingDocument.class);
  }

}
