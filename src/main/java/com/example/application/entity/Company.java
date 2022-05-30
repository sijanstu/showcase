package com.example.application.entity;

import java.io.Serializable;

/**
 * @author Sijan
 */
public class Company implements Serializable {

    String published_date;
    String open;
    String high;
    String low;
    String close;
    String per_change;
    String traded_quantity;
    String traded_amount;
    String status;

    public Company() {

    }

    public Company(String published_date, String close) {
        this.published_date = published_date;
        this.close = close;
    }

    public Company(String published_date, String open, String high, String low, String close, String per_change, String traded_quantity, String traded_amount, String status) {
        this.published_date = published_date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.per_change = per_change;
        this.traded_quantity = traded_quantity;
        this.traded_amount = traded_amount;
        this.status = status;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getPer_change() {
        return per_change;
    }

    public void setPer_change(String per_change) {
        this.per_change = per_change;
    }

    public String getTraded_quantity() {
        return traded_quantity;
    }

    public void setTraded_quantity(String traded_quantity) {
        this.traded_quantity = traded_quantity;
    }

    public String getTraded_amount() {
        return traded_amount;
    }

    public void setTraded_amount(String traded_amount) {
        this.traded_amount = traded_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Company{" + "published_date=" + published_date + ", open=" + open + ", high=" + high + ", low=" + low + ", close=" + close + ", per_change=" + per_change + ", traded_quantity=" + traded_quantity + ", traded_amount=" + traded_amount + ", status=" + status + '}';
    }

}
