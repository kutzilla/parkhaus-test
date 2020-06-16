package de.viadee.parkhaus.manager.entity;

import javax.persistence.Id;

public class Payment {

    @Id
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
