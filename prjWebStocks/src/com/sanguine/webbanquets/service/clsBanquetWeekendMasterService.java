package com.sanguine.webbanquets.service;

import org.hibernate.Query;

import com.sanguine.webbanquets.bean.clsBanquetSetupMasterBean;
import com.sanguine.webbanquets.bean.clsBanquetWeekendMasterBean;
import com.sanguine.webbanquets.model.clsBanquetWeekendMasterModel;

public interface clsBanquetWeekendMasterService{

	public void funAddUpdateBanquetWeekendMaster(clsBanquetWeekendMasterModel objMaster);

	public clsBanquetWeekendMasterModel funGetBanquetWeekendMaster(String clientCode);

	public clsBanquetSetupMasterBean funGetWeekendMaster(String clientCode);
	
	public void funDeleteWeekendMaster(String clientCOde);
}
