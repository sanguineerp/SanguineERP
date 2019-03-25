package com.sanguine.webbooks.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.base.service.intfBaseService;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webbooks.bean.clsIncomeStmtReportBean;
import com.sanguine.webbooks.bean.clsUserDefineReportBean;
import com.sanguine.webbooks.model.clsACGroupMasterModel;
import com.sanguine.webbooks.model.clsUserDefinedReportDtlModel;
import com.sanguine.webbooks.model.clsUserDefinedReportHdModel;
import com.sanguine.webbooks.service.clsACGroupMasterService;

@Controller
public class clsUserDefineReportProcessingController {
	
	private static final int Integer = 0;

	@Autowired
	private intfBaseService objBaseService;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	
	@Autowired
	private clsACGroupMasterService objACGroupMasterService;
	
	// Open  Form
		@RequestMapping(value = "/frmUserDefineReportProcessing", method = RequestMethod.GET)
		public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
			String urlHits = "1";
			try {
				urlHits = request.getParameter("saddr").toString();
			} catch (NullPointerException e) {
				urlHits = "1";
			}
			model.put("urlHits", urlHits);
		
			
			if ("2".equalsIgnoreCase(urlHits)) {
				return new ModelAndView("frmUserDefineReportProcessing_1", "command", new clsUserDefineReportBean());
			} else if ("1".equalsIgnoreCase(urlHits)) {
				return new ModelAndView("frmUserDefineReportProcessing", "command", new clsUserDefineReportBean());
			} else {
				return null;
			}

		}
		
		

		// Open Report through User define report 
		@SuppressWarnings({ "unused", "rawtypes" })
		@RequestMapping(value = "/getUserDefinedReportProcess", method = RequestMethod.POST)
		public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsUserDefineReportBean objBean, BindingResult result, HttpServletRequest req) throws Exception
		{	
			String urlHits = "1";

			clsUserDefinedReportHdModel objUserDefModel = null;
			try
			{
				urlHits = req.getParameter("saddr").toString();
			}
			catch (NullPointerException e)
			{
				urlHits = "1";
			}
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String fDate=objBean.getDteFDate();
			String tDate=objBean.getDteTDate();
			 List listData=new ArrayList(); 
			
			StringBuilder sbSql = new StringBuilder("from clsUserDefinedReportHdModel where strReportId='" + objBean.getStrReportId() + "' and strClientCode='" + clientCode + "'");
			List list = null;
			list = objBaseService.funGetListForWebBooks(sbSql, "hql");
            if(list!=null && list.size()>0)
            {
            	objUserDefModel=(clsUserDefinedReportHdModel) list.get(0);
            	objUserDefModel.getListUserDefRptDtlModel().size();
            }
            else
            {
            	objUserDefModel=new clsUserDefinedReportHdModel(); 
            }
           
            Map<Integer,List<clsUserDefinedReportDtlModel>> hmRowData=new TreeMap<Integer,List<clsUserDefinedReportDtlModel>>();
            List listRow=new ArrayList<clsUserDefinedReportDtlModel>();
            String date=objUserDefModel.getDteUserDefDate();
            
            for(clsUserDefinedReportDtlModel objUserDefModelDtl:objUserDefModel.getListUserDefRptDtlModel())
            {
            	if(hmRowData.containsKey(objUserDefModelDtl.getIntSrNo()))
            	{
            		listRow=hmRowData.get(objUserDefModelDtl.getIntSrNo());
            		listRow.add(objUserDefModelDtl);
            		hmRowData.put(objUserDefModelDtl.getIntSrNo(),listRow);
            	}else{
            		listRow=new ArrayList<clsUserDefinedReportDtlModel>();
            		listRow.add(objUserDefModelDtl);
            		hmRowData.put(objUserDefModelDtl.getIntSrNo(),listRow);
            	}
            	
            }	
            
    /*      for(Map.Entry<Integer, List<clsUserDefinedReportDtlModel>> objUserDefMod:hmRowData.entrySet())
            {
            	List<clsUserDefinedReportDtlModel> listData=objUserDefMod.getValue();
            	listData.sort(new Comparator<clsUserDefinedReportDtlModel>() {

					@Override 
					public int compare(clsUserDefinedReportDtlModel o1, clsUserDefinedReportDtlModel o2) {
						// TODO Auto-generated method stub
						return o1.getStrColumn()-(o2.getStrColumn());
					}
				});
            	hmRowData.put(objUserDefMod.getKey(),listData);
            }*/
            
        	List listLedger = new ArrayList();
    		listLedger.add("Ledger_" + fDate + "to" + tDate + "_" + userCode);
    		String[] ExcelHeader = { "", "", "", "", "", "", "" };
    		listLedger.add(ExcelHeader);
    		int lastColumn=4;
    		
    		 Map<String,clsIncomeStmtReportBean> hmUserDefined=new LinkedHashMap<String,clsIncomeStmtReportBean>();
    		if(!hmRowData.isEmpty())
            {
    			 for(Map.Entry<Integer, List<clsUserDefinedReportDtlModel>> hmUserDefMod:hmRowData.entrySet())
                 {
    				 List<clsUserDefinedReportDtlModel> listuserDefData=hmUserDefMod.getValue();
    				 List dataList=new ArrayList();
    				 List listAccount=new ArrayList();
//    				 for(int i=1;i<lastColumn;i++)
//    				 {
	    				  clsUserDefinedReportDtlModel objUserDefMod=listuserDefData.get(0);
//	    				  if(i==objUserDefMod.getStrColumn())
//	    				  {
	    				  if(objUserDefMod.getStrType().equals("Text"))
	        			  {
	    					  if(objUserDefMod.getStrDescription().contains(" "))
	    					  {
	    						  String column[]=objUserDefMod.getStrDescription().split(" ");
	    						  dataList.add(column[0]);
		    					  dataList.add(column[1]);
		    					  dataList.add(column[2]);
		    					  dataList.add(column[3]);
	    					  }
	    					 
	        			  }
	    				  
		    				  if(objUserDefMod.getStrType().equals("Group"))
		        			  {
		    					  dataList.add(objUserDefMod.getStrFGroup());
		    					  clsACGroupMasterModel acGroupMasterModel = objACGroupMasterService.funGetACGroupMaster(objUserDefMod.getStrFGroup(), clientCode);
		    					  dataList.add(acGroupMasterModel.getStrGroupName());
		    					  dataList.add("");
		    					  dataList.add("");
		        			  }
		    				  if(objUserDefMod.getStrType().equals("GL_Code"))
		    				  {
		    					  if(objUserDefMod.getStrOperator().equals("Equal"))
		    					  {
		    						  listAccount.add(objUserDefMod.getStrFAccount());
		    					     if(date.equals("Between Date"))
		    					     {
		    					    	 funCalculateBalanceSheetListAccountCode( "tbljvhd", "tbljvdtl", fDate, tDate, 0, clientCode,  sbSql,  hmUserDefined, listAccount, propertyCode);	
		    					    	 funCalculateBalanceSheetListAccountCode( "tblpaymenthd", "tblpaymentdtl", fDate, tDate, 0, clientCode,  sbSql,  hmUserDefined, listAccount, propertyCode);
		    					    	 funCalculateBalanceSheetListAccountCode( "tblreceipthd", "tblreceiptdtl", fDate, tDate, 0, clientCode,  sbSql,  hmUserDefined, listAccount, propertyCode);
		    					     }if(date.equals("Upto Date"))
		    					     {
		    					    	 
		    					    	 funCalculateBalanceSheetListAccountCode( "tbljvhd", "tbljvdtl", fDate, tDate, 1, clientCode,  sbSql,  hmUserDefined, listAccount, propertyCode);	
		    					    	 funCalculateBalanceSheetListAccountCode( "tblpaymenthd", "tblpaymentdtl", fDate, tDate, 1, clientCode,  sbSql,  hmUserDefined, listAccount, propertyCode);
		    					    	 funCalculateBalanceSheetListAccountCode( "tblreceipthd", "tblreceiptdtl", fDate, tDate, 1, clientCode,  sbSql,  hmUserDefined, listAccount, propertyCode);
		    					     }
		    					     
		    					     
		    					     
		    					      for(Map.Entry<String,clsIncomeStmtReportBean> hmBean:hmUserDefined.entrySet())
		    					      {
		    					    	  clsIncomeStmtReportBean objData=hmBean.getValue();
		    					    	  dataList=new ArrayList();
		    					    	  dataList.add(objData.getStrAccountCode());
				    					  dataList.add(objData.getStrAccountName());
				    					  if(objUserDefMod.getStrColumn()==3)
				    					  {
				    						  dataList.add(objData.getDblDrAmt());
					    					  dataList.add("");
				    					  }  
				    					  if(objUserDefMod.getStrColumn()==4){
				    						  dataList.add("");
				    						  dataList.add(objData.getDblCrAmt());
				    					  }
		    					      }
		    					     
		    					    
		    					    
		    					  }
		    				  }
//	    				  }
//    				 }
		    		
		    		 listData.add(dataList);
    				 listLedger.add(listData);
                 }
            
            }
    		
    		
    		
    		
    		
    		
/*    		int lastColumn=10;
            if(!hmRowData.isEmpty())
            {
            	  for(Map.Entry<Integer, List<clsUserDefinedReportDtlModel>> hmUserDefMod:hmRowData.entrySet())
                  {
            		  List<clsUserDefinedReportDtlModel> listuserDefData=hmUserDefMod.getValue();
//            		  int i=1;
            		  List dataList=new ArrayList();
            		  for(int i=1;i<lastColumn;i++)
            		  {
            			 
            			  int z=0;
            			  clsUserDefinedReportDtlModel objUserDefMod=new clsUserDefinedReportDtlModel();
            			  try{
            				  objUserDefMod= listuserDefData.get(i-1);
            			  }catch(Exception e)
            			  {
            				  z++;
            			  }
            			  if(z==0)
            			  {
            			  if(i==objUserDefMod.getStrColumn())
            			  {
            				  if(objUserDefMod.getStrType().equals("Group"))
                			  {
            					  dataList.add(objUserDefMod.getStrFGroup());
                			  }
            				  if(objUserDefMod.getStrType().equals("GL_Code"))
            				  {
            					  if(objUserDefMod.getStrType().equals("Equal"))
            					  {
            					    dataList.add(objUserDefMod.getStrFAccount());
            					  }
            					  
            				  }
            				
            			  }else{
            				  for(int j=i;j<objUserDefMod.getStrColumn();j++)
            				  {
            					  dataList.add("");
            				  }
            				  if(objUserDefMod.getStrType().equals("Group"))
                			  {
            					  dataList.add(objUserDefMod.getStrFGroup());
                			  }
            				  if(objUserDefMod.getStrType().equals("GL_Code"))
            				  {
            					  if(objUserDefMod.getStrOperator().equals("Equal"))
            					  {
            					    dataList.add(objUserDefMod.getStrFAccount());
            					  }
            					  
            				  }
            				  if(objUserDefMod.getStrType().equals("Text"))
            				  {
            					  if(objUserDefMod.getStrDescription().equals("Name"))
            					  {
            					  dataList.add("AccountName");
            					  }
            					  if(objUserDefMod.getStrDescription().equals("Amount"))
            					  {
            					  dataList.add("Amount");
            					  }
            				  }
            			  }
            			 
            		
            		  }else{
            			  dataList.add("");
            		  }
            		  }
            		  listLedger.add(dataList);
                  }
            	  
            	 
            	
            }*/
            
           
			sbSql.setLength(0);
			sbSql.append("select from tbl" );
			return new ModelAndView("excelViewWithReportName", "listWithReportName", listLedger);
			
		}
		
		private void funCalculateBalanceSheetListAccountCode(String hdTableName, String dtlTableName, String fromDate, String toDate
				, int cnt, String clientCode, StringBuilder sbSql, Map<String,clsIncomeStmtReportBean> hmIncomeStatement,List<String> list,String propCode)
		{
			
			StringBuilder sbOp=new StringBuilder(); 		
			
			for(String acc:list)
			{
			sbSql.setLength(0);
			sbSql.append("select a.strType,b.strGroupCode,b.strGroupName,ifnull(d.strCrDr,''),if((c.strVouchNo is null),0, IFNULL(SUM(d.dblCrAmt),0) ), if((c.strVouchNo is null),0,IFNULL(SUM(d.dblDrAmt),0))  ,a.strAccountName,a.strAccountCode, "
					+" b.strCategory from  tblacgroupmaster b,tblsubgroupmaster s,tblacmaster a "
					+" left outer join "+dtlTableName+" d on  a.strAccountCode=d.strAccountCode " 
					+" left outer join "+hdTableName+"  c on c.strVouchNo=d.strVouchNo   and date(c.dteVouchDate) between '" + fromDate + "' and '" + toDate + "' and c.strClientCode='"+clientCode+"'  and a.strPropertyCode='"+propCode+"' "
					+" where a.strSubGroupCode=s.strSubGroupCode "
					+ " and s.strGroupCode =b.strGroupCode "
					+" and a.strAccountCode='"+acc+"' "
//					+" and a.strType='GL Code'  "
					+" group by a.strAccountCode  ");
			
			List listJV = objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
			if (listJV != null && listJV.size() > 0)
			{
				for(int cn=0;cn<listJV.size();cn++)
				{
					Object[] objArr = (Object[]) listJV.get(cn);

					String groupCategory = objArr[1].toString();
					String groupName = objArr[2].toString();
					BigDecimal creditAmount = BigDecimal.valueOf(Double.parseDouble(objArr[4].toString()));
					BigDecimal debitAmount = BigDecimal.valueOf(Double.parseDouble(objArr[5].toString()));
					
					
					BigDecimal totalAmt=debitAmount.subtract(creditAmount);
					
					String accountCode=objArr[7].toString();
					clsIncomeStmtReportBean objBean = new clsIncomeStmtReportBean();
					
					if(hmIncomeStatement.containsKey(accountCode))
					{
						objBean=hmIncomeStatement.get(accountCode);
						objBean.setDblValue(objBean.getDblValue().add(totalAmt));
						objBean.setBdCrAmt(creditAmount.add(objBean.getBdCrAmt()));
						objBean.setBdCrAmt(debitAmount.add(objBean.getBdDrAmt()));
					}
					else
					{
						objBean.setStrGroupCategory(groupCategory);
						objBean.setStrGroupName(groupName);
						
						objBean.setStrAccountName(objArr[6].toString());
						objBean.setStrAccountCode(objArr[7].toString());
						objBean.setBdCrAmt(creditAmount);
						objBean.setBdDrAmt(debitAmount);
					    
						BigDecimal opAmt=new BigDecimal(0);
						if(cnt==1)
						{
						sbOp.setLength(0);
						
						sbOp.append(" select IF(a.strCrDr='Dr',a.intOpeningBal,0) dblDebitAmt, IF(a.strCrDr='Cr',a.intOpeningBal,0) dblCreditAmt, (IF(a.strCrDr='Dr',a.intOpeningBal,0) - IF(a.strCrDr='Cr',a.intOpeningBal,0)) dblBalanceAmt  from tblacmaster a where a.strAccountCode='"+objArr[7].toString()+"' "
					     +" and a.strClientCode='"+clientCode+"' ");
						List listOP = objGlobalFunctionsService.funGetListModuleWise(sbOp.toString(), "sql");
					    if(listOP.size()>0)
					    {
					    	Object obj[]=(Object[])listOP.get(0);
					    	opAmt=BigDecimal.valueOf( Double.parseDouble(obj[2].toString()));
					    }
						}
					    objBean.setDblValue(opAmt.add(totalAmt));
					}
//					objBean.setStrCategory(catType);
					hmIncomeStatement.put(accountCode, objBean);
				}
			}
			}
			}
		
}
