package com.sanguine.webbanquets.service;

import com.sanguine.webbanquets.model.clsBanquetMasterModel;

public interface clsBanquetMasterService{

	public void funAddUpdateBanquetMaster(clsBanquetMasterModel objMaster);

	public clsBanquetMasterModel funGetBanquetMaster(String docCode,String clientCode);

}
