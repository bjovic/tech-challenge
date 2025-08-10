# techchallenge

This is a Spring Boot application for the tech-challenge.

## Running the Application with Docker

The easiest way to run the application and all its dependencies is by using the provided `docker-compose.yaml` file. This will start the application along with Kafka, Zookeeper, Elasticsearch.

### Prerequisites

- Docker and Docker Compose must be installed on your system.

### Steps

1.  **Build and Start Services:**
    Open a terminal in the project root and run the following command:
    ```bash
    docker-compose up --build -d
    ```
    This command will build the Docker image for the application and start all the services in detached mode.

2.  **Stopping the Services:**
    To stop all the running containers, use the following command:
    ```bash
    docker-compose down
    ```

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

