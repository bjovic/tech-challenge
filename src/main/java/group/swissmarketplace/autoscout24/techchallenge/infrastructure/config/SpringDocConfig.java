package group.swissmarketplace.autoscout24.techchallenge.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

  @Value("${app.swagger.server-url}")
  private String url;

  @Bean
  public GroupedOpenApi carListingsApi() {
    return GroupedOpenApi.builder()
        .group("car-listings")
        .pathsToMatch("/api/car-listings/**")
        .packagesToScan("group.swissmarketplace.autoscout24.techchallenge")
        .build();
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .addServersItem(new Server().url(url))
        .info(new Info()
            .title("Car Listing API")
            .version("1.0")
            .description("API for managing car listings."));
  }
}
