package com.sanguine.dao;

import java.util.Map;

import com.sanguine.model.clsManufactureMasterModel;

public interface clsManufactureMasterDao {

	public void funAddUpdate(clsManufactureMasterModel objModel);

	public clsManufactureMasterModel funGetObject(String strCode, String clientCode);

	public Map<String, String> funGetManufacturerComboBox(String clientCode);

}
