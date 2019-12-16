package com.sanguine.webpms.bean;

import java.util.ArrayList;
import java.util.List;

public class clsDIrtyRoomBean {
	
	private String strHouseKeepCode;
	
	private String strHouseKeepName;
	
	private boolean strAdd;
	
	private String strRemarks;
	
	private String strRoomCode;
	
	private String strGuestName;
	

	List<clsDIrtyRoomBean> listBean;


	public String getStrHouseKeepCode() {
		return strHouseKeepCode;
	}


	public void setStrHouseKeepCode(String strHouseKeepCode) {
		this.strHouseKeepCode = strHouseKeepCode;
	}


	public String getStrHouseKeepName() {
		return strHouseKeepName;
	}


	public void setStrHouseKeepName(String strHouseKeepName) {
		this.strHouseKeepName = strHouseKeepName;
	}


	public boolean isStrAdd() {
		return strAdd;
	}


	public void setStrAdd(boolean strAdd) {
		this.strAdd = strAdd;
	}


	public String getStrRemarks() {
		return strRemarks;
	}


	public void setStrRemarks(String strRemarks) {
		this.strRemarks = strRemarks;
	}


	public String getStrRoomCode() {
		return strRoomCode;
	}


	public void setStrRoomCode(String strRoomCode) {
		this.strRoomCode = strRoomCode;
	}


	public List<clsDIrtyRoomBean> getListBean() {
		return listBean;
	}


	public void setListBean(List<clsDIrtyRoomBean> listBean) {
		this.listBean = listBean;
	}


	public String getStrGuestName() {
		return strGuestName;
	}


	public void setStrGuestName(String strGuestName) {
		this.strGuestName = strGuestName;
	}

	
	

}
