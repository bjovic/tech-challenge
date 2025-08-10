# techchallenge

This is a Spring Boot application for the tech challenge.

## Running the Application with Docker

The easiest way to run the application and all its dependencies is by using the provided `docker-compose.yaml` file. This will start the application along with Kafka, Zookeeper, Elasticsearch, and Redis.

### Prerequisites

- Docker and Docker Compose must be installed on your system.

### Steps

1.  **Build and Start Services:**
    Open a terminal in the project root and run the following command:
    ```bash
    docker-compose up --build -d
    ```
    This command will build the Docker image for the application and start all the services in detached mode.

2.  **Accessing the Application:**
    Once everything is running, the application will be accessible at [http://localhost:8080/api/car-listings](http://localhost:8080/api/car-listings).

3.  **Stopping the Services:**
    To stop all the running containers, use the following command:
    ```bash
    docker-compose down
    ```


## API Documentation

The API documentation is available through Swagger UI. Once the application is running, you can access the Swagger UI at the following URL:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

The OpenAPI specification is available at:

[http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Testing the App

To test the application, you can produce a message to the `car_listings` Kafka topic.

1.  Open your web browser and navigate to the Kafka UI at [http://localhost:3030](http://localhost:3030).
2.  In the Kafka UI, navigate to the **Topics** section and select the `car_listings` topic.
3.  Go to the **Produce** tab and paste the following JSON message into the **Value** field:

```json
{
  "type": "CREATE",
  "listing": {
    "id": "3N1BC13E99L480541",
    "make": "honda",
    "model": "civic",
    "year": 2019,
    "price": 20000,
    "mileage": 137000
  }
}
```

4.  Click the **Produce** button to send the message to the Kafka topic.
5.  You can then use the `/api/car-listings` endpoint to search for the newly created car listing. For example, you can use the following `curl` command to search for the car:

```bash
curl http://localhost:8080/api/car-listings?make=honda
```

## TODO

- **Implement OAuth2 Security (Authorization Code Flow with JWT)**
    - Integrate Spring Security OAuth2 to secure API endpoints.
    - Use **Authorization Code flow** to authenticate users via an external identity provider (e.g., Keycloak, Auth0, AWS Cognito).
    - Issue **JWT tokens** for access and refresh.
  
- **Rate Limit API**
    - Use **Bucket4j** for rate limiting on endpoints.
    - Store rate limit counters in Redis for distributed enforcement across instances.
    - Return appropriate HTTP status (e.g., `429 Too Many Requests`) when limit is exceeded.
    - Add configuration properties to easily adjust rate limits per endpoint.
