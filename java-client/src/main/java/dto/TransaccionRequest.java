package dto;

public class TransaccionRequest {
    private String ID;
    private Integer phone;
    private Double amount;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }



    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public TransaccionRequest(String ID, Integer phone, Double amount) {
        this.ID = ID;
        this.phone = phone;
        this.amount = amount;
    }

    public TransaccionRequest() {
    }
}
