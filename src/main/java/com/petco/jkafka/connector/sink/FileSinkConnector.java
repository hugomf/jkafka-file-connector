package com.azteckoder.jkafka.connector.sink;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.ConfigValue;
import org.apache.kafka.connect.connector.Task;

import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileSinkConnector extends SinkConnector {

    
    private static final Logger logger = LoggerFactory.getLogger(FileSinkConnector.class);

    public static final String TOPICS_CONFIG = "topics";
    public static final String FILENAME_CONFIG = "filename";
    private Map<String, String> connectorConfig;


    @Override
    public void start(Map<String, String> props) {
         List<String> errorMessages = new ArrayList<>();
        for (ConfigValue v : config().validate(props)) {
            if (!v.errorMessages().isEmpty()) {
                errorMessages.add("Property " + v.name() + " with value " + v.value()
                        + " does not validate: " + String.join("; ", v.errorMessages()));
            }
        }
        if (!errorMessages.isEmpty()) {
            throw new ConfigException("Configuration does not validate: \n\t"
                    + String.join("\n\t", errorMessages));
        }
        connectorConfig = new HashMap<>(props);
        logger.info(hashMapToString(connectorConfig));
    }


     public static String hashMapToString(Map<String, String> map) {
        return "{" +
                map.entrySet().stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.joining(", ")) +
                "}";
    }

    @Override
    public Class<? extends Task> taskClass() {
        return FileSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        logger.info("At most {} will be started", maxTasks);
         return Collections.nCopies(maxTasks, connectorConfig);
    }

    @Override
    public void stop() {
        // Clean up resources here if needed
        logger.debug("Stop");
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef()
            .define(FILENAME_CONFIG, Type.STRING, Importance.HIGH, "The name of the file to write to")
            .define(TOPICS_CONFIG, ConfigDef.Type.LIST, ConfigDef.Importance.HIGH, "The topics to consume from");

    }

    @Override
    public String version() {
        return "1.0";
    }
}
