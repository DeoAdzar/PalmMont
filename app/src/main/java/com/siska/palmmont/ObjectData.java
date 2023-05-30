package com.siska.palmmont;

public class ObjectData {
    String Moisture, pH, temperature;
    boolean pump;

    public ObjectData() {
    }

    public ObjectData(String moisture, String pH, String temperature, boolean pump) {
        Moisture = moisture;
        this.pH = pH;
        this.temperature = temperature;
        this.pump = pump;
    }

    public String getMoisture() {
        return Moisture;
    }

    public void setMoisture(String moisture) {
        Moisture = moisture;
    }

    public String getpH() {
        return pH;
    }

    public void setpH(String pH) {
        this.pH = pH;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public boolean isPump() {
        return pump;
    }

    public void setPump(boolean pump) {
        this.pump = pump;
    }
}
