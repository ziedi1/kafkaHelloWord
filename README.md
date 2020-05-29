# kafkaHelloWord
We will be installing Kafka on our local machine using docker and docker compose and creating a simple Kafka Producer in Java.

1-Environment Setup:
    Java 1.8 and Scala 2.10 should be installed on your machine.
    You should have docker and docker compose on your machine.
    
2- Create instance of Kafka and Zookeeper using docker image:
    Creating the docker compose file you will find it in the project.
    Go to the folder where docker-compose.yml file is present.
    use the below command to run the services in foreground mode.
      sudo docker-compose -f docker-compose.yml up -d
     
3- Create topic and Consuming messages form the topic:
    Create topic:
      sudo docker exec -it kafka /bin/sh
      cd /opt/kafka_2.12-2.5.0
    
     ./bin/kafka-topics.sh --create --zookeeper <zookeeper-host:port> --replication-factor 1 --partitions 1 --topic <topic-name>
      In our case:
    ./bin/kafka-topics.sh --create --zookeeper "zookeeper:2181" --replication-factor 1 --partitions 1 --topic first_topic
    
  Consuming messages form the topic:
      ./bin/kafka-console-consumer.sh --bootstrap-server <kafka-host:port> --topic <topic-name> --from-beginning
    In our case:
      ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --from-beginning
  
4-Run the Runner class
    
    
    
