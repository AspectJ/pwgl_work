package com.ydp.pwgl.expandbean;

import com.ydp.pwgl.entity.Session;

/**
 * Created by 董亮 on 2017/5/8.
 */
public class SessionExpand extends Session{
    String itemname;
    String hallname;
    String venuename;
    String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVenuename() {
        return venuename;
    }

    public void setVenuename(String venuename) {
        this.venuename = venuename;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getHallname() {
        return hallname;
    }

    public void setHallname(String hallname) {
        this.hallname = hallname;
    }
}
