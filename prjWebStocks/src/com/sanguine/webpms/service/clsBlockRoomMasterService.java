package com.sanguine.webpms.service;

import java.util.List;

import com.sanguine.webpms.model.clsBlockRoomModel;
import com.sanguine.webpms.model.clsRoomMasterModel;

public interface clsBlockRoomMasterService {
	
	public void funAddUpdateRoomMaster(clsRoomMasterModel objMaster);

	public clsBlockRoomModel funGetRoomMaster(String roomCode, String clientCode);

	public List<String> funGetRoomTypeList(String clientCode);

	void funAddUpdateRoomMaster(clsBlockRoomModel objMaster);

}
