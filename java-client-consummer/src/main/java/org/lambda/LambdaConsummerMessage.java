package org.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dto.TransaccionResponse;
import com.amazonaws.services.lambda.runtime.events.KafkaEvent;
import service.TransaccionService;

import java.util.Base64;

public class LambdaConsummerMessage implements  RequestHandler <KafkaEvent, TransaccionResponse> {

    @Override
    public TransaccionResponse handleRequest(KafkaEvent event, Context context) {

        TransaccionResponse response = new TransaccionResponse();
        event.getRecords().forEach((topic, records) -> {
            records.forEach(record -> {

                String decodedMessage = new String(
                        Base64.getDecoder().decode(record.getValue())
                );

                System.out.println("Evento recibido desde Kafka:");

                System.out.println(decodedMessage);
                TransaccionService service = new TransaccionService();
                String responseFraude =  service.TransaccionFraude(decodedMessage);
                response.setMessage(responseFraude);
                response.setStatus(200);
                response.setError("");
            });
        });
        return response;
    }
}
