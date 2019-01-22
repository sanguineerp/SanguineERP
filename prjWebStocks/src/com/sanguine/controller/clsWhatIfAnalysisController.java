package com.sanguine.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.bean.clsMISBean;
import com.sanguine.bean.clsWhatIfAnalysisBean;
import com.sanguine.bean.clsWorkOrderBean;
import com.sanguine.model.clsBomDtlModel;
import com.sanguine.model.clsProductMasterModel;
import com.sanguine.model.clsWhatIfAnalysisModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsProductMasterService;
import com.sanguine.controller.clsWhatIfAnalysisController;

@Controller
public class clsWhatIfAnalysisController {

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	clsGlobalFunctions objGlobalFunction;

	@Autowired
	clsProductMasterService objProductMasterService;

	Map<String, List<String>> mapChildNodes;
	List<List<String>> listChildNodes;
	List<clsWhatIfAnalysisModel> listWhatIfAnalysis;

	@RequestMapping(value = "/frmWhatIfAnalysis", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		clsWhatIfAnalysisBean objBean = new clsWhatIfAnalysisBean();
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmWhatIfAnalysis_1", "command", objBean);
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmWhatIfAnalysis", "command", objBean);
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/saveWhatIfAnanlysis", method = RequestMethod.POST)
	public ModelAndView funSaveWhatIf(@ModelAttribute("command") clsWhatIfAnalysisBean objBean, Model model, BindingResult result, HttpServletRequest req) {
		mapChildNodes = new HashMap<String, List<String>>();
		listChildNodes = new ArrayList<List<String>>();
		listWhatIfAnalysis = new ArrayList<clsWhatIfAnalysisModel>();
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		for (String product : objBean.getListProduct()) {
			List list = funGetAllChildNodes(product.split(",")[0], clientCode, Double.parseDouble(product.split(",")[3]));
		}
		return new ModelAndView("frmWhatIfAnalysisFlash");
	}

	@RequestMapping(value = "/loadProductFromRecipe", method = RequestMethod.GET)
	public @ResponseBody List funLoadProduct(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String prodCode = request.getParameter("prodCode").toString();
		String sql = "select a.strParentCode,b.strPartNo,b.strProdName,b.dblCostRM " + "from clsBomHdModel a, clsProductMasterModel b " + "where a.strParentCode=b.strProdCode and a.strParentCode='" + prodCode + "' " + "and a.strClientCode='" + clientCode + "'";
		List listRecipeProduct = objGlobalFunctionsService.funGetList(sql, "hql");
		return listRecipeProduct;
	}

	@RequestMapping(value = "/getChildNodes", method = RequestMethod.GET)
	public @ResponseBody List<List<String>> funGetChildNodes(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String param = request.getParameter("prodCode").toString();
		String[] sp = param.split(",");
		mapChildNodes = new HashMap<String, List<String>>();
		listChildNodes = new ArrayList<List<String>>();
		List list = funGetAllChildNodes(sp[0], clientCode, Double.parseDouble(sp[1]));
		return listChildNodes;
	}


	@RequestMapping(value = "/getChildNodes1", method = RequestMethod.GET)
	public @ResponseBody Map<String, clsWhatIfAnalysisFields> funGetChildNodes1(HttpServletRequest request) {
		Map<String, clsWhatIfAnalysisFields> hmChildNodes = new HashMap<String, clsWhatIfAnalysisFields>();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List<String> listChildNodes = new ArrayList<String>();
		String param = request.getParameter("prodCode").toString();
		String rateFrom = request.getParameter("rateFrom").toString();
		String semiProduct = request.getParameter("semiProduct").toString();
		
		
		param = param.substring(1, param.length());
		String[] sp = param.split(",");
		for (int cn = 0; cn < sp.length; cn++) {
			String sp1[] = sp[cn].split("!");
			double reqdQty = Double.parseDouble(sp1[1]);
			if(semiProduct.equals("Yes"))
			{
			funGetBOMNodes(sp1[0], 0, reqdQty, listChildNodes,semiProduct);
			}else{
			funGetBOMNodes(sp1[0], 0, reqdQty, listChildNodes);
			}
		}
		String proprtyWiseStock="N";
		for (int cnt = 0; cnt < listChildNodes.size(); cnt++) {
			String temp = (String) listChildNodes.get(cnt);
			String prodCode = temp.split(",")[0];
			double reqdQty = Double.parseDouble(temp.split(",")[1]);
			double openPOQty = funGetOpenPOQty(prodCode, clientCode);
			String startDate = request.getSession().getAttribute("startDate").toString();
			String[] fmDate = startDate.split(" ");
			String[] spDate = fmDate[0].split("/");
			startDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];
			String toDate = objGlobalFunction.funGetCurrentDateTime("yyyy-MM-dd");
			String locationCode = request.getSession().getAttribute("locationCode").toString();
			
			double currentStock = objGlobalFunction.funGetCurrentStockForProduct(prodCode, locationCode, clientCode, userCode, startDate, toDate,proprtyWiseStock);
			double orderQty = reqdQty - (openPOQty + currentStock);
			if (orderQty < 0)
				orderQty = 0;
			
			String productInfo = funGetProdInfo(prodCode);
			String leadTime = "0", prodName = "", uom = "", suppCode = "", suppName = "";
			double conversion=1;
			
			
			if (productInfo.trim().length() > 0) {
				String[] spProd = productInfo.split("#");
				prodName = spProd[0];
				uom = spProd[7];
				suppCode = spProd[2];
				suppName = spProd[3];
				conversion = Double.parseDouble(spProd[4]);
				if(spProd.length>8)
					leadTime = spProd[6];

			} else {
				clsProductMasterModel objModel = objProductMasterService.funGetObject(prodCode, clientCode);
				prodName = objModel.getStrProdName();
				conversion = objModel.getDblReceiveConversion();
			}

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(leadTime));
			Date expDate = cal.getTime();
			String expectedDate = expDate.getDate() + "/" + (expDate.getMonth() + 1) + "/" + (expDate.getYear() + 1900);

			if (null != hmChildNodes.get(prodCode)) {
				clsWhatIfAnalysisFields objWhatIfAnalysisFields = hmChildNodes.get(prodCode);
				reqdQty = reqdQty + objWhatIfAnalysisFields.getReqdQty();
				currentStock = currentStock + objWhatIfAnalysisFields.getCurrentStock();
				openPOQty = openPOQty + objWhatIfAnalysisFields.getOpenPOQty();
				orderQty = orderQty + objWhatIfAnalysisFields.getOrderQty();
				hmChildNodes.remove(prodCode);
			}

			double bomrate =0;
			double amt=0.0;
			List<String> listChildNodes11 = new ArrayList<String>();
			if(semiProduct.equals("No")){
			if(rateFrom.equals("Last Purchase Rate")){
				bomrate=funGetBOMLastPurchaseRate(prodCode, clientCode);
			}else{
				bomrate=funGetBOMRate(prodCode, clientCode);	
			}
			amt=bomrate*reqdQty;
			}
			
			if(semiProduct.equals("Yes")){
				bomrate =0;
				 amt=0.0;
				funGetBOMNodes(prodCode, 0, reqdQty, listChildNodes11);
				for(String prodCode11:listChildNodes11)
				{
//					String temp11 = (String) listChildNodes11.get(cnt);
					String prodCode1 = prodCode11.split(",")[0];
					double reqdQty11 = Double.parseDouble(prodCode11.split(",")[1]);
					if(rateFrom.equals("Last Purchase Rate")){
						bomrate=funGetBOMLastPurchaseRate(prodCode1, clientCode);
					}else{
						bomrate=funGetBOMRate(prodCode1, clientCode);	
					}
					bomrate=bomrate*reqdQty11;
					amt=amt+bomrate;
				}
			}
			
		
						
			clsWhatIfAnalysisFields objWhatIfAnalysisFields=new clsWhatIfAnalysisFields();
			objWhatIfAnalysisFields.setProdCode(prodCode);
			objWhatIfAnalysisFields.setProdName(prodName);
			objWhatIfAnalysisFields.setReqdQty(reqdQty*conversion);
			objWhatIfAnalysisFields.setUom(uom);
			objWhatIfAnalysisFields.setOpenPOQty(openPOQty);
			objWhatIfAnalysisFields.setOrderQty(orderQty);
			objWhatIfAnalysisFields.setSuppCode(suppCode);
			objWhatIfAnalysisFields.setSuppName(suppName);
			objWhatIfAnalysisFields.setLeadTime(leadTime);
			objWhatIfAnalysisFields.setAmount(amt);
			objWhatIfAnalysisFields.setExpectedDate(expectedDate);
			objWhatIfAnalysisFields.setCurrentStock(currentStock*conversion);
			
			if(hmChildNodes.containsKey(prodCode))
			{
				double reqdQty1=reqdQty*conversion;
				double amount1=bomrate * reqdQty;
				
				objWhatIfAnalysisFields=hmChildNodes.get(prodCode);
				objWhatIfAnalysisFields.setOpenPOQty(objWhatIfAnalysisFields.getOpenPOQty()+openPOQty);
				objWhatIfAnalysisFields.setReqdQty(objWhatIfAnalysisFields.getReqdQty()+reqdQty1);
				objWhatIfAnalysisFields.setOrderQty(objWhatIfAnalysisFields.getOrderQty()+orderQty);
				objWhatIfAnalysisFields.setAmount(objWhatIfAnalysisFields.getAmount()+amount1);
			}
			
			hmChildNodes.put(prodCode, objWhatIfAnalysisFields);
			System.out.println(hmChildNodes);
		}
		
		return hmChildNodes;
	}

	public String funGetProdInfo(String prodCode) {
		String prodInfo = "";
		/*String sql = " select ifnull(a.strProdName,''),if(IFNULL(a.strRecipeUOM,'')='',IFNULL(a.strReceivedUOM,''),a.strRecipeUOM),ifnull(b.strSuppCode,''),ifnull(c.strPName,''),a.dblRecipeConversion,a.dblReceiveConversion,ifnull(b.strLeadTime,'0') " 
			+ " from tblproductmaster a left outer join tblprodsuppmaster b on a.strProdCode=b.strProdCode and b.strDefault='Y' "
			+ " left outer join tblpartymaster c on b.strSuppCode=c.strPCode "
			+ " where  a.strProdCode='" + prodCode + "'  ";*/
		
		String sql = " select ifnull(a.strProdName,''),a.strReceivedUOM,ifnull(b.strSuppCode,''),ifnull(c.strPName,''),a.dblRecipeConversion,a.dblReceiveConversion,ifnull(b.strLeadTime,'0') ,a.strRecipeUOM" 
			+ " from tblproductmaster a left outer join tblprodsuppmaster b on a.strProdCode=b.strProdCode and b.strDefault='Y' "
			+ " left outer join tblpartymaster c on b.strSuppCode=c.strPCode "
			+ " where  a.strProdCode='" + prodCode + "'  ";

		List list = objGlobalFunctionsService.funGetList(sql, "sql");
		if (list.size() > 0) {
			Object[] arrObj = (Object[]) list.get(0);
			prodInfo = arrObj[0] + "#" + arrObj[1] + "#" + arrObj[2] + "#" + arrObj[3] + "#" + arrObj[4]+ "#" + arrObj[5]+ "#" + arrObj[6]+ "#" + arrObj[7];
		}

		return prodInfo;
	}

	public double funGetOpenPOQty(String prodCode, String clientCode) {
		BigDecimal openPOQty = new BigDecimal(0);
		double dblPOQty = 0;
		String sql = "select a.dblOrdQty - ifnull(b.POQty,0) as BalanceQty " + "from tblpurchaseorderdtl a left outer join (SELECT b.strCode AS POCode, b.strProdCode, SUM(b.dblQty) AS POQty " + "FROM tblgrnhd a INNER JOIN tblgrndtl b ON a.strGRNCode = b.strGRNCode " + "WHERE (a.strAgainst = 'Purchase Order') GROUP BY POCode, b.strProdCode) b on a.strPOCode = b.POCode "
				+ "and a.strProdCode = b.strProdCode left outer join tblproductmaster c on a.strProdCode=c.strProdCode " + "where a.dblOrdQty > ifnull(b.POQty,0) and a.strProdCode='" + prodCode + "' and a.strClientCode='" + clientCode + "'";
		List list = objGlobalFunctionsService.funGetList(sql, "sql");
		if (list.size() > 0) {
			Object ob = list.get(0);
			openPOQty = (BigDecimal) ob;
			dblPOQty = openPOQty.doubleValue();
		}

		return dblPOQty;
	}

	public int funGetBOMNodes(String parentProdCode, double bomQty, double qty, List<String> listChildNodes) {
			
		String sql = "select b.strChildCode from  tblbommasterhd a,tblbommasterdtl b " 
			+ "where a.strBOMCode=b.strBOMCode and a.strParentCode='" + parentProdCode + "' ";
		List listTemp = objGlobalFunctionsService.funGetList(sql, "sql");
		if (listTemp.size() > 0) {
			for (int cnt = 0; cnt < listTemp.size(); cnt++) {
				String childNode = (String) listTemp.get(cnt);
				bomQty = funGetBOMQty(childNode, parentProdCode);
				funGetBOMNodes(childNode, bomQty, qty * bomQty, listChildNodes);
			}
		} else {
			listChildNodes.add(parentProdCode + "," + qty);
		}
		return 1;
	}
	
	public int funGetBOMNodes(String parentProdCode, double bomQty, double qty, List<String> listChildNodes,String semiProduct) {
		
		String sql = "select b.strChildCode from  tblbommasterhd a,tblbommasterdtl b " 
			+ "where a.strBOMCode=b.strBOMCode and a.strParentCode='" + parentProdCode + "' ";
		List listTemp = objGlobalFunctionsService.funGetList(sql, "sql");
	
			for (int cnt = 0; cnt < listTemp.size(); cnt++) {
				String childNode = (String) listTemp.get(cnt);
				bomQty = funGetBOMQty(childNode, parentProdCode);
				listChildNodes.add(childNode + "," + qty * bomQty);
			}
		
			
		
		return 1;
	}

	public double funGetBOMQty(String childCode, String parentCode) {
		double bomQty = 0;
		try {
//			String sql = "select ifnull(left(((c.dblReceiveConversion/c.dblIssueConversion)/c.dblRecipeConversion),6) * b.dblQty,0) as BOMQty " + "from tblbommasterhd a,tblbommasterdtl b,tblproductmaster c ,tblproductmaster d " + "where a.strBOMCode=b.strBOMCode and a.strParentCode=d.strProdCode and a.strParentCode='" + parentCode + "' and b.strChildCode=c.strProdCode " + "and b.strChildCode='"+ childCode + "'";
			String sql = "select ifnull(((c.dblReceiveConversion/c.dblIssueConversion)/c.dblRecipeConversion) * b.dblQty,0) as BOMQty " + "from tblbommasterhd a,tblbommasterdtl b,tblproductmaster c ,tblproductmaster d " + "where a.strBOMCode=b.strBOMCode and a.strParentCode=d.strProdCode and a.strParentCode='" + parentCode + "' and b.strChildCode=c.strProdCode " + "and b.strChildCode='"+ childCode + "'";
			List listChildQty = objGlobalFunctionsService.funGetList(sql, "sql");
			if (listChildQty.size() > 0) {
				bomQty =  Double.parseDouble((listChildQty.get(0).toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bomQty;
	}

	public double funGetBOMRate(String childCode, String clientCode) {
		double bomRate = 0;
		try {
			String sql = "select ifNull(a.dblCostRM,0) as Rate  from tblproductmaster a where a.strProdCode='" + childCode + "' and a.strClientCode='" + clientCode + "' ";
			List listChildRate = objGlobalFunctionsService.funGetList(sql, "sql");
			if (listChildRate.size() > 0) {
				bomRate = Double.parseDouble(listChildRate.get(0).toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bomRate;
	}
	
	public double funGetBOMLastPurchaseRate(String childCode, String clientCode) {
		double bomRate = 0;
		try {
			String sql = "select b.dblUnitPrice from tblgrnhd a, tblgrndtl b where a.strGRNCode=b.strGRNCode and a.strClientCode='"+clientCode+"' and b.strProdCode='"+childCode+"' order by a.dtBillDate desc limit 1 ;";
			List listChildRate = objGlobalFunctionsService.funGetList(sql, "sql");
			if (listChildRate.size() > 0) {
				bomRate = Double.parseDouble(listChildRate.get(0).toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bomRate;
	}

	private List funGetAllChildNodes(String prodCode, String clientCode, double qty) {

		List list = new ArrayList<String>();
		String sql = "select b.strChildCode, c.strProdName, c.strPartNo, b.dblQty as Quantity " + ",ifnull(d.BalanceQty,0) as PurchaseQty " + ",b.dblQty-(ifnull(d.BalanceQty,0)) as OrderQty " + ",left(((c.dblReceiveConversion/c.dblIssueConversion)/c.dblRecipeConversion),6) as Conversion "
				+ ",left(((c.dblReceiveConversion/c.dblIssueConversion)/c.dblRecipeConversion),6)*(b.dblQty-(ifnull(d.BalanceQty,0))) as FinalQty " + ",ifnull(e.strSuppCode,''),ifnull(f.strPName,''),ifnull(e.strLeadTime,0),ifnull(e.dblLastCost,0)" + ",c.strReceivedUOM "
				+ "from tblbommasterhd a ,tblbommasterdtl b left outer join (select a.strProdCode as Product,a.dblOrdQty - ifnull(b.POQty,0) as BalanceQty " + "from tblpurchaseorderdtl a left outer join (SELECT b.strCode AS POCode, b.strProdCode, SUM(b.dblQty) AS POQty " + "FROM tblgrnhd a INNER JOIN tblgrndtl b ON a.strGRNCode = b.strGRNCode WHERE (a.strAgainst = 'Purchase Order') "
				+ "GROUP BY POCode, b.strProdCode) b on a.strPOCode = b.POCode and a.strProdCode = b.strProdCode " + "left outer join tblproductmaster c on a.strProdCode=c.strProdCode " + "where a.dblOrdQty > ifnull(b.POQty,0) and a.strProdCode='" + prodCode + "' and a.strClientCode='" + clientCode + "' ) d " + "on b.strChildCode=d.Product, tblproductmaster c "
				+ "left outer join tblprodsuppmaster e on c.strProdCode=e.strProdCode and e.strDefault='Y' " + "left outer join tblpartymaster f on e.strSuppCode=f.strPCode " + "where a.strBOMCode=b.strBOMCode and b.strChildCode=c.strProdCode " + "and a.strParentCode='" + prodCode + "' and a.strClientCode='" + clientCode + "'";
		List listChildProducts = objGlobalFunctionsService.funGetList(sql, "sql");
		if (listChildProducts.size() > 0) {
			for (int cnt = 0; cnt < listChildProducts.size(); cnt++) {
				Object[] arrObjNodes = (Object[]) listChildProducts.get(cnt);
				double reqQty = Double.parseDouble(arrObjNodes[7].toString());
				reqQty = reqQty * qty;
				List listTemp = funGetAllChildNodes(arrObjNodes[0].toString(), clientCode, reqQty);
				if (listTemp.size() == 0) {
					List listNodes = new ArrayList<String>();

					listNodes.add(arrObjNodes[0].toString()); // Prod Code

					listNodes.add(arrObjNodes[1].toString()); // Prod Name

					listNodes.add(arrObjNodes[12].toString()); // Received UOM

					listNodes.add(reqQty); // Required Qty

					listNodes.add("0"); // Current Stock

					listNodes.add(arrObjNodes[5].toString()); // Open PO

					listNodes.add(arrObjNodes[7].toString()); // Order Qty

					listNodes.add(arrObjNodes[8].toString()); // Supplier Code

					listNodes.add(arrObjNodes[9].toString()); // Supplier Name

					String leadTime = objGlobalFunction.funIfNull(arrObjNodes[10].toString(), "0", arrObjNodes[10].toString());
					listNodes.add(leadTime); // Lead Time

					double rate = Double.parseDouble(arrObjNodes[11].toString());
					double orderQty = Double.parseDouble(arrObjNodes[7].toString());
					double value = rate * orderQty;
					listNodes.add(arrObjNodes[11].toString()); // Rate
					Date today = new Date();
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(leadTime));
					Date expDate = cal.getTime();
					String expectedDate = expDate.getDate() + "/" + (expDate.getMonth() + 1) + "/" + (expDate.getYear() + 1900);
					listNodes.add(expectedDate); // Expected Date
					listNodes.add(value); // Value

					clsWhatIfAnalysisModel objWhatIfModel = new clsWhatIfAnalysisModel();
					objWhatIfModel.setStrProductCode(arrObjNodes[0].toString());
					objWhatIfModel.setStrProductName(arrObjNodes[1].toString());
					objWhatIfModel.setStrUOM(arrObjNodes[2].toString());
					objWhatIfModel.setDblCurrentStk(0);
					objWhatIfModel.setDblOpenPOQty(Double.parseDouble(arrObjNodes[5].toString()));
					objWhatIfModel.setDblOrderQty(Double.parseDouble(arrObjNodes[7].toString()));
					objWhatIfModel.setStrSuppCode(arrObjNodes[8].toString());
					objWhatIfModel.setStrSuppName(arrObjNodes[9].toString());
					objWhatIfModel.setDblRate(Double.parseDouble(arrObjNodes[11].toString()));
					objWhatIfModel.setDblLeadTime(Double.parseDouble(leadTime));
					objWhatIfModel.setStrExpectedDate(expectedDate);

					mapChildNodes.put(arrObjNodes[0].toString(), listNodes);
					listChildNodes.add(listNodes);
				}
			}
		}
		return listChildProducts;
	}
}


class clsWhatIfAnalysisFields
{
	private String prodCode;
	private String prodName;
	private String uom;
	private double reqdQty;
	private double currentStock;
	private double openPOQty;
	private double orderQty;
	private String suppCode;
	private String leadTime;
	private String suppName;
	private double amount;
	private String expectedDate;
	
	
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public double getReqdQty() {
		return reqdQty;
	}
	public void setReqdQty(double reqdQty) {
		this.reqdQty = reqdQty;
	}
	public double getCurrentStock() {
		return currentStock;
	}
	public void setCurrentStock(double currentStock) {
		this.currentStock = currentStock;
	}
	public double getOpenPOQty() {
		return openPOQty;
	}
	public void setOpenPOQty(double openPOQty) {
		this.openPOQty = openPOQty;
	}
	public double getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(double orderQty) {
		this.orderQty = orderQty;
	}
	public String getSuppCode() {
		return suppCode;
	}
	public void setSuppCode(String suppCode) {
		this.suppCode = suppCode;
	}
	public String getLeadTime() {
		return leadTime;
	}
	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}
	public String getSuppName() {
		return suppName;
	}
	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getExpectedDate() {
		return expectedDate;
	}
	public void setExpectedDate(String expectedDate) {
		this.expectedDate = expectedDate;
	}
	
	
	
}



