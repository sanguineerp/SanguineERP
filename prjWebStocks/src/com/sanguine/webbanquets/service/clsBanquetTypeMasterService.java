package com.sanguine.webbanquets.service;

import com.sanguine.webbanquets.model.clsBanquetTypeMasterModel;

public interface clsBanquetTypeMasterService{

	public void funAddUpdateBanquetTypeMaster(clsBanquetTypeMasterModel objMaster);

	public clsBanquetTypeMasterModel funGetBanquetTypeMaster(String docCode,String clientCode);

}
