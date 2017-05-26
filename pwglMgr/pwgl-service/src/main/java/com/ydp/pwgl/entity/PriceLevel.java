package com.ydp.pwgl.entity;

/**
 * Created by john on 2017/4/27.
 */
public class PriceLevel {
    private Integer pricelevelid;
    private String  pricelevelname;
    private String  color;

    public Integer getPricelevelid() {
        return pricelevelid;
    }

    public void setPricelevelid(Integer pricelevelid) {
        this.pricelevelid = pricelevelid;
    }

    public String getPricelevelname() {
        return pricelevelname;
    }

    public void setPricelevelname(String pricelevelname) {
        this.pricelevelname = pricelevelname;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "PriceLevel{" +
                "pricelevelid=" + pricelevelid +
                ", pricelevelname='" + pricelevelname + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
