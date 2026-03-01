package repository;

import software.amazon.awssdk.services.kafka.KafkaClient;
import software.amazon.awssdk.services.kafka.model.*;

public class Mskbroker {

    public static String getBrokers(String clusterArn) {

        KafkaClient client = KafkaClient.create();

        GetBootstrapBrokersResponse response =
                client.getBootstrapBrokers(
                        GetBootstrapBrokersRequest.builder()
                                .clusterArn(clusterArn)
                                .build()
                );

        return response.bootstrapBrokerStringSaslIam();
    }
}
