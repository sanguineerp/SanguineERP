package com.sanguine.webbanquets.dao;

import com.sanguine.webbanquets.bean.clsBanquetSetupMasterBean;
import com.sanguine.webbanquets.bean.clsBanquetWeekendMasterBean;
import com.sanguine.webbanquets.model.clsBanquetWeekendMasterModel;

public interface clsBanquetWeekendMasterDao{

	public void funAddUpdateBanquetWeekendMaster(clsBanquetWeekendMasterModel objMaster);

	public clsBanquetWeekendMasterModel funGetBanquetWeekendMaster(String clientCode);

	public clsBanquetSetupMasterBean funGetWeekendMaster(String clientCode);
	
	public void funDeleteWeekendMaster(String clientCode);
	

}
