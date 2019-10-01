package com.sanguine.webbanquets.service;

import com.sanguine.webbanquets.model.clsEquipmentModel;

public interface clsEquipmentService{

	public void funAddUpdateEquipment(clsEquipmentModel objMaster);

	public clsEquipmentModel funGetEquipment(String docCode,String clientCode);

}
