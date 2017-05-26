package com.ydp.pwgl.entity;

/**
 * Created by john on 2017/4/26.
 */
public class ProductType {

    private String producttypeid;
    private String typename;
    private String pxh;
    private String fprodtypeid;

    private String des;
    private String status;
    private String delstatus;
    private Double vid;

    private String cid;
    private String ctime;
    private String mid;
    private String mtime;

    public String getProducttypeid() {
        return producttypeid;
    }

    public void setProducttypeid(String producttypeid) {
        this.producttypeid = producttypeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getPxh() {
        return pxh;
    }

    public void setPxh(String pxh) {
        this.pxh = pxh;
    }

    public String getFprodtypeid() {
        return fprodtypeid;
    }

    public void setFprodtypeid(String fprodtypeid) {
        this.fprodtypeid = fprodtypeid;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
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
        return "ProductType{" +
                "producttypeid='" + producttypeid + '\'' +
                ", typename='" + typename + '\'' +
                ", pxh='" + pxh + '\'' +
                ", fprodtypeid='" + fprodtypeid + '\'' +
                ", des='" + des + '\'' +
                ", status='" + status + '\'' +
                ", delstatus='" + delstatus + '\'' +
                ", vid=" + vid +
                ", cid='" + cid + '\'' +
                ", ctime='" + ctime + '\'' +
                ", mid='" + mid + '\'' +
                ", mtime='" + mtime + '\'' +
                '}';
    }
}
