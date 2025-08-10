package group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch;

import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingSearchCriteria;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.PageInformation;
import group.swissmarketplace.autoscout24.techchallenge.infrastructure.config.CacheNames;
import group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch.document.CarListingDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.retry.annotation.EnableRetry;

import static group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingSortType.CHEAPEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EnableRetry
@ExtendWith(MockitoExtension.class)
class CarListingElasticsearchRepositoryTest {

  @Mock
  private ElasticsearchOperations elasticsearchOperations;

  @Mock
  private CacheManager cacheManager;

  @InjectMocks
  private CarListingElasticsearchRepository carListingRepository;

  @Test
  void search_whenElasticsearchThrowsException_shouldThrowException() {
    final var criteria = mock(CarListingSearchCriteria.class);
    final var pageInformation = new PageInformation(0, 10, CHEAPEST);

    when(elasticsearchOperations.search(any(Query.class), eq(CarListingDocument.class)))
        .thenThrow(new DataAccessResourceFailureException("Test Exception"));

    assertThatThrownBy(() -> carListingRepository.search(criteria, pageInformation))
        .isInstanceOf(DataAccessResourceFailureException.class);

    verify(elasticsearchOperations, times(1)).search(any(Query.class),
        eq(CarListingDocument.class));
  }

  @Test
  void recover_whenCacheIsEmpty_returnEmptyPage() {
    final var exception = mock(DataAccessResourceFailureException.class);
    final var criteria = mock(CarListingSearchCriteria.class);
    final var pageInformation = new PageInformation(0, 10, CHEAPEST);
    final var cache = mock(Cache.class);

    when(cacheManager.getCache(eq(CacheNames.CAR_LISTINGS))).thenReturn(cache);

    final var result = carListingRepository.recover(exception, criteria, pageInformation);

    assertThat(result).isNotNull();
    assertThat(result.page()).isEqualTo(pageInformation);
    assertThat(result.total()).isEqualTo(0);
    assertThat(result.content()).isEmpty();
  }

}
