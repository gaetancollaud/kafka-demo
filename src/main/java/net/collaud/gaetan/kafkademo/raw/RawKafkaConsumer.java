package net.collaud.gaetan.kafkademo.raw;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("raw")
@Component
public class RawKafkaConsumer {
}
