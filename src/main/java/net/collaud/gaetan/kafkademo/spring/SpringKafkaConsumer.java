package net.collaud.gaetan.kafkademo.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Profile("spring")
@Component
public class SpringKafkaConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(SpringKafkaConsumer.class);

    @KafkaListener(topics = "spring-topic")
    public void processMessage(String content, Acknowledgment ack) {
        LOG.info("Message received : '{}'", content);
    }
}
