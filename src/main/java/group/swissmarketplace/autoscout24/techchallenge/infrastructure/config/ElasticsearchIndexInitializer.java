package group.swissmarketplace.autoscout24.techchallenge.infrastructure.config;

import group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch.document.CarListingDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBooleanProperty(name = "app.elasticsearch.index-initializer.enabled")
public class ElasticsearchIndexInitializer implements ApplicationRunner {

  private final ElasticsearchOperations elasticsearchOperations;

  @Override
  public void run(ApplicationArguments args) {
    createIndicesIfNotExists();
  }

  private void createIndicesIfNotExists() {
    try {
      waitForElasticsearch();
      createVehicleIndex();
      log.info("All Elasticsearch indices have been initialized successfully");
    } catch (Exception e) {
      log.error("Failed to initialize Elasticsearch indices", e);
    }
  }

  private void waitForElasticsearch() {
    int maxRetries = 30;
    int retryCount = 0;

    while (retryCount < maxRetries) {
      try {
        IndexOperations indexOps = elasticsearchOperations.indexOps(CarListingDocument.class);
        indexOps.exists();
        log.info("Elasticsearch is available");
        return;
      } catch (Exception e) {
        retryCount++;
        log.warn("Waiting for Elasticsearch to be available... Attempt {}/{}", retryCount,
            maxRetries);
        try {
          Thread.sleep(2000);
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
          throw new RuntimeException("Interrupted while waiting for Elasticsearch", ie);
        }
      }
    }
    throw new RuntimeException("Elasticsearch is not available after " + maxRetries + " attempts");
  }

  private void createVehicleIndex() {
    try {
      IndexOperations indexOps = elasticsearchOperations.indexOps(CarListingDocument.class);

      if (indexOps.exists()) {
        log.info("CarListingDocument index already exists");
        return;
      }

      log.info("Creating CarListingDocument index...");

      boolean created = indexOps.create();

      if (created) {
        indexOps.putMapping();
        log.info("CarListingDocument index created successfully");
      } else {
        log.warn("CarListingDocument index creation returned false");
      }

    } catch (Exception e) {
      log.error("Error creating CarListingDocument index", e);
    }
  }

}