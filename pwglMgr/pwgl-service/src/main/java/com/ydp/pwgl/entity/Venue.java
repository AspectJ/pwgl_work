package com.ydp.pwgl.entity;

/**
 * Created by john on 2017/4/26.
 */
public class Venue {

    private String venueid;
    private String venuename;
    private String address;
    private String areaid;

    private String introduction;
    private String status;
    private String delstatus;
    private Double vid;

    private String cid;
    private String ctime;
    private String mid;
    private String mtime;

    public String getVenueid() {
        return venueid;
    }

    public void setVenueid(String venueid) {
        this.venueid = venueid;
    }

    public String getVenuename() {
        return venuename;
    }

    public void setVenuename(String venuename) {
        this.venuename = venuename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "venueid='" + venueid + '\'' +
                ", venuename='" + venuename + '\'' +
                ", address='" + address + '\'' +
                ", areaid='" + areaid + '\'' +
                ", introduction='" + introduction + '\'' +
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
