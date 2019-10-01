package com.sanguine.webbanquets.dao;

import com.sanguine.webbanquets.model.clsEquipmentModel;

public interface clsEquipmentDao{

	public void funAddUpdateEquipment(clsEquipmentModel objMaster);

	public clsEquipmentModel funGetEquipment(String docCode,String clientCode);

	
			

}
