package com.sanguine.webclub.service;

import java.util.List;

import org.hibernate.Query;

import com.sanguine.webclub.bean.clsWebClubPDCBean;
import com.sanguine.webclub.model.clsWebClubPDCModel;

public interface clsWebClubPDCService{

	public void funAddUpdateWebClubPDC(clsWebClubPDCModel objMaster);

	public List funGetWebClubPDC(String memCode, String clientCode);
	
	public void funDeleteDtlRecieved(String memCode, String clientCode);
	
	public void funDeleteDtlIssued(String memCode, String clientCode);

	void funUpdateWebClubPDC(clsWebClubPDCModel objMaster);
	

}
