package com.sanguine.webbanquets.dao;

import com.sanguine.webbanquets.model.clsItemMasterModel;

public interface clsItemMasterDao{

	public void funAddUpdateItemMaster(clsItemMasterModel objMaster);

	public clsItemMasterModel funGetItemMaster(String docCode,String clientCode);

}
