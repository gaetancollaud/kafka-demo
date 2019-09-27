package net.collaud.gaetan.kafkademo.spring;


import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Profile("spring")
@Component
public class SpringKafkaProducer implements CommandLineRunner {

    private static final Duration DELAY = Duration.ofSeconds(5);
    private static final Logger LOG = LoggerFactory.getLogger(SpringKafkaProducer.class);

    @Autowired
    private ScheduledExecutorService executorService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void run(String... args) throws Exception {
        executorService.scheduleWithFixedDelay(this::sendMessage, 0, DELAY.toMillis(), TimeUnit.MILLISECONDS);
    }

    public void sendMessage() {
        String message = Instant.now().toString();
        LOG.info("Sending message '{}'", message);
        try {
            this.kafkaTemplate.send("spring-topic", message).get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error while publishing to kafka", e);
        }
    }

}
