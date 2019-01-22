package com.sanguine.webbooks.service;

import org.springframework.beans.factory.annotation.Autowired;


import com.sanguine.webbooks.model.clsPettyCashEntryHdModel;

public interface clsPettyCashEntryService {

	
	public void funAddUpdatePettyHd(clsPettyCashEntryHdModel objHdModel);
	
	public clsPettyCashEntryHdModel funGetPettyList(String vouchNo, String clientCode, String propertyCode);
	
}
