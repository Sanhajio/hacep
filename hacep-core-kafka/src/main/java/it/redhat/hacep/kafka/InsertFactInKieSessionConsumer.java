package it.redhat.hacep.kafka;

import java.util.Arrays;

import it.redhat.hacep.HACEP;
import it.redhat.hacep.cache.session.KieSessionSaver;
import it.redhat.hacep.configuration.KafkaConfiguration;
import it.redhat.hacep.model.Fact;
import it.redhat.hacep.model.Key;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertFactInKieSessionConsumer extends AbstractConsumer {

    private final static Logger LOGGER = LoggerFactory.getLogger(InsertFactInKieSessionConsumer.class);

    private final KafkaConsumer<Key, Fact> consumer;

    public InsertFactInKieSessionConsumer(KafkaConfiguration kafkaConfiguration, HACEP hacep) {
        super(kafkaConfiguration, hacep);
        consumer = new KafkaConsumer<>(getProperties());
    }

    // As Fact and Key are Serializable Kafka should get 
    // As Serializer and Deserializer ByteArraySerializer
    // and ByteArrayDeserializer.
    // The run method does what is in FactListenerPost
    // May result in ConcurrentModificationException
    @Override
    public void run() {
        try {
            consumer.subscribe(Arrays.asList("facts"));
            while (!closed.get()) {
                ConsumerRecords<Key, Fact> records = consumer.poll(1000);
                for (ConsumerRecord<Key, Fact> record: records) {
                    eventReceived(record.key(), record.value());
                }
            }
        } catch (WakeupException e) {
            if (!closed.get()) throw e;
        } finally {
            consumer.close();
        }
    }
    
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }

    public void eventReceived(Key key, Fact value) {
        KieSessionSaver saver = getHacep().getKieSessionSaver();
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Event received: (" + key + ", " + value + ")");

        if (isAnHACEPEvent(key, value)) {
            LOGGER.warn("Event is not HACEP compliant: (" + key + ", " + value + ")");
            return;
        }
        saver.insert(key, value);
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Chain complete for: (" + key + ", " + value + ")");

    }

    private boolean isAnHACEPEvent(Object key, Object value) {
        return !(checkClass(Fact.class, value) && checkClass(Key.class, key));
    }

    private boolean checkClass(Class<?> clazz, Object o) {
        return (o != null && clazz.isAssignableFrom(o.getClass()));
    }
}
