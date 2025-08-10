package group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch;

import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListing;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingSearchCriteria;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.Page;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.PageInformation;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.exception.RetryableException;
import group.swissmarketplace.autoscout24.techchallenge.domain.out.CarListingRepository;
import group.swissmarketplace.autoscout24.techchallenge.infrastructure.config.CacheNames;
import group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch.document.CarListingDocument;
import group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch.query.CarListingSearchQueryBuilder;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CarListingElasticsearchRepository implements CarListingRepository {

  private final ElasticsearchOperations elasticsearchOperations;
  private final CacheManager cacheManager;

  // TODO: Externalize retry configuration
  @Override
  @Retryable(
      retryFor = {DataAccessResourceFailureException.class},
      noRetryFor = {NoSuchIndexException.class},
      maxAttempts = 5,
      backoff = @Backoff(delay = 100, multiplier = 2)
  )
  public void save(CarListing listing) {
    elasticsearchOperations.save(CarListingDocument.toDocument(listing));
  }

  // TODO: Externalize retry configuration
  @Override
  @Retryable(
      retryFor = {DataAccessResourceFailureException.class},
      noRetryFor = {NoSuchIndexException.class},
      maxAttempts = 5,
      backoff = @Backoff(delay = 100, multiplier = 2)
  )
  public void update(CarListing listing) {
    elasticsearchOperations.update(CarListingDocument.toDocument(listing));
  }

  // TODO: Externalize retry configuration
  @Override
  @Retryable(
      retryFor = {DataAccessResourceFailureException.class},
      noRetryFor = {NoSuchIndexException.class},
      maxAttempts = 5,
      backoff = @Backoff(delay = 200, multiplier = 2)
  )
  @Cacheable(
      cacheManager = "cacheManager",
      cacheNames = CacheNames.CAR_LISTINGS,
      keyGenerator = "carListingSearchKeyGenerator",
      unless = "#result == null or #result.total() <= 0"
  )
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

  // TODO: Externalize retry configuration
  @Override
  @Retryable(
      retryFor = {DataAccessResourceFailureException.class},
      maxAttempts = 5,
      backoff = @Backoff(delay = 100, multiplier = 2)
  )
  public void delete(CarListing listing) {
    elasticsearchOperations.delete(listing.id(), CarListingDocument.class);
  }

  @Recover
  @SuppressWarnings("unchecked")
  public Page<CarListing> recover(
      DataAccessResourceFailureException e,
      CarListingSearchCriteria criteria,
      PageInformation pageInformation
  ) {
    log.error("Retries exhausted for search with <criteria={}>. Returning empty page.", criteria,
        e);
    final var key = CarListingSearchQueryBuilder.buildCacheKey(criteria, pageInformation);
    return Optional.ofNullable(cacheManager.getCache(CacheNames.CAR_LISTINGS))
        .map(cache -> cache.get(key, Page.class))
        .orElse(new Page<CarListing>(pageInformation, 0, java.util.Collections.emptyList()));
  }

  @Recover
  public Page<CarListing> recover(
      NoSuchIndexException e,
      CarListingSearchCriteria criteria,
      PageInformation pageInformation
  ) {
    log.error("CarListingDocument index does not exist. Returning empty page.", e);
    return new Page<>(pageInformation, 0, java.util.Collections.emptyList());
  }

  @Recover
  public void recover(DataAccessResourceFailureException e, CarListing listing) {
    log.error(
        "Retries exhausted while processing <listing={}>. Throwing exception to trigger kafka retry",
        listing,
        e
    );
    throw new RetryableException(e);
  }

  @Recover
  public void recover(NoSuchIndexException e, CarListing listing) {
    log.error("CarListingDocument index does not exist. Cannot save <listing={}>", listing, e);
    throw e;
  }

}
