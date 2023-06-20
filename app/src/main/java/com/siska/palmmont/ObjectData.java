package com.siska.palmmont;

public class ObjectData {
    String moist, pH, temp;
    boolean pump;

    public ObjectData() {
    }

    public ObjectData(String moist, String pH, String temp, boolean pump) {
        this.moist = moist;
        this.pH = pH;
        this.temp = temp;
        this.pump = pump;
    }

    public String getMoist() {
        return moist;
    }

    public void setMoist(String moist) {
        this.moist = moist;
    }

    public String getpH() {
        return pH;
    }

    public void setpH(String pH) {
        this.pH = pH;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public boolean isPump() {
        return pump;
    }

    public void setPump(boolean pump) {
        this.pump = pump;
    }
}
