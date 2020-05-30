/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kafkahelloworld;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

/**
 *
 * @author ziedi
 */
public class LogConsumer {

    private final static String TOPIC = "logTest";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private static KafkaConsumer<String, String> consumer;


    private static KafkaConsumer<String, String> getConsumer(){
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        kafkaProps.put("group.id", "recordConsumerGroup");
        kafkaProps.put("max.poll.records", "10");
        kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(kafkaProps);
        return consumer;
    }

    public void subscribe(){
        consumer = getConsumer();
        consumer.subscribe(Collections.singletonList(TOPIC));
        final int giveUp = 100;
        int noRecordsCount = 0;
        long t1 = System.currentTimeMillis();
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);

                //System.out.println("No. of records="+ records.count());

                if (records.count()==0) {
                    long t2 = System.currentTimeMillis();
                    System.out.println(" time taken when number of records became 0="+ (t2-t1) + " in ms");
                    noRecordsCount++;
                    if (noRecordsCount > giveUp) break;
                    else continue;
                }

                records.forEach(record -> {
                    System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
                            record.key(), record.value(),
                            record.partition(), record.offset());
                });
                consumer.commitAsync();
            }
        } catch (Exception e) {
            System.out.println("Unexpected error"+ e);
        }finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
                long t2 = System.currentTimeMillis();
                System.out.println(" time taken ="+ (t2-t1) + " in ms");
            }
        }

    }

    public String readLastMessage(String topic, int partition){
        consumer = getConsumer();
        final long TIMEOUT = 100; // in MS
        consumer.subscribe(Collections.singletonList(topic));
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        consumer.poll(0);
        consumer.seekToEnd(Collections.singletonList(topicPartition));
        long currentOffset = consumer.position(topicPartition) -1;
        consumer.seek(topicPartition, currentOffset);
        consumer.commitSync();
        ConsumerRecords<String, String> messages = consumer.poll(TIMEOUT);
        if(messages.iterator().hasNext()){
            return messages.iterator().next().value();
        }
        return null;
    }

    public static void main(String[] args){
        LogConsumer obj = new LogConsumer();
        obj.subscribe();
        System.out.println("Last Record = "+obj.readLastMessage("test", 0));
}
    
}
