package com.azteckoder.jkafka.connector.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileSourceConnector extends SourceConnector  {

     private static final Logger logger = LoggerFactory.getLogger(FileSourceConnector.class);

    public static final String TOPIC_CONFIG = "topic";
    public static final String FILENAME_CONFIG = "filename";

    private String filename;
    private String topic;

    @Override
    public void start(Map<String, String> props) {
        filename = props.get(FILENAME_CONFIG);
        topic = props.get(TOPIC_CONFIG);
        if (filename == null || topic == null) {
            throw new ConnectException("Missing filename or topic configuration");
        }
    }

    @Override
    public Class<? extends Task> taskClass() {
        return FileSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> configs = new ArrayList<>();
        Map<String, String> config = new HashMap<>();
        config.put(FILENAME_CONFIG, filename);
        config.put(TOPIC_CONFIG, topic);
        for (int i = 0; i < maxTasks; i++) {
            configs.add(config);
        }
        return configs;
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
            .define(TOPIC_CONFIG, Type.STRING, Importance.HIGH, "The Kafka topic to consume from");
    }

    @Override
    public String version() {
        return "1.0";
    }
    
}
