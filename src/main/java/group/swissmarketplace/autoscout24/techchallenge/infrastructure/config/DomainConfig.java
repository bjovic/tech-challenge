package group.swissmarketplace.autoscout24.techchallenge.infrastructure.config;

import group.swissmarketplace.autoscout24.techchallenge.domain.in.CarListingService;
import group.swissmarketplace.autoscout24.techchallenge.domain.out.CarListingRepository;
import group.swissmarketplace.autoscout24.techchallenge.domain.service.DomainCarListingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

  @Bean
  public CarListingService domainCarListingService(
      final CarListingRepository carListingRepository
  ) {
    return new DomainCarListingService(carListingRepository);
  }

}
