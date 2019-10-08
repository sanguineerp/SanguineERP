package com.sanguine.webbanquets.service;

import org.hibernate.Query;

import com.sanguine.webbanquets.bean.clsBanquetWeekendMasterBean;
import com.sanguine.webbanquets.model.clsBanquetWeekendMasterModel;

public interface clsBanquetWeekendMasterService{

	public void funAddUpdateBanquetWeekendMaster(clsBanquetWeekendMasterModel objMaster);

	public clsBanquetWeekendMasterModel funGetBanquetWeekendMaster(String clientCode);

	public clsBanquetWeekendMasterBean funGetWeekendMaster(String clientCode);
	
	public void funDeleteWeekendMaster(String clientCOde);
}
