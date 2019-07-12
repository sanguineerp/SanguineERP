package com.sanguine.webpms.dao;

import java.util.List;

import com.sanguine.webpms.model.clsBlockRoomModel;
import com.sanguine.webpms.model.clsRoomMasterModel;

public interface clsBlockRoomMasterDao {
	
	public void funAddUpdateRoomMaster(clsBlockRoomModel objMaster);

	public clsRoomMasterModel funGetRoomMaster(String roomCode, String clientCode);

	public List<String> funGetRoomTypeList(String clientCode);

}
