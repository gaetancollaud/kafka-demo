package net.collaud.gaetan.kafkademo.raw;

import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("raw")
@Component
public class RawKafkaProducer implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(RawKafkaProducer.class);

    public static final Duration DELAY = Duration.ofSeconds(5);
    public static String KAFKA_BROKERS = "kafka:9092";
    public static String TOPIC_NAME = "raw-topic";

    private final Producer<String, String> producer;

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    public RawKafkaProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void run(String... args) throws Exception {
        scheduledExecutorService.scheduleWithFixedDelay(this::sendMessage, 0, DELAY.toMillis(), TimeUnit.MILLISECONDS);
    }

    public void sendMessage() {
        String message = Instant.now().toString();
        LOG.info("Sending message '{}'", message);

        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, message);

        try {
            producer.send(record).get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error while publishing to kafka", e);
        }
    }
}
