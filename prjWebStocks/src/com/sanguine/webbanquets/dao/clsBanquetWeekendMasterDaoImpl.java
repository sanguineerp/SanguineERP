package com.sanguine.webbanquets.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webbanquets.bean.clsBanquetSetupMasterBean;
import com.sanguine.webbanquets.bean.clsBanquetWeekendMasterBean;
import com.sanguine.webbanquets.model.clsBanquetWeekendMasterModel;
import com.sanguine.webbanquets.model.clsBanquetWeekendMasterModel_ID;

@Repository("clsBanquetWeekendMasterDao")
public class clsBanquetWeekendMasterDaoImpl implements clsBanquetWeekendMasterDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Autowired
	private clsGlobalFunctions objGlobal;
	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateBanquetWeekendMaster(clsBanquetWeekendMasterModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsBanquetWeekendMasterModel funGetBanquetWeekendMaster(String clientCode){
		return (clsBanquetWeekendMasterModel) webPMSSessionFactory.getCurrentSession().get(clsBanquetWeekendMasterModel.class,new clsBanquetWeekendMasterModel_ID(clientCode));
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsBanquetSetupMasterBean funGetWeekendMaster(String clientCode) 
	{
		List<String> list = null;
		clsBanquetSetupMasterBean objModel = new clsBanquetSetupMasterBean();
			list = webPMSSessionFactory.getCurrentSession().createSQLQuery("select strDay from tblweekendmaster where strClientCode='" + clientCode + "'  ").list();	
			if(!list.isEmpty())
			{
				for(int i=0;i<list.size();i++)
				{
					switch (list.get(i))
					{
					case "Sunday":
						objModel.setStrSunday(objGlobal.funIfNull(list.get(i),"",list.get(i)));			
						break;
						
					case "Monday":
						objModel.setStrMonday(objGlobal.funIfNull(list.get(i),"",list.get(i)));
						break;
						
					case "Tuesday":
						objModel.setStrTuesday(objGlobal.funIfNull(list.get(i),"",list.get(i)));
						break;
						
					case "Wednesday":
						objModel.setStrWednesday(objGlobal.funIfNull(list.get(i),"",list.get(i)));
						break;
						
					case "Thursday":
						objModel.setStrThursday(objGlobal.funIfNull(list.get(i),"",list.get(i)));
						break;
						
					case "Friday":
						objModel.setStrFriday(objGlobal.funIfNull(list.get(i),"",list.get(i)));
						break;
						
					case "Saturday":
						objModel.setStrSaturday(objGlobal.funIfNull(list.get(i),"",list.get(i)));
						break;						
					}
				}		
			}
		return objModel;
	}
	
	public void funDeleteWeekendMaster(String clientCode) {
		Query query = webPMSSessionFactory.getCurrentSession().createSQLQuery("delete from tblweekendmaster " + " where strClientCode='" + clientCode + "'  ");
		query.executeUpdate();
	}


	
	
}
