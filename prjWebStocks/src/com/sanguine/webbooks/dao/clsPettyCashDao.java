package com.sanguine.webbooks.dao;


import com.sanguine.webbooks.model.clsPettyCashEntryHdModel;

public interface clsPettyCashDao {
	
	public void funAddUpdatePettyHd(clsPettyCashEntryHdModel objHdModel);
	
	public clsPettyCashEntryHdModel funGetPettyList(String vouchNo, String clientCode, String propertyCode);

}
