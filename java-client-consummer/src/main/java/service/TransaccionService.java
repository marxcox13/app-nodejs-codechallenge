package service;

import dto.TransaccionRequest;
import dto.TransaccionStatus;
import models.Transaccion;
import org.json.JSONObject;
import repository.TransaccionRepository;

import java.time.Instant;
import java.util.UUID;

public class TransaccionService {

    public String TransaccionFraude(String message) {

        JSONObject json = new JSONObject(message);
        Transaccion transaccion = new Transaccion();

        transaccion.setTransaccionID(json.getString("transaccionID"));
        transaccion.setStatus(json.getString("status"));
        transaccion.setAmmount(json.getDouble("amount"));
        transaccion.setPhone(json.getInt("phone"));
        transaccion.setCreateAt(json.getString("createAt"));
        transaccion.setStatus(TransaccionStatus.APPROVED.toString());

        if (transaccion.getAmmount().compareTo(new Double("1000")) > 0) {
            transaccion.setStatus(TransaccionStatus.REJECTED.toString());
        }
        TransaccionRepository repository = new TransaccionRepository();
        System.out.println("TRANSACCION POR ACTUALIZAR"+ transaccion.getTransaccionID());
        System.out.println("ESTADO"+ transaccion.getStatus());
        repository.UpdateTransaccion(transaccion);
        return "Transaccion finished status:" + transaccion.getStatus();
    }
}
