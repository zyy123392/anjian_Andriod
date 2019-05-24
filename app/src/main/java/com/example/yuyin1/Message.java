package com.example.yuyin1;

import org.litepal.crud.DataSupport;

public class Message extends DataSupport {
    private String date;
    private String name;
    private  String flightId;
    private String goods;
    private int amount;
    private String way;
    private String boxer;
    private String starter;
    private String remark;

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public int getAmount() {
        return amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setBoxer(String boxer) {
        this.boxer = boxer;
    }

    public void setStarter(String starter) {
        this.starter = starter;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getGoods() {
        return goods;
    }

    public String getBoxer() {
        return boxer;
    }

    public String getStarter() {
        return starter;
    }

    public String getRemark() {
        return remark;
    }
}
