package de.viadee.parkhaus.manager.entity;

public class Payment {

   String id;
   Double payment;

    public Payment(String id, Double payment) {
        this.id = id;
        this.payment = payment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }
}
