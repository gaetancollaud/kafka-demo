# Kafka demo

This is a simple producer/consumer demo with kafka. There are two implementations:
 * One using the raw kakfa client (profile: raw)
 * One using spring-kafka (profile: spring)

Just use the profiles `raw` and `spring` to choose the implementation.

## Running kafka

There is docker container already configured. Just use `docker-compose up -d kafka`.

## Build and run in docker

```bash
mvn clean package
docker-compose build demo
docker-compose up demo
```

You can change the profile using the environment variable in docker-compose.yml
