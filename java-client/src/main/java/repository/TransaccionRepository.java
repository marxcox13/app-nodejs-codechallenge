package repository;
import models.Transaccion;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import org.apache.kafka.clients.producer.*;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.Map;


public class TransaccionRepository {
    private DynamoDbClient clientDb;
    public TransaccionRepository() {
        DynamoDbClient dynamoDb = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();

        clientDb = dynamoDb;
    }

    public void SaveTransaccion (Transaccion transaccion) {
        System.out.println("id por creearse"+ transaccion.getTransaccionID());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("transaccion")
                .item(Map.of(
                        "transaccionID", AttributeValue.builder().s(transaccion.getTransaccionID()).build(),
                        "status", AttributeValue.builder().s(transaccion.getStatus()).build(),
                        "amount", AttributeValue.builder().s(transaccion.getAmmount().toString()).build(),
                        "phone", AttributeValue.builder().n(transaccion.getPhone().toString()).build(),
                        "createAt", AttributeValue.builder().s(transaccion.getCreateAt()).build()

                )).build();
        clientDb.putItem(putItemRequest);
        System.out.println("id CREADO"+ transaccion.getTransaccionID());

    }

    public void SendMessageKafka (Transaccion transaccion) {

        String brokersConfig = System.getenv("BROKERS_CONFIG");
        String topicConfig = System.getenv("TOPIC_CONFIG");
        System.out.println("intentando conectar a MSK..." + brokersConfig);
        KafkaProducer<String, String> producer =
                MskProducer.create(brokersConfig);

        System.out.println("id por enviarse"+ transaccion.getTransaccionID());

        String event = String.format("""
        {
          "transaccionID": "%s",
          "status": "%s",
          "amount": %s,
          "phone": %s,
          "createAt": "%s"
        }
        """, transaccion.getTransaccionID(), transaccion.getStatus(), transaccion.getAmmount(), transaccion.getPhone(), transaccion.getCreateAt());

        ProducerRecord<String, String> record =
                new ProducerRecord<>(  topicConfig, //"transaction-topic",
                        transaccion.getTransaccionID(),
                        event);

        try {
            RecordMetadata metadata = producer.send(record).get();
            System.out.println("topic" + metadata.topic());
            System.out.println("partition" + metadata.partition());
            System.out.println("offset" + metadata.offset());
            System.out.println("id enviado"+ transaccion.getTransaccionID());

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            producer.close();
        }
        System.out.println("evento enviado");
    }

    public String scanTransacciones() {

        ScanRequest scanRequest = ScanRequest.builder()
                .tableName("transaccion")
                .build();
        ScanResponse response = clientDb.scan(scanRequest);

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < response.items().size(); i++) {

            Map<String, AttributeValue> item = response.items().get(i);

            json.append("{")
                    .append("\"transaccionID\":\"").append(item.get("transaccionID").s()).append("\",")
                    .append("\"status\":\"").append(item.get("status").s()).append("\",")
                    .append("\"phone\":").append(item.get("phone").n()).append(",")
                    .append("\"amount\":").append(item.get("amount").n())
                    .append("\"createAt\":").append(item.get("createAt").s())
                    .append("}");

            if (i < response.items().size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }


}
