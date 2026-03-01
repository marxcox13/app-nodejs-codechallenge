package repository;

import models.Transaccion;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.util.Map;


public class TransaccionRepository {
    private DynamoDbClient clientDb;
    public TransaccionRepository() {
        DynamoDbClient dynamoDb = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();

        clientDb = dynamoDb;
    }

    public void UpdateTransaccion (Transaccion transaccion) {
        System.out.println("TRANSACCION POR ACTUALIZAR"+ transaccion.getTransaccionID());
        System.out.println("ESTADO"+ transaccion.getStatus());

        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName("transaccion")
                .key(Map.of(
                        "transaccionID", AttributeValue.builder()
                                .s(transaccion.getTransaccionID())
                                .build(),
                        "phone", AttributeValue.builder()
                                .n(String.valueOf(transaccion.getPhone()))
                                .build()
                ))
                .updateExpression("SET #st = :nuevoEstado")
                .expressionAttributeNames(Map.of(
                        "#st", "status"
                ))
                .expressionAttributeValues(Map.of(
                        ":nuevoEstado", AttributeValue.builder()
                                .s(transaccion.getStatus())
                                .build()
                ))
                .build();

        clientDb.updateItem(updateRequest);
        System.out.println("TRANSACCION POR ACTUALIZAR"+ transaccion.getTransaccionID());
        System.out.println("ESTADO"+ transaccion.getStatus());
        System.out.println("STATUS ACTUALIZADO" + transaccion.getStatus());
    }

}
