package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.dao.clsEquipmentDao;
import com.sanguine.webbanquets.model.clsEquipmentModel;

@Service("clsEquipmentService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsEquipmentServiceImpl implements clsEquipmentService{
	@Autowired
	private clsEquipmentDao objEquipmentDao;

	@Override
	public void funAddUpdateEquipment(clsEquipmentModel objMaster){
		objEquipmentDao.funAddUpdateEquipment(objMaster);
	}

	@Override
	public clsEquipmentModel funGetEquipment(String docCode,String clientCode){
		return objEquipmentDao.funGetEquipment(docCode,clientCode);
	}



}
