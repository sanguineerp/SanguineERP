package com.sanguine.webbanquets.service;

import com.sanguine.webbanquets.model.clsCostCenterMasterModel;

public interface clsCostCenterMasterService{

	public void funAddUpdateCostCenterMaster(clsCostCenterMasterModel objMaster);

	public clsCostCenterMasterModel funGetCostCenterMaster(String docCode,String clientCode);

}
