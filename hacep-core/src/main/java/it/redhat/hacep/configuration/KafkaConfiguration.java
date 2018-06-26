package it.redhat.hacep.configuration;

public interface KafkaConfiguration {

    String getBootstrapServers();

    String getAutoCommitEnabled();

    String getGroupId();

    String getCommitInverval();

    String getKeyDeserializer();

    String getValueDeserializer();
}
