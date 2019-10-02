package com.sanguine.webbanquets.dao;

import com.sanguine.webbanquets.model.clsMenuHeadMasterModel;

public interface clsMenuHeadMasterDao{

	public void funAddUpdatefrmMenuHeadMaster(clsMenuHeadMasterModel objMaster);

	public clsMenuHeadMasterModel funGetfrmMenuHeadMaster(String docCode,String clientCode);

}
