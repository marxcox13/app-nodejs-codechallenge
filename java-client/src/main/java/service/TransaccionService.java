package service;

import dto.TransaccionRequest;
import dto.TransaccionStatus;
import models.Transaccion;
import repository.TransaccionRepository;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.Instant;
import java.util.UUID;

public class TransaccionService {

    public String SaveTransaccionService(TransaccionRequest transaccionRequest) {
        String transaccionIdUUID = UUID.randomUUID().toString();
        System.out.println("uuid creado" + transaccionIdUUID);
        Transaccion transaccion = new Transaccion();
        transaccion.setTransaccionID(transaccionIdUUID);
        transaccion.setAmmount(transaccionRequest.getAmount());
        transaccion.setPhone(transaccionRequest.getPhone());
        transaccion.setStatus(TransaccionStatus.PENDING.toString());
        transaccion.setCreateAt(Instant.now().toString());
        TransaccionRepository repository = new TransaccionRepository();
        repository.SaveTransaccion(transaccion);
        repository.SendMessageKafka(transaccion);
        return transaccionIdUUID;
    }

    public String getTransaccion() {
        System.out.println("CONSULTANDO SERVICE");
        TransaccionRepository repository = new TransaccionRepository();
        return repository.scanTransacciones();
    }
}
