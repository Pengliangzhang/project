package com.example.amusementpark;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Ticket implements Serializable {
    private String Type;
    private String Date;
    private String Price;

    public Ticket(String type, String date, String price) {
        Type = type;
        Date = date;
        Price = price;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
