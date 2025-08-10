package group.swissmarketplace.autoscout24.techchallenge.integration;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {

    public static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer("redis:6.2.6-alpine").withExposedPorts(6379);
    public static final ConfluentKafkaContainer KAFKA_CONTAINER = new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.8.1")).withExposedPorts(9092);
    public static final GenericContainer<?> ELASTICSEARCH_CONTAINER = new GenericContainer<>(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.14.3"))
        .withExposedPorts(9200)
        .withEnv("discovery.type", "single-node")
        .withEnv("xpack.security.enabled", "false");

    static {
        REDIS_CONTAINER.start();
        KAFKA_CONTAINER.start();
        ELASTICSEARCH_CONTAINER.start();
    }

    @Autowired
    protected WebTestClient webTestClient;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", () -> ELASTICSEARCH_CONTAINER.getHost() + ":" + ELASTICSEARCH_CONTAINER.getFirstMappedPort());
        registry.add("spring.cloud.stream.kafka.binder.brokers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort);
        registry.add("spring.data.redis.ssl.enabled", () -> false);
    }
}
