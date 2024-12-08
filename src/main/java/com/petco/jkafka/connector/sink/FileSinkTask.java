package com.azteckoder.jkafka.connector.sink;

import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class FileSinkTask extends SinkTask {

    private String filename;
    private BufferedWriter writer;

   @Override
    public void start(Map<String, String> props) {
        filename = props.get("filename");
        try {
             File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile(); // Create the file if it doesn't exist
            }
            writer = new BufferedWriter(new FileWriter(filename));
        } catch (IOException e) {
            throw new RuntimeException("Failed to open file for writing: " + filename, e);
        }
    }

    public void put(Collection<SinkRecord> records) {
        try {
            for (SinkRecord record : records) {
                writer.write(record.value().toString());
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write records to file", e);
        }
    }

    @Override
    public void stop() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while closing the file writer", e);
        }
    }

    @Override
    public String version() {
        return "1.0";
    }

    // @Override
    // public String version() {
    //     return getClass().getPackage().getImplementationVersion();
    // }
}
