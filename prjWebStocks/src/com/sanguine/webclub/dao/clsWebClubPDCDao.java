package com.sanguine.webclub.dao;

import java.util.List;

import com.sanguine.webclub.bean.clsWebClubPDCBean;
import com.sanguine.webclub.model.clsWebClubPDCModel;

public interface clsWebClubPDCDao{

	public void funAddUpdateWebClubPDC(clsWebClubPDCModel objMaster);

	public List funGetWebClubPDC(String memCode,String clientCode);

	public void funDeleteDtlRecieved(String memCode, String clientCode);
	
	public void funDeleteDtlIssued(String memCode, String clientCode);

	void funUpdateWebClubPDC(clsWebClubPDCModel objMaster);

}
