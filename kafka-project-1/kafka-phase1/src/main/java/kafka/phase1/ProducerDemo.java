package kafka.phase1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {
    public static void main(String[] args) {
        String bootStrapServers = "127.0.0.1:9092";
        final Logger logger = LoggerFactory.getLogger(ProducerDemo.class);

        // Create Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());


        // Create The Producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);



        // Send Data
        for(int i = 0; i<10;i++) {
            //Create a Producer Record
            ProducerRecord<String,String> record = new ProducerRecord<String, String>("javatopic","new message "+Integer.toString(i));
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        logger.info("Topic: " + recordMetadata.topic() + "\n" +
                                "Partition: " + recordMetadata.partition() + "\n" +
                                "Offset: " + recordMetadata.offset() + "\n" +
                                "Timestamp: " + recordMetadata.timestamp());
                    } else {
                        logger.error("Error while producing: ", e);
                    }

                }
            });

        }
        producer.flush();
        producer.close();
    }
}
