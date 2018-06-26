package it.redhat.hacep.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import it.redhat.hacep.HACEP;
import it.redhat.hacep.configuration.KafkaConfiguration;
import it.redhat.hacep.kafka.AbstractConsumer;
import it.redhat.hacep.kafka.InsertFactInKieSessionConsumer;
import it.redhat.hacep.kafka.LoadFactFromEntryPointConnector;
import javax.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
public class KafkaRouter {
    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaRouter.class);

    private static final String Kafka_TOPIC = "facts";

    private final List<Thread> consumerThreads;

    private final AtomicBoolean started = new AtomicBoolean(false);

    public KafkaRouter() {
        this.consumerThreads = new ArrayList<>();
    }

    public void start(KafkaConfiguration kafkaConfiguration, HACEP hacep) {
        if (started.compareAndSet(false, true)) {
            try {
                // LoadFactFromEntryPoint should be something like a connector rather than a consumer;
                //consumers.add(new LoadFactFromEntryPoint(kafkaConfiguration));
                consumerThreads.add(new Thread(new InsertFactInKieSessionConsumer(kafkaConfiguration, hacep)));
                consumerThreads.forEach((consumerThread)-> consumerThread.start());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stop() {
        if (started.compareAndSet(true, false)) {
            try {
                consumerThreads.forEach((consumerThread)->consumerThread.stop());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
