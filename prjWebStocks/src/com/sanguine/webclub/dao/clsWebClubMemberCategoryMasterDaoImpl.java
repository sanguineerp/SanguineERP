package com.sanguine.webclub.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sanguine.webclub.model.clsWebClubCategeoryWiseFacilityModel;
import com.sanguine.webclub.model.clsWebClubFacilityMasterModel;
import com.sanguine.webclub.model.clsWebClubFacilityMasterModel_ID;
import com.sanguine.webclub.model.clsWebClubMemberCategoryMasterModel;
import com.sanguine.webclub.model.clsWebClubMemberCategoryMasterModel_ID;

@Repository("clsWebClubMemberCategoryMasterDao")
public class clsWebClubMemberCategoryMasterDaoImpl implements clsWebClubMemberCategoryMasterDao {

	@Autowired
	private SessionFactory WebClubSessionFactory;
	
	@Override
	public void funAddUpdateWebClubMemberCategoryMaster(clsWebClubMemberCategoryMasterModel objMaster) {
		WebClubSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	public clsWebClubMemberCategoryMasterModel funGetWebClubMemberCategoryMaster(String docCode, String clientCode) {
		return (clsWebClubMemberCategoryMasterModel) WebClubSessionFactory.getCurrentSession().get(clsWebClubMemberCategoryMasterModel.class, new clsWebClubMemberCategoryMasterModel_ID(docCode, clientCode));
	}	
	@Override
	public void funGetCategoryWiseFacilityDtl(String strCatCode,String strClientCode) {
	
			String sql= "DELETE FROM tblcategeorywisefacilitydtl"
					+ "  where  strCatCode=:strCatCode and strClientCode=:strClientCode";
			try {
						
				
				Query query =  WebClubSessionFactory.getCurrentSession().createSQLQuery(sql);	
				query.setParameter("strCatCode", strCatCode);
				query.setParameter("strClientCode", strClientCode);
				int result = query.executeUpdate();					

			} catch (Exception e) {
				e.printStackTrace();
			}
			
	}
	
	@Override
	public void funAddUpdateWebClubCategeoryWiseFacility(clsWebClubCategeoryWiseFacilityModel objMaster) {
		WebClubSessionFactory.getCurrentSession().save(objMaster);
	}
	
	@Override
	public clsWebClubFacilityMasterModel funGetWebClubMemberFacilityMaster(String strCatCode,String clientCode) {
		return (clsWebClubFacilityMasterModel) WebClubSessionFactory.getCurrentSession().get(clsWebClubFacilityMasterModel.class, new clsWebClubFacilityMasterModel_ID(strCatCode,clientCode));
	}	

	@Override
	public List<String> funGetCategoryWiseFacilityDtlList(String strCatCode,String strClientCode) {
		Query query=null;
		/*String sql= " FROM tblcategeorywisefacilitydtl"
				+ "  where  strCatCode=:strCatCode";*/
		List<String> objListItem = new ArrayList<String>();
		
		try {
				String sql = "select a.strCatCode,a.strFacilityCode,a.strFacilityName,a.strOperationalYN,a.strClientCode " + "from tblcategeorywisefacilitydtl a where a.strCatCode='" + strCatCode + "' and a.strClientCode='"+strClientCode+"'  ";
				objListItem = WebClubSessionFactory.getCurrentSession().createSQLQuery(sql).list();

			
			
	/*		query =  WebClubSessionFactory.getCurrentSession().createSQLQuery(sql);	
			query.setParameter("strCatCode", strCatCode);
			int result = query.executeUpdate();	*/
			

		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return  objListItem;
	
	
	}	

	
	

}
