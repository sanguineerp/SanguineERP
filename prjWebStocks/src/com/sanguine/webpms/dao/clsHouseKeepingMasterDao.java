package com.sanguine.webpms.dao;

import com.sanguine.webpms.model.clsHouseKeepingMasterModel;

public interface clsHouseKeepingMasterDao{

	public void funAddUpdateHouseKeepingMaster(clsHouseKeepingMasterModel objMaster);

	public clsHouseKeepingMasterModel funGetHouseKeepingMaster(String docCode,String clientCode);

}
