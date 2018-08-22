package com.sanguine.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.service.clsGlobalFunctionsService;


@Controller
public class clsDownloadExcelController {
	@Autowired
	private clsGlobalFunctionsService objGlobalService;
	@Autowired
	clsGlobalFunctions objGlobal;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
	public ModelAndView downloadExcel(@RequestParam(value = "param1") String param1, @RequestParam(value = "fDate") String fDate, @RequestParam(value = "tDate") String tDate, @RequestParam(value = "prodType") String prodType, @RequestParam(value = "ManufCode") String strManufactureCode, HttpServletRequest req, HttpServletResponse resp) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String[] spParam1 = param1.split(",");
		String reportType = spParam1[0];
		String locCode = spParam1[1];
		String propCode = spParam1[2];
		String showZeroItems = spParam1[3];
		String strSGCode = spParam1[4];
		String strNonStkItems = spParam1[5];
		String strGCode = spParam1[6];
		String qtyWithUOM = spParam1[7];

		String fromDate = objGlobal.funGetDate("yyyy-MM-dd", fDate);
		String toDate = objGlobal.funGetDate("yyyy-MM-dd", tDate);
		List listStock = new ArrayList();
		listStock.add("StockFlash_" + fromDate + "to" + toDate + "_" + userCode);
		String strCurr = req.getSession().getAttribute("currValue").toString();
		double currValue = Double.parseDouble(strCurr);
		
		double totalOpeningStock=0.00,totalGRN=0.00,totalSCGRN=0.00,totalStkTransferIn=0.00,totalStkAdjIn=0.00;
		double totalMISIn=0.00,totalProducedQty=0.00,totalSalesRet=0.00,totalMaterialRet=0.00,totalPurchaseRet=0.00,totalDelNote=0.00;
		double totalStkTransOut=0.00,totalStkAdjOut=0.00,totalMISOut=0.00,totalQtyConsumed=0.00,totalSaleAmt=0.00,totalClosingStk=0.00,totalValueTotal = 0.00,totalIssueUOMStk=0.00;
		double totalReciept=0.00,totalIssue=0.00;
		
		String stockableItem = "B";
		if (strNonStkItems.equals("Stockable")) {
			stockableItem = "Y";
		}
		if (strNonStkItems.equals("Non Stockable")) {
			stockableItem = "N";
		}

		if ("Detail".equalsIgnoreCase(reportType)) {
			String[] ExcelHeader = { "Property Name", "Product Code", "Product Name", "Location", "Group", "Sub Group", "UOM", "Bin No", "Unit Price", "Opening Stock", "GRN", "SCGRN", "	Stock Transfer In", "Stock Adj In", "MIS In", "Qty Produced", "Sales Return", "Material Return", "Purchase Return", "Delivery Note", "Stock Trans Out", "Stock Adj Out", "MIS Out", "Qty Consumed", "Sales",
					"Closing Stock", "Value", "Issue UOM Stock", "Issue Conversion", "Issue UOM", "Part No" };
			listStock.add(ExcelHeader);

			String startDate = req.getSession().getAttribute("startDate").toString();
			String[] sp = startDate.split(" ");
			String[] spDate = sp[0].split("/");
			startDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];
			System.out.println(startDate);
			objGlobal.funInvokeStockFlash(startDate, locCode, fromDate, toDate, clientCode, userCode, stockableItem, req, resp);

			StringBuilder sqlBuilder = new StringBuilder();
			if (qtyWithUOM.equals("No")) {
				if (strGCode.equals("ALL") && strSGCode.equals("ALL")) // for
																		// All
																		// Group
																		// and
																		// All
																		// SubGroup
				{
					sqlBuilder.setLength(0);
					sqlBuilder.append("select f.strPropertyName,a.strProdCode,b.strProdName,e.strLocName" + ",d.strGName,c.strSGName,b.strUOM,b.strBinNo"
							+ " ,(if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice))/" + currValue + " , " + "a.dblOpeningStk,a.dblGRN,a.dblSCGRN" + ",a.dblStkTransIn,a.dblStkAdjIn,a.dblMISIn,a.dblQtyProduced" + ",a.dblSalesReturn,a.dblMaterialReturnIn,a.dblPurchaseReturn" + ",a.dblDeliveryNote,a.dblStkTransOut,a.dblStkAdjOut,a.dblMISOut" + ",a.dblQtyConsumed,a.dblSales,a.dblMaterialReturnOut "
							+ ",a.dblClosingStk,"
							+ "(a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice))/" + currValue + " as Value " + ",a.dblClosingStk as IssueUOMStock " + ",b.dblIssueConversion,b.strIssueUOM,b.strPartNo "
							+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
							+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' " + "and a.strClientCode='" + clientCode + "'  " + "and a.strLocCode='" + locCode + "' and e.strPropertyCode='" + propCode + "' ");

					if (strNonStkItems.equals("Non Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='Y' ");
					} else if (strNonStkItems.equals("Stockable")) {
						sqlBuilder.append( "	and b.strNonStockableItem='N' ");
					}
					if(!strManufactureCode.equals("")){
						sqlBuilder.append( " and b.strManufacturerCode='" + strManufactureCode + "'");	
					}
					if (!(prodType.equalsIgnoreCase("ALL"))) {
						sqlBuilder.append( " and  b.strProdType <> '" + prodType + "'  ");
					}
				} else if (!(strGCode.equals("ALL")) && strSGCode.equals("ALL")) // for
																					// Particulor
																					// group
																					// and
																					// All
																					// SubGroup
				{
					sqlBuilder.setLength(0);
					sqlBuilder.append("select f.strPropertyName,a.strProdCode,b.strProdName,e.strLocName" + ",d.strGName,c.strSGName,b.strUOM,b.strBinNo"
							+ " ,if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice)/" + currValue + " , " + "a.dblOpeningStk,a.dblGRN,a.dblSCGRN" + ",a.dblStkTransIn,a.dblStkAdjIn,a.dblMISIn,a.dblQtyProduced" + ",a.dblSalesReturn,a.dblMaterialReturnIn,a.dblPurchaseReturn" + ",a.dblDeliveryNote,a.dblStkTransOut,a.dblStkAdjOut,a.dblMISOut" + ",a.dblQtyConsumed,a.dblSales,a.dblMaterialReturnOut "
							+ ",a.dblClosingStk,"
							+ "(a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice))/" + currValue + " as Value,"
							+ "a.dblClosingStk as IssueUOMStock " + ",b.dblIssueConversion,b.strIssueUOM,b.strPartNo "
							+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
							+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' " + "and a.strClientCode='" + clientCode + "' "
							+ "and c.strGCode='" + strGCode + "' " + "and a.strLocCode='" + locCode + "' and e.strPropertyCode='" + propCode + "' ");

					if (strNonStkItems.equals("Non Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='Y' ");
					} else if (strNonStkItems.equals("Stockable")) {
						sqlBuilder.append( "	and b.strNonStockableItem='N' ");
					}
					if(!strManufactureCode.equals("")){
						sqlBuilder.append(" and b.strManufacturerCode='" + strManufactureCode + "'");	
					}
					if (!(prodType.equalsIgnoreCase("ALL"))) {
						sqlBuilder.append(" and  b.strProdType <> '" + prodType + "'  ");
					}

				} else // // for Particulor group and Particulor SubGroup
				{
					sqlBuilder.setLength(0);
					sqlBuilder.append("select f.strPropertyName,a.strProdCode,b.strProdName,e.strLocName" + ",d.strGName,c.strSGName,b.strUOM,b.strBinNo"
							+ " ,(if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice))/" + currValue + ", " + "a.dblOpeningStk,a.dblGRN,a.dblSCGRN" + ",a.dblStkTransIn,a.dblStkAdjIn,a.dblMISIn,a.dblQtyProduced" + ",a.dblSalesReturn,a.dblMaterialReturnIn,a.dblPurchaseReturn" + ",a.dblDeliveryNote,a.dblStkTransOut,a.dblStkAdjOut,a.dblMISOut" + ",a.dblQtyConsumed,a.dblSales,a.dblMaterialReturnOut "
							+ ",a.dblClosingStk,"
							+ "(a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice)) as Value,"
							+ "a.dblClosingStk as IssueUOMStock " + ",b.dblIssueConversion,b.strIssueUOM,b.strPartNo "
							+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
							+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' " + "and a.strClientCode='" + clientCode + "' "
							+ "and c.strGCode='" + strGCode + "' and b.strSGCode='" + strSGCode + "' " + "and a.strLocCode='" + locCode + "' and e.strPropertyCode='" + propCode + "' ");

					if (strNonStkItems.equals("Non Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='Y' ");
					} else if (strNonStkItems.equals("Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='N' ");
					}
					if(!strManufactureCode.equals("")){
						sqlBuilder.append( " and b.strManufacturerCode='" + strManufactureCode + "'");	
					}
					if (!(prodType.equalsIgnoreCase("ALL"))) {
						sqlBuilder.append( " and  b.strProdType <> '" + prodType + "'  ");
					}
				}

			} else {
				sqlBuilder.setLength(0);
				sqlBuilder.append( "select f.strPropertyName,a.strProdCode,b.strProdName,e.strLocName" + " ,d.strGName,c.strSGName,b.strUOM,b.strBinNo"
				+ " ,(if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice))/" + currValue + " " + ",funGetUOM(a.dblOpeningStk,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM) as OpeningStk"

				+ " ,funGetUOM(a.dblGRN,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblSCGRN,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblStkTransIn,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblStkAdjIn,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblMISIn,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblQtyProduced,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblSalesReturn,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblMaterialReturnIn,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblPurchaseReturn,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblDeliveryNote,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblStkTransOut,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblStkAdjOut,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblMISOut,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblQtyConsumed,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblSales,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblMaterialReturnOut,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblClosingStk,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ ",(a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice))/" + currValue + " as Value," + "a.dblClosingStk as IssueUOMStock " + ",b.dblIssueConversion,b.strIssueUOM,b.strPartNo "
						
				+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
				+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' " + "and a.strClientCode='" + clientCode + "' ");
				

				if (strNonStkItems.equals("Non Stockable")) {
					sqlBuilder.append( "	and b.strNonStockableItem='Y' ");
				} else if (strNonStkItems.equals("Stockable")) {
					sqlBuilder.append("	and b.strNonStockableItem='N' ");
				}

				if (!(prodType.equalsIgnoreCase("ALL"))) {
					sqlBuilder.append(" and  b.strProdType <> '" + prodType + "'  ");
				}

				if (!strGCode.equalsIgnoreCase("All")) {
					sqlBuilder.append( "and d.strGCode='" + strGCode + "' ");
				}

				if (!strSGCode.equalsIgnoreCase("All")) {
					sqlBuilder.append( "and c.strSGCode='" + strSGCode + "' ");
				}
				if(!strManufactureCode.equals("")){
					sqlBuilder.append(" and b.strManufacturerCode='" + strManufactureCode + "'");	
				}
				
			}
			if (showZeroItems.equals("No")) {
				sqlBuilder.append( "and (a.dblOpeningStk >0 or a.dblGRN >0 or dblSCGRN >0 or a.dblStkTransIn >0 or a.dblStkAdjIn >0 " + "or a.dblMISIn >0 or a.dblQtyProduced >0 or a.dblMaterialReturnIn>0 or a.dblStkTransOut >0 " + "or a.dblStkAdjOut >0 or a.dblMISOut >0 or a.dblQtyConsumed  >0 or a.dblSales  >0 " + "or a.dblMaterialReturnOut  >0 or a.dblDeliveryNote > 0)");
			}

			List list = objGlobalService.funGetList(sqlBuilder.toString());

			List listStockFlashModel = new ArrayList();
			if (qtyWithUOM.equals("No")) {
				for (int cnt = 0; cnt < list.size(); cnt++) {
					Object[] arrObj = (Object[]) list.get(cnt);
					List dataList = new ArrayList();
					dataList.add(arrObj[0].toString());
					dataList.add(arrObj[1].toString());
					dataList.add(arrObj[2].toString());
					dataList.add(arrObj[3].toString());
					dataList.add(arrObj[4].toString());
					dataList.add(arrObj[5].toString());
					dataList.add(arrObj[6].toString());
					dataList.add(arrObj[7].toString());
					dataList.add(arrObj[8].toString());
					dataList.add(arrObj[9].toString());
					dataList.add(arrObj[10].toString());
					dataList.add(arrObj[11].toString());
					dataList.add(arrObj[12].toString());
					dataList.add(arrObj[13].toString());
					dataList.add(arrObj[14].toString());
					dataList.add(arrObj[15].toString());
					dataList.add(arrObj[16].toString());
					dataList.add(arrObj[17].toString());
					dataList.add(arrObj[18].toString());
					dataList.add(arrObj[19].toString());
					dataList.add(arrObj[20].toString());
					dataList.add(arrObj[21].toString());
					dataList.add(arrObj[22].toString());
					dataList.add(arrObj[23].toString());
					dataList.add(arrObj[24].toString());


					dataList.add(arrObj[26].toString());
					double value = Double.parseDouble(arrObj[27].toString());
					if (value < 0) {
						
					}
					
					
					dataList.add(value);
					dataList.add(arrObj[26].toString());
					dataList.add(arrObj[29].toString());
					dataList.add(arrObj[30].toString());
					dataList.add(arrObj[31].toString());

					listStockFlashModel.add(dataList);
					
					totalOpeningStock+=Double.parseDouble(arrObj[9].toString());
					totalGRN+= Double.parseDouble(arrObj[10].toString());
					totalSCGRN+= Double.parseDouble(arrObj[11].toString());
					totalStkTransferIn+= Double.parseDouble(arrObj[12].toString());
					totalStkAdjIn+= Double.parseDouble(arrObj[13].toString());
					totalMISIn+= Double.parseDouble(arrObj[14].toString());
					totalProducedQty+= Double.parseDouble(arrObj[15].toString());
					totalSalesRet+= Double.parseDouble(arrObj[16].toString());
					totalMaterialRet+= Double.parseDouble(arrObj[17].toString());
					totalPurchaseRet+= Double.parseDouble(arrObj[18].toString());
					totalDelNote+= Double.parseDouble(arrObj[19].toString());
					totalStkTransOut+= Double.parseDouble(arrObj[20].toString());
					totalStkAdjOut+= Double.parseDouble(arrObj[21].toString());
					totalMISOut+= Double.parseDouble(arrObj[22].toString());
					totalQtyConsumed+= Double.parseDouble(arrObj[23].toString());
					totalSaleAmt+= Double.parseDouble(arrObj[24].toString());
					totalClosingStk+= Double.parseDouble(arrObj[26].toString());
					totalValueTotal+= value;
					totalIssueUOMStk+= Double.parseDouble(arrObj[28].toString());

				}

			} else {
				for (int cnt = 0; cnt < list.size(); cnt++) {
					Object[] arrObj = (Object[]) list.get(cnt);
					List dataList = new ArrayList<>();
					dataList.add(arrObj[0].toString());
					dataList.add(arrObj[1].toString());
					dataList.add(arrObj[2].toString());
					dataList.add(arrObj[3].toString());
					dataList.add(arrObj[4].toString());
					dataList.add(arrObj[5].toString());
					dataList.add(arrObj[6].toString());
					dataList.add(arrObj[7].toString());
					dataList.add(arrObj[8].toString());
					dataList.add(funGetDecimalValue(arrObj[9].toString()));
					dataList.add(funGetDecimalValue(arrObj[10].toString()));
					dataList.add(funGetDecimalValue(arrObj[11].toString()));
					dataList.add(funGetDecimalValue(arrObj[12].toString()));
					dataList.add(funGetDecimalValue(arrObj[13].toString()));
					dataList.add(funGetDecimalValue(arrObj[14].toString()));
					dataList.add(funGetDecimalValue(arrObj[15].toString()));
					dataList.add(funGetDecimalValue(arrObj[16].toString()));
					dataList.add(funGetDecimalValue(arrObj[17].toString()));
					dataList.add(funGetDecimalValue(arrObj[18].toString()));
					dataList.add(funGetDecimalValue(arrObj[19].toString()));
					dataList.add(funGetDecimalValue(arrObj[20].toString()));
					dataList.add(funGetDecimalValue(arrObj[21].toString()));
					dataList.add(funGetDecimalValue(arrObj[22].toString()));
					dataList.add(funGetDecimalValue(arrObj[23].toString()));
					dataList.add(funGetDecimalValue(arrObj[24].toString()));

					dataList.add(funGetDecimalValue(arrObj[26].toString()));
					double value = Double.parseDouble(arrObj[27].toString());
					if (value < 0) {
						// value=value*(-1);
					}
					
					//dblValueTotal += value;
					dataList.add(value);
					dataList.add(funGetDecimalValue(arrObj[26].toString()));
					dataList.add(arrObj[29].toString());
					dataList.add(arrObj[30].toString());
					dataList.add(arrObj[31].toString());

					listStockFlashModel.add(dataList);
					
					/*totalOpeningStock+=Double.parseDouble(arrObj[9].toString().split(" ")[0]);
					totalGRN+= Double.parseDouble(arrObj[10].toString());
					totalSCGRN+= Double.parseDouble(arrObj[11].toString());
					totalStkTransferIn+= Double.parseDouble(arrObj[12].toString());
					totalStkAdjIn+= Double.parseDouble(arrObj[13].toString());
					totalMISIn+= Double.parseDouble(arrObj[14].toString());
					totalProducedQty+= Double.parseDouble(arrObj[15].toString());
					totalSalesRet+= Double.parseDouble(arrObj[16].toString());
					totalMaterialRet+= Double.parseDouble(arrObj[17].toString());
					totalPurchaseRet+= Double.parseDouble(arrObj[18].toString());
					totalDelNote+= Double.parseDouble(arrObj[19].toString());
					totalStkTransOut+= Double.parseDouble(arrObj[20].toString());
					totalStkAdjOut+= Double.parseDouble(arrObj[21].toString());
					totalMISOut+= Double.parseDouble(arrObj[22].toString());
					totalQtyConsumed+= Double.parseDouble(arrObj[23].toString());
					totalSaleAmt+= Double.parseDouble(arrObj[24].toString());
					totalClosingStk+= Double.parseDouble(arrObj[26].toString());
					totalValueTotal+= value;
					totalIssueUOMStk+= Double.parseDouble(arrObj[28].toString());*/
				}
			}
			List dataList = new ArrayList<>();
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			/*dataList.add("Total ");
			dataList.add(totalOpeningStock);
			dataList.add(totalGRN);
			dataList.add(totalSCGRN);
			dataList.add(totalStkTransferIn);
			dataList.add(totalStkAdjIn);
			dataList.add(totalMISIn);
			dataList.add(totalProducedQty);
			dataList.add(totalSalesRet);
			dataList.add(totalMaterialRet);
			dataList.add(totalPurchaseRet);
			dataList.add(totalDelNote);
			dataList.add(totalStkTransOut);
			dataList.add(totalStkAdjOut);
			dataList.add(totalMISOut);
			dataList.add(totalQtyConsumed);
			dataList.add(totalSaleAmt);
			dataList.add(totalClosingStk);
			NumberFormat formatter = new DecimalFormat("###.#####");
			String f = formatter.format(totalValueTotal);
			dataList.add(f);
			dataList.add(totalIssueUOMStk);*/
			
			dataList.add(" ");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			NumberFormat formatter = new DecimalFormat("###.#####");
			String f = formatter.format(totalValueTotal);
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");

			listStockFlashModel.add(dataList);
			listStock.add(listStockFlashModel);
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("excelViewWithReportName", "listWithReportName", listStock);
		} else if ("Summary".equalsIgnoreCase(reportType)) {
			String[] ExcelHeader = { "Property Name", "Product Code", "Product Name", "Location", "Group", "Sub Group", "UOM", "Bin No", "Unit Price", "Opening Stock", "Receipts", "Issue", "Closing Stock", "Value", "Issue UOM Stock", "Issue Conversion", "Issue UOM", "Part No" };
			listStock.add(ExcelHeader);

			String startDate = req.getSession().getAttribute("startDate").toString();
			String[] sp = startDate.split(" ");
			String[] spDate = sp[0].split("/");
			startDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];
			System.out.println(startDate);
			objGlobal.funInvokeStockFlash(startDate, locCode, fromDate, toDate, clientCode, userCode, stockableItem, req, resp);

			
			StringBuilder sqlBuilder = new StringBuilder();


			if (qtyWithUOM.equals("No")) {
				sqlBuilder.setLength(0);
				sqlBuilder.append("select f.strPropertyName,a.strProdCode,b.strProdName,e.strLocName" + ",d.strGName,c.strSGName,b.strUOM,b.strBinNo "
						+ " ,if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice), " + "a.dblOpeningStk,(a.dblGRN+dblSCGRN+a.dblStkTransIn+a.dblStkAdjIn+a.dblMISIn+a.dblQtyProduced+a.dblMaterialReturnIn) as Receipts " + ",(a.dblStkTransOut-a.dblStkAdjOut-a.dblMISOut-a.dblQtyConsumed-a.dblSales-a.dblMaterialReturnOut-a.dblDeliveryNote) as Issue " + ",a.dblClosingStk,"
						+ "(a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice)) as Value," + "a.dblClosingStk as IssueUOMStock " + ",b.dblIssueConversion,b.strIssueUOM,b.strPartNo "
						+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
						+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' " + "and a.strClientCode='" + clientCode + "' ");
			

				if (strNonStkItems.equals("Non Stockable")) {
					sqlBuilder.append("	and b.strNonStockableItem='Y' ");
				} else if (strNonStkItems.equals("Stockable")) {
					sqlBuilder.append( "	and b.strNonStockableItem='N' ");
				}

				if (!(prodType.equalsIgnoreCase("ALL"))) {
					sqlBuilder.append( " and  b.strProdType <> '" + prodType + "'  ");
				}
				if(!strManufactureCode.equals("")){
					sqlBuilder.append( " and b.strManufacturerCode='" + strManufactureCode + "'");	
				}
			} else {
				sqlBuilder.setLength(0);
				sqlBuilder.append( "select f.strPropertyName,a.strProdCode,b.strProdName,e.strLocName" + ",d.strGName,c.strSGName,b.strUOM,b.strBinNo "
						+ " ,if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice) "
						+ ",funGetUOM(a.dblOpeningStk,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM) as OpeningStk"
						+ ",funGetUOM((a.dblGRN+dblSCGRN+a.dblStkTransIn+a.dblStkAdjIn+a.dblMISIn+a.dblQtyProduced+a.dblMaterialReturnIn),b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM) as Receipts "
						+ ",funGetUOM((a.dblStkTransOut-a.dblStkAdjOut-a.dblMISOut-a.dblQtyConsumed-a.dblSales-a.dblMaterialReturnOut-a.dblDeliveryNote),b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM) as Issue "
						+ ",funGetUOM(a.dblClosingStk,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"
						+ ",(a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice)) as Value," + " a.dblClosingStk as IssueUOMStock " + ",b.dblIssueConversion,b.strIssueUOM,b.strPartNo "
						+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
						+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' "
						+ "and a.strClientCode='" + clientCode + "' ");
	
				if (strNonStkItems.equals("Non Stockable")) {
					sqlBuilder.append( "	and b.strNonStockableItem='Y' ");
				} else if (strNonStkItems.equals("Stockable")) {
					sqlBuilder.append( "	and b.strNonStockableItem='N' ");
				}

				if (!(prodType.equalsIgnoreCase("ALL"))) {
					sqlBuilder.append( " and  b.strProdType <> '" + prodType + "'  ");
				}
				if(!strManufactureCode.equals("")){
					sqlBuilder.append(" and b.strManufacturerCode='" + strManufactureCode + "'");	
				}

			}

			if (!strGCode.equalsIgnoreCase("All")) {
				sqlBuilder.append( "and d.strGCode='" + strGCode + "' ");
			}

			if (!strSGCode.equalsIgnoreCase("All")) {
				sqlBuilder.append( "and c.strSGCode='" + strSGCode + "' ");
			}


			if (showZeroItems.equals("No")) {
				sqlBuilder.append( "and (a.dblOpeningStk >0 or a.dblGRN >0 or dblSCGRN >0 or a.dblStkTransIn >0 or a.dblStkAdjIn >0 " + "or a.dblMISIn >0 or a.dblQtyProduced >0 or a.dblMaterialReturnIn>0 or a.dblStkTransOut >0 " + "or a.dblStkAdjOut >0 or a.dblMISOut >0 or a.dblQtyConsumed  >0 or a.dblSales  >0 " + "or a.dblMaterialReturnOut  >0 or a.dblDeliveryNote > 0)");
			}

			List list = objGlobalService.funGetList(sqlBuilder.toString());
			List listStockFlashModel = new ArrayList();

			for (int cnt = 0; cnt < list.size(); cnt++) {
				Object[] arrObj = (Object[]) list.get(cnt);
				List dataList = new ArrayList<>();

				dataList.add(arrObj[0].toString());
				dataList.add(arrObj[1].toString());
				dataList.add(arrObj[2].toString());
				dataList.add(arrObj[3].toString());
				dataList.add(arrObj[4].toString());
				dataList.add(arrObj[5].toString());
				dataList.add(arrObj[6].toString());
				dataList.add(arrObj[7].toString());
				dataList.add(arrObj[8].toString());
				dataList.add(arrObj[9].toString());
				dataList.add(arrObj[10].toString());
				double issueQty = 0.0;
				if (qtyWithUOM.equals("Yes")) {
					if (!arrObj[11].toString().equals("")) {
						issueQty = Double.parseDouble(arrObj[11].toString().split(" ")[0]);
						if (issueQty < 0) {
							issueQty = issueQty * (-1);
						}
						dataList.add(issueQty);

					} else {
						dataList.add(arrObj[11].toString());

					}
				} else {
					issueQty = Double.parseDouble(arrObj[11].toString());
					if (issueQty < 0) {
						issueQty = issueQty * (-1);
					}
					dataList.add(issueQty);
				}

				dataList.add(arrObj[12].toString());

				double value = Double.parseDouble(arrObj[13].toString());
			
				if (value < 0) {
					value = value * (-1);
				}
				dataList.add(value);
				dataList.add(arrObj[12].toString());
				dataList.add(arrObj[15].toString());
				dataList.add(arrObj[16].toString());
				dataList.add(arrObj[17].toString());

				listStockFlashModel.add(dataList);
				
				/*totalOpeningStock+=Double.parseDouble(arrObj[9].toString());
				totalReciept+= Double.parseDouble(arrObj[10].toString());
				if ( Double.parseDouble(arrObj[11].toString()) < 0) 
				{
					totalIssue+= Double.parseDouble(arrObj[11].toString()) * (-1);
				}
				else
				{
					totalIssue+= Double.parseDouble(arrObj[11].toString());
				}
				totalClosingStk+= Double.parseDouble(arrObj[12].toString());
				totalValueTotal+= value;
				totalIssueUOMStk+= Double.parseDouble(arrObj[14].toString());*/
			}
			List dataList = new ArrayList<>();
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
//			dataList.add("Total ");
//			dataList.add(totalOpeningStock);
//			dataList.add(totalReciept);
//			dataList.add(totalIssue);
//			dataList.add(totalClosingStk);
//			dataList.add(totalValueTotal);
//			dataList.add(totalIssueUOMStk);
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");
			dataList.add("");

			listStockFlashModel.add(dataList);
			listStock.add(listStockFlashModel);
			return new ModelAndView("excelViewWithReportName", "listWithReportName", listStock);
		} 
		else if("Total".equalsIgnoreCase(reportType))
		{
			String[] ExcelHeader = { "Transaction Type","Value"};
			listStock.add(ExcelHeader);

			String startDate = req.getSession().getAttribute("startDate").toString();
			String[] sp = startDate.split(" ");
			String[] spDate = sp[0].split("/");
			startDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];
			System.out.println(startDate);
			objGlobal.funInvokeStockFlash(startDate, locCode, fromDate, toDate, clientCode, userCode, stockableItem, req, resp);

			StringBuilder sqlBuilder = new StringBuilder();
			if (qtyWithUOM.equals("No")) {
				if (strGCode.equals("ALL") && strSGCode.equals("ALL")) 
				{
					sqlBuilder.setLength(0);
					sqlBuilder.append("select f.strPropertyName,a.strProdCode,b.strProdName,e.strLocName" + ",d.strGName,c.strSGName,b.strUOM,b.strBinNo"
							+ " ,(if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice))/" + currValue + " , " + "a.dblOpeningStk,a.dblGRN,a.dblSCGRN" + ",a.dblStkTransIn,a.dblStkAdjIn,a.dblMISIn,a.dblQtyProduced" + ",a.dblSalesReturn,a.dblMaterialReturnIn,a.dblPurchaseReturn" + ",a.dblDeliveryNote,a.dblStkTransOut,a.dblStkAdjOut,a.dblMISOut" + ",a.dblQtyConsumed,a.dblSales,a.dblMaterialReturnOut "
							+ ",a.dblClosingStk,"
							+ "(a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice))/" + currValue + " as Value " + ",a.dblClosingStk as IssueUOMStock " + ",b.dblIssueConversion,b.strIssueUOM,b.strPartNo "
							+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
							+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' " + "and a.strClientCode='" + clientCode + "'  " + "and a.strLocCode='" + locCode + "' and e.strPropertyCode='" + propCode + "' ");

					if (strNonStkItems.equals("Non Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='Y' ");
					} else if (strNonStkItems.equals("Stockable")) {
						sqlBuilder.append( "	and b.strNonStockableItem='N' ");
					}
					if(!strManufactureCode.equals("")){
						sqlBuilder.append( " and b.strManufacturerCode='" + strManufactureCode + "'");	
					}
					if (!(prodType.equalsIgnoreCase("ALL"))) {
						sqlBuilder.append( " and  b.strProdType <> '" + prodType + "'  ");
					}
				} else if (!(strGCode.equals("ALL")) && strSGCode.equals("ALL")) 
				{
					sqlBuilder.setLength(0);
					sqlBuilder.append("select f.strPropertyName,a.strProdCode,b.strProdName,e.strLocName" + ",d.strGName,c.strSGName,b.strUOM,b.strBinNo"
							+ " ,if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice)/" + currValue + " , " + "a.dblOpeningStk,a.dblGRN,a.dblSCGRN" + ",a.dblStkTransIn,a.dblStkAdjIn,a.dblMISIn,a.dblQtyProduced" + ",a.dblSalesReturn,a.dblMaterialReturnIn,a.dblPurchaseReturn" + ",a.dblDeliveryNote,a.dblStkTransOut,a.dblStkAdjOut,a.dblMISOut" + ",a.dblQtyConsumed,a.dblSales,a.dblMaterialReturnOut "
							+ ",a.dblClosingStk,"
							+ "(a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice))/" + currValue + " as Value,"
							+ "a.dblClosingStk as IssueUOMStock " + ",b.dblIssueConversion,b.strIssueUOM,b.strPartNo "
							+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
							+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' " + "and a.strClientCode='" + clientCode + "' "
							+ "and c.strGCode='" + strGCode + "' " + "and a.strLocCode='" + locCode + "' and e.strPropertyCode='" + propCode + "' ");

					if (strNonStkItems.equals("Non Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='Y' ");
					} else if (strNonStkItems.equals("Stockable")) {
						sqlBuilder.append( "	and b.strNonStockableItem='N' ");
					}
					if(!strManufactureCode.equals("")){
						sqlBuilder.append( " and b.strManufacturerCode='" + strManufactureCode + "'");	
					}
					if (!(prodType.equalsIgnoreCase("ALL"))) {
						sqlBuilder.append(" and  b.strProdType <> '" + prodType + "'  ");
					}

				} else // // for Particulor group and Particulor SubGroup
				{
					sqlBuilder.setLength(0);
					sqlBuilder.append( "select f.strPropertyName,a.strProdCode,b.strProdName,e.strLocName" + ",d.strGName,c.strSGName,b.strUOM,b.strBinNo"					
							+ " ,(if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice))/" + currValue + ", " + "a.dblOpeningStk,a.dblGRN,a.dblSCGRN" + ",a.dblStkTransIn,a.dblStkAdjIn,a.dblMISIn,a.dblQtyProduced" + ",a.dblSalesReturn,a.dblMaterialReturnIn,a.dblPurchaseReturn" + ",a.dblDeliveryNote,a.dblStkTransOut,a.dblStkAdjOut,a.dblMISOut" + ",a.dblQtyConsumed,a.dblSales,a.dblMaterialReturnOut "
							+ ",a.dblClosingStk,"							
							+ "(a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice)) as Value,"
							+ "a.dblClosingStk as IssueUOMStock " + ",b.dblIssueConversion,b.strIssueUOM,b.strPartNo "
							+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
							+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' " + "and a.strClientCode='" + clientCode + "' "
							+ "and c.strGCode='" + strGCode + "' and b.strSGCode='" + strSGCode + "' " + "and a.strLocCode='" + locCode + "' and e.strPropertyCode='" + propCode + "' ");

					if (strNonStkItems.equals("Non Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='Y' ");
					} else if (strNonStkItems.equals("Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='N' ");
					}
					if(!strManufactureCode.equals("")){
						sqlBuilder.append(" and b.strManufacturerCode='" + strManufactureCode + "'");	
					}
					if (!(prodType.equalsIgnoreCase("ALL"))) {
						sqlBuilder.append(" and  b.strProdType <> '" + prodType + "'  ");
					}
				}

			} else {
				sqlBuilder.setLength(0);
				sqlBuilder.append( "select f.strPropertyName,a.strProdCode,b.strProdName,e.strLocName" + " ,d.strGName,c.strSGName,b.strUOM,b.strBinNo"
				
				+ " ,(if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice))/" + currValue + " " + ",funGetUOM(a.dblOpeningStk,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM) as OpeningStk"

				+ " ,funGetUOM(a.dblGRN,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblSCGRN,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblStkTransIn,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblStkAdjIn,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblMISIn,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblQtyProduced,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblSalesReturn,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblMaterialReturnIn,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblPurchaseReturn,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblDeliveryNote,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblStkTransOut,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblStkAdjOut,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblMISOut,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblQtyConsumed,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblSales,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblMaterialReturnOut,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ " ,funGetUOM(a.dblClosingStk,b.dblRecipeConversion,b.dblIssueConversion,b.strReceivedUOM,b.strRecipeUOM)"

				+ ",(a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice))/" + currValue + " as Value," + "a.dblClosingStk as IssueUOMStock " + ",b.dblIssueConversion,b.strIssueUOM,b.strPartNo "
				+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
				+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' " + "and a.strClientCode='" + clientCode + "' ");
				

				if (strNonStkItems.equals("Non Stockable")) {
					sqlBuilder.append( "	and b.strNonStockableItem='Y' ");
				} else if (strNonStkItems.equals("Stockable")) {
					sqlBuilder.append( "	and b.strNonStockableItem='N' ");
				}

				if (!(prodType.equalsIgnoreCase("ALL"))) {
					sqlBuilder.append(  " and  b.strProdType <> '" + prodType + "'  ");
				}

				if (!strGCode.equalsIgnoreCase("All")) {
					sqlBuilder.append(  "and d.strGCode='" + strGCode + "' ");
				}

				if (!strSGCode.equalsIgnoreCase("All")) {
					sqlBuilder.append( "and c.strSGCode='" + strSGCode + "' ");
				}
				if(!strManufactureCode.equals("")){
					sqlBuilder.append( " and b.strManufacturerCode='" + strManufactureCode + "'");	
				}

			}
			if (showZeroItems.equals("No")) {
				sqlBuilder.append( "and (a.dblOpeningStk >0 or a.dblGRN >0 or dblSCGRN >0 or a.dblStkTransIn >0 or a.dblStkAdjIn >0 " + "or a.dblMISIn >0 or a.dblQtyProduced >0 or a.dblMaterialReturnIn>0 or a.dblStkTransOut >0 " + "or a.dblStkAdjOut >0 or a.dblMISOut >0 or a.dblQtyConsumed  >0 or a.dblSales  >0 " + "or a.dblMaterialReturnOut  >0 or a.dblDeliveryNote > 0)");
			}

			
			List list = objGlobalService.funGetList(sqlBuilder.toString());

			List listStockFlashModel = new ArrayList();
			if (qtyWithUOM.equals("No")) {
				for (int cnt = 0; cnt < list.size(); cnt++) {
					Object[] arrObj = (Object[]) list.get(cnt);
					double value = Double.parseDouble(arrObj[27].toString());
					if (value < 0) {
						// value=value*(-1);
					}
					totalOpeningStock+=Double.parseDouble(arrObj[9].toString());
					totalGRN+= Double.parseDouble(arrObj[10].toString());
					totalSCGRN+= Double.parseDouble(arrObj[11].toString());
					totalStkTransferIn+= Double.parseDouble(arrObj[12].toString());
					totalStkAdjIn+= Double.parseDouble(arrObj[13].toString());
					totalMISIn+= Double.parseDouble(arrObj[14].toString());
					totalProducedQty+= Double.parseDouble(arrObj[15].toString());
					totalSalesRet+= Double.parseDouble(arrObj[16].toString());
					totalMaterialRet+= Double.parseDouble(arrObj[17].toString());
					totalPurchaseRet+= Double.parseDouble(arrObj[18].toString());
					totalDelNote+= Double.parseDouble(arrObj[19].toString());
					totalStkTransOut+= Double.parseDouble(arrObj[20].toString());
					totalStkAdjOut+= Double.parseDouble(arrObj[21].toString());
					totalMISOut+= Double.parseDouble(arrObj[22].toString());
					totalQtyConsumed+= Double.parseDouble(arrObj[23].toString());
					totalSaleAmt+= Double.parseDouble(arrObj[24].toString());
					totalClosingStk+= Double.parseDouble(arrObj[26].toString());
					totalValueTotal+= value;
					totalIssueUOMStk+= Double.parseDouble(arrObj[28].toString());

				}

			} else {
				for (int cnt = 0; cnt < list.size(); cnt++) {
					Object[] arrObj = (Object[]) list.get(cnt);
					double value = Double.parseDouble(arrObj[27].toString());
					if (value < 0) {
						
					}
					totalOpeningStock+=Double.parseDouble(arrObj[9].toString());
					totalGRN+= Double.parseDouble(arrObj[10].toString());
					totalSCGRN+= Double.parseDouble(arrObj[11].toString());
					totalStkTransferIn+= Double.parseDouble(arrObj[12].toString());
					totalStkAdjIn+= Double.parseDouble(arrObj[13].toString());
					totalMISIn+= Double.parseDouble(arrObj[14].toString());
					totalProducedQty+= Double.parseDouble(arrObj[15].toString());
					totalSalesRet+= Double.parseDouble(arrObj[16].toString());
					totalMaterialRet+= Double.parseDouble(arrObj[17].toString());
					totalPurchaseRet+= Double.parseDouble(arrObj[18].toString());
					totalDelNote+= Double.parseDouble(arrObj[19].toString());
					totalStkTransOut+= Double.parseDouble(arrObj[20].toString());
					totalStkAdjOut+= Double.parseDouble(arrObj[21].toString());
					totalMISOut+= Double.parseDouble(arrObj[22].toString());
					totalQtyConsumed+= Double.parseDouble(arrObj[23].toString());
					totalSaleAmt+= Double.parseDouble(arrObj[24].toString());
					totalClosingStk+= Double.parseDouble(arrObj[26].toString());
					totalValueTotal+= value;
					totalIssueUOMStk+= Double.parseDouble(arrObj[28].toString());
				}
			}
			List dataList = new ArrayList<>();
			dataList.add("Opening Stock");
			dataList.add(totalOpeningStock);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("GRN");
			dataList.add(totalGRN);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("SCGRN");
			dataList.add(totalSCGRN);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Stock Transfer");
			dataList.add(totalStkTransferIn);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Stock Adj In");
			dataList.add(totalStkAdjIn);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("MIS In");
			dataList.add(totalMISIn);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Qty Produced");
			dataList.add(totalProducedQty);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Sales Return");
			dataList.add(totalSalesRet);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Material Return");
			dataList.add(totalMaterialRet);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Purchase Return");
			dataList.add(totalPurchaseRet);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Delivery Note");
			dataList.add(totalDelNote);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Stock Transfer out");
			dataList.add(totalStkTransOut);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Stock Adj Out");
			dataList.add(totalStkAdjOut);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("MIS Out");
			dataList.add(totalMISOut);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Quantity Consumed");
			dataList.add(totalQtyConsumed);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Sale Amount");
			dataList.add(totalSaleAmt);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Closing Stock");
			dataList.add(totalClosingStk);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Value");
			NumberFormat formatter = new DecimalFormat("###.#####");
			String f = formatter.format(totalValueTotal);
			dataList.add(f);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("Issue UOM Stock");
			dataList.add(totalIssueUOMStk);
			listStockFlashModel.add(dataList);
			dataList = new ArrayList<>();
			dataList.add("");
			dataList.add("");
			listStockFlashModel.add(dataList);
			listStock.add(listStockFlashModel);
			return new ModelAndView("excelViewWithReportName", "listWithReportName", listStock);
		}
		else // if("Mini".equalsIgnoreCase(reportType))
		{

			String[] ExcelHeader = { "Product Code", "Product Name", "Closing Stock", "Value" };
			listStock.add(ExcelHeader);

			String startDate = req.getSession().getAttribute("startDate").toString();
			String[] sp = startDate.split(" ");
			String[] spDate = sp[0].split("/");
			startDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];
			System.out.println(startDate);
			objGlobal.funInvokeStockFlash(startDate, locCode, fromDate, toDate, clientCode, userCode, stockableItem, req, resp);

			StringBuilder sqlBuilder = new StringBuilder();
			if (qtyWithUOM.equals("No")) {
				if (strGCode.equals("ALL") && strSGCode.equals("ALL")) // for
																		// All
																		// Group
																		// and
																		// All
																		// SubGroup
				{
					sqlBuilder.setLength(0);
					sqlBuilder.append( " select a.strProdCode,b.strProdName, "
							+ " a.dblClosingStk,"
							+ " (a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice)) as Value "
							+ ",d.strGName,c.strSGName "
							+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
							+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' "
							+ " and a.strClientCode='" + clientCode + "' "
							+ " and a.strLocCode='" + locCode + "' and e.strPropertyCode='" + propCode + "' " + "  order by c.intSortingNo ");
					if (strNonStkItems.equals("Non Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='Y' ");
					} else if (strNonStkItems.equals("Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='N' ");
					}

					if (!(prodType.equalsIgnoreCase("ALL"))) {
						sqlBuilder.append(" and  b.strProdType <> '" + prodType + "'  ");
					}
				} else if (!(strGCode.equals("ALL")) && strSGCode.equals("ALL")) // for
																					// Particulor
																					// group
																					// and
																					// All
																					// SubGroup
				{
					sqlBuilder.setLength(0);
					sqlBuilder.append( "select a.strProdCode,b.strProdName," + " a.dblClosingStk,"
							+ "(a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice)) as Value " + ",d.strGName,c.strSGName "						
							+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
							+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' " + " and a.strClientCode='" + clientCode + "' "
							+ " and c.strGCode='" + strGCode + "' " + " and a.strLocCode='" + locCode + "' and e.strPropertyCode='" + propCode + "' " + "  order by c.intSortingNo ");

					if (strNonStkItems.equals("Non Stockable")) {
						sqlBuilder.append( "	and b.strNonStockableItem='Y' ");
					} else if (strNonStkItems.equals("Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='N' ");
					}

					if (!(prodType.equalsIgnoreCase("ALL"))) {
						sqlBuilder.append(" and  b.strProdType <> '" + prodType + "'  ");
					}
					if(!strManufactureCode.equals("")){
						sqlBuilder.append(" and b.strManufacturerCode='" + strManufactureCode + "'");	
					}

				} else // // for Particulor group and Particulor SubGroup
				{
					sqlBuilder.setLength(0);
					sqlBuilder.append( "select a.strProdCode,b.strProdName, " + " a.dblClosingStk,"							
							+ ",(a.dblClosingStk*if(ifnull(g.dblPrice,0)=0,b.dblCostRM,g.dblPrice)) as Value" + ",d.strGName,c.strSGName "							
							+ " FROM tblcurrentstock a " + " left outer join tblproductmaster b on a.strProdCode=b.strProdCode " + " left outer join tblsubgroupmaster c on b.strSGCode=c.strSGCode " + " left outer join tblgroupmaster d on c.strGCode=d.strGCode " + " left outer join tbllocationmaster e on a.strLocCode=e.strLocCode "
							+ " left outer join tblpropertymaster f on e.strPropertyCode=f.strPropertyCode " + " left outer join tblreorderlevel g on a.strProdCode=g.strProdCode and g.strLocationCode='" + locCode + "'  " + " where  a.strUserCode='" + userCode + "' "
							+ " and a.strClientCode='" + clientCode + "' "							
							+ " and c.strGCode='" + strGCode + "' and b.strSGCode='" + strSGCode + "' " + " and a.strLocCode='" + locCode + "' and e.strPropertyCode='" + propCode + "' " + "  order by c.intSortingNo ");

					if (strNonStkItems.equals("Non Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='Y' ");
					} else if (strNonStkItems.equals("Stockable")) {
						sqlBuilder.append("	and b.strNonStockableItem='N' ");
					}

					if (!(prodType.equalsIgnoreCase("ALL"))) {
						sqlBuilder.append(" and  b.strProdType <> '" + prodType + "'  ");
					}
					if(!strManufactureCode.equals("")){
						sqlBuilder.append(" and b.strManufacturerCode='" + strManufactureCode + "'");	
					}
				}

			}

			if (showZeroItems.equals("No")) {
				sqlBuilder.append( "and (a.dblOpeningStk >0 or a.dblGRN >0 or dblSCGRN >0 or a.dblStkTransIn >0 or a.dblStkAdjIn >0 " + "or a.dblMISIn >0 or a.dblQtyProduced >0 or a.dblMaterialReturnIn>0 or a.dblStkTransOut >0 " + "or a.dblStkAdjOut >0 or a.dblMISOut >0 or a.dblQtyConsumed  >0 or a.dblSales  >0 " + "or a.dblMaterialReturnOut  >0 or a.dblDeliveryNote > 0)");
			}

			List list = objGlobalService.funGetList(sqlBuilder.toString());

			List listStockFlashModel = new ArrayList();

			for (int cnt = 0; cnt < list.size(); cnt++) {
				Object[] arrObj = (Object[]) list.get(cnt);
				List dataList = new ArrayList<>();
				dataList.add(arrObj[0].toString());
				dataList.add(arrObj[1].toString());
				dataList.add(arrObj[2].toString());
				double value = Double.parseDouble(arrObj[3].toString());
				if (value < 0) {
					
				}
				dataList.add(value);

				listStockFlashModel.add(dataList);

			}
			listStock.add(listStockFlashModel);
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("excelViewWithReportName", "listWithReportName", listStock);

		}

	}

	private String funGetDecimalValue(String strValue) {
		String strVal = "";
		String[] spl = strValue.split(" ");
		if (spl.length == 2) // for Single UOM
		{

			String[] splValue = strValue.split("\\.");
			if (splValue.length == 2) {
				String firstValue = splValue[0];
				String secondValue = splValue[1];
				strVal = firstValue + " " + secondValue.split(" ")[1];
			} else {
				strVal = splValue[0];
			}
		}
		if (spl.length == 3)// for Two UOM
		{
			String[] splValue = strValue.split("\\.");
			if (splValue.length > 1) {
				String firstValue = splValue[0];
				String secondValue = splValue[1];
				if (splValue.length == 3) {
					strVal = firstValue + " " + secondValue + " " + splValue[2].split(" ")[1];
				}
				if (splValue.length == 2) {
					strVal = firstValue + " " + secondValue;
				}
			} else {
				strVal = splValue[0];
			}
		}

		return strVal;
	}



}
