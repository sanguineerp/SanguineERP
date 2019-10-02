package com.sanguine.webbanquets.service;

import com.sanguine.webbanquets.model.clsItemMasterModel;

public interface clsItemMasterService{

	public void funAddUpdateItemMaster(clsItemMasterModel objMaster);

	public clsItemMasterModel funGetItemMaster(String docCode,String clientCode);

}
