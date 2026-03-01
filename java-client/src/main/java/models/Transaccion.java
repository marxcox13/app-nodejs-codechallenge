package models;

public class Transaccion {

    public String getTransaccionID() {
        return transaccionID;
    }

    public void setTransaccionID(String transaccionID) {
        this.transaccionID = transaccionID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmmount() {
        return ammount;
    }

    public void setAmmount(Double ammount) {
        this.ammount = ammount;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Transaccion(String transaccionID, String status, Double ammount, String createAt, Integer phone) {
        this.transaccionID = transaccionID;
        this.status = status;
        this.ammount = ammount;
        this.createAt = createAt;
        this.phone = phone;
    }

    private String transaccionID;
    private String status;
    private Double ammount;
    private String createAt;
    private Integer phone;

    public Transaccion() {
    }
}
