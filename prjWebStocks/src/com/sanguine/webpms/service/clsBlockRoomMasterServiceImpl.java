package com.sanguine.webpms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webpms.dao.clsBlockRoomMasterDao;
import com.sanguine.webpms.dao.clsRoomMasterDao;
import com.sanguine.webpms.model.clsBlockRoomModel;
import com.sanguine.webpms.model.clsRoomMasterModel;


@Service("clsBlockRoomMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, value = "WebPMSTransactionManager")
public class clsBlockRoomMasterServiceImpl implements clsBlockRoomMasterService{
	@Autowired
	private clsBlockRoomMasterDao objBlockRoomMasterDao;


	@Override
	public void funAddUpdateRoomMaster(clsBlockRoomModel objMaster) {
		objBlockRoomMasterDao.funAddUpdateRoomMaster(objMaster);
	}

	@Override
	public clsBlockRoomModel funGetRoomMaster(String roomCode, String clientCode) {
		return objBlockRoomMasterDao.funGetRoomMaster(roomCode, clientCode);
	}


	@Override
	public List<String> funGetRoomTypeList(String clientCode) {
		return objBlockRoomMasterDao.funGetRoomTypeList(clientCode);
	}

	@Override
	public void funAddUpdateRoomMaster(clsRoomMasterModel objMaster) {
		// TODO Auto-generated method stub
		
	}
}
