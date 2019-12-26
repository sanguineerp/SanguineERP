package com.sanguine.webpms.service;

import com.sanguine.webpms.model.clsHouseKeepingMasterModel;

public interface clsHouseKeepingMasterService{

	public void funAddUpdateHouseKeepingMaster(clsHouseKeepingMasterModel objMaster);

	public clsHouseKeepingMasterModel funGetHouseKeepingMaster(String docCode,String clientCode);

}
