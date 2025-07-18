📊 Hashtag Analytics Service

This project is a microservice-based hashtag analytics system built using Spring Boot, Apache Kafka, Redis and Docker.

It allows users to submit Instagram-like captions containing hashtags, and tracks the frequency of those hashtags in real time using Kafka streaming.

🚀 Tech Stack

Java 17

Spring Boot (Web + Kafka)

Apache Kafka + Zookeeper

Redis (for persistent analytics)

Docker & Docker Compose

Postman (for testing APIs)

🧱 Architecture

Caption Service (Producer) --> Kafka Topic "captions" --> Hashtag Service (Consumer)

caption-service: Accepts user captions via REST, extracts hashtags, and publishes messages to Kafka.

hashtag-service: Consumes hashtag events from Kafka, aggregates counts, and exposes analytics APIs.

🐳 Docker Setup

Build & Run the System

From root folder (hashtag-analytics-service/)

docker-compose build

docker-compose up

This will:

Start Kafka, Zookeeper, and Redis

Build and run both services (caption-service, hashtag-service)

🥪 API Usage (via Postman)

🔹 1. Submit a Caption

Request

POST http://localhost:8080/captions

Body (JSON)

{
  "caption": "Exploring the #oceans and #sun today!"
}

Response

Caption received and hashtags sent to Kafka.

🔹 2. Get Top Hashtags

Request

GET http://localhost:8081/analytics/top-hashtags

Response

[
  {
    "hashtag": "sun",
    "count": 2
  },
  {
    "hashtag": "mountains",
    "count": 1
  },
  {
    "hashtag": "oceans",
    "count": 1
  }
]

🔹 3. Get Top Hashtags (Redis-Persistent)

Request

GET http://localhost:8081/analytics/v2/top-hashtags

Response

[
  {
    "hashtag": "sun",
    "count": 4
  },
  {
    "hashtag": "beach",
    "count": 2
  },
  {
    "hashtag": "moon",
    "count": 2
  }
]

📂 Project Structure

hashtag-analytics-service/
|── docker-compose.yml
|── caption-service/         # Spring Boot app to produce captions
|── hashtag-service/         # Spring Boot app to consume and analyze hashtags

✅ Features

Real-time hashtag tracking

Kafka-based stream processing

Scalable microservice architecture

Easily testable with Postman

📜 TODO (optional extensions)

Track hashtags over time windows

Build a frontend dashboard
