package com.ydp.pwgl.dubbo.entity;

import java.io.Serializable;

public class Seat implements Serializable {
	//��λid
	private String pewid;
	//����id
	private String sessionsid;
	private String hallid;
	private String zoneid;
	private String pewname;
	private String row;
	private String col;
	
	private String pricelevelid;
	private double price;
	//�Ƿ����
	private String isaddpew;
	//Ԥ������id
	private String reserveid;
	//�Ƿ��Ԥ��
	private String ispresell;
	//�Ƿ������
	private String issale;
	//�Ƿ��ȡ��
	private String iscancel;
	//��λ״̬
	private String pewstatus;
	//����״̬
	private String status;
	//ɾ��״̬
	private String delstatus;
	public Seat(){
		super();
	}
	public String getPewid() {
		return pewid;
	}
	public void setPewid(String pewid) {
		this.pewid = pewid;
	}
	public String getSessionsid() {
		return sessionsid;
	}
	public void setSessionsid(String sessionsid) {
		this.sessionsid = sessionsid;
	}
	
	public String getHallid() {
		return hallid;
	}
	public void setHallid(String hallid) {
		this.hallid = hallid;
	}
	public String getZoneid() {
		return zoneid;
	}
	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}
	public String getPewname() {
		return pewname;
	}
	public void setPewname(String pewname) {
		this.pewname = pewname;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}

	public String getPricelevelid() {
		return pricelevelid;
	}
	public void setPricelevelid(String pricelevelid) {
		this.pricelevelid = pricelevelid;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getIsaddpew() {
		return isaddpew;
	}
	public void setIsaddpew(String isaddpew) {
		this.isaddpew = isaddpew;
	}
	public String getReserveid() {
		return reserveid;
	}
	public void setReserveid(String reserveid) {
		this.reserveid = reserveid;
	}
	public String getIspresell() {
		return ispresell;
	}
	public void setIspresell(String ispresell) {
		this.ispresell = ispresell;
	}
	public String getIssale() {
		return issale;
	}
	public void setIssale(String issale) {
		this.issale = issale;
	}
	public String getIscancel() {
		return iscancel;
	}
	public void setIscancel(String iscancel) {
		this.iscancel = iscancel;
	}
	public String getPewstatus() {
		return pewstatus;
	}
	public void setPewstatus(String pewstatus) {
		this.pewstatus = pewstatus;
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
	
}
