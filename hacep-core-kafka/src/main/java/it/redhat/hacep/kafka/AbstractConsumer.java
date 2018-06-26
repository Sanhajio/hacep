package it.redhat.hacep.kafka;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import it.redhat.hacep.HACEP;
import it.redhat.hacep.configuration.KafkaConfiguration;

public abstract class AbstractConsumer implements Runnable {

    private final HACEP hacep;
    
    private final KafkaConfiguration kafkaConfiguration;

    private final Properties kafkaProperties = new Properties();

    protected final AtomicBoolean closed = new AtomicBoolean(false);

    public AbstractConsumer(KafkaConfiguration kafkaConfiguration, HACEP hacep) {
        this.kafkaConfiguration = kafkaConfiguration;
        this.hacep = hacep;
        setProperties(kafkaProperties);
    }

    protected void setProperties(Properties properties) {
        properties.put("bootstrap.servers", kafkaConfiguration.getBootstrapServers());
        properties.put("enable.auto.commit", kafkaConfiguration.getAutoCommitEnabled());
        properties.put("group.id", kafkaConfiguration.getGroupId());
        properties.put("auto.commit.interval.ms", kafkaConfiguration.getCommitInverval());
        properties.put("key.deserializer", kafkaConfiguration.getKeyDeserializer());
        properties.put("value.deserializer", kafkaConfiguration.getValueDeserializer());
    }

    public KafkaConfiguration getKafkaConfiguration() {
        return kafkaConfiguration;
    }

    public Properties getProperties() {
        return kafkaProperties;
    }

    public HACEP getHacep() {
        return hacep;
    }

}

