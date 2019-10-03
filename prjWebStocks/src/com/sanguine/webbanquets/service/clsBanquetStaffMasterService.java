package com.sanguine.webbanquets.service;

import com.sanguine.model.clsLocationMasterModel;
import com.sanguine.webbanquets.model.clsBanquetStaffMasterModel;

public interface clsBanquetStaffMasterService{

	public void funAddUpdateBanquetStaffMaster(clsBanquetStaffMasterModel objMaster);

	public clsBanquetStaffMasterModel funGetBanquetStaffMaster(String docCode,String clientCode);
	
	public clsBanquetStaffMasterModel funGetObject(String code, String clientCode);

}
