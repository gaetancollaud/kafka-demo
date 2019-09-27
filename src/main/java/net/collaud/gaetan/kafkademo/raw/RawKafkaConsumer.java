package net.collaud.gaetan.kafkademo.raw;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("raw")
@Component
public class RawKafkaConsumer implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(RawKafkaConsumer.class);

    public static String KAFKA_BROKERS = "kafka:9092";
    public static String TOPIC_NAME = "raw-topic";
    public static String GROUP_ID_CONFIG = "spring-consumer-group";
    public static String OFFSET_RESET = "earliest";
    public static Integer MAX_POLL_RECORDS = 10;

    private final Consumer<String, String> consumer;
    private boolean running = true;

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    public RawKafkaConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OFFSET_RESET);
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));
    }

    @Override
    public void run(String... args) throws Exception {
        scheduledExecutorService.schedule(this::runConsumer, 0, TimeUnit.MILLISECONDS);
    }

    private void runConsumer() {
        while (running) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));

            if (consumerRecords.count() > 0) {
                consumerRecords.forEach(record -> {
                    LOG.info("Record received: '{}'", record.value());
                });

                // commits the offset of record to broker.
                consumer.commitAsync();
            }
        }
        consumer.close();
    }
}
