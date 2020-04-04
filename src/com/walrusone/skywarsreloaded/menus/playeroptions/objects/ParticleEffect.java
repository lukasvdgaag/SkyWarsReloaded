package com.walrusone.skywarsreloaded.menus.playeroptions.objects;

public class ParticleEffect {
    private String type;
    private float offsetYL;
    private float offsetYU;
    private float data;
    private int amountU;
    private int amountL;

    public ParticleEffect(String type, float offsetYL, float offsetYU, float data, int amountU, int amountL) {
        setType(type);
        setOffsetYL(offsetYL);
        setOffsetYU(offsetYU);
        setData(data);
        setAmountL(amountL);
        setAmountU(amountU);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

    public float getOffsetYL() {
        return offsetYL;
    }

    private void setOffsetYL(float offsetYL) {
        this.offsetYL = offsetYL;
    }

    public float getOffsetYU() {
        return offsetYU;
    }

    private void setOffsetYU(float offsetYU) {
        this.offsetYU = offsetYU;
    }

    public int getAmountU() {
        return amountU;
    }

    private void setAmountU(int amountU) {
        this.amountU = amountU;
    }

    public int getAmountL() {
        return amountL;
    }

    private void setAmountL(int amountL) {
        this.amountL = amountL;
    }
}
