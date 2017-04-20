import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.oats.scripting.modules.basic.api.FunctionLibrary;
import oracle.oats.scripting.modules.basic.api.IteratingVUserScript;
import oracle.oats.scripting.modules.basic.api.ScriptService;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;
import oracle.oats.scripting.modules.functionalTest.api.PropertyTestList;
import oracle.oats.scripting.modules.functionalTest.api.TestOperator;
import oracle.oats.scripting.modules.webdom.api.WebDomService;
import oracle.oats.scripting.modules.webdom.api.elements.DOMCheckbox;
import oracle.oats.scripting.modules.webdom.api.elements.DOMDocument;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.utilities.FileUtility;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.applet.api.*;
import lib.*;

public class script extends IteratingVUserScript {
	private static final int  SYNCTIME = 100;
	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;
	@ScriptService oracle.oats.scripting.modules.applet.api.AppletService applet;
	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;
	@FunctionLibrary("GENLIB") lib.ebsqafwk.GENLIB gENLIB;
	@FunctionLibrary("WEBTABLELIB") lib.ebsqafwk.WEBTABLELIB wEBTABLELIB;
	
	public void initialize() throws Exception {

				browser.launch();
				
				initializeWebTable();
	}
	public void initializeWebTable() throws Exception {

		WebTable.web = web;
		WebTable.javaScriptFilePath = getScriptPackage().getScriptPath().replace(getScriptPackage().getScriptName()+".jwg", "")+"work_around.js";
		WebTable.itv = this; 
	}

	/**
	 * Add code to be executed each iteration for this virtual user.
	 */
	public void run() throws Exception {
		
		verifyPaymentProcessRequest("my req1234", "Completed") ;
	}

	/**
	 * 
	 * @param sWhatYouWantToDo Add,Search And Select
	 */
	public void finish() throws Exception {
	}
	
	
	public void verifyDialogMessage(String msgToVerify, String action) throws Exception{
		
		if(web.exists("")){}
	//	web.dialog("").
		
	}
	public void setNextRandomNumber(String window,String field) throws AbstractScriptException
	{
		int n=Integer.parseInt("2");

		long num=Math.round((Math.random()*(10^n)));
		
		while(true){

			num = num + 15;

			forms.textField("//forms:textField[(@name='"+field+"')]").setText(num+"");

			forms.window( "//forms:window[(@name='"+window+"')]").clickToolBarButton("Save");

			if(forms.choiceBox("//forms:choiceBox").exists()){

				forms.choiceBox("//forms:choiceBox").clickButton("OK");
			}else
				break;
		}
	}

	
	public void awardTableAction(String supplier, String awardOption, String objectType, String value)  throws Exception {
		
		initializeWebTable();
		awardOption = awardOption.trim();
		supplier = supplier.trim();
		int objIndex = -1;
		
		List<String> listOfObjects = new ArrayList<String>();
		listOfObjects.add("checkbox");
		listOfObjects.add("radiobutton");
		listOfObjects.add("textarea");
		listOfObjects.add("textbox");
		
		String awardOptions [] = awardOption.split(";");
		String actionVals[] = value.toLowerCase().split(";");
		
		if(awardOptions.length != actionVals.length){
			
			reportFailure("PROCLIB: awardTableAction: You have entered"+awardOptions.length+"options in test data but specified only"+actionVals.length+" values in testdata");
		}
		
		
		WebTable awardTable = new WebTable(supplier);
		
		objectType= objectType.toLowerCase();
		
		objIndex = listOfObjects.indexOf(objectType);
		
		if(objIndex < 0){
			
			reportFailure("PROCLIB: awardTableAction"+ objectType+" specified isn't in the known objects list"+ listOfObjects.toString());
		}
		 
	
		switch(objIndex){
		
		case 0: {
			for(int i =0 ; i< awardOptions.length ; i++){
			awardTable.selectCheckBox("Label", awardOptions[i], supplier,toBoolean(actionVals[i]) );
			}
			break;
		}
		case 1: {
				for(int i =0 ; i< awardOptions.length ; i++){
				awardTable.selectRadioButton("Label", awardOptions[i], supplier);
				}
			
			break;
		}
		case 2: {
			
			for(int i =0 ; i< awardOptions.length ; i++){
				awardTable.setText("Label", awardOptions[i], supplier,actionVals[i] );
				}
			
			break;
		}
		case 3: {
			
			for(int i =0 ; i< awardOptions.length ; i++){
				
				awardTable.setTextArea("Label", awardOptions[i], supplier,actionVals[i] );
				
				}
			
			break;
		}
		
		}
		
	}
	public String getAwardOption(String supplier, String awardOption) throws Exception {
		
		initializeWebTable();
		awardOption = awardOption.trim();
		supplier = supplier.trim();
		
		String optionVal = "";
		
		WebTable awardTable = new WebTable(supplier);
		if(awardOption.equals("Award Quantity")){
			
			optionVal = awardTable.getText("Label", awardOption, supplier);
		}
		else if(awardOption.equals("Internal Note")||(awardOption.equals("Note to Supplier"))){
			
			optionVal = awardTable.getTextAreaValue("Label", awardOption, supplier);
			
		}else{
			
			optionVal = awardTable.getCellData("Label", awardOption, supplier, "td").get(0);
		}
		
		if(optionVal.contains("Pricing Details")){
			
			optionVal = optionVal.replace("Pricing Details", "");	
		}
		return optionVal.trim();
		
		
	}
	
	public void verifyPaymentProcessRequest(String reqName, String status) throws Exception {
		
		int reqTime = 0 ;
		gENLIB.webSelectLOV("Search for Payment Process Request", "Payment Process Request Name", reqName, "Payment Process Request Name", reqName);
		
		delay(5000);
		
		web.button("/web:window[@title='Payment Process Requests']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@value='Go' or @text='Go']").click();
		
		String reqStatus ="";
		do{
			
			delay(5000);
			
		WebTable reqtable = new WebTable("Payment Process Request Status");
		reqStatus = reqtable.getCellData("Payment Process Request", reqName, "Payment Process Request Status").get(0);
		
		if(reqStatus.equalsIgnoreCase(status)){
		
			info("verifyPaymentProcessRequest : Payment Process Request for "+ reqName + " is in"+reqStatus);
			
			break;
			
		}else if(reqTime ==4)
		{
			reportFailure("verifyPaymentProcessRequest : Payment Process Request for "+ reqName + " isn't in"+reqStatus+" state after 5min's");
			
		}
			else{
			
			reqTime++;
			
		}
		
		
		web.button("/web:window[@title='Payment Process Requests']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='ResultsTable:Refresh:1' or @value='Refresh Status' or @text='Refresh Status']").click();
		}while(!reqStatus.equalsIgnoreCase(status));
	}

	public void verifyAwardOption(String supplier, String awardOption, String expectedVal) throws Exception {
		
		awardOption = awardOption.trim();
		supplier = supplier.trim();
		String actualVal =	getAwardOption( supplier,  awardOption) ;
		
		actualVal= actualVal.trim();
		expectedVal = expectedVal.trim();
		
		boolean bPresent = false;
		
		if(expectedVal.length()<actualVal.length()){
			
			bPresent = actualVal.contains(expectedVal)? true : false ; 
			
		}else{
			
			bPresent = actualVal.equals(expectedVal)? true : false ; 
		}
		if(bPresent){
			
			info("PROCLIB: verifyAwardOption:  "+ awardOption +" value is "+expectedVal+"which is expected");
		}
		else{
			
			reportFailure("PROCLIB: verifyAwardOption:  "+ awardOption +" actual value is "+actualVal+"but Expected value is"+expectedVal);
		}
		
		
	}

	public void launchCustomURL(String url) throws Exception {
		
		browser.launch();
		String pageindex = web.getFocusedWindow().getAttribute("index");
		web.window("/web:window[@index='"+pageindex+"']").navigate(url);
	}
	
	
	/**
	 *  carWebSelectLOV("Funding or Requesting Agency ID Lookup","Agency ID","9543","Agency ID","9543");
	 *  
	 * @param lovName
	 * @param searchByOption
	 * @param searchValue
	 * @param colName
	 * @param rowValue
	 * @throws Exception
	 */
	public void carWebSelectLOV(String lovName, String searchByOption, String searchValue, String colName, String rowValue) throws Exception{
		
		List<String> lColNames = new ArrayList<String>();
		List<String> lrowValues = new ArrayList<String>();
		
		if(colName.contains(";")){
			
			String[] sColNames =  colName.split(";");
			lColNames = Arrays.asList(sColNames);
		}else{
			lColNames.add(colName);
		}
		
		if(rowValue.contains(";")){
			String[] srowValues =  rowValue.split(";");
			lrowValues = Arrays.asList(srowValues);
		}else{
			lrowValues.add(rowValue);
		}
		
		// Converting List to Array
		String[] colNames = lColNames.toArray(new String[lColNames.size()]);
		String[] rowValues = lrowValues.toArray(new String[lrowValues.size()]);
		
		if(colNames.length == rowValues.length){
			carWebSelectLOV(lovName, searchByOption, searchValue, colNames, rowValues);
		}else{
			info("Unable to select the value from WebSelectLOV table as the values provided not mathcing:");
			info("Column Names"+Arrays.asList(rowValues).toString()+"    RowValues :"+Arrays.asList(rowValues).toString());
		}
	}

	
	/**
	 *  Select From LOV
	 *  
	 * @param lovName
	 * @param searchByOption
	 * @param searchValue
	 * @param colNames
	 * @param rowValues
	 * @throws Exception
	 */
	public void carWebSelectLOV(String lovName, String searchByOption, String searchValue, String[] colNames, String[] rowValues) throws Exception{
		
		info("******************** Strat of webSelectLoV ************************");
		info("** lovName  :\""+lovName+"\" searchByOption :\""+searchByOption+"\"  searchValue :\""+searchValue+"\"");
		info("** colNames  :\""+Arrays.asList(colNames).toString()+"\" rowValues :\""+Arrays.asList(rowValues).toString()+"\"");
		
		int SYNCTIME = 10;
		int currentWindowIndex = Integer.parseInt(web.getFocusedWindow().getAttribute("index"));;
		
		if((lovName != null) && !("".equals(lovName))){
			
			// Get Image window Index & Title
			currentWindowIndex = Integer.parseInt(web.getFocusedWindow().getAttribute("index"));
			String title = web.getFocusedWindow().getTitle();
			
			// Get Window Path
			String windowPath = "/web:window[@index='"+currentWindowIndex+"' or @title='"+title+"']";
			
			// Get Doc Path
			String docPath = windowPath+"/web:document[@index='0']";
			
			info("Main Window DocPath :"+docPath );
			
			// Click on Image
			web.image(docPath+ "/web:input_image[@title='"+lovName+"']").waitFor();
			web.image(docPath+ "/web:input_image[@title='"+lovName+"']").click();
		}
		
		// Wait for the window
		web.window("/web:window[@index='1']").waitForPage(50000);
		
		// Select Window Details
		String title = web.getFocusedWindow().getTitle();
		int selectWindowIndex = Integer.parseInt(web.getFocusedWindow().getAttribute("index"));
		String windowPath = "/web:window[@index='"+selectWindowIndex+"' or @title='"+title+"']";
		String selectDocPath = windowPath+"/web:document[@index='0']";
		
		info("Select Window DocPath :"+selectDocPath );
			
		
		
		Thread.sleep(5000);
		
		// Set the value in Search Field
		if((searchByOption != null)&&(!("".equals(searchByOption)))){
			
			// Clear Search Fields
			web.image(selectDocPath+ "/web:input_image[@title='Clear']").waitFor();
			web.image(selectDocPath+ "/web:input_image[@title='Clear']").click();
			
			
			//Enter the data in the search field
			web.textBox(selectDocPath+ "/web:input_text[@title='"+searchByOption+"']").waitFor();
			web.textBox(selectDocPath+ "/web:input_text[@title='"+searchByOption+"']").setText(searchValue);
			
			// Click on Search Value
			web.image(selectDocPath+ "/web:input_image[@title='Search']").click();
			
			Thread.sleep(5000);
		}
		
		
		info("** Start of Table in WebSelectLov ****");
		
		/* Select Table */
		initializeWebTable();

		// "Quick Select;1" Table Name = Quick Select , docIndex=1
		WebTable searchTable = new WebTable(colNames[0]);
		searchTable.selectRadioButton(colNames, rowValues,"Select");

		/*HashMap <String,String>search27350 = new HashMap<String,String>();
		List <String[]> action41329 = new ArrayList<String[]>();
		search27350.put(searchByOption,searchValue);
		action41329.add( new String[]{"SELECT","RADIOBUTTON","Select","","","","TRUE"});
		wEBTABLELIB.tableactions(searchByOption,search27350,action41329);*/
		
		info("** End of Table in WebSelectLov ****");
		
		//Click on Select button
		web.image(selectDocPath+ "/web:input_image[@title='Select']").click();
		
		Thread.sleep(10000);
		
		// Waiting for the page to load
		web.window("/web:window[@index='1']").waitForPage(SYNCTIME);
		
		info("******************** End of webSelectLoV ************************");
		
	}
	
	
	
	public void verifyCheckBoxImageBasedOnLabel(String labelName, String status) throws Exception {
		
		PropertyTestList list = new PropertyTestList();
		list.add("text",labelName, TestOperator.StringWildCard);
		
		List<DOMElement> controlRules = web.document(getCurrentWindowXPath()+"/web:document[@index='0']").getElementsByTagName("span", list);
	
		
		if(controlRules.size()<1){
		
			controlRules = web.document(getCurrentWindowXPath()+"/web:document[@index='0'").getElementsByTagName("div", list);
	
			reportFailure("PROCLIB: verifyImage : No Images found for the specified label");
		}

		if(controlRules.size()<1){
	
			reportFailure("PROCLIB: verifyImage : No Div/Span element found for specified label");
		}
	
		DOMElement tr  = getParentRow(controlRules.get(0));
		
		List<DOMElement> imgList = tr.getElementsByTagName("img");
		
		if(imgList.size()<1){
			
			reportFailure("PROCLIB: verifyImage : No Images found for the specified label");
		}
		
		DOMElement requiredImg = null;
		String imgStatus = null;
		for(DOMElement img: imgList){
			
			String imgAlt= img.getAttribute("alt");
			
			if((imgAlt != null)&&(!imgAlt.equals(""))){
				
				if(imgAlt.contains("Read only Checkbox")){
					
					
					requiredImg =(img);
					imgStatus= imgAlt;
					break;
				}
			}
			
		}
		
		if(status.toLowerCase().equalsIgnoreCase("true")){
			
			if(imgStatus.equals("Read only Checkbox Checked")){
				
				info("PROCLIB: verifyImage : Image For Label:"+labelName+" is in Checked status which is expected");
			}
			else{
				
				reportFailure("PROCLIB: verifyImage : Expected status is Checked but Img status is "+imgStatus);
			}
		}
		else{
			
			if(imgStatus.equals("Read only Checkbox Not Checked")){
				
				info("PROCLIB: verifyImage : Image For Label:"+labelName+" is in Unchecked status which is expected");
			}
			else{
				
				reportFailure("PROCLIB: verifyImage : Expected status is Unchecked but Img status is "+imgStatus);
			}
		}
	}

	public DOMElement getParentRow(DOMElement cell ) throws Exception{
		
		DOMElement parent = cell.getParent();
		
		if((parent!=null)&&(parent.getTag().equalsIgnoreCase("tr"))){
			
			return parent;
		}else{
			if(parent ==null){
				
				return null;
			}
			else{
			
				return	getParentRow(parent );
			}
		}
	}
	
	public String getOfferReceiveTime(String dateFormatType , String dateValue ) throws Exception{
		
		String format = ((dateFormatType!=null)&&(!dateFormatType.equals("")))? dateFormatType:"dd-MMM-yyyy HH:mm:ss"; 
		
		return  ((dateValue!=null)&&(!dateValue.equals("")))? generateofferReceiveTime(format,dateValue,2):"";
		
		
	}
	
	public String generateofferReceiveTime(String dateFormatType, String dateValue, int incrementValue) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatType);
		
		Date offerDate = dateFormat.parse(dateValue);

		Calendar cal = Calendar.getInstance(); 

		//cal.se
		cal.setTime(offerDate);
		
		cal.add(Calendar.SECOND,incrementValue);
		
		String DateVal = dateFormat.format(cal.getTime());
		
		return DateVal;
	}
	public void formsSetChargeAccount(String ChargeAccount, String distributionLineNumber) throws Exception {
		// TODO Auto-generated method stub
		//distributionLineNumber = (toInt(distributionLineNumber)-1)+"";
		forms.textField("//forms:textField[(@name='DISTRIBUTIONS_CHARGE_ACCOUNT_FLEX_"+distributionLineNumber+"')]").setFocus();
		//forms.textField("//forms:textField[(@name='DISTRIBUTIONS_REQ_LINE_QUANTITY_"+distributionLineNumber+"')]").invokeSoftKey("NEXT_FIELD");
		
		
		Thread.sleep(5000);
		
		if(forms.flexWindow("//forms:flexWindow").exists()){
			System.out.println("Flex window exists. Closing Flex window and setting Charge Account.");
			forms.flexWindow("//forms:flexWindow").clickCancel();
			forms.textField("//forms:textField[(@name='DISTRIBUTIONS_CHARGE_ACCOUNT_FLEX_"+distributionLineNumber+"')]").setText(ChargeAccount);
			//forms.textField("//forms:textField[(@name='DISTRIBUTIONS_CHARGE_ACCOUNT_FLEX_"+distributionLineNumber+"')]").invokeSoftKey("NEXT_FIELD");
		}
		else{
			System.out.println("Flex window does not exists. Entering Charge Account directly");
				forms.textField("//forms:textField[(@name='DISTRIBUTIONS_CHARGE_ACCOUNT_FLEX_"+distributionLineNumber+"')]").setText(ChargeAccount);
				//forms.textField("//forms:textField[(@name='DISTRIBUTIONS_CHARGE_ACCOUNT_FLEX_"+distributionLineNumber+"')]").invokeSoftKey("NEXT_FIELD");
			}
		/****************** Search Val is returned as -1*******************/
		/*//gENLIB.formSpreadTableSearch(searchCol, attrValue)
		
		HashMap <String, String>searchCol = new HashMap<String, String>();
		searchCol.put("DISTRIBUTIONS_DISTRIBUTION_NUM_0", distributionLineNumber);
		String lineNo = gENLIB.formTableSearch(searchCol, 5);
		
		forms.textField("//forms:textField[(@name='DISTRIBUTIONS_CHARGE_ACCOUNT_FLEX_"+lineNo+"')]").setFocus();
		
		Thread.sleep(5000);
		
		if(forms.flexWindow("//forms:flexWindow").exists()){
			System.out.println("Flex window exists. Closing Flex window and setting Charge Account.");
			forms.flexWindow("//forms:flexWindow").clickCancel();
			
			forms.textField("//forms:textField[(@name='DISTRIBUTIONS_CHARGE_ACCOUNT_FLEX_"+lineNo+"')]").setFocus();
			
			forms.textField("//forms:textField[(@name='DISTRIBUTIONS_CHARGE_ACCOUNT_FLEX_"+lineNo+"')]").setText(ChargeAccount);
			forms.textField("//forms:textField[(@name='DISTRIBUTIONS_CHARGE_ACCOUNT_FLEX_"+lineNo+"')]").invokeSoftKey("NEXT_FIELD");
		}*/
		
	}
	
	
	
	public void clearShopppingCart() throws Exception{
		
		initializeWebTable();
		
		String noOfLinesXpath = "//web:span[@id='NumberOfLines']";
		
		gENLIB.webClickLink("Shop");
		
		web.window("/web:window[@title= 'Oracle iProcurement: Shop']").waitForPage(20, true);
		
		if(web.exists("//web:button[@id='Checkout']")){
		
			web.button("//web:button[@id='Checkout']").click();
			
			web.window("/web:window[@title= 'Oracle iProcurement: Checkout']").waitForPage(20, true);
			
			WebTable itemsCart = new WebTable("Line");		
			int rowCount = itemsCart.getRowCount()-2;
			System.out.println("No oF rows in CheckOut page "+rowCount);
			
			for(int row=1;row<=rowCount;row++)
			{
				
			WebTable itemsCart1 = new WebTable("Line");
			
			itemsCart1.clickImage(1, "Delete");
			
			web.window("/web:window[@title= 'Oracle iProcurement: Checkout']").waitForPage(20, true);
			
			Thread.sleep(4000);
			delay(5);
			}
			
			gENLIB.webClickLink("Shop");
			
			web.window("/web:window[@title= 'Oracle iProcurement: Shop']").waitForPage(60, true);
			
			String noOfLines = web.element(noOfLinesXpath).getAttribute("text");
			
			if(noOfLines.equals("Your cart is empty.")){
				
				info("ProcLib: clearShopppingCart() : Shopping cart is cleared");
				
			}else{
				
				reportFailure("ProcLib: clearShopppingCart() : Shopping cart still has items");
			}
		
		}else{
			
					
			String noOfLines = web.element(noOfLinesXpath).getAttribute("text");
			
			if(noOfLines.equals("Your cart is empty.")){
				
				info("ProcLib: clearShopppingCart() : Shopping cart is cleared");
				
			}else{
				
				reportFailure("ProcLib: clearShopppingCart() : View Cart Checkout button isnt present. Hence Shopping cart couldn't be cleared");
			}
		}
	}
	
	public void Award_Bid_To_Supplier(String supplierName , String award, String awardOption,String ValueToEnter, String InternalNote , String NotetoSupplier)throws Exception{
		
		initializeWebTable();
		
		WebTable awardTable = new WebTable("Label");
		int colNum = awardTable.getColumnNumber(supplierName);
		
		if((award!=null)&&(!award.equals(""))){
			int awardRowNum = awardTable.getRowNumber(new String []{"Label"},new String [] {"Award"});
			awardTable.selectRadioButton(awardRowNum, colNum);
			}
		
		if((awardOption!=null)&&(!awardOption.equals(""))){
			int internalNoteRowNum = awardTable.getRowNumber(new String []{"Label"},new String [] {awardOption});
			awardTable.setText(internalNoteRowNum, colNum, ValueToEnter);
		}
		
		if((InternalNote!=null)&&(!InternalNote.equals(""))){
		int internalNoteRowNum = awardTable.getRowNumber(new String []{"Label"},new String [] {"Internal Note"});
		awardTable.setTextArea(internalNoteRowNum, colNum, InternalNote);
		}
		
		if((NotetoSupplier!=null)&&(!NotetoSupplier.equals(""))){
			int NotetoSupplierRowNum = awardTable.getRowNumber(new String []{"Label"},new String [] {"Note to Supplier"});
			awardTable.setTextArea(NotetoSupplierRowNum, colNum, InternalNote);
		}
		
	}
	
	
	public void Verify_AwardBid_Details_Supplier(String supplierName ,  String awardOption,String valueToVerifY)throws Exception{
		
		initializeWebTable();
		
		WebTable awardTable = new WebTable("Label");
		int colNum = awardTable.getColumnNumber(supplierName);
		
		
		if((awardOption!=null)&&(!awardOption.equals(""))){
			int internalNoteRowNum = awardTable.getRowNumber(new String []{"Label"},new String [] {awardOption});
			List<String> data =awardTable.getCellData(internalNoteRowNum, colNum);
			
			if(data.toString().contains(valueToVerifY)){
				
				info("");
			}else{
				
				this.reportFailure("Verify_AwardBid_Details_Supplier:"+awardOption+"text"+data.toString()+"doesn't match with "+valueToVerifY);
			}
		}
		
		
	}
	

	public void clickBidinAwardBid(String supplierName  )throws Exception  {
	
	initializeWebTable();
	
	WebTable awardTable = new WebTable("Label");
	int colNum = awardTable.getColumnNumber(supplierName);
	
		int awardRowNum = awardTable.getRowNumber(new String []{"Label"},new String [] {"Bid"});
		awardTable.clickLink(awardRowNum, colNum);
		
	
}
	
	public void selectRadiobuttonBasedonLabel(String labelNameWithIndex) throws Exception {
		
		String winnIndex = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();
	
		boolean bFound =false;
		String winPath = "/web:window[@index='"+winnIndex+"' or @title='"+title+"']";
		String xPath = null;
		
			xPath = winPath+"/web:document[@index='0']/web:form[@index='0']";
			
			String[] label = labelNameWithIndex.split(";");
			
			int similarLabelIndex = 0; // default value "0" i.e first radio lable in the page
			int requiredLabelIndex = 0;
			String labelName = "";
			
			if(label.length > 1){
				labelName =  label[0];
				requiredLabelIndex = Integer.parseInt(label[1]);
			}else{
				labelName =  label[0];
			}
			
			
			for(int i=0;web.exists(xPath+"/web:input_radio[@index='"+i+"']");i++){
				
				DOMElement labels = web.radioButton(xPath+"/web:input_radio[@index='"+i+"']").getNextSibling();
				String labelVal = labels.getAttribute("text");
				
				System.out.println("lableVal :"+labelVal);
				
				if((labelVal!=null)&&(labelVal.equals(labelName))){
	
					if(similarLabelIndex == requiredLabelIndex){
						bFound =true;					
						web.radioButton(xPath+"/web:input_radio[@index='"+i+"']").select();					
						break;	
					}
					
					similarLabelIndex++;
				}
			}
			
			if(!bFound){
				
				reportFailure("No radio button found for label"+labelName);
			}
	}
	
	public String encryptURL(String ou) throws Exception{
		
		initializeWebTable();
		
		WebTable ouTable = new WebTable("Encryption String");
		List<String> encryptURL = ouTable.getCellData("Operating Unit", ou, "Encryption String");
		
		System.out.println("encryptURL"+encryptURL.size());
		if(encryptURL.size()<=0){

			ouTable = new WebTable("Encryption String");
			ouTable.selectCheckBox("Operating Unit", ou, "Select");
			 
			
			String encryptButtonPath = "/web:window[@index='0' or @title='Supplier Registration Setup']/web:document[@index='0' or @name='12ktssy0m3_1']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='EncryptButton' or @value='Encrypt']";
			/*Click on Encrypt button*/	
			web.button(encryptButtonPath).click();
			
			/*Wait for page to Load*/
			Thread.sleep(15000);
			 
		}
		
		ouTable = new WebTable("Encryption String");
		encryptURL = ouTable.getCellData("Operating Unit", ou, "Encryption String");
		
		String appUrl = web.document("/web:window/web:document").getRecordedURL();
		
		String path = "//span[@id='UrlExample']";
		
		String urlPattern = web.element(path).getAttribute("text");
		
		String urlparts[] =appUrl.split(":");
		
		String server = urlparts[0]+":"+urlparts[1];

		String urlSubparts[] =urlparts[2].split("/");
		
		String potNum = urlSubparts[0];
		
		urlPattern=urlPattern.replace("http(s)://<server>", server);
		
		urlPattern=urlPattern.replace("<port>", potNum);
		
		urlPattern=urlPattern.replace("<encryption string>", encryptURL.get(0));
		
		
		return urlPattern;
		
	}
	
public void addAttachments(String[] fieldLabels , String[] fieldValues, String action) throws Exception{
		
		/* Convert Data from Arrays to Lists*/
		List<String> lFieldLabels = Arrays.asList(fieldLabels);
		List<String> lFieldValues = Arrays.asList(fieldValues);
		
		/* Set "title" field */
		if(lFieldLabels.contains("Title")){
			
			String title = lFieldValues.get(lFieldLabels.indexOf("Title"));
			web.textBox("{{obj.PROCLIB.web_AddAttachment_Title_textfield}}").waitFor(SYNCTIME);
			web.textBox("{{obj.PROCLIB.web_AddAttachment_Title_textfield}}").setText(title);
		}
		
		/* Set "Description" field */
		if(lFieldLabels.contains("Description")){
			
			String description = lFieldValues.get(lFieldLabels.indexOf("Description"));
			web.textArea("{{obj.PROCLIB.web_AddAttachment_Description_textarea}}").waitFor(SYNCTIME);
			web.textArea("{{obj.PROCLIB.web_AddAttachment_Description_textarea}}").setText(description);
		}
		
		
		/* Select "Category" field */
		if(lFieldLabels.contains("Category")){
			
			String category = lFieldValues.get(lFieldLabels.indexOf("Category"));
			web.selectBox("{{obj.PROCLIB.web_AddAttachment_Category_select}}").waitFor(SYNCTIME);
			web.selectBox("{{obj.PROCLIB.web_AddAttachment_Category_select}}").selectOptionByText(category);
		}
		
		/* Select "Attachment" field */
		if(lFieldLabels.contains("AttchmentType")){
			
			String attachmentType = lFieldValues.get(lFieldLabels.indexOf("AttchmentType"));
			String attachmentVallue = lFieldValues.get(lFieldLabels.indexOf("AttchmentValue"));
			
			if(attachmentType.equalsIgnoreCase("file")){
				
				/* Select File Radio button */
				web.radioButton("{{obj.PROCLIB.web_AddAttachment_File_radiobutton}}").select();
				
				/* click File Text Field */
				web.textBox("{{obj.PROCLIB.web_AddAttachment_File_textfield}}").waitFor(SYNCTIME);
				web.textBox("{{obj.PROCLIB.web_AddAttachment_File_textfield}}").click();
				
				
				/* Set the text in Open Dialog Box */
				web.dialog("{{obj.PROCLIB.web_dialog_unknown_Look__in_}}").waitFor(SYNCTIME);
				web.dialog("{{obj.PROCLIB.web_dialog_unknown_Look__in_}}").setText(0, attachmentVallue);
				
				/* Clcik on Open button */
				web.dialog("{{obj.PROCLIB.web_dialog_unknown_Look__in_}}").clickButton(0);
				
				/* Set the Focus to File Text Field */
				web.textBox("{{obj.PROCLIB.web_AddAttachment_File_textfield}}").focus();
				
			}else if(attachmentType.equalsIgnoreCase("url")){
				
				/* Select URL Radio button */
				web.radioButton("{{obj.PROCLIB.web_AddAttachment_URL_radiobutton}}").select();
				
				/* click URL Text Field */
				web.textBox("{{obj.PROCLIB.web_AddAttachment_URL_textfield}}").waitFor(SYNCTIME);
				web.textBox("{{obj.PROCLIB.web_AddAttachment_URL_textfield}}").setText(attachmentVallue);
				
			}else if(attachmentType.equalsIgnoreCase("text")){
				
				/* Select Text Radio button */
				web.radioButton("{{obj.PROCLIB.web_AddAttachment_TEXT_radiobutton}}").select();
				
				/* click Text Text Area */
				web.textArea("{{obj.PROCLIB.web_AddAttachment_Text_textarea}}").waitFor(SYNCTIME);
				web.textArea("{{obj.PROCLIB.web_AddAttachment_Text_textarea}}").setText(attachmentVallue);
			}
			
		}
		
		if(action.equalsIgnoreCase("apply")){
			
			web.button("{{obj.PROCLIB.web_AddAttachment_Apply_button}}").waitFor(SYNCTIME);
			web.button("{{obj.PROCLIB.web_AddAttachment_Apply_button}}").click();
			
			
		} else if(action.equalsIgnoreCase("add another")){
			
			web.button("{{obj.PROCLIB.web_AddAttachment_AddAnother_button}}").waitFor(SYNCTIME);
			web.button("{{obj.PROCLIB.web_AddAttachment_AddAnother_button}}").click();
			
			/* Wait for Add Attachment page to load  */
			Thread.sleep(20000);
			
		}
		
	}


public void verifyDocumentNumberDetails(String documentNumberDetail, String  value) throws Exception {
	
}

public void verifyItemPricingDetails(String pricingDetail, String  pricingValue) throws Exception {

	/* Variable Declaration*/
	String value = null;
	String path = getCurrentWindowXPath() +"/web:document[@index='0']";
	
	/* Pricing Details attributes, which will be changed based on the selections done, while creating the SLIN */
	String[] pricingAttributes = { "Quantity", "UOM", "Estimated Quantity", "Maximum Cost", "Fixed Fee",
			"Unit Price", "Target Cost", "Target Fee", "Minimum Fee", "Maximum Fee", "Govt. Share Above Target %",
			"Govt. Share Below Target %", "Estimated Max Cost", "Estimated Cost", "Govt Share Percent",
			"Award Fee", "Maximum Quantity", "Target Profit", "Ceiling Price", "Ceiling on Firm Target Profit",
			"Floor on Firm Target Profit", "Minimum Quantity", "Other Direct Costs", "Base Fee","Target Profit" };
	
	String[] pricingVerifyAttributes = { "Total Amount", "Fixed Fee %", "Govt Share Amount", "Const. Share Amount",
			"Estimated Price", "Extended Price", "Maximum Price", "Total Target Price", "Ceiling Price %",
			"Target Unit Price" };
	
	/* Convert Data from Arrays to Lists*/
	List<String> lFieldNames = Arrays.asList(pricingDetail);
	List<String> lFieldValues = Arrays.asList(pricingValue);
	List<String> lPricingAttributes = Arrays.asList(pricingAttributes);
	List<String> lpricingVerifyAttributes = Arrays.asList(pricingVerifyAttributes);
	
	/* Wait for the Pricing Details page to open*/
	Thread.sleep(3000);
	
	
	/* Verification of the Expected Values*/
	for(int index = 0; index< lFieldNames.size(); index++){
		
		String element = lFieldNames.get(index);
		
		if(lpricingVerifyAttributes.contains(element)){
			
			/* Expected Value to Verify */
			value = lFieldValues.get(lFieldNames.indexOf(element));
			
			/* Actual Value */
			info("text value is :"+value);
			String xPath = path+"/web:span[@text='"+value+"*']";
			String actVal = web.element(xPath).getAttribute("text");
			
			if(actVal.equals(value)){
				
				info("Actual value of "+element+" : "+actVal+"  is matched with expectd value :"+value);
				
			}else{
				
				info("Actual value of "+element+" : "+actVal+"  is not matched with expectd value :"+value);
			}
		}
	}		 
	
}

	public void addItemPricingDetails(String pricingDetail, String  pricingValue) throws Exception {

		/* Variable Declaration*/
		String value = null;
		String path = getCurrentWindowXPath()+ "/web:document[@index='0']";
		
		/* Pricing Details attributes, which will be changed based on the selections done, while creating the SLIN */
		String[] pricingAttributes = { "Quantity", "UOM", "Estimated Quantity", "Maximum Cost", "Fixed Fee",
				"Unit Price", "Target Cost", "Target Fee", "Minimum Fee", "Maximum Fee", "Govt. Share Above Target %",
				"Govt. Share Below Target %", "Estimated Max Cost", "Estimated Cost", "Govt Share Percent",
				"Award Fee", "Maximum Quantity", "Target Profit", "Ceiling Price", "Ceiling on Firm Target Profit",
				"Floor on Firm Target Profit", "Minimum Quantity", "Other Direct Costs", "Base Fee","Target Profit" };
		
		String[] pricingVerifyAttributes = { "Total Amount", "Fixed Fee %", "Govt Share Amount", "Const. Share Amount",
				"Estimated Price", "Extended Price", "Maximum Price", "Total Target Price", "Ceiling Price %",
				"Target Unit Price" };
		
		/* Convert Data from Arrays to Lists*/
		List<String> lFieldNames = Arrays.asList(pricingDetail);
		List<String> lFieldValues = Arrays.asList(pricingValue);
		List<String> lPricingAttributes = Arrays.asList(pricingAttributes);
		List<String> lpricingVerifyAttributes = Arrays.asList(pricingVerifyAttributes);
		
		/* Wait for the Pricing Details page to open*/
		Thread.sleep(3000);
		
		/* Fill all the specified fields in "Pricing Details" window*/
		for(int index = 0; index< lFieldNames.size(); index++){
			
			String element = lFieldNames.get(index);
			
			if(lPricingAttributes.contains(element)){
				
				System.out.println("Pricing items :"+element);
				/* Expected Value to Verify */
				value = lFieldValues.get(lFieldNames.indexOf(element));
				
				if(element.equals("UOM")){
					
					/* Select Data from LOV*/
					String uomXPath = getCellCompXPath("UOM",2,"img","Search for UOM");
		            web.image(uomXPath).click();
		            
		            /* Select from LOV */
					selectFromLOV(null, "Display Name",value, "Display Name",value);
					
				}
				
				else{
					/* Set the Value in the Text Fields */
					String xPath = getCellCompXPath(element,2,"input",0);
					web.textBox(xPath).setText(value);
				}
			}
		}
		
	}
	
	@SuppressWarnings("unchecked") 
	public String getCellCompXPath(String itemName, int expIndex, String compType, int compSeq) throws Exception {
		
		String windowPath = getCurrentWindowXPath();
		
		int currentWindowIndex = Integer.parseInt(web.window(windowPath).getAttribute("index"));
		
		String docPath = "/web:window[@index='"+currentWindowIndex+"']/web:document[@index='"+currentWindowIndex+"']";
		
		// NegotiationComplexPricingRN
		// ResponseComplexPricingRN
		String tablePath = docPath + "/web:table[@id='*ComplexPricingRN']";
		
		info("tablePath :"+tablePath);
		
		web.table(tablePath).waitFor(SYNCTIME);
		List<DOMElement> tdComps = web.table(tablePath).getElementsByTagName("td");
		
		String  cellValIndex = null;
		for(int i=0;i<tdComps.size();i++){
			if(tdComps.get(i).getAttribute("text") != null){
				if(tdComps.get(i).getAttribute("text").equals(itemName)){
					cellValIndex = tdComps.get(i).getAttribute("index");
					break;
				}
			}
		}
		
		/* Source Item Index */
		//String cellValIndex = web.element(cellPath).getAttribute("index");
		//info("Source Cell Index :"+cellValIndex);
		
		/* Expected Item Index */
		int actualCellIndex = Integer.parseInt(cellValIndex) + expIndex;
		//info("Target Cell Index :"+actualCellIndex);
		
		/* Expected item Td Path*/
		String actualCellPath = docPath + "/web:td[@index='"+actualCellIndex+"']";
		
		List<DOMElement> comps = web.element(actualCellPath).getElementsByTagName(compType);
		
		/* Get Tag Attributes */
		HashMap<String, String> tagAttributes = getTagAttributes(comps.get(compSeq));
		
		/* Get XPath */
		String xPath = getCompXPath(compType,tagAttributes);
		
		return docPath+xPath;
	}
	
	
	public String getCompXPath(String component,HashMap<String,String> tagAtttirbutes){
		
		String xPath = "";
		
		/* Get Attributes*/
		Object[] attrObjects = tagAtttirbutes.keySet().toArray();
		
		/* Get Attribute Values*/
		Object[] attrValuesObjects = tagAtttirbutes.values().toArray();
		
		String[] attrNames = Arrays.asList(attrObjects).toArray(new String[attrObjects.length]);
		String[] attrValues = Arrays.asList(attrValuesObjects).toArray(new String[attrValuesObjects.length]);
		
		for(int tagIndex=0;tagIndex<attrNames.length;tagIndex++){
			
			if(tagIndex == 0){
				xPath = xPath + "@"+attrNames[tagIndex]+"='"+attrValues[tagIndex]+"'";
			}
			
			if(tagIndex > 0){
				xPath = xPath + " and @"+attrNames[tagIndex]+"='"+attrValues[tagIndex]+"'";
			}
		}
		
		xPath = xPath.trim();
		
		/* Change Array values to List Values */
		List<String> lAttrNames = Arrays.asList(attrNames);
		List<String> lAttrValues = Arrays.asList(attrValues);
	
		if(component.equalsIgnoreCase("input")){
			
			String type = lAttrValues.get(lAttrNames.indexOf("type"));
			
			if(type.equalsIgnoreCase("text")){
				
				xPath = "/web:input_text["+xPath+"]";
				
			}else if(type.equalsIgnoreCase("checkbox")){
				
				xPath = "/web:input_checkbox["+xPath+"]";
								
			}else if(type.equalsIgnoreCase("radio")){
				
				xPath = "/web:input_radio["+xPath+"]";
				
			}
			
		}else if(component.equalsIgnoreCase("textarea")){
			
			xPath = "/web:textarea["+xPath+"]";
			
		}else if(component.equalsIgnoreCase("a")){
			
			xPath = "/web:a["+xPath+"]";
			
		}else if(component.equalsIgnoreCase("select")){
			
			xPath = "/web:select["+xPath+"]";
			
		}else if(component.equalsIgnoreCase("img")){
			
			xPath = "/web:img["+xPath+"]";
			
		}else if(component.equalsIgnoreCase("span") || component.equalsIgnoreCase("div")){
			
			xPath = "/web:span["+xPath+"]";
		}
		
		//System.out.println("xPath :"+xPath);
		return xPath.trim();
	}
	
public HashMap<String,String> getTagAttributes(DOMElement element) throws Exception {
		
		String[] defAttributes = {"id","name","value","text","innertext","type","summary","index","alt","title","src"};
		
		return getTagAttributes(element, defAttributes);
	}

public HashMap<String,String> getTagAttributes(DOMElement element, String[] expAttributes) throws Exception {
	
	String[] attributes = null;

	attributes = expAttributes;
	
	HashMap<String,String> tagAttributes = new HashMap<String,String>();
	
	for(int attrIndex=0;attrIndex<attributes.length;attrIndex++){
		
		String attrVal = element.getAttribute(attributes[attrIndex]);
		
		if (attrVal != null) {

			if(attributes[attrIndex] == "type" && attrVal=="hidden"){
				return null;
			}
			
			if (attrVal.isEmpty()) {
				tagAttributes.put(attributes[attrIndex],"");
			} else {
				tagAttributes.put(attributes[attrIndex],attrVal);
			}
		}
	}
	
	/* if the component doesn't have specified attributes, then return null for that component */
	if(expAttributes != null){
		
		if(tagAttributes.keySet().size() > expAttributes.length)
		{
			tagAttributes = null;
		}
	}
	
	return tagAttributes;
}

public void selectFromLOV(String lovName, String searchByOption, String searchValue, String colName, String rowValue) throws Exception{
	
	String[] colNames = {colName};
	String[] rowValues = {rowValue};
	
	selectFromLOV(lovName, searchByOption, searchValue, colNames, rowValues);
}

public void selectFromLOV(String lovName, String searchByOption, String searchValue, String[] colNames, String[] rowValues) throws Exception{
	
	int SYNCTIME = 200;
	int currentWindowIndex = 0;
	
	if(lovName != null){
		
		String windowPath = getCurrentWindowXPath();
		
		currentWindowIndex = Integer.parseInt(web.window(windowPath).getAttribute("index"));
		info("currentWindowIndex :"+currentWindowIndex );
		
		String lovPath = windowPath + "/web:document[@index='"+currentWindowIndex+"']" + "/web:img[@alt='"+lovName+"']";
		info("windowPath :"+windowPath );
		
		/* Wait for LOV */
		web.image(lovPath).waitFor(SYNCTIME);
		
		/* Click on LOV */
		web.image(lovPath).click();
	}
	
	
	/* */
	int searchWindowIndex = currentWindowIndex+1;
	info("searchWindowIndex :"+searchWindowIndex );
	String searchWindowPath = "/web:window[@index='"+searchWindowIndex+"']/web:document[@index='"+searchWindowIndex+"']/web:form[@index='0']";
	
	/* Select Search By Field */
	web.selectBox(searchWindowPath+"/web:select[(@id='categoryChoice' or @name='categoryChoice') and multiple mod 'False']").selectOptionByText(searchByOption);
	
	/* Enter Search Text */
	web.textBox(searchWindowPath+"/web:input_text[@name='searchText']").waitFor(SYNCTIME);
	web.textBox(searchWindowPath+"/web:input_text[@name='searchText']").setText(searchValue);
	
	/* Click on Go button */
	web.button(searchWindowPath+"/web:button[@value='Go']").click();
	
	/* Select Table */
	Thread.sleep(15000);
	
	/* Source Col Index */
	
	String sourceHeaderPath = searchWindowPath + "/web:th[@text='"+searchByOption+"']";
	String destHeaderPath = searchWindowPath + "/web:th[@text='Quick Select']";
	
	int sourceColIndex = Integer.parseInt(web.element(sourceHeaderPath).getAttribute("index"));
	
	/* Destination Col Index */
	int targetColIndex = Integer.parseInt(web.element(destHeaderPath).getAttribute("index"));
	
	/* Get the Range */
	int range = targetColIndex - sourceColIndex;
	
	String serchTableCellPath = getCellCompXPath(searchValue,range,"img","Quick Select");
	web.image(serchTableCellPath).click();
	
	Thread.sleep(5000);
	
	web.window("/web:window[@index='"+currentWindowIndex+"']").waitForPage(SYNCTIME);
	
}

@SuppressWarnings("unchecked") 
public String getCellCompXPath(String itemName, int expIndex, String compType, String value) throws Exception {
	
	String windowPath = getCurrentWindowXPath();
	
	int currentWindowIndex = Integer.parseInt(web.window(windowPath).getAttribute("index"));
	
	String docPath = "/web:window[@index='"+currentWindowIndex+"']/web:document[@index='"+currentWindowIndex+"']";
	
	String cellPath = docPath + "/web:td[@text='"+itemName+"']";
	
	web.element(cellPath).waitFor(SYNCTIME);
	
	/* Source Item Index */
	String cellValIndex = web.element(cellPath).getAttribute("index");
	info("Source Cell Index :"+cellValIndex);
	
	/* Expected Item Index */
	int actualCellIndex = Integer.parseInt(cellValIndex) + (expIndex);
	info("Target Cell Index :"+actualCellIndex);
	
	/* Expected item Td Path*/
	String actualCellPath = docPath + "/web:td[@index='"+actualCellIndex+"']";
	
	List<DOMElement> comps = web.element(actualCellPath).getElementsByTagName(compType);
	
	info("Size :"+comps.size());
	/* Get xPath for the specified Image */
	String xPath = "";
	for(int compIndex=0;compIndex<comps.size(); compIndex++){
		
		info("compIndex :"+compIndex);
		if(compType.equalsIgnoreCase("img")){
			/* If Tag Value is matched , then get the xpath */
			info("alt :"+comps.get(compIndex).getAttribute("alt"));
			info("title :"+comps.get(compIndex).getAttribute("title"));
			
			if(comps.get(compIndex).getAttribute("alt") != null){
				
				if(comps.get(compIndex).getAttribute("alt").contains(value) || comps.get(compIndex).getAttribute("title").contains(value)){
					/* Get Tag Attributes */
					HashMap<String, String> tagAttributes = getTagAttributes(comps.get(compIndex));
					
					/* Get XPath */
					xPath = getCompXPath(compType,tagAttributes);
					
					break;
				}
			}
			
			if(comps.get(compIndex).getAttribute("title") != null){
				
				if(comps.get(compIndex).getAttribute("title").contains(value)){
					/* Get Tag Attributes */
					HashMap<String, String> tagAttributes = getTagAttributes(comps.get(compIndex));
					
					/* Get XPath */
					xPath = getCompXPath(compType,tagAttributes);
					
					break;
				}
			}
		}else if(compType.equalsIgnoreCase("a")){
			/* If Tag Value is matched , then get the xpath */
			if(comps.get(compIndex).getAttribute("text").contains(value) || comps.get(compIndex).getAttribute("value").contains(value)){
				/* Get Tag Attributes */
				HashMap<String, String> tagAttributes = getTagAttributes(comps.get(compIndex));
				
				/* Get XPath */
				xPath = getCompXPath(compType,tagAttributes);
				
				break;
			}
		}
	}
	
	return docPath+xPath;
}

public String getCurrentWindowXPath() throws Exception {
	
	String index = web.getFocusedWindow().getAttribute("index");
	String title = web.getFocusedWindow().getTitle();
	
	String xPath = "/web:window[@index='"+index+"' or @title='"+title+"']";
	
	return xPath;
}

public String getCurrentWindowTitle() throws Exception {
	
	String title = web.getFocusedWindow().getTitle();
	
	return title;
}

public boolean isEmpty(String input){
	
	input = input.replaceAll(" ", "");
	
	input = input.trim();
	
	boolean bResult = (input.length()>0) ?  false: true;
	
	return  bResult; 
}

/**
 * 
 * @param labelName 
 * @param value 
 */
public void selectFirstOptionInSelectBoxBasedOnLabel( String labelName ) 	throws Exception {
	
	String label[] = labelName.trim().split(";");
	int eleIndex = 0;
	if(label.length>1){
		
		eleIndex= toInt(label[1]);
		
	}
	String RTObject_Xpath=null;
	ArrayList<DOMElement> elements =new ArrayList<DOMElement>();
	String xPath= getCurrentWindowXPath()+"/web:document[@index='0']/web:span[@text='"+label[0]+"']";
	info(xPath);
	web.element(xPath).waitFor(30);
	
	DOMElement textElement = web.element(xPath);
	/* Get the Parent Item*/
	 elements= (ArrayList<DOMElement>)getCompsBasedOnLabel(labelName, "select");
	//elements=(ArrayList<DOMElement>) textElement.getParent().getParent().getElementsByTagName(compType);
	 System.out.println("Number of fields matching "+labelName+" : "+elements.size());
	DOMElement element = elements.get(0);
	RTObject_Xpath=getCurrentWindowXPath()+"/web:document[@index='0']/web:select[@id='"+element.getAttribute("id")+"']";
	System.out.println("Xpath of "+"select"+" is : "+RTObject_Xpath);
	List<DOMElement> optionsList = web.selectBox(RTObject_Xpath).getOptions();
	int optionsCount = optionsList.size();
	List<String> optionVal = new ArrayList<String>();
	for(DOMElement option : optionsList){
		 
		 String optionText = option.getAttribute("text");
		 
		 if(optionText == null){
			 optionVal.add("");
		 }else{
			 optionVal.add(optionText);
		 }
	 }
	
	if(isEmpty(optionVal.get(0))&&(optionsCount>1)){
		
		web.selectBox(RTObject_Xpath).selectOptionByIndex(1);
	}
	else {
		
		web.selectBox(RTObject_Xpath).selectOptionByIndex(0);
		
	}
}
public void setValueBasedOnLabel(String labelName,String compType, String valueToSet)throws Exception{
	String label[] = labelName.trim().split(";"); 
	 int eleIndex = 0;
	if(label.length>1){
		
		eleIndex= toInt(label[1]);
		
	}
	
	 String RTObject_Xpath=null; 
	 ArrayList<DOMElement> elements =new ArrayList<DOMElement>(); 
	 String xPath= getCurrentWindowXPath()+"/web:document[@index='0']/web:span[@text='"+label[0]+"']"; 
	 info(xPath);
	 web.element(xPath).waitFor(30);
	 
	 
	 DOMElement textElement = web.element(xPath);
	 
	 /* Get the Parent Item*/
	 elements= (ArrayList<DOMElement>)getCompsBasedOnLabel(labelName, compType);
	 //elements=(ArrayList<DOMElement>) textElement.getParent().getParent().getElementsByTagName(compType);
	 System.out.println("Number of fields matching "+labelName+" : "+elements.size());
	 
	 DOMElement element = elements.get(0); 		 
	 
	 
	 if (compType.equalsIgnoreCase("select")){
		 RTObject_Xpath=getCurrentWindowXPath()+"/web:document[@index='0']/web:select[@id='"+element.getAttribute("id")+"']";
		 System.out.println("Xpath of "+compType+" is : "+RTObject_Xpath);
		 web.selectBox(RTObject_Xpath).selectOptionByText(valueToSet);
	 }
	 if (compType.equalsIgnoreCase("textArea")){
		 info(element.getAttribute("id"));
		 RTObject_Xpath=getCurrentWindowXPath()+"/web:document[@index='0']/web:textArea[@id='"+element.getAttribute("id")+"']";
		 System.out.println("Xpath of "+compType+" is : "+RTObject_Xpath);
		 web.textArea(RTObject_Xpath).setText(valueToSet);
	 }
	 if (compType.equalsIgnoreCase("input")){
		 RTObject_Xpath=getCurrentWindowXPath()+"/web:document[@index='0']/web:input_text[@id='"+element.getAttribute("id")+"']";
		 System.out.println("Xpath of "+compType+" is : "+RTObject_Xpath);
		 web.textBox(RTObject_Xpath).setText(valueToSet);
	 }
	 
	 if (compType.equalsIgnoreCase("checkbox")){
		 RTObject_Xpath=getCurrentWindowXPath()+"/web:document[@index='0']/web:input_checkbox[@id='"+element.getAttribute("id")+"']";
		 System.out.println("Xpath of "+compType+" is : "+RTObject_Xpath);
		 web.checkBox(RTObject_Xpath).check(toBoolean(valueToSet));
	 }
}	

/************************Wrapper functions for setValueBasedOnLabel **********************************/
public   void setSelectValueBasedOnLabel(String labelName, String value)throws Exception{
	
	  setValueBasedOnLabel( labelName,"select",  value);
}


public   void setTextAreaValueBasedOnLabel(String labelName, String value)throws Exception{
	
	  setValueBasedOnLabel( labelName,"textArea",  value);
}

public   void setEditValueBasedOnLabel(String labelName, String value)throws Exception{

	
	  setValueBasedOnLabel( labelName,"input",  value);
}


public   void setCheckboxValueBasedOnLabel(String labelName, String value)throws Exception{
	
	  setValueBasedOnLabel( labelName,"checkbox",  value);
}

	public String getValueBasedOnLabel(String labelName,String compType)throws Exception{
	 
	 String RTObject_Xpath=null; 
	 ArrayList<DOMElement> elements =new ArrayList<DOMElement>(); 
	 String xPath="/web:window[@index='0']/web:document[@index='0']/web:span[@text='"+labelName+"']"; 
	 info(xPath);
	 web.element(xPath).waitFor(200);
	 
	 
	 DOMElement textElement = web.element(xPath);
	 
	 /* Get the Parent Item*/
	 elements= (ArrayList<DOMElement>)getCompsBasedOnLabel(labelName, compType);
	 //elements=(ArrayList<DOMElement>) textElement.getParent().getParent().getElementsByTagName(compType);
	 System.out.println("Number of fields matching "+labelName+" : "+elements.size());
	 
	 DOMElement element = elements.get(0); 		 
	 
	 
	 if (compType.equalsIgnoreCase("select")){
		 RTObject_Xpath="/web:window[@index='0']/web:document[@index='0']/web:select[@id='"+element.getAttribute("id")+"']";
		 System.out.println("Xpath of "+compType+" is : "+RTObject_Xpath);
		 return web.selectBox(RTObject_Xpath).getSelectedText()[0];
	 }
	 if (compType.equalsIgnoreCase("textArea")){
		 info(element.getAttribute("id"));
		 RTObject_Xpath="/web:window[@index='0']/web:document[@index='0']/web:textArea[@id='"+element.getAttribute("id")+"']";
		 System.out.println("Xpath of "+compType+" is : "+RTObject_Xpath);
		 return	web.textArea(RTObject_Xpath).getAttribute("value");
	 }
	 if (compType.equalsIgnoreCase("input")){
		 RTObject_Xpath="/web:window[@index='0']/web:document[@index='0']/web:input_text[@id='"+element.getAttribute("id")+"']";
		 System.out.println("Xpath of "+compType+" is : "+RTObject_Xpath);
		 return	 web.textBox(RTObject_Xpath).getAttribute("value");
	 }
	 
	 if (compType.equalsIgnoreCase("checkbox")){
		 RTObject_Xpath="/web:window[@index='0']/web:document[@index='0']/web:input_checkbox[@id='"+element.getAttribute("id")+"']";
		 System.out.println("Xpath of "+compType+" is : "+RTObject_Xpath);
		 return	web.checkBox(RTObject_Xpath).getAttribute("checked");
	 }
	 return "";
}	

	/************************Wrapper functions for getValueBasedOnLabel **********************************/
public String getSelectValueBasedOnLabel(String labelName )throws Exception{
		
		return getValueBasedOnLabel( labelName,"select");
	}
	
	
	public String getTextAreaValueBasedOnLabel(String labelName )throws Exception{
		
		return getValueBasedOnLabel( labelName,"textArea");
	}
	
	public String getEditValueBasedOnLabel(String labelName )throws Exception{
		
		return getValueBasedOnLabel( labelName,"input");
	}
	
	
	public String getCheckboxValueBasedOnLabel(String labelName )throws Exception{
		
		return getValueBasedOnLabel( labelName,"checkbox");
	}
	
	
	
	
	/************************Wrapper functions for VerifyValueBasedOnLabel **********************************/
	public boolean verifySelectValueBasedOnLabel(String labelName, String expectedValue)throws Exception{
		
		return verifyValueBasedOnLabel( labelName,"select",  expectedValue);
	}
	
	
	public boolean verifyTextAreaValueBasedOnLabel(String labelName, String expectedValue)throws Exception{
		
		return verifyValueBasedOnLabel( labelName,"textArea",  expectedValue);
	}
	
	public boolean verifyEditValueBasedOnLabel(String labelName, String expectedValue)throws Exception{
		
		return verifyValueBasedOnLabel( labelName,"input",  expectedValue);
	}
	
	
	public boolean verifyCheckboxValueBasedOnLabel(String labelName, String expectedValue)throws Exception{
		
		return verifyValueBasedOnLabel( labelName,"checkbox",  expectedValue);
	}
	
	
	public boolean verifyValueBasedOnLabel(String labelName,String compType, String expectedValue)throws Exception{
		 
		 String actualValue = getValueBasedOnLabel( labelName, compType);
		 boolean	retVal	=	false;
		 if(actualValue.equals(expectedValue)){
			 
			 retVal	=	true;
			 info("verifyValueBasedOnLabel: actualValue: "+actualValue+"is same as expected value:"+expectedValue);
		 }else{
			 
			 this.reportFailure("verifyValueBasedOnLabel: actualValue: "+actualValue+"isn't same as expected value:"+expectedValue);
		 }
		 return retVal;
	}

@SuppressWarnings("unchecked") 
public List<DOMElement> getCompsBasedOnLabel(String label, String compType) throws Exception {
	///web:window[@index='0' or @title='Update Work Order']/web:document[@index='0']/. 
	
	
	String winnIndex = web.getFocusedWindow().getAttribute("index");
	String title = web.getFocusedWindow().getTitle();
	
	String winPath = "/web:window[@index='"+winnIndex+"' or @title='"+title+"']";
	
	String xPath = winPath+"/web:document[@index='0']/web:td[@text='"+label+"']";
	System.out.println("ProcLIb: getCompsBasedOnLabel: xpath"+ xPath);
	
	String labelVal[] = label.trim().split(";"); 
	 int eleIndex = 0;
	if(labelVal.length>1){
		
		eleIndex= toInt(labelVal[1]);
		
	}
	List<DOMElement> tdList = web.document(winPath+"/web:document[@index='0']").getElementsByTagName("td");
	
	List<DOMElement> requiredtdList  = new ArrayList<DOMElement>();
	
	
	for(DOMElement td: tdList){
		
		String innerText = td.getAttribute("innerText");
		
		if((innerText!= null)&&(innerText.equals(labelVal[0]))){
			
			requiredtdList.add(td);
			
		}
	}
	
	String tdIndex = requiredtdList.get(eleIndex).getAttribute("index");
	System.out.println("tdIndex :"+tdIndex);
	
	int finalTdIndex = Integer.parseInt(tdIndex)+2;
	System.out.println("finalIndex :"+finalTdIndex);

	String finalTdXPath = winPath+"/web:document[@index='0']/web:td[@index='"+finalTdIndex+"']";
	
	
	List<DOMElement> elements =  web.element(finalTdXPath).getElementsByTagName(compType);
	
	return elements;
}

public List<DOMElement> getCompsBasedOnLabel2(String label, String compType) throws Exception {
	
	String winnIndex = web.getFocusedWindow().getAttribute("index");
	String title = web.getFocusedWindow().getTitle();
	
	String winPath = "/web:window[@index='"+winnIndex+"' or @title='"+title+"']";
	
	String xPath = winPath+"/web:document[@index='0']/web:td[@text='"+label+"']";
	
	String tdIndex = web.element(xPath).getAttribute("index");
	System.out.println("tdIndex :"+tdIndex);
	
	int finalTdIndex = Integer.parseInt(tdIndex);
	System.out.println("finalIndex :"+finalTdIndex);
	
	String finalTdXPath = winPath+"/web:document[@index='0']/web:td[@index='"+finalTdIndex+"']";
	
	
	List<DOMElement> elements =  web.element(finalTdXPath).getElementsByTagName(compType);
	
	return elements;
}

public void handleEditDocumentNumber(String DoDAAC,String InstrumentType,String AllowedRange, String SerialNum) throws Exception {
	
	
	/*gENLIB.webClickButton("Save as Draft");  // This button should be clicked mandatorily to edit the existing document number
	Thread.sleep(5000);*/  // to wait for the page refresh.
		
	gENLIB.webClickLink("Edit Document Number...");
			
	Thread.sleep(15000);  // to wait for the web form to open.
	
		if(DoDAAC!=null && !DoDAAC.isEmpty()){
			setValueBasedOnLabel("DoDAAC","input",DoDAAC);		
		}
		if(InstrumentType!=null && !InstrumentType.isEmpty()){
		    gENLIB.webSelectLOV("Search for Instrument Type", "Instrument Type", InstrumentType, "Instrument Type", InstrumentType);
		}
		if(AllowedRange!=null && !AllowedRange.isEmpty()){
			gENLIB.webSelectLOV("Search for Allowed Range", "Allowed Range", AllowedRange, "Allowed Range", AllowedRange);
		}
		if(SerialNum!=null && !SerialNum.isEmpty()){
			setValueBasedOnLabel("DoDAAC","input",SerialNum);		
		}
	
	gENLIB.webClickButton("OK");
}

public void editRequisitionNumber(String prefix, String agencyIdentifier, String allowedRange, String serialNumber) throws Exception{
	
	gENLIB.webClickLink("Edit Requisition Number");
	
	delay(10000);
	String winnIndex = web.getFocusedWindow().getAttribute("index");
	String title = web.getFocusedWindow().getTitle();
	
	String winPath = "/web:window[@index='"+winnIndex+"' or @title='"+title+"']";
	
	web.element(winPath+"/web:document[@index='0']/web:h1[@text='Requisition Header Details']").waitFor();
	
	
	setEditValueBasedOnLabel("Prefix", prefix);
	
	
	gENLIB.webSelectLOV("Search for Agency Identifier", "Display Name", agencyIdentifier, "Display Name", agencyIdentifier);
	
	
	gENLIB.webSelectLOV("Search for Allowed Range", "Display Name", allowedRange, "Display Name", allowedRange);
	
	setEditValueBasedOnLabel("Serial Number", serialNumber);
	
	gENLIB.webClickButton("OK");
	
	web.window(winPath).waitForPage(SYNCTIME, true);
	
	
}

public void verifyRequisitionNumber(String Prefix,  String agencyIdentifier, String allowedRange, String delimiter) throws Exception{
	
	String actualprefix= getEditValueBasedOnLabel("Prefix");

	if(actualprefix.trim().equals(Prefix)){
		
		info("ProcLib: verifyRequisitionNumber: Prefix matches with expected value"+actualprefix);
	}
	else{
		
		reportFailure("ProcLib: verifyRequisitionNumber: Prefix "+actualprefix+"doesnt match with expected value"+Prefix);
	
		
	}
	
	String actualfiscalYear = getEditValueBasedOnLabel("Prefix");

	String fiscalYear = Calendar.getInstance().toString();
	if(actualfiscalYear.trim().equals("")){
		
		info("ProcLib: verifyRequisitionNumber: fiscalYear matches with expected value"+fiscalYear);
	}
	else{
		
		reportFailure("ProcLib: verifyRequisitionNumber: fiscalYear "+actualfiscalYear+"doesnt match with expected value"+fiscalYear);
	
		
	}
	String actualAgencyIdentifier = getEditValueBasedOnLabel("Agency Identifier");
	if(actualAgencyIdentifier.trim().equals(agencyIdentifier)){
		
		info("ProcLib: verifyRequisitionNumber: agencyIdentifier matches with expected value");
	}
	else{
		
		reportFailure("ProcLib: verifyRequisitionNumber: agencyIdentifier "+actualAgencyIdentifier+"doesnt match with expected value"+agencyIdentifier);
	
		
	}
	
	
	String actualSerialNum = getEditValueBasedOnLabel("Serial Number");
	
	if(actualSerialNum.trim().length()==4){
		
		info("ProcLib: verifyRequisitionNumber: Serial Number is a 4 digit Number");
		
	}
	
	String serialNumber= "";

	if(actualSerialNum.trim().equals(serialNumber)){
		
		info("ProcLib: verifyRequisitionNumber: serialNumber matches with expected value"+serialNumber);
	}
	else{
		
		reportFailure("ProcLib: verifyRequisitionNumber: serialNumber "+actualSerialNum+"doesnt match with expected value"+serialNumber);
	
		
	}
	
	
	String actualAllowedRange = getEditValueBasedOnLabel("Allowed Range");

	if(actualAllowedRange.trim().equals(allowedRange)){
		
		info("ProcLib: verifyRequisitionNumber: Allowed Range matches with expected value"+actualAllowedRange);
	}
	else{
		
		reportFailure("ProcLib: verifyRequisitionNumber: Allowed Range "+actualAllowedRange+"doesnt match with expected value"+allowedRange);
	
		
	}
	
	
	
	
}

public void selectSearchRadioOption(String radioOption) throws Exception {
	
	String windowindex = web.getFocusedWindow().getAttribute("index");
	String windowTitle= web.getFocusedWindow().getTitle();
	String windowPath="/web:window[@index='"+windowindex+"' or @title='"+windowTitle+"']";
	int currentWindowIndex =Integer.parseInt(web.window(windowPath).getAttribute("index"));
	String docPath=windowPath+"/web:document[@index='"+currentWindowIndex+"']";	
	int indexSelection=0;
	
		if(radioOption.equalsIgnoreCase("True")){
			indexSelection=1;  // Search option index for When any conditions is met
		}
		else /*if(radioOption.equalsIgnoreCase("when any condition is met"))*/{
			indexSelection=0; // Search option index for When all conditions are met
		}
	
	String xpath =docPath+"/web:form[ @index='0']/web:input_radio[(@name='advancedSearchRadioGroup') and @index='"+indexSelection+"']";
	
	web.radioButton(xpath).select();	
	
}

public void handleWebTermsWindow(String Action) throws Exception{	
	
	if(getCurrentWindowTitle().contains("@title='Terms and Conditions'")){
		
		//info("Window exists");			
		
		if(Action.equalsIgnoreCase("Accept")){	
			gENLIB.webClickButton("Accept");
		}
		else if(Action.equalsIgnoreCase("Reject")){
			gENLIB.webClickButton("Reject");
		}
	}
}

public void viewContractFileNavigation(String mainResp , String subResp ,String lastresp) throws Exception {

	DOMElement respArea = web.element("//table[@id='rowLayout']");
	respArea.focus();
	boolean bMainRespFound = false;
	
	List<DOMElement> linksList = respArea.getElementsByTagName("a");

	DOMElement mainRespLink = null, subRespArea = null;
	
	for(DOMElement link : linksList){
	
		String linkText = link.getAttribute("text");
	
		System.out.println("linkText"+linkText);
		if((linkText!=null)&&(linkText.contains(mainResp))){
		
			bMainRespFound= true;
		
			mainRespLink = link;
		
			break;
		}
	
	}

if(!(bMainRespFound)){
	
	reportFailure("CLMViewContractNavigator: "+mainResp+"isn't found. So further navigation isn't possible");
}
else{
	
	mainRespLink.focus();
	
	String expandStatus = mainRespLink.getChildren().get(0).getAttribute("alt");
	
	if(expandStatus.equals("Select to show information")){
		
		mainRespLink.click();
		
		delay(10);
		
		DOMElement div =  mainRespLink.getParent();
		System.out.println("div"+(div!=null));
		System.out.println("div"+(div.getAttribute("className")));
		subRespArea = div.getParent();
		System.out.println("subRespArea"+(subRespArea!=null));
		System.out.println("subRespArea"+(subRespArea.getAttribute("innerHtml")));
		subRespArea.focus();
		
	}
	else{
		
		System.out.println("Inside else of main");
		subRespArea = mainRespLink.getParent().getParent();
		
		subRespArea.focus();
		
						
	}
	
	
	boolean bFoundinSpan = false;
	
	StringTokenizer responsibilities = new StringTokenizer(subResp,",");
	
while(responsibilities.hasMoreTokens()){
	
	List<DOMElement> subresp =  subRespArea.getElementsByTagName("span");
	
	String subRespString = responsibilities.nextToken();
	
	for(DOMElement span: subresp){
		
		String spanText = span.getAttribute("text");
		
		if((spanText!=null)&&(spanText.equals(subRespString))){
			
			bFoundinSpan= true;
			
			List<DOMElement> links = span.getParent().getParent().getParent().getElementsByTagName("a");
			
			links.get(0).focus();
			
			String imageStatus = links.get(0).getChildren().get(0).getAttribute("alt");
			
			if(!imageStatus.equals("Select to collapse")){
				
				links.get(0).click();
			}
			
		}
	}
	if(!bFoundinSpan){
		
		info("Inside links subresp");
		List<DOMElement> Linksubresp =  subRespArea.getElementsByTagName("a");
	
		for(DOMElement span: Linksubresp){
		
			String spanText = span.getAttribute("text");
			
			System.out.println(spanText);
			if((spanText!=null)&&(spanText.contains(subRespString))){
			
				System.out.println("spanText");
				bFoundinSpan= true;
			
				List<DOMElement> links = span.getParent().getParent().getParent().getElementsByTagName("a");
			
				links.get(0).focus();
			
				String imageStatus = links.get(0).getChildren().get(0).getAttribute("alt");
			
				if(!imageStatus.equals("Select to collapse")){
				
					links.get(0).click();
				}
			
			}
		}
	}
}
				
	List<DOMElement> links = subRespArea.getElementsByTagName("a");
	boolean bLastRespFound = false;
	for(DOMElement Link: links){
		
		String linktext = Link.getAttribute("text");
		
		if((linktext!=null)&&(linktext.contains(lastresp))){
			
			bLastRespFound = true;
			Link.click();
			
			break;
		}
	}
	if(bLastRespFound){
		
	}else{
		warn("No Link found with specified:"+lastresp);
	}
}
}

public void addToCartBasedOnSource(String itemName, String source ) throws Exception {
	
	List<DOMElement> Linkslist= web.document("/web:window[@index='0']/web:document[@index='0']").getElementsByTagName("a");
	
	List<DOMElement> slinsList = new ArrayList<DOMElement>();
	
	/*Store links which match search Results*/
	//int index = 0;
	if(Linkslist.size()!=0){
		
		for(DOMElement t: Linkslist){
			String value= t.getAttribute("text");
			if(value!=null){
				
				if(value.equals(itemName)){
					slinsList.add(t);
				}
			}
			
		}
		
		
		for(DOMElement t: slinsList){
			String value= t.getAttribute("id");
			
			if(value!=null){
				
				String index = value.replace("SearchResultsTableRN1:ShortDesc:", "").trim();
				String IDV ="/web:window[@index='0' or @title='Oracle iProcurement: Shop']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:span[@id='SearchResultsTableRN1:Attribute7:"+index+"']";
				
				String IDVNum= web.element(IDV).getAttribute("text");
				
				if(source.contains(IDVNum)){
					String path ="/web:window[@index='0' or @title='Oracle iProcurement: Shop']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='SearchResultsTableRN1:AddToCart:"+index+"' and @value='Add to Cart']";
					web.button(path).focus();
					web.button(path).click();
					
				}
				
			}
			
		}
	}
	else {
		
		info("Item :"+itemName+"not found");
	}
	
}

public void addIDVStructuretoCart(String idvItem, String idvNum ,String price) throws Exception {
	
	List<DOMElement> Linkslist= web.document(getCurrentWindowXPath() +"/web:document[@index='0']").getElementsByTagName("a");
	
	List<DOMElement> slinsList = new ArrayList<DOMElement>();
	
	/*Store links which match search Results*/
	//int index = 0;
	if(Linkslist.size()!=0){
		
		for(DOMElement t: Linkslist){
			String value= t.getAttribute("text");
			if(value!=null){
				
				if(value.equals(idvItem)){
					slinsList.add(t);
				}
			}
			
		}
		
		
		for(DOMElement t: slinsList){
			String value= t.getAttribute("id");
			
			if(value!=null){
				
				String index = value.replace("SearchResultsTableRN1:ShortDesc:", "").trim();
				String IDV =getCurrentWindowXPath() + "/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:span[@id='SearchResultsTableRN1:Attribute7:"+index+"']";
				
				String IDVNum= web.element(IDV).getAttribute("text");
				
				if(idvNum.contains(IDVNum)){
					String path =getCurrentWindowXPath() + "/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='SearchResultsTableRN1:AddGBPAStructure:"+index+"' and @value='Add IDV Structure to Cart']";
					web.button(path).click();
					
				}
				
			}
			
		}
	}
	else {
		
		info("Item :"+idvItem+"not found");
	}
	
}

	public List<DOMElement> getLinksInPage(String itemName) throws Exception{
	
	List<DOMElement> Linkslist= web.document(getCurrentWindowXPath() +"/web:document[@index='0']").getElementsByTagName("a");

	System.err.println("Linkslist size ="+Linkslist.size());
	
	List<DOMElement> slinsList = new ArrayList<DOMElement>();

	for(DOMElement t: Linkslist){
			String value= t.getAttribute("text");
			if(value!=null){
				
				System.err.println(value);
				if(value.trim().equals(itemName)){
					
					System.err.println("added");
					slinsList.add(t);
				}
			}
			
	}
	
	return slinsList;
	}

public void addItemFromFavToDocument(String itemName, String idvNum , String quantity) throws Exception {
	
	while(web.exists(getCurrentWindowXPath() + "/web:document[@index='0']/web:img[@alt='Select to view previous set']")){
		
		
		web.element(getCurrentWindowXPath() + "/web:document[@index='0']/web:img[@alt='Select to view previous set']").getParent().click();
		
		delay(10000);
		
	}
	
	
	boolean bFound = false;
	boolean bNext = true;
	///web:window[@index='0' or @title='Oracle iProcurement: Shop']/web:document[@index='0']/web:img[@alt='Select to view next set' or @index='29' or @src='http://rws3210406.us.oracle.com:8000/OA_HTML/cabo/images/cache/ctnavn.gif']
while((!bFound)&&(bNext)){
	
	
	List<DOMElement> slinsList = getLinksInPage(itemName);
		
		
		if((idvNum!= null)&&(idvNum.equals(""))){
		
			for(DOMElement t: slinsList){
				
			String value= t.getAttribute("id");
			System.err.println(value);
			if(value!=null){
				
				String index = value.replace("SearchResultsTableRN1:ShortDesc:", "").trim();
				String IDV =getCurrentWindowXPath() + "/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:span[@id='SearchResultsTableRN1:Attribute8:"+index+"']";
				
				String IDVNum= web.element(IDV).getAttribute("text");
				
				if(idvNum.contains(IDVNum)){
					String quantitypath =getCurrentWindowXPath() + "/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='SearchResultsTableRN1:Quantity:"+index+"']";
					web.textBox(quantitypath).setText(quantity);
					String path =getCurrentWindowXPath() + "/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='SearchResultsTableRN1:AddToDocument:"+index+"' and @value='Add to Document']";
					web.button(path).click();
					bFound = true;					 
					break;
					
				}
				
			}
			
		}
	}
		
	else{
			
			if(slinsList.size()>0){
				bFound = true;	
				DOMElement t = slinsList.get(0);
				t.focus();
				
				String value= t.getAttribute("id");
				System.err.println("value");
				if(value!=null){
					
					String index = value.replace("SearchResultsTableRN1:ShortDesc:", "").trim();
					
				
						String path =getCurrentWindowXPath() + "/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='SearchResultsTableRN1:AddToDocument:"+index+"' and @value='Add to Document']";
						
						web.button(path).click();
						bFound = true;					 
						break;
						
					
					
				}
			}else{
				System.err.println("Insided else of where links arent found");
			}
		}
		if(web.exists(getCurrentWindowXPath() + "/web:document[@index='0']/web:img[@alt='Select to view next set']")){
			
			web.element(getCurrentWindowXPath() + "/web:document[@index='0']/web:img[@alt='Select to view next set']").getParent().click();
			
			delay(10000);
			
		}else{
			bNext = false;
			if(!bFound){
				
				reportFailure("proclib: addItemFromFavToDocument Item :"+itemName+"not found");
				break;
			}
		}
	}
	
}
	

	public void selectApproverInManageApprovals(String approver) throws Exception{
	
	String index = web.getFocusedWindow().getAttribute("index");
	String docpath ="/web:window[@index='"+index+"']/web:document[@index='0']";
	List<DOMElement> bodyList = web.document(docpath).getElementsByTagName("body");
	DOMElement body = bodyList.get(0);
	List<DOMElement> spanList  = body.getElementsByTagName("span");
	DOMCheckbox requiredCheckBox  = null; 
	for(DOMElement  span :spanList){
		String text = span.getAttribute("text");
		
		if((text!=null)&&(text.equals(approver))){
			span.focus();
			requiredCheckBox  =(DOMCheckbox) span.getElementsByTagName("input").get(0);
			break;
			
		}
		
	}
	requiredCheckBox.check(true);
	}
	
	public void selectFirstValueFromLOV(String lovName) throws Exception{	
		
		String index = web.getFocusedWindow().getAttribute("index");
		String docpath ="/web:window[@index='"+index+"']/web:document[@index='0']";
		
		DOMElement actuallov = web.image(docpath+"/web:img[@alt='"+lovName+"*']").getParent();
		
		if((actuallov!=null)&&(actuallov.getTag().equals("a"))){
			
			actuallov.click();
			
		}else{
			
		web.image(docpath+"/web:img[@alt='"+lovName+"*']").click();
		}
		
		web.window("/web:window[@title='Search and Select List of Values']").waitForPage(20, true);
		
		web.selectBox("/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:form[@id='_LOVResFrm' or @name='_LOVResFrm' or @index='0']/web:select[(@id='categoryChoice' or @name='categoryChoice' or @index='0') and multiple mod 'False']").waitFor();
		web.selectBox("/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:form[@id='_LOVResFrm' or @name='_LOVResFrm' or @index='0']/web:select[(@id='categoryChoice' or @name='categoryChoice' or @index='0') and multiple mod 'False']").selectOptionByText("Display Name");
		
		web.textBox("/web:window[ @title='Search and Select List of Values']/web:document[@index='1']/web:form[@id='_LOVResFrm' or @name='_LOVResFrm' or @index='0']/web:input_text[@name='searchText' or @index='0']").setText("%");
		web.button("/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:form[@id='_LOVResFrm' or @name='_LOVResFrm' or @index='0']/web:button[@value='Go']").click();
		
		delay(10000);
		/*web.radioButton("/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:form[@id='_LOVResFrm' or @name='_LOVResFrm' or @index='0']/web:input_radio[@id='N1:N8:0' or (@name='N1:selected' and @value='0') or @index='0']").select();			
		
		web.button("/web:window[@index='1' or @title='Search and Select List of Values']/web:document[@index='1']/web:button[@value='Select']").click();
		*/
		
		web.image("/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:img[@alt='Quick Select']").click();
		
		delay(10000);
	}
}
class WebTable {
	@ScriptService oracle.oats.scripting.modules.basic.api.IteratingVUserScript IteratingVUserScript;
	
	/* Variable initialization */
	public static String javaScriptFilePath = null;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;

	public static WebDomService web = null;
	public static IteratingVUserScript itv = null;
	int SYNCTIME = 600;
	
	DOMDocument doc 		= null;
	String docPath 			= null;
	String formPath 		= null;
	String tableColName 	= null;
	String tableIndex 		= "0";
	String[] colNames 		= null;
	String tableListPath 	= null;
	String listPath			= null;
	int maxRowCount 		= 10;
	
	String[] defComponents = {"input","checkbox","radio","img","a","select","span","div","textarea","td"};
	String[] defAttributes = {"id","name","value","text","innertext","type","summary","index","alt","title","src"};

	@ScriptService oracle.oats.scripting.modules.applet.api.AppletService applet;

	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;

	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	
   public WebTable()  {
		
		//this(tableColName, 0);
	}
	
	
	WebTable(String tableColName) throws Exception {
		
		this(tableColName, 0);
	}
	
	WebTable(String tableColName, int tableIndex) throws Exception {
		
		this(tableColName, tableIndex, 200);
	}
	
	WebTable(String tableColName, String listPath) throws Exception {
		
		this(tableColName, 0, listPath, 200);
	}
	
	WebTable(String tableColName, String listPath,int syncTime) throws Exception {
		
		this(tableColName, 0, listPath, syncTime);
	}
	
	
	WebTable(String tableColName, int tableIndex ,int syncTime) throws Exception {
		
		this(tableColName, tableIndex, "/web:select[(@id='M__Id*') and multiple mod 'False']", syncTime);
	}

	
	WebTable(String tableColName, int tableIndex, String listPath, int syncTime) throws Exception {
		
		/* Set Column Name */
		this.tableColName = tableColName;
		
		this.tableIndex = Integer.toString(tableIndex);
		
		this.listPath = listPath;
		
		/* Initialize Sync Time */
		this.SYNCTIME = syncTime;
		
		/* initialize Table */
		initialize();
	}
	
	
	
	public void initialize() throws Exception {
		
		/* Document Path */
		String windowPath = getCurrentWindowXPath();
		
		/* Current Window XPath */
		int currentWindowIndex = Integer.parseInt(web.window(windowPath).getAttribute("index"));
		
		/* Doc Path */
		docPath = windowPath + "/web:document[@index='"+currentWindowIndex+"']";
		
		/* Form Path*/
		formPath = docPath + "/web:form[@index='0']";
		
		// Example: "c:\\work_around.js"
		String js = FileUtility.fileToString(WebTable.javaScriptFilePath);//Put the js on any path.
		
		/* Get webDocument */
		doc = web.document(docPath);
		
		/* Execute Java Script */
		doc.executeJavaScript(js);
		
		/* Table List Path */
		if(listPath != null){
			tableListPath = formPath + listPath;
		}
		
		waitForColumn(this.tableColName);
		
		getColumns();
		
	}
	
	public static void setScriptPath(String filePath){
		
		WebTable.javaScriptFilePath = filePath;
	}
	
	public void setMaxRowCount(int maxRowCount){
		
		this.maxRowCount = maxRowCount;
	}
	
	
	@SuppressWarnings("unchecked") 
	public String[] getColumns() throws Exception
	{
		String[] actColNames = doc.executeJsFunction("getColumns", tableColName, tableIndex);
		String[] colNames = new String[actColNames.length];
		
		for(int i=0;i<actColNames.length;i++){
			colNames[i] = actColNames[i].trim();
		}
		
		this.colNames = colNames;
			
		List<String> cols = new ArrayList(Arrays.asList(colNames));
		itv.info("Column Names :"+cols.toString());
			
		return colNames;
	}
	
	public int getColumnNumber(String colName ) throws Exception {
		
		colName= colName.trim();
		int colIndex = -2;
		
		if(this.colNames == null){
			getColumns();
		}
		if(!isANumber(colName))
		{
			List<String> lColNames = Arrays.asList(this.colNames);
		
			if(lColNames.contains(colName)){
				colIndex = lColNames.indexOf(colName);
			}
		}else{
			
			colIndex = Integer.parseInt(colName);
		
		}
		return colIndex;
	}
	
	
	public int getRowCount() throws Exception
	{
		String rowCount = doc.executeJsFunction("getRowCount", tableColName, tableIndex)[0];
		
		return Integer.parseInt(rowCount);
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 *  Clicks the image at specified  "Row Number" and "Column Number"
	 *  
	 * @param rowNum
	 * @param colNum
	 * @throws Exception
	 */
	public void clickImage(int rowNum, int colNum) throws Exception{
		
		/* Click Image with compSequence=0(first image in the cell)*/
		clickImage(rowNum, colNum, 0);
		
	}
	
	/**
	 * Clicks the image at specified "Row Number" and "Column Number"
	 * 
	 * @param rowNum
	 * @param colName
	 * @throws Exception
	 */
	public void clickImage(int rowNum, String colName) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click Image with compSequence=0(first image in the cell)*/
		clickImage(rowNum, colNum, 0);
		
	}

	/**
	 *  Clicks the Specified image at the specified "Row Number" and "Column Number"
	 *  
	 * @param rowNum
	 * @param colName
	 * @param imageName
	 * 			imageName means "alt" tag
	 * @throws Exception
	 */
	public void clickImage(int rowNum, String colName, String imageName) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click thre Image having specefied Image Tag */
		clickImage(rowNum, colNum, imageName);
	}
	
	/**
	 * Clicks image at the specified "Row Number" and "Column Number based on Sequence number "
	 * 
	 * @param rowNum
	 * @param colName
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickImage(int rowNum, String colName, int compSequence) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click Image with compSequence= any number 0(first image = 0 , second image =1)*/
		clickImage(rowNum, colNum, compSequence);
	}
	
	/**
	 *  Clicks the image at target column, based on the row number of specified sourcolName & rowValue
	 *  
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @throws Exception
	 */
	public void clickImage(String sourcolName, String rowValue, String targetCol) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickImage(colNames, rowValues , targetCol, 0);
	}
	
	/**
	 * Clicks the image at target column, based on the row number of specified sourcolNames & rowValues
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @throws Exception
	 */
	public void clickImage(String[] sourcolNames, String[] rowValues, String targetCol) throws Exception{
		
		clickImage(sourcolNames, rowValues , targetCol, 0);
	}
	
	/**
	 * Clicks the image at target column, based on the row number of specified sourcolName & rowValue
	 * And also depends on the sequence number of the component within the cell
	 * 
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickImage(String sourcolName, String rowValue, String targetCol, int compSequence) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickImage(colNames, rowValues , targetCol, compSequence);
	}

	/**
	 * Clicks the image at target column, based on the row number of specified sourcolNames & rowValues
	 * And also depends on the sequence number of the component within the cell
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickImage(String[] sourcolNames, String[] rowValues, String targetCol, int compSequence) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Image baded on RowNumber , Column Number , ImageName*/
		clickImage(rowNum, colNum, compSequence);
	}
	
	/**
	 * Clicks the image at target column, based on the row number of specified sourcolName & rowValue
	 * And also depends on the Image Name of the component within the cell
	 * 
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @param imageName
	 * 			ImageName means alt tag of the component
	 * @throws Exception
	 */
	public void clickImage(String sourcolName, String rowValue, String targetCol, String imageName) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickImage(colNames, rowValues , targetCol, imageName);
	}
	
	/**
	 * Clicks the image at target column, based on the row number of specified sourcolNames & rowValues
	 * And also depends on the Image Name of the component within the cell
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @param imageName
	 * 			ImageName means alt tag of the component
	 * @throws Exception
	 */
	public void clickImage(String[] sourcolNames, String[] rowValues, String targetCol, String imageName) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Image baded on RowNumber , Column Number , ImageName*/
		clickImage(rowNum, colNum, imageName);
	}
	
	/**
	 *  Clicks on image based on the RowNum , Column Number and Image Name
	 *  
	 * @param rowNum
	 * @param colNum
	 * @param imageName
	 * 			ImageName means alt tag of the component
	 * @throws Exception
	 */
	public void clickImage(int rowNum, int colNum, String imageName) throws Exception{
		
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected Image Name  :"+imageName);
		
		try{
			String ret = doc.executeJsFunction("clickElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "img", imageName, "null")[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to Click Image at "+rowNum+","+colNum);
			}else{
				itv.info("Clicked Image at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Clicks on image based on the RowNum , Column Number and Component Sequence Number
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param compSequence
	 * 			Sequence Number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickImage(int rowNum, int colNum, int compSequence) throws Exception{
		
			String errorMessage = "";
			
			System.out.println("Expected Row Num :"+rowNum);
			System.out.println("Expected Column Num :"+colNum);
			System.out.println("Expected compSequence Num  :"+compSequence);
			System.out.println("Expected tableColName  :"+tableColName);
			System.out.println("Expected tableIndex :"+tableIndex);
			
			System.out.println("Expected Integer.toString(rowNum):"+Integer.toString(rowNum));
			System.out.println("Expected Integer.toString(colNum) :"+Integer.toString(colNum));
			System.out.println("Expected Integer.toString(compSequence) :"+Integer.toString(compSequence));
			

			try{
				String ret = doc.executeJsFunction("clickElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "img", "null", Integer.toString(compSequence))[0];
				
				if(ret.equals("False")){
					throw new Exception("Unable to Click Image at "+rowNum+","+colNum);
				}else{
					itv.info("Clicked Image at "+rowNum+","+colNum);
				}
				
			}catch(Exception e){
				throw new Exception(e.getMessage());
			}
	}
	
	/**
	 *  Clicks the Lin at specified  "Row Number" and "Column Number"
	 *  
	 * @param rowNum
	 * @param colNum
	 * @throws Exception
	 */
	public void clickLink(int rowNum, int colNum) throws Exception{
		
		/* Click Link with compSequence=0(first link in the cell)*/
		clickLink(rowNum, colNum, 0);
		
	}
	
	/**
	 * Clicks the Link at specified "Row Number" and "Column Number"
	 * 
	 * @param rowNum
	 * @param colName
	 * @throws Exception
	 */
	public void clickLink(int rowNum, String colName) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click Link with compSequence=0(first link in the cell)*/
		clickLink(rowNum, colNum, 0);
		
	}

	/**
	 *  Clicks the Specified link at the specified "Row Number" and "Column Number"
	 *  
	 * @param rowNum
	 * @param colName
	 * @param linkName
	 * 			linkName means the text that appears
	 * @throws Exception
	 */
	public void clickLink(int rowNum, String colName, String linkName) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click the Link having specefied Link Tag */
		clickLink(rowNum, colNum, linkName);
	}
	
	/**
	 * Clicks link at the specified "Row Number" and "Column Number based on Sequence number "
	 * 
	 * @param rowNum
	 * @param colName
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickLink(int rowNum, String colName, int compSequence) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click Link with compSequence= any number 0(first link = 0 , second link =1)*/
		clickLink(rowNum, colNum, compSequence);
	}
	
	/**
	 *  Clicks the link at target column, based on the row number of specified sourcolName & rowValue
	 *  
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @throws Exception
	 */
	public void clickLink(String sourcolName, String rowValue, String targetCol) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickLink(colNames, rowValues , targetCol, 0);
	}
	
	/**
	 * Clicks the link at target column, based on the row number of specified sourcolNames & rowValues
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @throws Exception
	 */
	public void clickLink(String[] sourcolNames, String[] rowValues, String targetCol) throws Exception{
		
		clickLink(sourcolNames, rowValues , targetCol, 0);
	}
	
	/**
	 * Clicks the link at target column, based on the row number of specified sourcolName & rowValue
	 * And also depends on the sequence number of the component within the cell
	 * 
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickLink(String sourcolName, String rowValue, String targetCol, int compSequence) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickLink(colNames, rowValues , targetCol, compSequence);
	}

	/**
	 * Clicks the image at target column, based on the row number of specified sourcolNames & rowValues
	 * And also depends on the sequence number of the component within the cell
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickLink(String[] sourcolNames, String[] rowValues, String targetCol, int compSequence) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Image baded on RowNumber , Column Number , ImageName*/
		clickLink(rowNum, colNum, compSequence);
	}
	
	/**
	 * Clicks the link at target column, based on the row number of specified sourcolName & rowValue
	 * And also depends on the Link Name of the component within the cell
	 * 
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @param linkName
	 * 			linkName means the text that appears
	 * @throws Exception
	 */
	public void clickLink(String sourcolName, String rowValue, String targetCol, String linkName) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickLink(colNames, rowValues , targetCol, linkName);
	}
	
	/**
	 * Clicks the link at target column, based on the row number of specified sourcolNames & rowValues
	 * And also depends on the Link Name of the component within the cell
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @param linkName
	 * 			linkName means the text that appears
	 * @throws Exception
	 */
	public void clickLink(String[] sourcolNames, String[] rowValues, String targetCol, String linkName) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Link baded on RowNumber , Column Number , linkName*/
		clickLink(rowNum, colNum, linkName);
	}
	
	
	/**
	 *  Clicks on link based on the RowNum , Column Number and Image Name
	 *  
	 * @param rowNum
	 * @param colNum
	 * @param linkName
	 * 			linkName means the text that appears
	 * @throws Exception
	 */
	public void clickLink(int rowNum, int colNum, String linkName) throws Exception{
		
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected Link Name  :"+linkName);
		
		try{
			String ret = doc.executeJsFunction("clickElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "a", linkName, null)[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to Click Link at "+rowNum+","+colNum);
			}else{
				itv.info("Clicked Link at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Clicks on link based on the RowNum , Column Number and Component Sequence Number
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param compSequence
	 * 			Sequence Number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickLink(int rowNum, int colNum, int compSequence) throws Exception{
		
			String errorMessage = "";
			
			System.out.println("Expected Row Num :"+rowNum);
			System.out.println("Expected Column Num :"+colNum);
			System.out.println("Expected compSequence Num  :"+compSequence);
			
			try{
				String ret = doc.executeJsFunction("clickElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "a", "null", Integer.toString(compSequence))[0];
				
				if(ret.equals("False")){
					throw new Exception("Unable to Click Link at "+rowNum+","+colNum);
				}else{
					itv.info("Clicked Link at "+rowNum+","+colNum);
				}
				
			}catch(Exception e){
				throw new Exception(e.getMessage());
			}
	}
	
	
	public void setText(int rowNum, int colNum, String valueToEnter) throws Exception{
		
		/* set TextBox with default option set to true */
		setText(rowNum, colNum, valueToEnter, 0);
	
	}

	public void setText(int rowNum, String colName, String valueToEnter) throws Exception{
	
		/* Slect RadioButton with compSequence=0(first textbox in the cell)*/
		setText(rowNum, colName, valueToEnter, 0);
	
	}

	public void setText(int rowNum, String colName, String valueToEnter, int compSequence) throws Exception{
	
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
	
		/* Slect RadioButton with compSequence=0(first textbox in the cell)*/
		setText(rowNum, colNum, valueToEnter, compSequence);
	
	}

	public void setText(String sourceColName, String rowValue, String targetCol, String valueToEnter) throws Exception{
	
		setText(sourceColName, rowValue , targetCol, valueToEnter, 0);
	}

	public void setText(String sourceColName, String rowValue, String targetCol, String valueToEnter, int compSequence) throws Exception{
	
		/* Convert to Array */
		String[] colNames = {sourceColName};
		String[] rowValues = {rowValue};
	
		setText(colNames, rowValues , targetCol, valueToEnter, compSequence);
	}

	public void setText(String[] sourceColNames, String[] rowValues, String targetCol, String valueToEnter) throws Exception{
	
		setText(sourceColNames, rowValues , targetCol, valueToEnter, 0);
	}

	public void setText(String[] sourceColNames, String[] rowValues, String targetCol, String valueToEnter, int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
	
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Image baded on RowNumber , Column Number , ImageName*/
		setText(rowNum, colNum, valueToEnter, compSequence);
	}

	public void setText(int rowNum, int colNum, String valueToEnter, int compSequence) throws Exception{
	
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		itv.info("Value to Enter  :"+valueToEnter);
		
		try{
			String ret = doc.executeJsFunction("setElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "input", valueToEnter, Integer.toString(compSequence))[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to Set the Value in the Text Box at "+rowNum+","+colNum);
			}else{
				itv.info("Set the Value in the Text Box at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}

	public String getTextAreaValue(int rowNum, int colNum) throws Exception{
		
		return getTextAreaValue(rowNum ,colNum, 0 );
	}

	public String getTextAreaValue(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getTextAreaValue(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getTextAreaValue(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getTextAreaValue(colNames, rowValues, targetCol, 0 );
	}

	public String getTextAreaValue(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getTextAreaValue(rowNum ,colNum, compSequence );
	}
	
	public String getTextAreaValue(int rowNum, int colNum,  int compSequence) throws Exception{
		
		String value = "";
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> text = getCellData(rowNum, colNum,"textarea",Integer.toString(compSequence));
		
		if(text.size() == 0){
			itv.info("TextArea Value at "+rowNum+","+colNum+" is : empty");
		}else{
			value = text.get(0);
			itv.info("TextArea Value at "+rowNum+","+colNum+" is "+text.toString());
		}
		
		
		return value;
	}
	
	
public String getLink(int rowNum, int colNum) throws Exception{
		
		return getLink(rowNum ,colNum, 0 );
	}

	public String getLink(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getLink(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getLink(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getLink(colNames, rowValues, targetCol, 0 );
	}

	public String getLink(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getLink(rowNum ,colNum, compSequence );
	}
	
	public String getLink(int rowNum, int colNum,  int compSequence) throws Exception{
		
		String value = "";
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> text = getCellData(rowNum, colNum,"a",Integer.toString(compSequence));
		
		if(text.size() == 0){
			itv.info("Link Value at "+rowNum+","+colNum+" is : empty");
		}else{
			value = text.get(0);
			itv.info("Link Value at "+rowNum+","+colNum+" is "+text.toString());
		}
		
		
		return value;
	}
	
	
public String getImage(int rowNum, int colNum) throws Exception{
		
		return getImage(rowNum ,colNum, 0 );
	}

	public String getImage(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getImage(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getImage(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getImage(colNames, rowValues, targetCol, 0 );
	}

	public String getImage(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getImage(rowNum ,colNum, compSequence );
	}
	
	public String getImage(int rowNum, int colNum,  int compSequence) throws Exception{
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> checkData = getCellData(rowNum, colNum,"img",Integer.toString(compSequence));
		
		itv.info("Img at "+rowNum+","+colNum+" is "+checkData.toString());
		
		return checkData.get(0);
	}
	
	public String getText(int rowNum, int colNum) throws Exception{
		
		return getText(rowNum ,colNum, 0 );
	}

	public String getText(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getText(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getText(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getText(colNames, rowValues, targetCol, 0 );
	}

	public String getText(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getText(rowNum ,colNum, compSequence );
	}
	
	public String getText(int rowNum, int colNum,  int compSequence) throws Exception{
		
		String value = "";
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> text = getCellData(rowNum, colNum,"input",Integer.toString(compSequence));
		
		if(text.size() == 0){
			itv.info("TextBox Value at "+rowNum+","+colNum+" is : empty");
		}else{
			value = text.get(0);
			itv.info("TextBox Value at "+rowNum+","+colNum+" is "+text.toString());
		}
		
		
		return value;
	}
	
	
	public void setTextArea(int rowNum, int colNum, String valueToEnter) throws Exception{
		
		/* set TextArea with default option set to true */
		setTextArea(rowNum, colNum, valueToEnter, 0);
	
	}

	public void setTextArea(int rowNum, String colName, String valueToEnter) throws Exception{
	
		/* Slect TextArea with compSequence=0(first TextArea in the cell)*/
		setTextArea(rowNum, colName, valueToEnter, 0);
	
	}

	public void setTextArea(int rowNum, String colName, String valueToEnter, int compSequence) throws Exception{
	
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
	
		/* Set TextArea with compSequence=0(first TextArea in the cell)*/
		setTextArea(rowNum, colNum, valueToEnter, compSequence);
	
	}

	public void setTextArea(String sourceColName, String rowValue, String targetCol, String valueToEnter) throws Exception{
	
		setTextArea(sourceColName, rowValue , targetCol, valueToEnter, 0);
	}

	public void setTextArea(String sourceColName, String rowValue, String targetCol, String valueToEnter, int compSequence) throws Exception{
	
		/* Convert to Array */
		String[] colNames = {sourceColName};
		String[] rowValues = {rowValue};
	
		setTextArea(colNames, rowValues , targetCol, valueToEnter, compSequence);
	}

	public void setTextArea(String[] sourceColNames, String[] rowValues, String targetCol, String valueToEnter) throws Exception{
	
		setTextArea(sourceColNames, rowValues , targetCol, valueToEnter, 0);
	}

	public void setTextArea(String[] sourceColNames, String[] rowValues, String targetCol, String valueToEnter, int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
	
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Image baded on RowNumber , Column Number , ImageName*/
		setTextArea(rowNum, colNum, valueToEnter, compSequence);
	}

	public void setTextArea(int rowNum, int colNum, String valueToEnter, int compSequence) throws Exception{
	
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		itv.info("Value to Enter in Text Area  :"+valueToEnter);
		
		try{
			String ret = doc.executeJsFunction("setElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "textarea", valueToEnter, Integer.toString(compSequence))[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to Set the Value in the Text Area at "+rowNum+","+colNum);
			}else{
				itv.info("Set the Value in the Text Area at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	
	
	public void selectCheckBox(int rowNum, int colNum) throws Exception{
		
		/* Slect CheckBox with default option set to true */
		selectCheckBox(rowNum, colNum, true);
		
	}
	
	public void selectCheckBox(int rowNum, int colNum, boolean checkCheckBox) throws Exception{
		
		/* Select CheckBox based on compSequence = '0' with the specified option "checkCheckBox"(true or false) */
		selectCheckBox(rowNum, colNum, checkCheckBox, 0);
	}
	
	public void selectCheckBox(int rowNum, String colName) throws Exception{
		
		/* Slect CheckBox with compSequence=0(first checkbox in the cell)*/
		selectCheckBox(rowNum, colName, true);
		
	}
	
	public void selectCheckBox(int rowNum, String colName, boolean checkCheckBox) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Slect CheckBox with compSequence=0(first checkbox in the cell)*/
		selectCheckBox(rowNum, colNum, checkCheckBox,0);
		
	}

	public void selectCheckBox(int rowNum, String colName, boolean checkCheckBox, int compSequence) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Slect CheckBox with compSequence=0(first checkbox in the cell)*/
		selectCheckBox(rowNum, colNum, checkCheckBox,compSequence);
		
	}

	public void selectCheckBox(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		selectCheckBox(sourceColName, rowValue , targetCol, true);
	}
	
	public void selectCheckBox(String sourceColName, String rowValue, String targetCol, boolean checkCheckBox) throws Exception{
		
		selectCheckBox(sourceColName, rowValue , targetCol, checkCheckBox,0);
	}
	
	public void selectCheckBox(String sourceColName, String rowValue, String targetCol, boolean checkCheckBox, int compSequence) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourceColName};
		String[] rowValues = {rowValue};
		
		selectCheckBox(colNames, rowValues , targetCol, checkCheckBox,compSequence);
	}
	
	public void selectCheckBox(String[] sourceColNames, String[] rowValues, String targetCol) throws Exception{
		
		selectCheckBox(sourceColNames, rowValues , targetCol, true);
	}
	
	public void selectCheckBox(String[] sourceColNames, String[] rowValues, String targetCol, boolean checkCheckBox) throws Exception{
		
		selectCheckBox(sourceColNames, rowValues , targetCol, checkCheckBox, 0);
	}
	
	public void selectCheckBox(String[] sourceColNames, String[] rowValues, String targetCol, boolean checkCheckBox, int compSequence) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Image baded on RowNumber , Column Number , ImageName*/
		selectCheckBox(rowNum, colNum, checkCheckBox, compSequence);
	}
	
	public void selectCheckBox(int rowNum, int colNum, boolean checkCheckBox, int compSequence) throws Exception{
		
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		itv.info("Expected Check Option :"+checkCheckBox);
		
		try{
			String ret = doc.executeJsFunction("setElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "checkbox", Boolean.toString(checkCheckBox), Integer.toString(compSequence))[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to check the Check Box at "+rowNum+","+colNum);
			}else{
				itv.info("Checked the Check Box at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	
	public String getCheckBoxOption(int rowNum, int colNum) throws Exception{
		
		return getCheckBoxOption(rowNum ,colNum, 0 );
	}

	public String getCheckBoxOption(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getCheckBoxOption(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getCheckBoxOption(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getCheckBoxOption(colNames, rowValues, targetCol, 0 );
	}

	public String getCheckBoxOption(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getCheckBoxOption(rowNum ,colNum, compSequence );
	}
	
	public String getCheckBoxOption(int rowNum, int colNum,  int compSequence) throws Exception{
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> checkData = getCellData(rowNum, colNum,"checkbox",Integer.toString(compSequence));
		
		itv.info("Check Box Status at "+rowNum+","+colNum+" is "+checkData.toString());
		
		return checkData.get(0);
	}
	
	
	
	
	
	
	
	
	public void selectList(int rowNum, int colNum, String itemToSelect) throws Exception{
		
		/* Select SelectBox based on compSequence = '0' with the specified select option */
		selectList(rowNum, colNum, itemToSelect, 0);
	}
	
	
	public void selectList(int rowNum, String colName, String itemToSelect) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Slect List with compSequence=0(first listbox in the cell)*/
		selectList(rowNum, colNum, itemToSelect,0);
		
	}

	public void selectList(int rowNum, String colName,String itemToSelect, int compSequence) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Slect List with compSequence=0(first selectBox in the cell)*/
		selectList(rowNum, colNum, itemToSelect,compSequence);
		
	}

	
	public void selectList(String sourceColName, String rowValue, String targetCol, String itemToSelect) throws Exception{
		
		selectList(sourceColName, rowValue , targetCol, itemToSelect,0);
	}
	
	public void selectList(String sourceColName, String rowValue, String targetCol, String itemToSelect, int compSequence) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourceColName};
		String[] rowValues = {rowValue};
		
		selectList(colNames, rowValues , targetCol, itemToSelect,compSequence);
	}
	
	public void selectList(String[] sourceColNames, String[] rowValues, String targetCol, String itemToSelect) throws Exception{
		
		selectList(sourceColNames, rowValues , targetCol, itemToSelect, 0);
	}
	
	public void selectList(String[] sourceColNames, String[] rowValues, String targetCol, String itemToSelect, int compSequence) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Select list Item based on RowNumber , Column Number , ImageName*/
		selectList(rowNum, colNum, itemToSelect, compSequence);
	}
	
	public void selectList(int rowNum, int colNum,  String itemToSelect, int compSequence) throws Exception{

		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected Item to Select :"+itemToSelect);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		try{
			String ret = doc.executeJsFunction("setElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "select", itemToSelect, Integer.toString(compSequence))[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to Select the Value in the ListBox at "+rowNum+","+colNum);
			}else{
				itv.info("Selected the Value in the ListBox at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	public String getSlectedOption(int rowNum, int colNum) throws Exception{
		
		return getSlectedOption(rowNum, colNum, 0 );
	}

	public String getSlectedOption(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getSlectedOption(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getSlectedOption(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getSlectedOption(colNames, rowValues, targetCol, 0 );
	}

	public String getSlectedOption(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getSlectedOption(rowNum ,colNum, compSequence );
	}
	
	public String getSlectedOption(int rowNum, int colNum,  int compSequence) throws Exception{
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> selectOption = getCellData(rowNum, colNum,"select",Integer.toString(compSequence));
		
		itv.info("Selected List Value at "+rowNum+","+colNum+" is "+selectOption.toString());
		
		return selectOption.get(0);
	}
	
	
	public void selectRadioButton(int rowNum, int colNum) throws Exception{
		
		/* Slect CheckBox with default option set to true */
		selectRadioButton(rowNum, colNum, 0);
	
	}

	public void selectRadioButton(int rowNum, String colName) throws Exception{
	
		/* Slect RadioButton with compSequence=0(first radiobutton in the cell)*/
		selectRadioButton(rowNum, colName, 0);
	
	}

	public void selectRadioButton(int rowNum, String colName, int compSequence) throws Exception{
	
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
	
		/* Slect RadioButton with compSequence=0(first RadioButton in the cell)*/
		selectRadioButton(rowNum, colNum, compSequence);
	
	}

	public void selectRadioButton(String sourceColName, String rowValue, String targetCol) throws Exception{
	
		selectRadioButton(sourceColName, rowValue , targetCol, 0);
	}

	public void selectRadioButton(String sourceColName, String rowValue, String targetCol, int compSequence) throws Exception{
	
		/* Convert to Array */
		String[] colNames = {sourceColName};
		String[] rowValues = {rowValue};
	
		selectRadioButton(colNames, rowValues , targetCol, compSequence);
	}

	public void selectRadioButton(String[] sourceColNames, String[] rowValues, String targetCol) throws Exception{
	
		selectRadioButton(sourceColNames, rowValues , targetCol, 0);
	}

	public void selectRadioButton(String[] sourceColNames, String[] rowValues, String targetCol, int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
	
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Select the Radiobutton based on RowNumber , Column Number , ImageName*/
		selectRadioButton(rowNum, colNum, compSequence);
	}

	public void selectRadioButton(int rowNum, int colNum,  int compSequence) throws Exception{
	
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		try{
			
			String ret = doc.executeJsFunction("setElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "radio", Boolean.toString(true), Integer.toString(compSequence))[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to Select Radio button at "+rowNum+","+colNum);
			}else{
				itv.info("Selected Radio button at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	public String getRadioOption(int rowNum, int colNum) throws Exception{
		
		return getRadioOption(rowNum, colNum, 0 );
	}

	public String getRadioOption(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getRadioOption(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getRadioOption(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getRadioOption(colNames, rowValues, targetCol, 0 );
	}

	public String getRadioOption(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getRadioOption(rowNum ,colNum, compSequence );
	}
	
	public String getRadioOption(int rowNum, int colNum,  int compSequence) throws Exception{
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> radioOption = getCellData(rowNum, colNum,"radio",Integer.toString(compSequence));
		
		itv.info("Radio Status at "+rowNum+","+colNum+" is "+radioOption.toString());
		
		return radioOption.get(0);
	}
	
	
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 *  Gets Row Number
	 *  
	 * @param colNames
	 * @param rowValues
	 * @return
	 * @throws Exception
	 */
	public int getRowNumber(String[] colNames, String[] rowValues) throws Exception{
		
		return getRowNumber(colNames, rowValues, true);
	}

	
	/**
	 *  Get Row Number
	 *  
	 * @param colNames
	 * @param rowValues
	 * @param partialMatch
	 * @return
	 * @throws Exception
	 */
	public int getRowNumber (String[] colNames, String[] rowValues, boolean partialMatch) throws Exception{
		
		
		/* Initialize Variables*/
		int rowNum = -1;
		boolean rowExists = false;
		int listIndex = 0;
		boolean listboxExists = false;
		
		itv.info("Expected Column Names : "+Arrays.asList(colNames).toString());
		itv.info("Expected Row Values : "+Arrays.asList(rowValues).toString());
		
		if(tableListBoxExists()){
			
			itv.info("Navigating Through Table");
			
			/* Get Table List Count */
			int listCount = getTableListCount();
			
			for(; listIndex < listCount; listIndex++){
				
				/* Select List one by one */
				selectTableList(listIndex);

				/* Wait for the Column */
				waitForColumn(colNames[0]);
				
				/* Get the Row Number */
				rowNum = getRowNum(colNames , rowValues , partialMatch);
				
				if(rowNum > 0 ){
					break;
				}
				
				listCount = getTableListCount();
			}
			
		} else {

			/* Get the Row Number */
			rowNum = getRowNum(colNames , rowValues , partialMatch);
		}	
			
		itv.info("Row Number in the Table :\""+(listIndex+1)+"\" is : "+rowNum);
		
		return rowNum;
	}
	
	/**
	 * 
	 * @param tablePath
	 * @param colNames
	 * @param rowValues
	 * @param partialMatch
	 * @return
	 * @throws Exception
	 */
	public int getRowNum(String[] colNames, String[] rowValues, boolean partialMatch) throws Exception{
		
		int rowNumber = -1;
		boolean itemFound = false;
		
		/* Get Column Names*/
		String[] actualColNames = this.colNames;
		List<String> lActualColNames = Arrays.asList(actualColNames);
		
		/* Get Row Count*/
		int rowCount = getRowCount();
		System.out.println("Row Count is : "+(rowCount));
		
		/* Navigate through each row for getting cell value 
		 * rowIndex start with "2" (since the rownum =1 starts with headers)
		 * */
		for(int rowIndex=1; rowIndex < rowCount; rowIndex++){
			
			/* Verification of Data in the Specified Row*/
			for(int expColIndex = 0 ; expColIndex < colNames.length ; expColIndex++){
				
				int colIndex = -1;
				if(!isANumber(colNames[expColIndex].trim() )){
					colIndex	= lActualColNames.indexOf(colNames[expColIndex]);
				}else{
				
					colIndex = Integer.parseInt(colNames[expColIndex].trim()); //
				}
				List<String> cellData = new ArrayList<String>();
				
				/*displayTime("Table GetCell Method : start");
				String cellValue = web.table(tablePath).getCell(rowIndex, colIndex+1);
				displayTime("Table GetCell Method : end");
				
				if(cellValue != null){
					cellData.add(cellValue);
				}else{
					 Get the Cell Value -- Here colIndex starts with "0"
					displayTime("Custom GetCell Method : start");
					cellData = getCellData(rowIndex-1, colIndex+1);
					displayTime("Custom GetCell Method : end");
				}*/
				
				/* Get the Cell Value -- Here colIndex starts with "0"*/
				cellData = getCellData(rowIndex, colIndex);
				
				System.out.println("cell Data :"+cellData.toString());
				
				/* Verification of actual Cell value with expected Cell Value
				 * If value is not matched then it comes out of verification "for" loop
				 * */
				if(partialMatch){
					
					boolean itemAvailable = false;
					for(int cellItemIndex=0; cellItemIndex<cellData.size();cellItemIndex++){
						if(compareStrings(rowValues[expColIndex],cellData.get(cellItemIndex))){
							itemAvailable = true;
							break;
						}
					}
					if(!itemAvailable)
					{
						break;
					}
				}else{
					if(!cellData.contains(rowValues[expColIndex]))
					{
						break;
					}
				}
				if(expColIndex == (colNames.length - 1)){
					itemFound = true;
				}
			}
			
			/* If Item is not found , Comes out of the For and then return -1 as row number
			 * If Item found , then returns the RowNumber;
			 * */
			if(itemFound){
				rowNumber = rowIndex;
				break;
			}
		}
		
		return (rowNumber);
	}
	
	
	
	
	

	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	public List<String> getCellData(int rowIndex, int ColumnIndex) throws Exception {
		
		return getCellData(rowIndex, ColumnIndex, "0");
	}
	
	
	public List<String> getCellData(int rowNum, String colName) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		return getCellData(rowNum, colNum, "0");
	}
	
	public List<String> getCellData(int rowNum, String colName, String compType) throws Exception{
		
		return getCellData(rowNum, colName, compType, "0");
	}
	
	public List<String> getCellData(int rowNum, String colName, String compType, String compIndex) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		return getCellData(rowNum, colNum, compType, compIndex);
	}	
	
	public List<String> getCellData(int rowNum, String colName, int compSequence) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		return getCellData(rowNum, colNum,Integer.toString(compSequence));
	}	
	
	public List<String> getCellData(String sourcolName, String rowValue, String targetCol) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		return getCellData(colNames, rowValues , targetCol, 0);
	}
	
	public List<String> getCellData(String[] sourcolNames, String[] rowValues, String targetCol) throws Exception{
		
		return getCellData(sourcolNames, rowValues , targetCol, 0);
	}
	
	public List<String> getCellData(String sourcolName, String rowValue, String targetCol, int compSequence) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		return getCellData(colNames, rowValues , targetCol, compSequence);
	}
	
	public List<String> getCellData(String[] sourcolNames, String[] rowValues, String targetCol, int compSequence) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		return getCellData(rowNum, colNum, Integer.toString(compSequence));
	}
	
	
	public List<String> getCellData(String sourcolName, String rowValue, String targetCol, String compType) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		return getCellData(colNames, rowValues, targetCol, compType);
	}
	
	public List<String> getCellData(String[] sourcolNames, String[] rowValues, String targetCol, String compType) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		return getCellData(rowNum, colNum, compType, "0");
	}


	public List<String> getCellData(int rowIndex, int ColumnIndex, String compIndex) throws Exception {
		
		List<String> cellData = new ArrayList<String>();
		
		for(int index=0;index<defComponents.length;index++){
			
			String[] data = getValueByType(Integer.toString(rowIndex), Integer.toString(ColumnIndex), defComponents[index], compIndex);
			
			for(int dataIndex = 0; dataIndex<data.length; dataIndex++){
				if(data[dataIndex] != ""){
					cellData.add(data[dataIndex]);
				}
			}
		}
		
		return cellData;
	}
	
	public List<String> getCellData(int rowIndex, int ColumnIndex, String compType, String compIndex) throws Exception {
		
		List<String> cellData = new ArrayList<String>();
		
		String[] data = getValueByType(Integer.toString(rowIndex), Integer.toString(ColumnIndex), compType, compIndex);
		
		for(int dataIndex = 0; dataIndex<data.length; dataIndex++){
			if(data[dataIndex] != ""){
				cellData.add(data[dataIndex]);
			}
		}
		
		return cellData;
	}
	
	public List<String> getCellData(int rowIndex, int ColumnIndex, String[] expCompTypes) throws Exception {
		
		return getCellData(rowIndex, ColumnIndex, expCompTypes, "0");
	}
	
	public List<String> getCellData(int rowIndex, int ColumnIndex, String[] expCompTypes, String compIndex) throws Exception {
		
		List<String> cellData = new ArrayList<String>();
		
		for(int index=0;index<expCompTypes.length;index++){
			
			String[] data = getValueByType(Integer.toString(rowIndex), Integer.toString(ColumnIndex), expCompTypes[index], compIndex);
			
			for(int dataIndex = 0; dataIndex<data.length; dataIndex++){
				if(data[dataIndex] != ""){
					cellData.add(data[dataIndex]);
				}
			}
		}
		
		return cellData;
	}
	
	/**
	 *  Verifies Row Data
	 *  
	 * @param colName
	 * @param rowValue
	 * @return
	 * @throws Exception
	 */
	public boolean verifyRowData(String colName, String rowValue) throws Exception {
		
		return verifyRowData(colName , rowValue ,  true);
		
	}
	
	/**
	 *  Verifies Row Data
	 *   
	 * @param colName
	 * @param rowValue
	 * @param partialMatch
	 * @return
	 * @throws Exception
	 */
	public boolean verifyRowData(String colName, String rowValue , boolean partialMatch) throws Exception {
	
		String[] columnNames = {colName};
		String[] rowValues = {rowValue};
		
		return verifyRowData(columnNames , rowValues ,  partialMatch);
	
	}
	
	/**
	 * Verifies Row Data
	 *  
	 * @param columnNames
	 * @param rowValues
	 * @return
	 * @throws Exception
	 */
	public boolean verifyRowData(String[] columnNames, String[] rowValues) throws Exception {
		
		return verifyRowData(columnNames , rowValues ,  true);
		
	}

	/**
	 * 
	 * Verifies Row Data
	 * 
	 * @param columnNames
	 * @param rowValues
	 * @param partialMatch
	 * @return
	 * @throws Exception
	 */
	public boolean verifyRowData(String[] columnNames, String[] rowValues , boolean partialMatch) throws Exception {
		
		System.out.println("*** Verifying Row Data  ******");
		System.out.println("Class Name : \"Table\" and Method Name : \" verifyRowData() \" ");
		
		/* Initialize Variables*/
		boolean rowExists = false;
		
		/* Get Row Data */
		int rowNum = getRowNumber(columnNames , rowValues , partialMatch);
		
		/**
		 * If Row Num is less than "1" means specified data is not there in the table
		 * 
		 * If Row Num greater than equal to "1" means specified data is available in the table
		 */
		if(rowNum < 1) {
			rowExists = false;
		}else{
			rowExists = true;
		}
		
		if(rowExists){
			itv.info("Specified data exists at row number : " +rowNum);
		}else{
			itv.info("Specified data doesnot exists");
		}
		
		return rowExists;
	}
	
	
	
	public boolean verifyTableData(String colName, String[][] expectedTableData) throws Exception {
		
		String[] colNames = {colName};
		String[] sExpComponents = defComponents;
		return verifyTableData(colNames , expectedTableData , sExpComponents);
		
	}
	
	public boolean verifyTableData(String colName, String[][] expectedTableData, String expComponent) throws Exception {
		
		String[] colNames = {colName};
		String[] sExpComponents = {expComponent};
		return verifyTableData(colNames , expectedTableData , sExpComponents);
		
	}

	public boolean verifyTableData(String[] colNames, String[][] expectedTableData , String[] sExpComponents) throws Exception {
		
		/* Table Data in HashMap */
		HashMap<String,List<String>> expectedTableLinesData = new HashMap<String,List<String>>();
		
		/* Convert all the array data into a HashMap */
		for(int hashIndex=1,index=0; index<expectedTableData.length; index++,hashIndex++){
		
			List<String> row = new ArrayList<String>();
			
			for(int colIndex=0;colIndex<expectedTableData[index].length;colIndex++){
				row.add(expectedTableData[index][colIndex]);
			}
			
			expectedTableLinesData.put(Integer.toString(hashIndex),row);
		}
		
		return verifyTableData(colNames , expectedTableLinesData , sExpComponents);
	}

	
	public boolean verifyTableData(String colName, HashMap<String,List<String>> expectedTableData) throws Exception {
		
		String[] colNames = {colName};
		String[] sExpComponents = defComponents;
		return verifyTableData(colNames , expectedTableData , sExpComponents);
		
	}

	public boolean verifyTableData(String colName, HashMap<String,List<String>> expectedTableData, String expComponent) throws Exception {
		
		String[] colNames = {colName};
		String[] sExpComponents = {expComponent};
		return verifyTableData(colNames , expectedTableData , sExpComponents);
		
	}

	public boolean verifyTableData(String[] colNames, HashMap<String,List<String>> expectedTableData , String[] sExpComponents) throws Exception {
		
		/* Initialize Data */
		HashMap<String,HashMap<String,List<String>>> tableData = new HashMap<String,HashMap<String,List<String>>>();
		HashMap<String,List<String>> nonVerifiedData = new HashMap<String,List<String>>();
		
		if(tableListBoxExists()){
			
			itv.info("Navigating Through Table");
			
			/* Get Table List Count */
			int listCount = getTableListCount();
			int listIndex = 0;
			
			for(; listIndex < listCount; listIndex++){
				
				/* Select List one by one */
				selectTableList(listIndex);

				/* Wait for the Column */
				waitForColumn(colNames[0]);
				
				/* Get the Row Number */
				tableData = getTableData(colNames, sExpComponents);
				
				/* Verify Table Data*/
				nonVerifiedData = verifyData(tableData, expectedTableData, colNames);
				
				/* Change Expected Data*/
				if(nonVerifiedData.size() > 0){
					expectedTableData = null;
					expectedTableData = nonVerifiedData;
				}else{
					break;
				}
				
				listCount = getTableListCount();
			}
			
		} else {
			
			/* Get the Row Number */
			tableData = getTableData(colNames, sExpComponents);
			itv.info("Actual Number of Rows in the Table :"+tableData.size());
			itv.info("Expected Number of Rows to verify :"+expectedTableData.size());
			
			/* Verify Table Data*/
			nonVerifiedData = verifyData(tableData, expectedTableData,colNames);
		}
		
		boolean dataVerified = false ;
		
		if(nonVerifiedData.size() == 0){
			dataVerified = true;
		}else{
			
			/* Row Number Keyset*/
			List<String> rowNum = convertStringArrayToList(nonVerifiedData.keySet().toArray());
			
			itv.info("Row(s) Data which is not available in the Table :");
			for(int index=0;index < rowNum.size();index++){
				List<String> colData = nonVerifiedData.get(rowNum.get(index));
				itv.warn(colData.toString());
			}
			dataVerified = false;
		}
		return dataVerified;
	
	}
	
	public HashMap<String,List<String>> verifyData(HashMap<String,HashMap<String,List<String>>> actualTableData ,HashMap<String,List<String>> expectedTableData, String[] colNames ) throws AbstractScriptException{
		
		HashMap<String,List<String>> verifiedTableData = new HashMap<String,List<String>>();
		HashMap<String,List<String>> nonVerifiedTableData = new HashMap<String,List<String>>();
		HashMap<String,HashMap<String,List<String>>> actualTableDataToVerify = new HashMap<String,HashMap<String,List<String>>>();
		
		for(int expIndex=0,verifiedIndex=1, notVerifiedIndex=1;expIndex<expectedTableData.size();expIndex++){
			
			/* Get Row Data from expected Data */
			int expRowIndex = expIndex+1;
			List<String> expRowData = expectedTableData.get(Integer.toString(expRowIndex));
			System.out.println("Expected Row"+expRowIndex+ "Data to verify :"+expRowData.toString());
			
			actualTableDataToVerify = actualTableData;
			actualTableData = new HashMap<String,HashMap<String,List<String>>>();
			boolean rowVerified = false;
			for(int index=0,yetToVerify=1;index<actualTableDataToVerify.size();index++){
				
				/* Get Row Data */
				int rowIndex = index+1;
				HashMap<String, List<String>> actRowData = actualTableDataToVerify.get(Integer.toString(rowIndex));
				
				boolean cellVerified = false;
				for(int expColIndex=0; expColIndex < expRowData.size(); expColIndex++){
					
					/* Get Expected Cell Data */
					String expCellVal = expRowData.get(expColIndex);
					System.out.println("expCellVal_verifyData : "+expCellVal);
					
					if(expCellVal == null || expCellVal == ""){
						continue;
					}
					
					/* Get Actual Cell Data */
					List<String> cellData  = actRowData.get(colNames[expColIndex]);
					System.out.println("actCellData_verifyData : "+cellData.toString());
					
					boolean cellDataExists = false;
					for(int cellDataIdex=0;cellDataIdex<cellData.size();cellDataIdex++){
						cellDataExists = compareStrings(expCellVal,cellData.get(cellDataIdex));
						if(cellDataExists){
							break;
						}
					}
					
					if(cellDataExists){
						cellVerified = true;
					}else{
						cellVerified = false;
						break;
					}
				}
				/* Checking row is verified completely or not
				 * If Yes, Go with Verification of another row
				 * */
				if(cellVerified){
					rowVerified = true;
					
					for(index =index+1;index<actualTableDataToVerify.size();index++){
						
						rowIndex = index+1;
						actRowData = actualTableDataToVerify.get(Integer.toString(rowIndex));
						
						actualTableData.put(Integer.toString(yetToVerify), actRowData);
						yetToVerify++;
					}
					break;
					
					
				}else{
					actualTableData.put(Integer.toString(yetToVerify), actRowData);
					yetToVerify++;
				}
			}
			
			if(rowVerified){
				itv.info("Verified Data: "+expRowData.toString());
				verifiedTableData.put(Integer.toString(verifiedIndex), expRowData);
				verifiedIndex++;
			}else{
				System.out.println("Non Verified Data: "+expRowData.toString());
				nonVerifiedTableData.put(Integer.toString(notVerifiedIndex), expRowData);
				notVerifiedIndex++;
			}
		}
		
		System.out.println("Size of nonVerifiedTableData :"+nonVerifiedTableData.size());
		return nonVerifiedTableData;
	}
	
	/**
	 *  Get Table Data (rowNumber, {ColName,Data})
	 *  
	 * @param colNames
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,HashMap<String,List<String>>> getTableData(String[] colNames) throws Exception {
		
		return getTableData(colNames , defComponents);
	}
	
	/**
	 *  Get Table Data (rowNumber, {ColName,Data})
	 *  
	 * @param colName
	 * @param expComponent
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,HashMap<String,List<String>>> getTableData(String colName , String expComponent) throws Exception {
		
		String[] colNames = {colName};
		String[] expComponents = {expComponent};
		
		return getTableData(colNames, expComponents);
	}	
		
		/**
		 *  Get Table Data (rowNumber, {ColName,Data})
		 *  
		 * @param tablePath
		 * @param colNames
		 * @return
		 * @throws Exception
		 */
		public HashMap<String,HashMap<String,List<String>>> getTableData(String[] colNames , String[] expComponents) throws Exception {
			
			/* Initialize Data */
			HashMap<String,HashMap<String,List<String>>> tableData = new HashMap<String,HashMap<String,List<String>>>();
			
			/* Get Row Count*/
			int rowCount = getRowCount();
			//itv.info("Row Count is : "+rowCount);
			
			/* Get Column Names*/
			String[] actualColNames = this.colNames;
			List<String> lActualColNames = Arrays.asList(actualColNames);
			
			/* RowCount includes "Header also"*/
			for(int rowIndex=1;rowIndex<rowCount;rowIndex++){
				
				/* Get the Data of the specified Columns in a row */
				HashMap<String,List<String>> rowData = new HashMap<String,List<String>>();
				for(int expColIndex =0;expColIndex <colNames.length;expColIndex ++){
					
					/* Expected Column Name */
					String colName = colNames[expColIndex];
					
					/* Expected Column Index */
					int colIndex = lActualColNames.indexOf(colName);
					
					/* Get the Cell Data */
					List<String> cellData = new ArrayList<String>();
					if(expComponents == null){
						cellData = getCellData(rowIndex, colIndex);
					}else{
						cellData = getCellData(rowIndex, colIndex, expComponents);
					}
					
					
					rowData.put(colName, cellData);
				}
				
				tableData.put(Integer.toString(rowIndex), rowData);
			}
			return tableData;
		}	
		

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private String[] getValueByType(String rowIndex, String columnIndex, String tag, String compIndex)throws Exception
	{
		String[] ret = doc.executeJsFunction("getValueByType", tableColName, tableIndex, rowIndex, columnIndex, tag, compIndex);
		
		return ret;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getCurrentWindowXPath() throws Exception {
		
		String index = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();
		
		String xPath = "/web:window[@index='"+index+"' or @title='"+title+"']";
		
		return xPath;
	}
	
	public String getCurrentWindowTitle() throws Exception {
		
		String title = web.getFocusedWindow().getTitle();
		
		return title;
	}
	
	public void waitForText(String text) throws Exception {
		
		waitForText(text, "span");
		
	}
	
	public void waitForText(String text, String compType) throws Exception {
		
		/* Document Path */
		String windowPath = getCurrentWindowXPath();
		
		int currentWindowIndex = Integer.parseInt(web.window(windowPath).getAttribute("index"));
		
		String docPath = windowPath + "/web:document[@index='"+currentWindowIndex+"']"; 
		
		/* Get Text Path */
		String textPath = docPath + "/web:"+compType+"[@text='"+text+"']";
		
		/* Wait for text to present */
		web.element(textPath).waitFor(SYNCTIME);
	}
	
	public void waitForColumn(String columnName) throws Exception {
		
		/* Document Path */
		String windowPath = getCurrentWindowXPath();
		
		int currentWindowIndex = Integer.parseInt(web.window(windowPath).getAttribute("index"));
		
		String docPath = windowPath + "/web:document[@index='"+currentWindowIndex+"']"; 
		
		/* Get Text Path */
		String textPath = docPath + "/web:th[@text='"+columnName+"']";
		
		/* Wait for text to present */
		web.element(textPath).waitFor(SYNCTIME);
		
		web.element(textPath).waitFor(SYNCTIME);
	}
	
	public boolean tableListBoxExists() throws Exception {
		
		boolean listBoxExists = false;
		
		if(web.exists(tableListPath, 10)){
			listBoxExists =  true;
		}
		
		return listBoxExists;
	}
	
	public int getTableListCount() throws Exception {
		
		int listCount = getListItems(tableListPath).size();
		
		return listCount;
	}
	
	public void selectTableList(int listIndex) throws Exception {
		
		/* Select the select box  one by one*/
		web.selectBox(tableListPath).selectOptionByIndex(listIndex);
		
		//web.selectBox(selectListPath).fireEvent("onchange");	
	}
	
	/**
	 *  Get the Items From the List Box
	 *  
	 * @param xPath		ListBox Xpath
	 * @return 			List of Items
	 * @throws Exception
	 */
	public List<String> getListItems(String xPath) throws Exception{
		
		List<String> actualItems = new ArrayList<String>();
		List<DOMElement> actualAttributes = web.selectBox(xPath).getOptions();
		
		/* Get the Elements one by one from the List Box*/
		for(int index = 0;index < actualAttributes.size();index++){
			actualItems.add(actualAttributes.get(index).getAttribute("text"));
		}
		
		return actualItems ;
	}
	
	public List<String> getHashMapKeySet(HashMap<String,String> hashmap){
		
		/* Get KeySet as Objects */
		Object[] keyObjects  = hashmap.keySet().toArray();
		
		/* Get KeySet as Array */
		String[] keys = null;
		keys = Arrays.asList(keyObjects).toArray(new String[keyObjects.length]);
		
		/* Get KeySet as List */
		List<String> keySet = Arrays.asList(keys);
		
		return keySet;
	}
	
	public List<String> convertStringArrayToList(Object[] objects){
		
		/* Get KeySet as Array */
		String[] keys = null;
		keys = Arrays.asList(objects).toArray(new String[objects.length]);
		
		/* Get KeySet as List */
		List<String> keySet = Arrays.asList(keys);
		
		
		return keySet;
	}
	
	public List<String> getHashMapValues(HashMap<String,String> hashmap){
		
		/* Get KeySet as Objects */
		Object[] valueObjects  = hashmap.values().toArray();
		
		/* Get KeySet as Array */
		String[] values = null;
		values = Arrays.asList(valueObjects).toArray(new String[valueObjects.length]);
		
		/* Get KeySet as List */
		List<String> valueSet = Arrays.asList(values);
		
		return valueSet;
	}
	
	public boolean compareStrings(String expectedString, String actualString){
		
		boolean stringMatched = false;
		Pattern pattern = Pattern.compile(expectedString);
		Matcher matcher = pattern.matcher(actualString.trim());

       if (matcher.matches()) {
       	stringMatched = true;
       } 
       
		return stringMatched;
	}
	
	
	public boolean isANumber(String input){
		
		Pattern p = Pattern.compile( "([0-9]*)" );

		Matcher m = p.matcher(input);
		
		return m.matches();
	}
	

	
	
	
}


