package com.sbitbd.deshi_express.model;

public class return_model {
    String id,return_date,customer_name,invoice;

    public return_model(String id, String return_date, String customer_name, String invoice) {
        this.id = id;
        this.return_date = return_date;
        this.customer_name = customer_name;
        this.invoice = invoice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}
