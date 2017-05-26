package com.ydp.pwgl.entity;

import java.util.Date;

public class Item {
    private String itemid;

    private String itemname;

    private String producttypeid;

    private String introduction;

    private String venueid;

    private String sel_status;

    private Date starttime;

    private Date endtime;

    private String image_path_arr;

    private String vedio_path_arr;

    private String status;

    private String delstatus;

    private Double vid;

    private String cid;

    private Date ctime;

    private String mid;

    private Date mtime;

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getProducttypeid() {
        return producttypeid;
    }

    public void setProducttypeid(String producttypeid) {
        this.producttypeid = producttypeid;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getVenueid() {
        return venueid;
    }

    public void setVenueid(String venueid) {
        this.venueid = venueid;
    }

    public String getSel_status() {
        return sel_status;
    }

    public void setSel_status(String sel_status) {
        this.sel_status = sel_status;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getImage_path_arr() {
        return image_path_arr;
    }

    public void setImage_path_arr(String image_path_arr) {
        this.image_path_arr = image_path_arr;
    }

    public String getVedio_path_arr() {
        return vedio_path_arr;
    }

    public void setVedio_path_arr(String vedio_path_arr) {
        this.vedio_path_arr = vedio_path_arr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelstatus() {
        return delstatus;
    }

    public void setDelstatus(String delstatus) {
        this.delstatus = delstatus;
    }

    public Double getVid() {
        return vid;
    }

    public void setVid(Double vid) {
        this.vid = vid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Date getMtime() {
        return mtime;
    }

    public void setMtime(Date mtime) {
        this.mtime = mtime;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemid='" + itemid + '\'' +
                ", itemname='" + itemname + '\'' +
                ", producttypeid='" + producttypeid + '\'' +
                ", introduction='" + introduction + '\'' +
                ", venueid='" + venueid + '\'' +
                ", sel_status='" + sel_status + '\'' +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", image_path_arr='" + image_path_arr + '\'' +
                ", vedio_path_arr='" + vedio_path_arr + '\'' +
                ", status='" + status + '\'' +
                ", delstatus='" + delstatus + '\'' +
                ", vid=" + vid +
                ", cid='" + cid + '\'' +
                ", ctime=" + ctime +
                ", mid='" + mid + '\'' +
                ", mtime=" + mtime +
                '}';
    }
}