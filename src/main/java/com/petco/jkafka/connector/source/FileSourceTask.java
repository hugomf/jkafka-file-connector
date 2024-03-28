package com.petco.jkafka.connector.source;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

public class FileSourceTask extends SourceTask  {



    private String filename;
    private String topic;

    @Override
    public void start(Map<String, String> props) {
        filename = props.get("filename");
        topic = props.get("topic");
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
              List<SourceRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming each line of the file represents a record
                // You might need to parse the line into structured data if your file has a specific format
                Struct value = new Struct(schema()).put("data", line);
                SourceRecord record = new SourceRecord(null, null, topic, null, schema(), value);
                records.add(record);
            }
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public void stop() {
        // Clean up resources here if needed
    }

    @Override
    public String version() {
        return "1.0";
    }

    private Schema schema() {
        return SchemaBuilder.struct()
                .field("data", Schema.STRING_SCHEMA)
                .build();
    }
}
