package com.siska.palmmont;

public class ObjectData {
    String moisture, ph, temp;
    boolean pump;

    public ObjectData() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public ObjectData(String moisture, String ph, String temp, boolean pump) {
        this.moisture = moisture;
        this.ph = ph;
        this.temp = temp;
        this.pump = pump;
    }

    public boolean isPump() {
        return pump;
    }

    public void setPump(boolean pump) {
        this.pump = pump;
    }

    public String getMoisture() {
        return moisture;
    }

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
