/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kafkahelloworld;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


/**
 *
 * @author ziedi
 */
public class SampleProducer {
    public SampleProducer()  {
        Properties properties = new Properties();
        properties.put("bootstrap.servers",  "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        ProducerRecord producerRecord = new ProducerRecord("channel", "name", "selftuts2");

        KafkaProducer kafkaProducer = new KafkaProducer<>(properties);
        kafkaProducer.send(producerRecord);
        kafkaProducer.close();
    }
    
}
