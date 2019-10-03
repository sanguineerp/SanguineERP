package com.sanguine.webbanquets.dao;

import com.sanguine.model.clsLocationMasterModel;
import com.sanguine.webbanquets.model.clsBanquetStaffMasterModel;

public interface clsBanquetStaffMasterDao{

	public void funAddUpdateBanquetStaffMaster(clsBanquetStaffMasterModel objMaster);

	public clsBanquetStaffMasterModel funGetBanquetStaffMaster(String docCode,String clientCode);

	public clsBanquetStaffMasterModel funGetObject(String code, String clientCode);

	
}
