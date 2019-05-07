package kafka.phase1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoWithKey {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String bootStrapServers = "127.0.0.1:9092";
        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithKey.class);

        // Create Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());


        // Create The Producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);



        // Send Data
        for(int i = 0; i<10;i++) {
            String topic="javatopic";
            String key="id_"+Integer.toString(i);
            String value="bandi "+Integer.toString(i);
            //Create a Producer Record
            ProducerRecord<String,String> record = new ProducerRecord<String, String>(topic,key,value);
            logger.info("key "+key);
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
            }).get();

        }
        producer.flush();
        producer.close();
    }
}
