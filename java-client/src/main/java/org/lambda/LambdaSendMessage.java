package org.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dto.TransaccionRequest;
import dto.TransaccionResponse;
import service.TransaccionService;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.json.JSONObject;

public class LambdaSendMessage implements  RequestHandler <APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {

            // DECLARACIONES
            System.out.println("REQUEST ORIGINAL" + request);
            JSONObject requestJson = new JSONObject();
            requestJson.put("headers", request.getHeaders());
            requestJson.put("body", request.getBody());
            JSONObject responseJson = new JSONObject();
            TransaccionService service = new TransaccionService();

            System.out.println("REQUEST" + requestJson);
            JSONObject headers = requestJson.optJSONObject("headers");
            System.out.println("REQUEST" + headers);
            String method = headers.optString("httpmethod", null);
            System.out.println("METODO: "+ method);

            if (method == null) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(400)
                        .withBody("{\"error\":\"httpMethod es null\"}");
            }
            // REDIRECCION POST/GET
            switch (method.toUpperCase()) {

                case "POST":
                    String body = requestJson.optString("body", null);
                    if (body == null || body.isEmpty()) {
                        return new APIGatewayProxyResponseEvent()
                                .withStatusCode(400)
                                .withBody("{\"error\":\"El body de la solicitud está vacío\"}");
                    }
                    JSONObject json = new JSONObject(body);
                    System.out.println("IN POST "+ json);
                    TransaccionRequest transaccion = new TransaccionRequest();
                    transaccion.setPhone(json.getInt("phone"));
                    transaccion.setAmount(json.getDouble("amount"));
                    responseJson.put("message", "mensaje enviado");
                    responseJson.put("status", 200);
                    responseJson.put("body", "TRANSACCION: " + service.SaveTransaccionService(transaccion)) ;
                    break;
                case "GET":
                    System.out.println("IN GET");
                    String getTransaccion = service.getTransaccion();
                    responseJson.put("message", "mensaje enviado");
                    responseJson.put("status", 200);
                    responseJson.put("body", getTransaccion);
                    break;

                default:
                    System.out.println("Metodo no soportado: " + method);
                    return new APIGatewayProxyResponseEvent()
                            .withStatusCode(405)
                            .withBody("Metodo no permitido");
            }

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(responseJson.toString());

        } catch (Exception e) {
            System.out.println("ERROR IN LAMBDA" +e);
            e.printStackTrace();

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("{\"error\":\"Error procesando la transacción\"}");
        }
    }
}
