package com.sanguine.webpms.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webpms.model.clsBlockRoomModel;
import com.sanguine.webpms.model.clsBlockRoom_ID;
import com.sanguine.webpms.model.clsRoomMasterModel;
import com.sanguine.webpms.model.clsRoomMasterModel_ID;

@Repository("clsBlockRoomMasterDao")
public class clsBlockRoomMasterDaoImpl implements clsBlockRoomMasterDao{

	
	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateRoomMaster(clsBlockRoomModel objMaster) {
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsBlockRoomModel funGetRoomMaster(String roomCode, String clientCode) 
	{   List<String> list = null;
		clsBlockRoomModel objModel=(clsBlockRoomModel)webPMSSessionFactory.getCurrentSession().get(clsBlockRoomModel.class, new clsBlockRoom_ID(roomCode, clientCode));
		if(objModel!=null)
		{
			list = webPMSSessionFactory.getCurrentSession().createSQLQuery("select strRoomTypeDesc from tblroomtypemaster where strclientCode='" + clientCode + "' and  strRoomTypeCode='"+objModel.getStrRoomCode()+"' ").list();	
			objModel.setStrRoomType((String)list.get(0));
		}
		return objModel;
	}

	@Override
	public List<String> funGetRoomTypeList(String clientCode) {
		List<String> list = null;
		list = webPMSSessionFactory.getCurrentSession().createSQLQuery("select strRoomTypeDesc from tblroomtypemaster where strclientCode='" + clientCode + "' ").list();
		return list;
	}

}
