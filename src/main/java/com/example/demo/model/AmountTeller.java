package com.example.demo.model;

import javax.persistence.*;

@Entity
//defining class name as Table name
@Table(name="amount_teller")
public class AmountTeller {
    @Id
    @Column(name = "serial_no")
    Integer serialNo;
    @Column(name = "final_amount")
    Integer finalAmount;
    @Column(name = "created")
    private String created;
    @Column(name = "updated")
    private String updated;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Integer finalAmount) {
        this.finalAmount = finalAmount;
    }


}
