# Apache Kafka File Connector

This Example will show how can to create a Kafka connector using docker compose



## Compile (build the jar file)

```
     ./gradlew clean shadowJar
```



## Verify the Plugin connector is loaded 

```
 curl localhost:8083/connector-plugins | jq  
```

If the Connector is properly loaded you will get the following output

![alt text](images/list-connector-plugins.png)

As you can see we will able to se both: *FileSinkConnector* and *FileSouceConnector*


> [!NOTE]  
> If you are getting `curl: (56) Recv failure: Connection reset by peer` your kafka connector might not be ready yet, wait few more minutes


## Create the topic
In a browser, type: `localhost:8080`, and then go to the topics Section, and click on the top left button **"Add a Topic"**
Enter the following:

Topic Name: "TEST_TOPIC"
Number of Partitions: 1
Leave the other parametors as their defaults

Click on "Create Topic"


![alt text](images/topics.png)


## Install the connector


* verify if the connector is installed:
```
 curl -sS localhost:8083/connectors | jq
```
![alt text](images/connector-empty.png)


* FileSinkConnector install

```
curl -sS -X POST -H "Content-Type: application/json" --data @./connector-sink.json localhost:8083/connectors | jq
```

![alt text](images/connector-sink.png)

* Runit again

```
 curl -sS localhost:8083/connectors | jq
```
![alt text](images/connector-sink-installed.png)


## Test the Kafka Connector

When the connector is installed, automatically will start to consume the messages from the topic

In order to see what messages are being consumed, you can display the contents of the file located inside the kafka connector container in the /connectors folder as follows

```
docker exec file-connect tail -f /connectors/file.txt 
```
To produce test messages, open another shell window and run the `com.petco.jkafka.connector.MessageProducer.java` as follows:

```
./gradlew runProducer
```

You will be able to see the produced mesages in the file:

![alt text](images/file-content.png)

It is possible to use **kafka-ui** to produce more messages, open a browser and type `localhost:8080`, go to the Topics section and select "TEST_TOPIC"
click on **"Produce Message"** a popup will appear, 

![alt text](images/produce-message.png)

Type a Value and click **Produce Message**
This is the result
![alt text](images/other-message.png)
