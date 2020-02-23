package com.hoolai.choosewife.entity;

public class RoleInfo {

    private int id;
    private String name;
    private int shareholders;
    private double sharePrice;
    private double todayGains;

    public RoleInfo(int id, String name, int shareholders, double sharePrice, double todayGains) {
        this.id = id;
        this.name = name;
        this.shareholders = shareholders;
        this.sharePrice = sharePrice;
        this.todayGains = todayGains;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShareholders(int shareholders) {
        this.shareholders = shareholders;
    }

    public void setSharePrice(float sharePrice) {
        this.sharePrice = sharePrice;
    }

    public void setTodayGains(float todayGains) {
        this.todayGains = todayGains;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getShareholders() {
        return shareholders;
    }

    public double getSharePrice() {
        return sharePrice;
    }

    public double getTodayGains() {
        return todayGains;
    }

    @Override
    public String toString() {
        return "RoleInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shareholders=" + shareholders +
                ", sharePrice=" + sharePrice +
                ", todayGains=" + todayGains +
                '}';
    }
}
