import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.scripting.modules.webdom.api.elements.DOMSelect;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.applet.api.*;
import lib.*;

public class script extends IteratingVUserScript {
	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;
	@ScriptService oracle.oats.scripting.modules.applet.api.AppletService applet;
	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;
	@FunctionLibrary("GENLIB") lib.ebsqafwk.GENLIB gENLIB;
	@FunctionLibrary("WEBTABLELIB") lib.ebsqafwk.WEBTABLELIB wEBTABLELIB;
	public void initialize() throws Exception {
	}

	/**
	 * Add code to be executed each iteration for this virtual user.
	 */
	public void run() throws Exception {
	
	}

	public void finish() throws Exception {
	}
	
//	************************************************CRM specific fucntions*****************************************************************
	
	
	public  boolean verifyDateBasedOnMonday(String logicalName,String time,String dateFormat,String minutesToBeAdded) throws AbstractScriptException  {
        
        String formatCase="";
        String newTime="";
        if(dateFormat.toUpperCase().startsWith("CC")){
        	
              formatCase=dateFormat.substring(0,2);
              dateFormat=dateFormat.substring(2, dateFormat.length());
        }


        try{
              DateFormat formatter = new SimpleDateFormat(dateFormat.trim());
              Date date = formatter.parse(time);
              Calendar cal=Calendar.getInstance();
              cal.setTime(date);
              int day=cal.get(Calendar.DAY_OF_WEEK);
              int diff=2-day;
              if(diff<0){
                    diff=7+diff;
              }
              if(diff==0){
                    cal.add(Calendar.MINUTE, Integer.parseInt(minutesToBeAdded));
              }else{
                    cal.add(Calendar.DATE, diff);
                    cal.set(Calendar.HOUR, 8);
                    cal.set(Calendar.MINUTE,Integer.parseInt(minutesToBeAdded));
              }
              cal.set(Calendar.SECOND, 0);
              if(formatCase.trim().equals("CC"))
                    newTime=formatter.format(cal.getTime()).toUpperCase();
              else if(formatCase.trim().equals("cc"))
                    newTime=formatter.format(cal.getTime()).toLowerCase();
              else
                    newTime=formatter.format(cal.getTime());
                    
        }
        catch(Exception e){
              e.printStackTrace();
        }
        
        String xpath="";
        
        if(logicalName.indexOf("=")==-1)
        	xpath="//forms:textField[(@name='"+logicalName+"')]";
        else{
        	xpath="//forms:textField[(@name='"+logicalName.split("=")[1].replaceAll("'", "")+"')]";
        }
       
        boolean verify=gENLIB.formVerifyEdit(xpath,newTime);
        
        return verify;
  }

	public boolean refreshWebItem(String buttonName, String tableName, String sourceColName, String sourceColValue, String targetColName, String targetColValue) throws AbstractScriptException, Exception{
		
		info("********************* Start of Refresh WebItem ******************* ");
		
		boolean verified = false;
		String buttonXPath="";
		
		String windowPath = getCurrentWindowXPath();
		String docPath = windowPath + "/web:document[@index='" + 0 + "']";
		String formPath = docPath+"/web:form[@index='" + 0 + "']";
		
		String inputBtnXPath = formPath+"/web:input_button[@value='"+buttonName+"']";
		String btnXPath = formPath+"/web:button[@value='"+buttonName+"']";
		
		if(web.exists(inputBtnXPath, 20)){
			buttonXPath = inputBtnXPath;
		}else{
			buttonXPath = btnXPath;
		}
		
		info("buttonXPath :"+buttonXPath);
		
		HashMap<String, String> searchColumns= new HashMap<String,String>();
		searchColumns.put(sourceColName, sourceColValue);
		
		HashMap<String, String> actions= new HashMap<String,String>();
		actions.put("displayname", targetColName);
		
		wEBTABLELIB.clearWebTableObject();
		List<String> cellData = wEBTABLELIB.getCellData(tableName, searchColumns, actions);
		info("cellData123 :"+cellData.toString());
		
		int iterateCount = 0;
		while(!cellData.contains(targetColValue)){
			iterateCount++;
			
			info("Iteration Count in Refresh WebItem :"+iterateCount);
			web.button(buttonXPath).waitFor(20);
			web.button(buttonXPath).click();
			//delay(15);
			Thread.sleep(15000);
			
			//Get the Cell Value
			wEBTABLELIB.clearWebTableObject();
			cellData = wEBTABLELIB.getCellData(tableName, searchColumns, actions);
			info("cellData115 :"+cellData.toString());
			
			if(iterateCount == 30){
				verified  = false;
				reportFailure("refreshWebItem : Unable to get value"+targetColValue+" for the column "+targetColName+" after "+(30*15)+" sec.");
				break;
			}
		}
		
		wEBTABLELIB.clearWebTableObject();
		
		if(iterateCount < 20){
			verified = true;
		}
		
		info("********************* End of Refresh WebItem ******************* ");
		
		return verified;
	}
	
	
	/**
	 *  Clics on Select button under Specific Address
	 *  
	 * @param address
	 * @throws Exception
	 */
	public void selectAddress(String address) throws Exception
	{
		// Focused Window Index & Title
		String index = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();
		
		// Prepare WindowPath
		String windowPath = "/web:window[@index='"+index+"' or @title='"+title+"']";
		
		// Document Path
		String xPath = windowPath+ "/web:document[@index='0']";
		
		// Get specified table based on table summary attribute
		List<DOMElement> tdElements = web.document(xPath).getElementsByTagName("td");
		
		info("TD Elemet Count :"+tdElements.size());
		
		for(int tdIndex=0; tdIndex < tdElements.size(); tdIndex++){
			
			
			String tdText = tdElements.get(tdIndex).getAttribute("innertext");
			
			if(tdText != null){
				
				String[] text = tdText.split("\n");
				
				String actAddress = "";
				for(int splitIndex=0; splitIndex < text.length; splitIndex++){
					
					actAddress = actAddress + " " + text[splitIndex].trim();
				}
				
				if(actAddress.trim().equalsIgnoreCase(address)){
					
					info("Actual Address:"+actAddress.trim());
					
					DOMElement trElement = tdElements.get(tdIndex).getParent();
					
					List<DOMElement> inputElements = trElement.getElementsByTagName("input");
					
					//info("input Count :"+inputElements.size());
					
					for(int inputIndex=0; inputIndex < inputElements.size(); inputIndex++){
						
						String valAttribute = inputElements.get(inputIndex).getAttribute("value");
						String typeAttribute = inputElements.get(inputIndex).getAttribute("type");
						
						if(valAttribute != null && typeAttribute != null){
							if(valAttribute.equalsIgnoreCase("select") && typeAttribute.equalsIgnoreCase("button")){
								//info("input value :"+inputElements.get(inputIndex).getAttribute("value"));
								inputElements.get(inputIndex).click();
								break;
							}
						}
					}
					
					break;
				}
			}
			
		}
		
		
	}
	
	
	
	/**
	 *  Clicks on Checkout butoon
	 *  
	 * @param cartType
	 * 			- Type of Cart i.e. "Saved Carts","Carts Shared With You",  "Carts Shared By You"
	 * @throws Exception
	 */
	public void cartCheckout(String cartType) throws Exception{
		
		actionOnCart(cartType, "input", null);
	}
	
	/**
	 * 	Select the list box and clicks on Go button
	 * 
	 * @param cartType
	 * 			- Type of Cart i.e. "Saved Carts","Carts Shared With You",  "Carts Shared By You"
	 * @param itemToSelect
	 * 			- Item to Select in the listbox
	 * @throws Exception
	 */
	public void selectCartAction(String cartType, String itemToSelect) throws Exception
	{
		
		actionOnCart(cartType,"select",itemToSelect);
	}
	
	public void actionOnCart(String cartName, String compType, String itemToSelect) throws Exception {
		
		// Focused Window Index & Title
		String index = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();
		
		// Prepare WindowPath
		String windowPath = "/web:window[@index='"+index+"' or @title='"+title+"']";
		
		// Document Path
		String xPath = windowPath+ "/web:document[@index='0']";
		
		// Property List
		PropertyTestList propertyTestList = new PropertyTestList();
		propertyTestList.add("summary",cartName);
		
		// Get specified table based on table summary attribute
		List<DOMElement> tableElements = web.document(xPath).getElementsByTagName("table",propertyTestList);
		
		//System.out.println("Table Elements: "+tableElements.size());
		
		// if the user wants to click on "Checkout" option
		if(compType.equalsIgnoreCase("input")){
			
			List<DOMElement> intputElements = tableElements.get(0).getElementsByTagName(compType);
			
			for(int eleIndex=0; eleIndex < intputElements.size(); eleIndex++){
				
				String elementValue = intputElements.get(eleIndex).getAttribute("value");
				
				if(elementValue.equalsIgnoreCase("checkout")){
					
					intputElements.get(eleIndex).click();
					
					System.out.println("alt :"+intputElements.get(eleIndex).getAttribute("alt"));
					break;
				}
			}
			
		}else if(compType.equalsIgnoreCase("select")){ // if the user wants to select listbox
			
			// Get all the input components in the specified table
			List<DOMElement> intputElements = tableElements.get(0).getElementsByTagName("input");
			
			// Get all the select components in the specified table
			List<DOMElement> selectElements = tableElements.get(0).getElementsByTagName("select");
			//info("List Boxes :"+selectElements.size());
			
			// Convert DOMelement to SelectBox
			DOMSelect selectBox = ((DOMSelect)selectElements.get(0));
			
			// Select ListBox
			selectBox.selectOptionByText(itemToSelect);
			
			// Click on Go  Button
			for(int eleIndex=0; eleIndex < intputElements.size(); eleIndex++){
				
				String elementValue = intputElements.get(eleIndex).getAttribute("value");
				
				if(elementValue.equalsIgnoreCase("go")){
					
					intputElements.get(eleIndex).click();
					break;
				}
			}
		}
		
	}
	
	
	
	//@SuppressWarnings("unchecked")
	/*public void clickLOVBasedOnLabel(String labelName) throws Exception{
		
		String LOV_Xpath=null;
		String winPath = "/web:window[@index='0']/web:document[@index='0']";
		String labelXpath = winPath+"/web:span[@text='"+labelName+"']";
		String tdxPath = winPath+"/web:td[@text='"+labelName+"']";
		String tdIndex = web.element(tdxPath).getAttribute("index");
		int finalTdIndex = Integer.parseInt(tdIndex)+2;
		String finalTdXPath = winPath+"/web:td[@index='"+finalTdIndex+"']";
		
		List<DOMElement> elements =  web.element(finalTdXPath).getElementsByTagName("img");

		for(int i=0;i<elements.size();i++)
			{
				DOMElement element = elements.get(i);
				
				LOV_Xpath = winPath + "/web:img[@index='"+element.getAttribute("index")+"']";
				String LOVName = web.image(LOV_Xpath).getAttribute("alt");
				if(web.image(LOV_Xpath).exists() && LOVName!=null)
				{
					if(LOVName.contains("Search for"))
					web.image(LOV_Xpath).click();
					break;
				}
			}
		}*/
	
	
	@SuppressWarnings("unchecked")
	public void clickLOVBasedOnLabel(String labelName) throws Exception{
		
		int currentWindowIndex = Integer.parseInt(web.getFocusedWindow().getAttribute("index"));
		int documentIndex = 0;
		int formIndex = 0;
		int labelIndex = 0;
		String expLabelName = "";
		String labelPathDetails[] = labelName.split(",");

		// Get the Button Details passed from User
		for (int index = 0; index < labelPathDetails.length; index++) {

			if (labelPathDetails[index].trim().startsWith("document=")) {

				documentIndex = Integer.parseInt(labelPathDetails[index].split("=")[1]);

			} else if (!labelPathDetails[index].contains("document=") && !labelPathDetails[index].contains("form=")) {

				String[] labelDetails = labelPathDetails[index].split(";");

				// Similar Lable Index
				if (labelDetails.length > 1) {
					labelIndex = Integer.parseInt(labelDetails[1]);
				}

				// Label Name
				expLabelName = labelDetails[0];

			} else if (labelPathDetails[index].trim().startsWith("form=")) {

				formIndex = Integer.parseInt(labelPathDetails[index].split("=")[1]);
			}
		}

		info("****** Label Details ********************* ");
		info("Label Name: \"" + expLabelName + "\"  Label Index: \"" + labelIndex + "\"  Document Index: \""
				+ documentIndex + "\"  Form Index: \"" + formIndex + "\"");

		// Document Path
		String path = "/web:window[@index ='"+currentWindowIndex+"']/web:document[@index='" + documentIndex + "']";
		info("Document Path :" + path);
		
		PropertyTestList searchLabel = new PropertyTestList();
		searchLabel.add("innerText", expLabelName);
		
		List<DOMElement>tdList = 	web.document(path).getElementsByTagName("td", searchLabel);
		info("Td List :"+tdList.size());
		
		DOMElement tdCell = tdList.get(labelIndex);
		tdCell.focus();
		
		
		DOMElement tr = getParentRow(tdCell);
		
		if(tr!=null){
			tr.focus();
		
		}else{
			throw new NullPointerException("No row contains the specified label");
		}
		
		List<DOMElement>imgsList = tr.getElementsByTagName("img");
		//info("image list :"+imgsList.size());
		//info("textFieldIndex :"+textFieldIndex);
		List<DOMElement> requiredImgs = new ArrayList<DOMElement>();
		
		//Old Code
		/*if(imgsList.size()>0){
			
			requiredImgs.get(textFieldIndex).click();
			info("Clicked on Image");
		}else{
				throw new Exception("No Images are present in row containing specified ImgName");
		}*/
		
		for(int index=0; index < imgsList.size(); index++){
			
			String alt = imgsList.get(index).getAttribute("alt");
			
			if(alt != null && alt != ""){
				imgsList.get(index).click();
				break;
			}
		}
	}
	
	
	@SuppressWarnings("unchecked") 
	public int searchEditableRow() throws Exception {
		
		String docpath ="/web:window[@index='0']/web:document[@index='0']";
		List<DOMElement> bodyList = web.document(docpath).getElementsByTagName("body");
		DOMElement body = bodyList.get(0);
		List<DOMElement> imgList  = body.getElementsByTagName("th");
		List<DOMElement> formList = null;
		DOMElement form = null;
		DOMElement  td = null;
		DOMElement  tr = null;
		String formName = "";
		for(DOMElement  img :imgList){
			
			String text = img.getAttribute("text");
			if((text!=null)&&(text.equalsIgnoreCase("Remove")))
				{
					tr = img.getParent();
					if(tr!=null)
						{
							do{
							tr = tr.getNextSibling();
							if(tr!=null) 
								{
									formList = tr.getElementsByTagName("input");
									if(formList !=null)
										{
										form = formList.get(0);		
										if(form !=null)
											{
											formName = form.getAttribute("name");
											}
										
										}
								
								}
							}while(!formName.contains("isEmpty"));
							break;
						}
				}
			
		}//end of main for loop

		formName = formName.replaceAll("OZFCodeMapUpdTbl.", "");
		formName = formName.replaceAll(".isEmpty", "");
		formName = formName.replaceAll("ROW", "");
		int intRowNum =  Integer.parseInt(formName);
		return intRowNum;
		
		}
	
@SuppressWarnings("unchecked") public void addToCartItemDetails(String Itemlabel,String Quantity) throws Exception {
		
		String docpath ="/web:window[@index='0']/web:document[@index='0']";
		List<DOMElement> bodyList = web.document(docpath).getElementsByTagName("body");
		DOMElement body = bodyList.get(0);
		List<DOMElement> spanList  = body.getElementsByTagName("span");
		List<DOMElement> formList = null;
		DOMElement form = null;
		DOMElement  td = null;
		DOMElement  tr = null;
		String formName = "";
		for(DOMElement  span :spanList){
			
			String text = span.getAttribute("text");
			if((text!=null)&&(text.equals(Itemlabel)))
				{
					td = span.getParent();
					if(td!=null)
						{
							tr = td.getParent();
							if(tr!=null) 
								{
									formList = tr.getElementsByTagName("form");
									if(formList !=null)
										{
										form = formList.get(0);		
										if(form !=null)
											{
											formName = form.getAttribute("name");
											if(formName!=null)
												break;
											}
										
										}
						}
				}
							
			}
			
		}
		String imgXpath = "/web:window[@index='0' or @title='*']/web:document[@index='0']/web:img[@alt='Item Details' or @id='"+formName+"*']";
		web.image(imgXpath).click();
		Thread.sleep(2000);
		web.textBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@name='popupAddToCart']/web:input_text[@id='qty' or @name='qty']").setText(Quantity);
		web.button("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@name='popupAddToCart']/web:input_button[@name='addtocartElement' or @value='Add to Cart']").click();
		Thread.sleep(5000);
		
	}

	public void expandCollapseBasedOnLabel(String labelNameWithIndex) throws Exception{
		
		String[] compTypes = {"span","h2","div","h3"};
		
		String winnIndex = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();

		int formIndex = 0;
		int documentIndex = 0;
		int defaultLabelIndex = 0; // default value "0" i.e first radio label in
									// the page
		int expLabelIndex = 0;
		String expLabelName = "";

		boolean bFound = false;
		String winPath = "/web:window[@index='" + winnIndex + "' or @title='" + title + "']";
		String xPath = null;

		String[] labelPathDetails = labelNameWithIndex.split(",");

		// Get the label Details passed from User
		for (int index = 0; index < labelPathDetails.length; index++) {

			if (labelPathDetails[index].trim().startsWith("document=")) {

				documentIndex = Integer.parseInt(labelPathDetails[index].split("=")[1]);

			} else if (!labelPathDetails[index].contains("document=") && !labelPathDetails[index].contains("form=")) {

				String[] labelDetails = labelPathDetails[index].split(";");

				// Handling Similar label Index
				if (labelDetails.length > 1) {
					expLabelIndex = Integer.parseInt(labelDetails[1]);
					info("expLabelIndex=" + expLabelIndex);
				}

				// text label
				expLabelName = labelDetails[0];

			} else if (labelPathDetails[index].trim().startsWith("form=")) {

				formIndex = Integer.parseInt(labelPathDetails[index].split("=")[1]);

			}
		}

		info("****** label Details ********************* ");
		info("Label Name: \"" + expLabelName + "\"  Label Index: \"" + expLabelIndex
				+ "\"  Document Index: \"" + documentIndex + "\"  Form Index: \"" + formIndex + "\"");

		
		xPath = winPath + "/web:document[@index='" + documentIndex + "']";
		
		/*if(formIndex == -1){
			
		}else{
			xPath = winPath + "/web:document[@index='" + documentIndex + "']/web:form[@index='" + formIndex + "']";
		}*/
		
		
		
		for(int index=0; index < compTypes.length; index++){
			
			if(web.exists(xPath + "/web:"+compTypes[index]+"[@text='"+expLabelName+"']")){
				
				String eleXPath = xPath + "/web:"+compTypes[index]+"[@text='"+expLabelName+"']";
				info("Label Path :"+eleXPath);
				
				DOMElement element = web.element(eleXPath);
				DOMElement actImgElement = null;
				
				for(;;){
					
					// Get the parent
					DOMElement parElement = element.getParent();
					
					// Finding the image within the parent
					if(parElement.getTag().equalsIgnoreCase("img")){
						actImgElement = parElement;
						break;
					}else{
						List<DOMElement> elements =parElement.getElementsByTagName("img");
						
						if(elements.size() > 0){
							actImgElement = elements.get(0);
							break;
						}
					}
					
					
					// if parent doesn't have any image, then go the previous sibling of the parent
					DOMElement parSibElement = parElement.getPreviousSibling();
					
					if(parSibElement != null){
						
						if(parSibElement.getTag().equalsIgnoreCase("img")){
							actImgElement = parSibElement;
							break;
						}else{
							List<DOMElement> elements =parSibElement.getElementsByTagName("img");
							
							if(elements.size() > 0){
								actImgElement = elements.get(0);
								break;
							}else{
								element = parSibElement;
							}
						}
					}else{
						element = parElement.getParent();
					}
				}
				
				if(actImgElement != null){
					
					info("Click on Expand Button");
					actImgElement.click();
					web.window(winPath).waitForPage(null);					
					
					//Select to show information
					
					/*
					 * Code added to first check if the image is already opened, if so do not click
					 */
					/*
					String altImg = actImgElement.getAttribute("alt");
					info("AImage Alt:::>"+altImg+"<::");
					if(altImg!=null && altImg.equals("Select to show information"))
					{
						actImgElement.click();
						web.window(winPath).waitForPage(null);
					}	
					else
					{
						System.out.println("Need not click on expand, as it is hidden already.");
					}
					*/
					//actImgElement.fireEvent("onclick");
					
				}
				
			}
		}
		
		/*String ExpandCollapseObject_Xpath=null; 
		String labelXpath = "/web:window[@index='0']/web:document[@index='0']/web:span[@text='"+labelNameWithIndex+"']";
		
		List<DOMElement> elements = gENLIB.getCompsBasedOnLabel2(labelNameWithIndex,"img");
		
		DOMElement element = elements.get(0);
		ExpandCollapseObject_Xpath = "/web:window[@index='0']/web:document[@index='0']/web:img[@index='"+element.getAttribute("index")+"']";
		
		web.image(ExpandCollapseObject_Xpath).click();*/
		
		
	}
	public void expandBasedOnLabel(String labelNameWithIndex) throws Exception{
		
		String[] compTypes = {"span","h2","div","h3"};
		
		String winnIndex = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();

		int formIndex = 0;
		int documentIndex = 0;
		int defaultLabelIndex = 0; // default value "0" i.e first radio label in
									// the page
		int expLabelIndex = 0;
		String expLabelName = "";

		boolean bFound = false;
		String winPath = "/web:window[@index='" + winnIndex + "' or @title='" + title + "']";
		String xPath = null;

		String[] labelPathDetails = labelNameWithIndex.split(",");

		// Get the label Details passed from User
		for (int index = 0; index < labelPathDetails.length; index++) {

			if (labelPathDetails[index].trim().startsWith("document=")) {

				documentIndex = Integer.parseInt(labelPathDetails[index].split("=")[1]);

			} else if (!labelPathDetails[index].contains("document=") && !labelPathDetails[index].contains("form=")) {

				String[] labelDetails = labelPathDetails[index].split(";");

				// Handling Similar label Index
				if (labelDetails.length > 1) {
					expLabelIndex = Integer.parseInt(labelDetails[1]);
					info("expLabelIndex=" + expLabelIndex);
				}

				// text label
				expLabelName = labelDetails[0];

			} else if (labelPathDetails[index].trim().startsWith("form=")) {

				formIndex = Integer.parseInt(labelPathDetails[index].split("=")[1]);

			}
		}

		info("****** label Details ********************* ");
		info("Label Name: \"" + expLabelName + "\"  Label Index: \"" + expLabelIndex
				+ "\"  Document Index: \"" + documentIndex + "\"  Form Index: \"" + formIndex + "\"");

		
		xPath = winPath + "/web:document[@index='" + documentIndex + "']";
		
		/*if(formIndex == -1){
			
		}else{
			xPath = winPath + "/web:document[@index='" + documentIndex + "']/web:form[@index='" + formIndex + "']";
		}*/
		
		
		
		for(int index=0; index < compTypes.length; index++){
			
			if(web.exists(xPath + "/web:"+compTypes[index]+"[@text='"+expLabelName+"']")){
				
				String eleXPath = xPath + "/web:"+compTypes[index]+"[@text='"+expLabelName+"']";
				info("Label Path :"+eleXPath);
				
				DOMElement element = web.element(eleXPath);
				DOMElement actImgElement = null;
				
				for(;;){
					
					// Get the parent
					DOMElement parElement = element.getParent();
					
					// Finding the image within the parent
					if(parElement.getTag().equalsIgnoreCase("img")){
						actImgElement = parElement;
						break;
					}else{
						List<DOMElement> elements =parElement.getElementsByTagName("img");
						
						if(elements.size() > 0){
							actImgElement = elements.get(0);
							break;
						}
					}
					
					
					// if parent doesn't have any image, then go the previous sibling of the parent
					DOMElement parSibElement = parElement.getPreviousSibling();
					
					if(parSibElement != null){
						
						if(parSibElement.getTag().equalsIgnoreCase("img")){
							actImgElement = parSibElement;
							break;
						}else{
							List<DOMElement> elements =parSibElement.getElementsByTagName("img");
							
							if(elements.size() > 0){
								actImgElement = elements.get(0);
								break;
							}else{
								element = parSibElement;
							}
						}
					}else{
						element = parElement.getParent();
					}
				}
				
				if(actImgElement != null){
					
					info("Click on Expand Button");
									
					
					//Select to show information
					
					/*
					 * Code added to first check if the image is already opened, if so do not click
					 */
					
					String altImg = actImgElement.getAttribute("alt");
					info("AImage Alt:::>"+altImg+"<::");
					if(altImg!=null && altImg.equals("Select to show information"))
					{
						actImgElement.click();
						web.window(winPath).waitForPage(null);
					}	
					else
					{
						System.out.println("Need not click on expand, as it is hidden already.");
					}
					
					//actImgElement.fireEvent("onclick");
					
				}
				
			}
		}
		
		/*String ExpandCollapseObject_Xpath=null; 
		String labelXpath = "/web:window[@index='0']/web:document[@index='0']/web:span[@text='"+labelNameWithIndex+"']";
		
		List<DOMElement> elements = gENLIB.getCompsBasedOnLabel2(labelNameWithIndex,"img");
		
		DOMElement element = elements.get(0);
		ExpandCollapseObject_Xpath = "/web:window[@index='0']/web:document[@index='0']/web:img[@index='"+element.getAttribute("index")+"']";
		
		web.image(ExpandCollapseObject_Xpath).click();*/
		
		
	}	
	
	//Precondition : Search Criteria must be specified as follows
	// Ex: "Name;is;test resource,Category;;Employee"
	public void setSearchParams(String searchCriteria) throws Exception{
		info("Entered set Search Params");
		String strSearchCriteria = searchCriteria;
		info("Value Passed:"+ searchCriteria);
		String[] arrSearchCriteria = strSearchCriteria.split(",");
		String strSearchParamValue = "";
		String[] arrSearchParamValue;
		String strSearchParam,strSearchOperator,strSearchValue,strOptionText;
		String lblCondID	=	"";
		String lblValueID	=	"";
		String indexForWindow	=	"1";
		
		
		//Added new variables for checking if parameters already exists
		List<DOMElement> spanList = new ArrayList<DOMElement>();
		List<DOMElement> labelList = new ArrayList<DOMElement>();
		List<DOMElement> selectList = new ArrayList<DOMElement>();
		List<DOMElement> condTRList = new ArrayList<DOMElement>();
		List<DOMElement> valueTRList = new ArrayList<DOMElement>();
		List<DOMElement> textList = new ArrayList<DOMElement>();
		
		String	docPath	=	"/web:window[@index='0' or @title='*']/web:document[@index='0']";
		System.out.println("docPath:"+docPath);
		docPath	=	getCurrentWindowXPath()+"/web:document[@index='0']";
		System.out.println("docPath:"+docPath);
		String tempWinPath	=	null;
		if(docPath.contains("Oracle Applications Home Page"))
		{
			System.out.println("Enttered for function getlatestwindowxpath");
			tempWinPath	=	getLatestWindowXPath();//indexForWindow
			docPath	=		tempWinPath + "/web:document[@index='0']";						
		}
		else																			
		{
			// tempWinPath	=	docPath.split("]")[0];
		}
		
		/*
		tempWinPath	=	tempWinPath.substring(tempWinPath.indexOf("@index='")+"@index='".length());
		System.out.println(tempWinPath);
		tempWinPath	=	tempWinPath.substring(0,tempWinPath.indexOf("'")+"'".length());
		System.out.println("index:"+tempWinPath);
		indexForWindow	=	tempWinPath;
		*/
		
		System.out.println("setSearhParams docpath:\n" + docPath);
		info("docpath printed");
		String customColumn	=	"";
		//;
		for (int i=0;i<arrSearchCriteria.length;i++)
		{
			strSearchParamValue = arrSearchCriteria[i];
			arrSearchParamValue = strSearchParamValue.split(";");
			strSearchParam = arrSearchParamValue[0];
			strSearchOperator = arrSearchParamValue[1];
			strSearchValue = arrSearchParamValue[2];
			if(arrSearchParamValue.length>3)
			{
				customColumn	=	arrSearchParamValue[3];
			}
			// check if the search parameters already exist.
			PropertyTestList propertyTestList = new PropertyTestList();
			propertyTestList.add("text",strSearchParam);
			//propertyTestList.add("class","xc"); // unable to find when this attribute is added.
			
			//info("document exists:"+ web.document(docPath));
			spanList = web.document(docPath).getElementsByTagName("span",propertyTestList);
			boolean flag	=	false;
			boolean foundInSpans	=	false;
			boolean foundInLabels	=	false;
			String filterLBId	=	"";
			int foundInd	=	0;
			for(int ind=0;ind<spanList.size();ind++)
			{
				System.out.println("retreived spans");
				String innerHTML	=	spanList.get(ind).getParent().getAttribute("innerHTML");
			//	info(innerHTML);
				if(innerHTML.contains("class=\"xc\"") || innerHTML.contains("class=xc") )
				{
					flag	=	true; 
					foundInd	=	ind;
					foundInSpans	=	true;
					System.out.println("Found in Spans");
					filterLBId	= spanList.get(ind).getParent().getParent().getChildren().get(2).getChildren().get(0).getAttribute("id");
					break;
				}
				
			}
			if(!flag)
			{
				labelList	=	 web.document(docPath).getElementsByTagName("label",propertyTestList);
				for(int ind=0;ind<labelList.size();ind++)
				{
					System.out.println("retreived labels");
					String innerHTML	=	labelList.get(ind).getParent().getAttribute("innerHTML");
				//	info(innerHTML);
					if(innerHTML.contains("class=\"xc\"") || innerHTML.contains("class=xc") )
					{
						flag	=	true; 
						foundInd	=	ind;
						foundInLabels	=	true;
						System.out.println("Found in Labels");
						filterLBId	= labelList.get(ind).getAttribute("id");
						System.out.println("For attribute value:"+labelList.get(ind).getAttribute("for"));
						String forAttribb	=	labelList.get(ind).getAttribute("for");
						if(forAttribb!=null)
							forAttribb	=	forAttribb.replace("Value_","");
						if(forAttribb==null)
						{
							//System.out.println(labelList.get(ind).getAttribute("innerHTML"));
							
							filterLBId	=	filterLBId.replace("item","");
							lblCondID	=	"Condition_"+(Integer.parseInt(filterLBId)-1)+"__xc_";
							//lblValueID	=	"Value_"+(Integer.parseInt(filterLBId)-1)+"__xc_";
							lblValueID	=	"Value_"+(Integer.parseInt(filterLBId)-1);
							
							labelList	=	 web.document(docPath).getElementsByTagName("label",propertyTestList);
							filterLBId	=	 "Condition_"+(Integer.parseInt(filterLBId)-1);
							// tr - id  - Condition_0__xc_
							System.out.println("Select id Attribute in labels:" + filterLBId);
						}
						else
						{
							lblCondID	=	"Condition_"+Integer.parseInt(forAttribb);
							lblValueID	=	"Value_"+Integer.parseInt(forAttribb);
							filterLBId	=	 "Condition_"+Integer.parseInt(forAttribb);
							System.out.println("Else:Select id Attribute in labels:" + filterLBId);
						}
						break;
					}
					
				}				
			}
			if(!flag)
			{
				PropertyTestList addAnotherSpan = new PropertyTestList();
				addAnotherSpan.add("text","Add Another");
				spanList = web.document(docPath).getElementsByTagName("span",addAnotherSpan);
				
				String addAnotherID	=	"";
				for(int ind=0;ind<spanList.size();ind++)
				{
					String innerHTML	=	spanList.get(ind).getParent().getAttribute("innerHTML");
				//	info(innerHTML);
					if(innerHTML.contains("class=\"xc\"") || innerHTML.contains("class=xc") )
					{
						foundInd	=	ind;
						addAnotherID	= spanList.get(ind).getParent().getParent().getChildren().get(2).getChildren().get(0).getAttribute("id");
						break;
					}
					
				}				
				
				
				
				web.selectBox(docPath+"/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='"+addAnotherID+"') and multiple mod 'False']").selectOptionByText(strSearchParam);
				web.button(docPath+"/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@value='Add']").click();
				Thread.sleep(2000);
				spanList = web.document(docPath).getElementsByTagName("span",propertyTestList);
				flag	=	false;
				filterLBId	=	"";
				for(int ind=0;ind<spanList.size();ind++)
				{
					String innerHTML	=	spanList.get(ind).getParent().getAttribute("innerHTML");
					info(innerHTML);
					if(innerHTML.contains("class=\"xc\"") || innerHTML.contains("class=xc") )
					{
						flag	=	true; 
						foundInd	=	ind;
						foundInSpans	=	true;
						filterLBId	= spanList.get(ind).getParent().getParent().getChildren().get(2).getChildren().get(0).getAttribute("id");
						break;
					}
					
				}	
				if(!flag)
				{
					labelList	=	 web.document(docPath).getElementsByTagName("label",propertyTestList);
					for(int ind=0;ind<labelList.size();ind++)
					{
						System.out.println("retreived labels");
						String innerHTML	=	labelList.get(ind).getParent().getAttribute("innerHTML");
						
					//	info(innerHTML);
						if(innerHTML.contains("class=\"xc\"") || innerHTML.contains("class=xc") )
						{
							flag	=	true; 
							foundInd	=	ind;
							foundInLabels	=	true;
							System.out.println("Found in Labels");
							filterLBId	= labelList.get(ind).getAttribute("id");
							String forAttr		=	labelList.get(ind).getAttribute("for");
							System.out.println("For Attribute:" + forAttr);							
							filterLBId	=	forAttr.replace("Value_","");
							lblCondID	=	"Condition_"+(Integer.parseInt(filterLBId)-1)+"__xc_";
							//lblValueID	=	"Value_"+(Integer.parseInt(filterLBId)-1)+"__xc_";
							lblValueID	=	forAttr;//"Value_"+(Integer.parseInt(filterLBId)-1);
							
							labelList	=	 web.document(docPath).getElementsByTagName("label",propertyTestList);
							filterLBId	=	 "Condition_"+(Integer.parseInt(filterLBId));
							// tr - id  - Condition_0__xc_
							System.out.println("Select id Attribute in labels:" + filterLBId);
							break;
						}
						
					}						
				}
			}
			
			//		if the search operator is not provided then the default value will be selected 
			if(strSearchOperator.isEmpty()||strSearchOperator.equalsIgnoreCase(""))
			{
				System.out.println("entered default");
				strOptionText = web.selectBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='"+filterLBId+"') and multiple mod 'False']").getAttribute("selectedText");
				web.selectBox(docPath+"/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='"+filterLBId+"') and multiple mod 'False']").selectOptionByText(strOptionText);
			}
			else
			{
				System.out.println("Before Condition Selection");
				web.selectBox(docPath + "/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='"+filterLBId+"') and multiple mod 'False']").selectOptionByText(strSearchOperator);
				
			}
			System.out.println("After Condition Selection");
			if(web.image(docPath+"/web:img[@alt='Search for "+strSearchParam+"']").exists())
			{
				System.out.println("Entered LOV options");
				//gENLIB.selectFromLov(indexForWindow,"Search for "+strSearchParam,strSearchValue);
				if(customColumn!=null && !"".equals(customColumn))
					gENLIB.webSelectLOV("Search for "+strSearchParam, customColumn, strSearchValue, customColumn, strSearchValue);
				else
					gENLIB.webSelectLOV("Search for "+strSearchParam, strSearchParam, strSearchValue, strSearchParam, strSearchValue);
				
			}
			/*
			else if(web.textBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@title='"+strSearchParam+"']").exists())
			{
				web.textBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@title='"+strSearchParam+"']").setText(strSearchValue);
			}
			*/
			else // case for select box and text field
			{
				System.out.println("If not ListOfValues");
				if(foundInSpans)
				{
					selectList	=	spanList.get(foundInd).getParent().getParent().getParent().getParent().getParent().getParent().getChildren().get(1).getElementsByTagName("select");
					info("Selects count:"+ selectList.size());
					if(selectList.size()>0)
					{
						String selAttr	=	selectList.get(0).getAttribute("id");
						//web.selectBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='"+selAttr+"') and multiple mod 'False']").selectOptionByText(strSearchValue);
						web.selectBox(docPath+"/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='"+selAttr+"') and multiple mod 'False']").selectOptionByText(strSearchValue);
					}
					else
					{
							
						textList	=	spanList.get(foundInd).getParent().getParent().getParent().getParent().getParent().getParent().getChildren().get(1).getElementsByTagName("input");
						String textId	=	"";
						for(int j=0;j<textList.size();j++)
						{
							String type	=	textList.get(j).getAttribute("type");
							if(type.equals("text"))
							{
								textId	=	textList.get(j).getAttribute("id");
								break;
							}
							
						}
						if(!"".equals(textId))
						{
							web.textBox(docPath+"/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@id='"+textId+"']").setText(strSearchValue);
						}
						else
							reportFailure("Unable to set Value:"  +strSearchValue) ;
					}					
				}	
				else if(foundInLabels)
				{
					System.out.println("Setting Values based on found in labels");
					//selectList	=	labelList.get(foundInd).getParent().getParent().getParent().getParent().getParent().getParent().getChildren().get(1).getElementsByTagName("select");
					PropertyTestList selProps = new PropertyTestList();
					selProps.add("id",lblValueID);
					selectList	=	 web.document(docPath).getElementsByTagName("select",selProps);
					if(selectList!=null && selectList.size()>0)
					{
						web.selectBox(docPath+"/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='"+lblValueID+"') and multiple mod 'False']").selectOptionByText(strSearchValue);
					}
					else
					{
						web.textBox(docPath+"/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@id='"+lblValueID+"']").setText(strSearchValue);
						
					}
					//return ;
				}
				
			}
			/*
			else if(web.selectBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@title='"+strSearchParam+"') and multiple mod 'False']").exists())
			{
				web.selectBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@title='"+strSearchParam+"') and multiple mod 'False']").selectOptionByText(strSearchValue);
			}
			*/
			
		}
	}
	
	
	public void setSearchParams_old(String searchCriteria) throws Exception{
		String strSearchCriteria = searchCriteria;
		String[] arrSearchCriteria = strSearchCriteria.split(",");
		String strSearchParamValue = "";
		String[] arrSearchParamValue;
		String strSearchParam,strSearchOperator,strSearchValue,strOptionText;
		for (int i=0;i<arrSearchCriteria.length;i++)
		{
			strSearchParamValue = arrSearchCriteria[i];
			arrSearchParamValue = strSearchParamValue.split(";");
			strSearchParam = arrSearchParamValue[0];
			strSearchOperator = arrSearchParamValue[1];
			strSearchValue = arrSearchParamValue[2];
			//		Checking whether the search parameter is displayed on UI,if not adding it to the search criteria
			if (!web.selectBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@title='Search Criteria: "+strSearchParam+"') and multiple mod 'False']").exists())
			{
				web.selectBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@name='addMorePickList') and multiple mod 'False']").selectOptionByText(strSearchParam);
				web.button("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@value='Add']").click();
				Thread.sleep(2000);
			}
			//		if the search operator is not provided then the default value will be selected 
			if(strSearchOperator.isEmpty()||strSearchOperator.equalsIgnoreCase(""))
			{
				strOptionText = web.selectBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@title='Search Criteria: "+strSearchParam+"') and multiple mod 'False']").getAttribute("selectedText");
				web.selectBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@title='Search Criteria: "+strSearchParam+"') and multiple mod 'False']").selectOptionByText(strOptionText);
			}
			else
			{
				web.selectBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@title='Search Criteria: "+strSearchParam+"') and multiple mod 'False']").selectOptionByText(strSearchOperator);
			}
			if(web.image("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:img[@alt='Search for "+strSearchParam+"']").exists())
			{
				gENLIB.selectFromLov("1","Search for "+strSearchParam,strSearchValue);
			}
			else if(web.textBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@title='"+strSearchParam+"']").exists())
			{
				web.textBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@title='"+strSearchParam+"']").setText(strSearchValue);
			}
			else if(web.selectBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@title='"+strSearchParam+"') and multiple mod 'False']").exists())
			{
				web.selectBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@title='"+strSearchParam+"') and multiple mod 'False']").selectOptionByText(strSearchValue);
			}
			
		}
	}
	
	
	
		
	
	
	public void crm_WebSelectLOV(String lovName, String searchByOption, String searchValue, String colName, String rowValue) throws Exception{
		
		String[] colNames = {colName};
		String[] rowValues = {rowValue};
		
		crm_WebSelectLOV(lovName, searchByOption, searchValue, colNames, rowValues);
	}
	
	
	public void crm_WebSelectLOV(String lovName, String searchByOption, String searchValue, String[] colNames, String[] rowValues) throws Exception{
		
		int SYNCTIME = 500;
		int currentWindowIndex = 0;
		String editBoxName ="";
		String editboxPath = "";
		String windowPath = "";
		
		windowPath = gENLIB.getCurrentWindowXPath();
		currentWindowIndex = Integer.parseInt(web.window(windowPath).getAttribute("index"));
		System.out.println("currentWindowIndex :"+currentWindowIndex );
		
		if(lovName != null)
		{
			if(lovName.startsWith("Search for"))
			{
				editBoxName = (lovName.replace("Search for", "")).trim();		
			}
			else
			{	
				editBoxName = lovName;	
			}
			
			editboxPath = windowPath + "/web:document[@index='"+currentWindowIndex+"']" + "/web:input_text[@id='*"+editBoxName+"*' or @name='*"+editBoxName+"*']";
			
			//entering the data into the textbox adjacent to LOV
			web.textBox(editboxPath).setText(searchValue);
			
			//performing selectLOV operation 
			if(lovName.startsWith("Search for"))
			{
				String lovPath = windowPath + "/web:document[@index='"+currentWindowIndex+"']" + "/web:img[@alt='"+lovName+"']";
				
				System.out.println("Clicking on LOV");
				
				/* Wait for LOV */
				web.image(lovPath).waitFor(SYNCTIME);
			
				/* Click on LOV */
				web.image(lovPath).click();
			
				this.think(10);
			}
			else
			{
				clickLOVBasedOnLabel(lovName);
			}
		}
		
		System.out.println("Selecting LOV Value");
		/* */
		int searchWindowIndex = currentWindowIndex+1;
		System.out.println("searchWindowIndex :"+searchWindowIndex );
		String searchWindowPath = "/web:window[@index='"+searchWindowIndex+"']/web:document[@index='"+searchWindowIndex+"']/web:form[@index='0']";
		
		/* Select Search By Field */
		web.selectBox(searchWindowPath+"/web:select[(@id='categoryChoice' or @name='categoryChoice') and multiple mod 'False']").waitFor(SYNCTIME);
		web.selectBox(searchWindowPath+"/web:select[(@id='categoryChoice' or @name='categoryChoice') and multiple mod 'False']").selectOptionByText(searchByOption);
		
		/* Enter Search Text */
		web.textBox(searchWindowPath+"/web:input_text[@name='searchText']").waitFor(SYNCTIME);
		web.textBox(searchWindowPath+"/web:input_text[@name='searchText']").setText(searchValue);
		
		/* Click on Go button */
		web.button(searchWindowPath+"/web:button[@value='Go']").click();
		
		/* Wait for the text "Quick Select"*/
		gENLIB.waitForText("Quick Select","th");
		this.think(10);
		
		/* Select Table */
		WebTable searchTable = new WebTable("Quick Select");
		searchTable.clickImage(colNames, rowValues, "Quick Select");
		
		this.think(10);
		web.window("/web:window[@index='"+currentWindowIndex+"']").waitForPage(SYNCTIME);
		
		if(lovName != null){
			
			 windowPath = gENLIB.getCurrentWindowXPath();
			
			currentWindowIndex = Integer.parseInt(web.window(windowPath).getAttribute("index"));
			System.out.println("currentWindowIndex :"+currentWindowIndex );
			
			String lovPath = windowPath + "/web:document[@index='"+currentWindowIndex+"']" + "/web:img[@alt='"+lovName+"']";
			System.out.println("windowPath :"+windowPath );
			
			/* Wait for OV */
			web.image(lovPath).waitFor(SYNCTIME);
		}
		
	}
	
	@SuppressWarnings("unchecked") 
	public void getRequestStatus(String requestID) throws Exception
	{

		int maxRefreshCount = 50;
		int reqRow = 2;
		String reqID = "";
		String phase = "";
		String Status = "";
		DOMElement  td = null;
		DOMElement  tr = null;
		String WebTableName = "Request ID";
		String pageindex = web.getFocusedWindow().getAttribute("index");
		String windowXpath = "/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']";
	    String tableXpath=windowXpath+"/web:table[@firstTableCell='"+WebTableName+"']";
	    String gobtnXpath = windowXpath+"/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@value='Go']";
	    
//	    click on search button
	    web.button("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='Fndcpsearchquery']").click();
	    web.window(windowXpath).waitForPage(null);
//	    Select specific requests
	    web.radioButton("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_radio[(@name='FndCPSearchTypeGrp') or @index='3']").click();
	    Thread.sleep(2000);
//	    Enter the request id to be searched
	    web.textBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@id='AdvSearchRequestId' or @name='AdvSearchRequestId']").setText(requestID);
//	    clicking on Go button to refresh
		web.button(gobtnXpath).click();
		web.window(windowXpath).waitForPage(null);
		
	    		for (int j=0; j<maxRefreshCount; j++)
	    		{
//					clicking on Go button to refresh
	    			web.button(gobtnXpath).click();
	    			web.window(windowXpath).waitForPage(null);
	    			List<DOMElement> bodyList = web.document(windowXpath).getElementsByTagName("body");
	    			DOMElement body = bodyList.get(0);
	    			List<DOMElement> headerList  = body.getElementsByTagName("th");
	    			DOMElement header = headerList.get(0);
	    			String text = header.getAttribute("text");
	    			if((text!=null)&&(text.equalsIgnoreCase("Request ID")))
					{
						tr = header.getParent().getNextSibling();
						List<DOMElement> spanList  = tr.getElementsByTagName("span");
						
						for(DOMElement  span :spanList)
						{
							Thread.sleep(7000);
							 reqID = span.getAttribute("text");
							 if(reqID.equalsIgnoreCase(requestID))
							 {								
	 							 DOMElement phaseSpan = spanList.get(2);
								 DOMElement statusSpan = spanList.get(3);
								 phase = phaseSpan.getAttribute("text");
								 info(phase);
								// Handling if request is completed...
								 
								 if (phase.equalsIgnoreCase("Completed"))
					    			{
									 	Status = statusSpan.getAttribute("text");
					    				info(Status);
					    				System.out.println("The request Id : "+requestID + " has Phase : "+phase+" and Status : "+Status);
					    				break;
					    			}
							 }					 
						}
						if (phase.equalsIgnoreCase("Completed"))
						{
						break;
						}
					}	    			
	    		}

	}
	
	public void crmWebSelectLOV(String label, String searchByOption, String searchValue, String colName, String rowValue) throws Exception{
		
		String labels[] = label.split(";");
		if(labels.length>1){
			
			crmWebSelectLOV( labels[0],toInt(labels[1]) ,0, searchByOption,  searchValue,  colName,  rowValue);
		}
		else{
			crmWebSelectLOV( label,0 ,0, searchByOption,  searchValue,  colName,  rowValue);
		}
	}
	
	private void crmWebSelectLOV(String label,int labelIndex , int textFieldIndex,String searchByOption, String searchValue, String colName, String rowValue) throws Exception{

		String path ="/web:window[@index ='0']/web:document[@index='0']";
		PropertyTestList searchLabel = new PropertyTestList();
		searchLabel.add("innerText", label);
		
		List<DOMElement>tdList = 	web.document(path).getElementsByTagName("td", searchLabel);
		
		DOMElement tdCell = tdList.get(labelIndex);
		
		tdCell.focus();
		
		DOMElement tr = getParentRow(tdCell);
		
		if(tr!=null){
			
		tr.focus();
		
		}else{
			
			throw new NullPointerException("No row contains the specified label");
		}
		
		List<DOMElement> inputList = tr.getElementsByTagName("input");
		
		List<DOMElement> requiredTextFields = new ArrayList<DOMElement>();
		
		if(inputList.size()>0){
			for(DOMElement input : inputList){
			
				String type = input.getAttribute("type");
			
				if(type.equalsIgnoreCase("text")){
				
					requiredTextFields.add(input);
				} 
			}
		}else{
			
			throw new Exception("No textfields are present in row containing specified label");
			
		}
		
		DOMElement requiredTxtField = requiredTextFields.get(textFieldIndex);
		
		info("Setting text box value besides lov image :"+searchValue);
		web.textBox("//web:input_text[@index='"+requiredTxtField.getIndex()+"']").setText(searchValue);
		
		List<DOMElement>imgsList = tr.getElementsByTagName("img");
		//info("image list :"+imgsList.size());
		//info("textFieldIndex :"+textFieldIndex);
		List<DOMElement> requiredImgs = new ArrayList<DOMElement>();
		
		//Old Code
		/*if(imgsList.size()>0){
			
			requiredImgs.get(textFieldIndex).click();
			info("Clicked on Image");
		}else{
				throw new Exception("No Images are present in row containing specified ImgName");
		}*/
		
		for(int index=0; index < imgsList.size(); index++){
			
			String alt = imgsList.get(index).getAttribute("alt");
			
			if(alt != null && alt != ""){
				imgsList.get(index).click();
				break;
			}
		}
		
		
		//spotnuru
		gENLIB.webSelectLOV(null, searchByOption, searchValue, colName, rowValue);
		
	}
	//Label, SearchByOption(if null passed skip selection of this inside the function ), SearchValue, Extra ColName(If null passed skip searching this),Extra Colum'ns RowValue (If null passed skip searching this)

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
	
	public String getLatestWindowXPath() throws Exception
	{
		info(getCurrentWindowXPath());
		String title = "";
		String xpath	=	"";
		int i=0;
		for(i=0;i<7;i++)
		{	
			//System.out.println("index :" + i);
			if(!web.window("/web:window[@index='" + i + "']").exists()) break;
		}
		if(i!=7)
		{
			if(i!=0)
			{
				i=i-1;
				String tempTitle 	=	web.window("/web:window[@index='" + i + "']").getAttribute("title");
				if(tempTitle != null && "Oracle Applications Home Page".equals(tempTitle))
				{
					i=i-1;
				}
			}
			title = web.window("/web:window[@index='" + i + "']").getAttribute("title");
			xpath	=	"/web:window[ @title='" + title + "']";
			
		}
		return xpath;
	}	
	public String getCurrentWindowXPath() throws Exception {

		String index = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();

		String xPath = "/web:window[@index='" + index + "' or @title='" + title + "']";
		//if(title.contains("Oracle Applications "))

		return xPath;
	}
	
	public void webClickDynamicLink(String linkName) throws Exception {
		
		/* Document Path */
		String docPath = getCurrentWindowXPath() + "/web:document[@index='0']";
		int index = 0;

		String[] linkNameIndex = linkName.split(";");

		if (linkNameIndex.length > 1) {

			linkName = linkNameIndex[0];
			index = toInt(linkNameIndex[1]);

		} else {

			linkName = linkNameIndex[0];
			index = 0;
		}
		
		
		
		// Pattern
		PropertyTestList propertyTestList = new PropertyTestList();
		propertyTestList.add("innertext","*"+linkName+"*",TestOperator.StringWildCard);

		List<DOMElement> linksList = web.document(docPath).getElementsByTagName("a",propertyTestList);

		System.out.println("linksList size :" + linksList.size());

		DOMElement link = linksList.get(index);

		link.click();
	}
	
	
	public void clickSiteLink(String sitename) throws Exception
	{
		PropertyTestList plist	=	new PropertyTestList();
		plist.add("text", sitename+"-American English");
		List<DOMElement> links	=		(List<DOMElement>)web.document("/web:window[@title='Site:Specialty Sites']/web:document[@index='0']").getElementsByTagName("a", plist);
		
		
		if(links!=null && links.size()>0)
		{
			info("Size of links:"+ links.size());
			links.get(0).click();
		}
		else
		{
			info("Link not found");
		}

	}
	
	public void promptAndLaunchJTTUrl() throws Exception
	{
		
		String url	=	javax.swing.JOptionPane.showInputDialog("Give JTT Url");
		if(url!=null && !"".equals(url))
		{
			web.window("/web:window[@index='0']").navigate(url);
			web.window( "/web:window[@index='0']").waitForPage(null,true);
		}
		
	}
	public void clickJTTLink(String link) throws Exception
	{
		if(link==null || "".equals(link))
		{
			reportFailure("JTT link cannot be empty");
			return;
		}
		//web.element("/web:window[@index='0']/web:document[@index='0']/web:span[@text='8227']").click();
		String arr[]	=	link.split(";");	
		PropertyTestList plist	=	new PropertyTestList();
		plist.add("text", arr[0]);
		List<DOMElement> links = web.document("/web:window[@index='0']/web:document[@index='0']").getElementsByTagName("span",plist);
		
		if(links!=null && links.size()>0)
		{
			int index	=	0;
			if(arr.length>1 && !"0".equals(arr[1]))
				index	=	Integer.parseInt(arr[1])-1;
			if(links.size()>=(index+1))
			{
				links.get(index).click();
				info("Clicked JTT Link:" + arr[0]);
			}
			
		}
		else
		{
			//info("JTT Link Not found");
			reportFailure("JTT link:"+arr[0]+" is not found");
		}
			
		
		
	}
	
	public void selectMediaContent(String searchFor, String searchForValue) throws Exception {
		
		String cols = "";
		String colVals = "";
		
		// info("0");
		web.window("/web:window[@title='Search and Select List of Values']").waitForPage(null,true);
		
		
		if (!"".equals(searchFor)) {
			web.selectBox("/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:form[@name='lovForm' or @index='0']/web:select[(@name='search_options') and multiple mod 'False']").selectOptionByText(
				searchFor);
		}
		// //info("2");
		if (!"".equals(searchForValue)) {
			web.textBox("/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:form[@name='lovForm' or @index='0']/web:input_text[@name='search_value']").setText(
				searchForValue);
			
			web.textBox("/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:form[@name='lovForm' or @index='0']/web:input_text[@name='search_value']").pressTab();
			web.window("/web:window[@title='Search and Select List of Values']").waitForPage(
				null,
				true);
			cols = searchFor;
			colVals = searchForValue;
		}
		// info("3");

		web.button("/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:form[@name='lovForm' or @index='0']/web:button[@value='Go' or @index='2']").click();
		web.window(	"/web:window[@title='Search and Select List of Values']").waitForPage(
			null,
			true);

		String colNames[] = cols.split(";");
		String colValues[] = colVals.split(";");
		// info("5");
		gENLIB.selectTableRadioButton("Select,document=1",colNames,colValues,"Select");
		
		if (web.button("/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:form[@name='lovForm' or @index='0']/web:button[@value='Select']").exists())
			web.button("/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:form[@name='lovForm' or @index='0']/web:button[@value='Select']").click();

	}
	
	
	
	
	public String getTableActionsRow(String tableName,List <String[]> searchList) throws Exception
	{
		String tbl=null;
		String docPath	=	"/web:window[@index='0']/web:document[@index='0']";
		//firstTableCell
		String tblArr[]	=	tableName.split(";");
		PropertyTestList plist	=	new PropertyTestList();
		plist.add("firstTableCell",tblArr[0]);
		
		List<DOMElement> tbls = web.document(docPath).getElementsByTagName("table",plist);
		
		
		int index	=	0;
		if(tblArr.length>1 && !"0".equals(tblArr[1]))
		{
			index	=	Integer.parseInt(tblArr[1])-1;
		}
		if(tbls!=null && tbls.size()>=(index+1))
		{
			info("Table Found:" + tableName);
			String tblIndex	=	tbls.get(index).getAttribute("index");
			String jttTblXpath	=	docPath + "/web:table[@firstTableCell='"+tableName+"' or index='"+tblIndex+"']";
			int rowCount	=	web.table(jttTblXpath).getRowCount();
			info("Number of rows:" + web.table(jttTblXpath).getRowCount());
			HashMap <String,Integer> cols	=	new HashMap<String,Integer>();
			int colCnt	=	web.table(jttTblXpath).getColumnCount();
			for(int i=1;i<=colCnt;i++)
			{
				
				if(web.table(jttTblXpath).getCell(1, i)!=null && !"".equals(web.table(jttTblXpath).getCell(1, i)))
				{
					cols.put(web.table(jttTblXpath).getCell(1, i).trim(), i);
					info("Column "+i+":" + web.table(jttTblXpath).getCell(1, i).trim());
				}
			}	
			if(searchList==null)
			{
				reportFailure("Searching cannot be empty");
				return null;
			}
				//Logic to find the actual Row number
				int foundRowIndex	=	-1;
				for(int row=2;row<=rowCount;row++)
				{
					boolean foundRow	=	false;	
					int matchCnt		=	0;
					for(int src=0;src<searchList.size();src++)
					{
						int colNum	=	cols.get(searchList.get(src)[0]);	
						info("Value("+row+","+colNum+"):" + web.table(jttTblXpath).getCell(row, colNum));
						if(searchList.get(src)[1].equals(web.table(jttTblXpath).getCell(row, colNum)))
						{
							matchCnt++;
						}
					}
					if(matchCnt==searchList.size())
					{
						foundRowIndex	=	row;
						info("Found at row:" +row);
						tbl	=	new TableObject(tableName,cols,jttTblXpath,foundRowIndex).toString();
						//TableObject	 tbl2	=	new TableObject(tbl);
						break;
						
					}
					
				}
		}	
						
		return tbl;
	}
	public void actionsOnJTTTable(String tblString,	List <String[]> actionList) throws Exception
	{
		
		String docPath	=	"/web:window[@index='0']/web:document[@index='0']";
		info("Function: actionsOnJTTTable called");
		TableObject	tbl	=	new TableObject(tblString);
		HashMap <String,Integer>	cols	=	tbl.getCols();
		String 	jttTblXpath	=	tbl.getTblXpath();
		int	foundRowIndex	=	tbl.getFoundRow();
		if(actionList!=null)
		{
			info("action list is not null");
			for(int act=0;act<actionList.size();act++)
			{
				info("before");
				String key		= 	actionList.get(act)[0].toUpperCase()	;
				String obj		=	actionList.get(act)[1].toUpperCase();
				String colName	=	actionList.get(act)[2];
				String value	=	actionList.get(act)[3];
				info("start");
				info("(key,obj,col)=>("+key+","+obj+","+colName+")");
				int col			=	cols.get(colName);
				info("column Number:"+col); 
				if(key.equals("CLICK") && obj.equals("LINK"))
				{
					String linkName	=	web.table(jttTblXpath).getCell(foundRowIndex, col);
					
					String linkPath	=	docPath + "/web:span[@text='"+linkName+"']";
					
					web.element(linkPath).click();
				}
				else if(key.equals("CLICK") && obj.equals("IMAGE"))
				{
					DOMElement img	=	web.table(jttTblXpath).getCellElement(foundRowIndex, col);
					img.click();
				
				}
				else if(key.equals("SETTEXT") && obj.equals("EDIT"))
				{
					
					DOMElement edit	=	web.table(jttTblXpath).getElementInCell(foundRowIndex, col, "input", 1);
					String ind	=	edit.getAttribute("index");
				
					String editXpath	=	docPath + "/web:form[@index='0']/web:input_text[@index='"+ind+"']";
					
					web.textBox(editXpath).setText(value);
				}
			}
		}
		else
		{
			info("Action List is null");
		}
		
	}	
	
	public void jttLogin(String username,String password) throws Exception
	{
		String url	=	"";
		//javax.swing.JOptionPane.showInputDialog("Wireless URL:");
		if(getVariables().get("oracle_jtt_url").toString()!="")
		{
			url	=	getVariables().get("oracle_jtt_url").toString();
		}
		else
		{
			url = javax.swing.JOptionPane.showInputDialog("JTT URL:");
		}
		
		if(url!=null)
		{
			browser.launch();
			web.window("/web:window[@index='0']").navigate(url);
			web.window("/web:window[@index='0']").waitForPage(null, true);
			web.textBox("/web:window[@index='0' ]/web:document[@index='0']/web:form[@index='0']/web:input_text[@name='username']").setText(username);
			web.textBox("/web:window[@index='0' ]/web:document[@index='0']/web:form[@index='0']/web:input_text[@name='username']").pressTab();
			
			web.textBox("/web:window[@index='0' ]/web:document[@index='0']/web:form[@index='0']/web:input_password[@name='password']").setText(password);
			
			web.image("/web:window[@index='0' ]/web:document[@index='0']/web:input_image[@name='MyHome']").click();
			web.window("/web:window[@index='0']").waitForPage(null, true);
			
			
		}
		else
		{
			reportFailure("Url not provided");
		}
	}
	
	
	public void clickAddToCart(String itemName)  throws Exception{
		
		DOMElement element = getItemElement(itemName);
		
		// Click on Add to Cart
		productActions(element, "Add to Cart");
	}
	
	public void clickExpressCheckOut(String itemName)  throws Exception {
		
		DOMElement element = getItemElement(itemName);
		
		// Click on Express Checkout
		productActions(element, "Express Checkout");
	}

	public void setCartQuantity(String itemName, String quantity)  throws Exception {
	
		DOMElement element = getItemElement(itemName);
		
		// Click on Set Quantity Image
		productActions(element, "item quantity");
		
		// Set the Quantity
		web.textBox("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@name='popupAddToCart']/web:input_text[@id='qty' or @name='qty']").setText(quantity);
		
		web.button("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:form[@name='popupAddToCart']/web:input_button[@name='addtocartElement' or @value='Add to Cart']").click();
		Thread.sleep(5000);
	}

	public void clickConfigure(String itemName) throws Exception{
	
		DOMElement element = getItemElement(itemName);
		
		// Click on Configure
		productActions(element, "Configure");
	}
	
	
	private DOMElement getItemElement(String itemName) throws Exception{
		
		List<DOMElement> expectedItems = new ArrayList<DOMElement>();
		int itemSeqNum = 1; // Default value is 1
		
		String[] item = itemName.split(";");
		
		String expectecdItem = item[0];
		if(item.length > 1){
			itemSeqNum = Integer.parseInt(item[1]);
		}
		
		
		String winnIndex = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();
		
		String winPath = "/web:window[@index='"+winnIndex+"' or @title='"+title+"']";	
		String docPath = winPath+"/web:document[@index='0']";
		//System.out.println("docPath  : "+docPath);
		
		
		List<DOMElement> elements= web.document(docPath).getElementsByTagName("a");
		System.out.println("Elements Size  : "+elements.size());
		
		for(int index=0; index < elements.size(); index++){
			
			String linkname = elements.get(index).getAttribute("innertext");
			//System.out.println("text name : "+linkname);
			
			if(expectecdItem.equals(linkname)){
				expectedItems.add(elements.get(index));
			}
		}
		
		System.out.println("Expected Item \""+expectecdItem+"\" is available :"+expectedItems.size()+" times.");
		
		
		
		DOMElement element = null;
		if(expectedItems.size() > 0 && expectedItems.size() >= itemSeqNum){
			
			element = expectedItems.get(itemSeqNum-1);
			
			return element;
			
		}else {
			reportFailure("Expected Item \""+expectecdItem+"\" is not available in the list");
			return null;
		}
	}
	
	private DOMElement getItemElement_old_06_06_13(String itemName) throws Exception{
		
		List<DOMElement> expectedItems = new ArrayList<DOMElement>();
		int itemSeqNum = 1; // Default value is 1
		
		String[] item = itemName.split(";");
		
		String expectecdItem = item[0];
		if(item.length > 1){
			itemSeqNum = Integer.parseInt(item[1]);
		}
		
		
		String winnIndex = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();
		
		String winPath = "/web:window[@index='"+winnIndex+"' or @title='"+title+"']";	
		String docPath = winPath+"/web:document[@index='0']";
		//System.out.println("docPath  : "+docPath);
		
		
		List<DOMElement> elements= web.document(docPath).getElementsByTagName("a");
		System.out.println("Elements Size  : "+elements.size());
		
		for(int index=0; index < elements.size(); index++){
			
			String linkname = elements.get(index).getAttribute("innertext");
			//System.out.println("text name : "+linkname);
			
			if(expectecdItem.equals(linkname)){
				expectedItems.add(elements.get(index));
			}
		}
		
		System.out.println("Expected Item \""+expectecdItem+"\" is available :"+expectedItems.size()+" times.");
		
		
		
		DOMElement element = null;
		if(expectedItems.size() > 0 && expectedItems.size() >= itemSeqNum){
			
			element = expectedItems.get(itemSeqNum-1);
			
			return element;
			
		}else {
			reportFailure("Expected Item \""+expectecdItem+"\" is not available in the list");
			return null;
		}
	}
	
	public void selectDisplayTemplate(String label) throws Exception
    {
		/**
	Scenario: 	 
	Login: ibe_admin/welcome
	Responsibility: iStore Administrator
	Navigation: iStore Merchant -- Catalog -- Sections -- Hierarchy

	1.Select Desktops under Vision Computers and Support and click on 'Update' button.
	2.Click on 'Display Template' link.
	3. Select any value in 'Select a Display Template' LOV and click on 'Go' button.

	Based on the value selected in 'Select a Display Template' LOV radio buttons gets changed.
		 */  
	web.window("/web:window[@title='Update Section: Display Template']").waitForPage(null,true);
        //info("1");
		 
	web.radioButton(34,"/web:window[@index='0' or @title='Update Section: Display Template']/web:document[@index='0']/web:form[@id='dispTemplateForm' or @name='dispTemplateForm' or @index='2']/web:input_radio[(@title='"+label+"' ) ]")
	.select();
       
    }
	
	
	/**
	 * Add to Cart button is available in two different ways
	 * 
	 * 1. it will be available in the same "td tag (tr1 > td)" (Parent of Item Element link)
	 * 2. it will be available in td tag of next tr element (tr1 > td, tr2 > td)
	 * 
	 * so modifying this function to handle both the scenarios
	 */
	private void productActions(DOMElement element , String actionName) throws Exception{
		
		String comptype = "";
		String attrbute = "";
		
		if(actionName.equalsIgnoreCase("add to cart") || actionName.equalsIgnoreCase("configure")) {
			comptype = "input";
			attrbute = "value";
			
		}else if (actionName.equalsIgnoreCase("express Checkout")){
			comptype = "input";
			attrbute = "value";
		}else if(actionName.equalsIgnoreCase("item quantity")){
			comptype = "img";
			attrbute = "alt";
		}
		
		List<DOMElement> children = new ArrayList<DOMElement>();
		
		while(true){
			
			if(element.getTag().equalsIgnoreCase("tr")){
				element = element.getNextSibling();
			}
			
			// Get the parent of Item
			element  = element.getParent();
			
			// Get all the input within the parent
			children = element.getElementsByTagName(comptype);
			
			if(children.size() > 0){
				break;
			}
		}
		
		
		
		for(int childIndex=0; childIndex < children.size(); childIndex++){
			
			DOMElement childElement = children.get(childIndex);
			
			//String elementType = childElement.getAttribute("type");	
			
			//"button".equalsIgnoreCase(elementType) && 
			
			String elementValue = childElement.getAttribute(attrbute);
			
			if(actionName.equals("item quantity")){
				
				childElement.click();
				break;
				
			}else if(actionName.equals(elementValue)){
				
				childElement.click();
				break;
			}
		}
	}
	
	
	private void productActions1(DOMElement element , String actionName) throws Exception{
		
		
		// Get the parent of Item
		DOMElement parElement  = element.getParent();
		
		String comptype = "";
		String attrbute = "";
		
		if(actionName.equalsIgnoreCase("add to cart") || actionName.equalsIgnoreCase("configure")) {
			comptype = "input";
			attrbute = "value";
			
		}else if (actionName.equalsIgnoreCase("express Checkout")){
			comptype = "input";
			attrbute = "value";
		}else if(actionName.equalsIgnoreCase("item quantity")){
			comptype = "img";
			attrbute = "alt";
		}
		
		// Get all the input within the parent
		List<DOMElement> children = parElement.getElementsByTagName(comptype);
		
		for(int childIndex=0; childIndex < children.size(); childIndex++){
			
			DOMElement childElement = children.get(childIndex);
			
			//String elementType = childElement.getAttribute("type");	
			
			//"button".equalsIgnoreCase(elementType) && 
			
			String elementValue = childElement.getAttribute(attrbute);
			
			if(actionName.equals("item quantity")){
				
				childElement.click();
				break;
				
			}else if(actionName.equals(elementValue)){
				
				childElement.click();
				break;
			}
		}
	}
	
	
	/**
	 *  Check or Uncheck a checkbox which is identified as Image
	 *  
	 * @param xPath
	 * @param checkValue
	 * @throws Exception
	 */
	public void checkImageCheckBox(String xPath, String checkValueString) throws Exception {
		boolean checkValue	=	false;
		
		if(checkValueString!=null && !"".equals(checkValueString) )
		{
			if(checkValueString.equals("true"))
				checkValue	=	true;
			else if(checkValueString.equals("false"))
				checkValue	=	false;
			else
				return;
			
		}
		
		// Check box true options
		List<String> valuesForSelected = new ArrayList<String>();
		valuesForSelected.add("selected");
		valuesForSelected.add("true");
		
		// Check box false options
		List<String> valuesForNotSelected = new ArrayList<String>();
		valuesForNotSelected.add("not selected");
		valuesForNotSelected.add("false");
		
		//Wait for Component to Load
		web.image(xPath).waitFor(20);
		
		/**
		 * Alt tag for checkbox is 
		 * Checked : Select Business Software: Selected
		 * Unchecked :Select Business Software: Not Selected
		 */
		String selectedOption= web.image(xPath).getAttribute("alt");
		String[] checkboxAltTag = selectedOption.split(":");
		String checkOption = checkboxAltTag[checkboxAltTag.length-1].toLowerCase().trim();
		System.out.println("Checked Value :"+checkOption);
		
		System.out.println("User Passed Value :"+checkValue);
		
		if(checkValue){
			
			if(!valuesForSelected.contains(checkOption)){
				
				System.out.println("Checking the Checkbox");
				
				web.image(xPath).click();
				
				Thread.sleep(2000);
			}
		}else {
			
			if(valuesForSelected.contains(checkOption)){
				
				System.out.println("Un Checking the Checkbox");
				
				web.image(xPath).click();
				
				Thread.sleep(2000);
			}
		}
		
		
	}
	
	
	public void selectImageRadiobutton(String xPath, String selectValueString) throws Exception {
		
		boolean	 selectValue = false;
		
		if(selectValueString!=null && !"".equals(selectValueString) )
		{
			if(selectValueString.equals("true"))
				selectValue	=	true;
			else if(selectValueString.equals("false"))
				selectValue	=	false;
			else
				return;
			
		}
		// Check box true options
		List<String> valuesForSelected = new ArrayList<String>();
		valuesForSelected.add("selected");
		valuesForSelected.add("true");
		
		// Check box false options
		List<String> valuesForNotSelected = new ArrayList<String>();
		valuesForNotSelected.add("not selected");
		valuesForNotSelected.add("false");
		
		//Wait for Component to Load
		web.image(xPath).waitFor(20);
		
		/**
		 * Alt tag for Radio button is 
		 * Checked : Select Business Software: Selected
		 * Unchecked :Select Business Software: Not Selected
		 */
		String selectedOption= web.image(xPath).getAttribute("alt");
		String[] checkboxAltTag = selectedOption.split(":");
		String checkOption = checkboxAltTag[checkboxAltTag.length-1].toLowerCase().trim();
		System.out.println("Selected Value :"+checkOption);
		
		System.out.println("User Passed Value :"+selectValue);
		
		if(selectValue){
			
			if(!valuesForSelected.contains(checkOption)){
				
				System.out.println("Selecting Radio button");
				
				web.image(xPath).click();
				
				Thread.sleep(2000);
			}
		}else {
			
			if(valuesForSelected.contains(checkOption)){
				
				System.out.println("De Selecting Radio button");
				
				web.image(xPath).click();
				
				Thread.sleep(2000);
			}
		}
		
	}
	
	/**
	 *  Click an Image in Inner Navigation Table (here Navigation is available in the table row)
	 * @param tableName
	 * 				Table Name
	 * @param navigationColumn
	 * 				Column in which navigation is available
	 * @param searchColumn
	 * 				Search Column
	 * @param searchValue
	 * 				Search Value
	 * @param targetColumn
	 * 				Target Column
	 * 
	 * Ex: clickImageInInnerNavigationTable("Focus", "Component","Component","TRANSACTION_AMOUNT","");
	 * @throws Exception 
	 */
	public void clickImageInInnerNavigationTable(String tableName, String navigationColumn, String searchColumn, String searchValue, String targetColumn) throws Exception{
		
		int rowNum = getRowNumberFromInnerNavigationTable(tableName, navigationColumn,searchColumn, searchValue);
		
		System.out.println("Row Number is :"+rowNum);
		
		HashMap<String,String> actions = new HashMap<String,String>();
		actions.put("displayname", targetColumn);
		
		wEBTABLELIB.clickImage(tableName, rowNum, actions);
		
		
		
	}
	
	public int getRowNumberFromInnerNavigationTable (String tableName, String navigationColumn,  String searchColumn, String searchValue) throws Exception{
		
		
		/* Initialize Variables*/
		int rowNum = -1;
		boolean rowExists = false;
		
		int previousRowNumber = 0;
		
		System.out.println("***************************Navigating to First Row ******************");
		
		// Go begining of the page
		for(int index=0;;index++){
			
			HashMap<String,String> searchColumns = new HashMap<String,String>();
			searchColumns.put(navigationColumn, "Previous");
			
			previousRowNumber = wEBTABLELIB.findRowNumber(tableName,  searchColumns);
			
			if(previousRowNumber > 0){
				break;
			}else{
				
				HashMap<String,String> previousSearchColumns = new HashMap<String,String>();
				previousSearchColumns.put(navigationColumn, "Previous .*");
				
				HashMap<String,String> actions = new HashMap<String,String>();
				actions.put("displayname", navigationColumn);
				wEBTABLELIB.clickLink(tableName, previousSearchColumns, actions);
				
			}
			
		}
		
		
		// Navigate to specific page i.e finding row number
		for(int i=1; ;i++){
			
			System.out.println("***************************Searching in Page :"+i+" ******************");
			
			HashMap<String,String> searchColumns = new HashMap<String,String>();
			searchColumns.put(searchColumn, searchValue); // find row number based Search Column & Search Value
			
			int compRowNum = wEBTABLELIB.findRowNumber(tableName,  searchColumns);
			//System.out.println("compRowNum  :"+compRowNum);
			
			if(compRowNum < 0){ // click on next link
				
				HashMap<String,String> nextSearchColumns = new HashMap<String,String>();
				nextSearchColumns.put(navigationColumn, "Next .*");
				
				HashMap<String,String> actions = new HashMap<String,String>();
				actions.put("displayname", navigationColumn);
				
				wEBTABLELIB.clickLink(tableName, nextSearchColumns, actions);
				
			}else{
				rowNum = compRowNum;
				break;
			}
			
		}
		return rowNum;
	}
	public void selectCustomer(String searchFor,String searchForValue,String accountNumber) throws Exception
	{
		String cols	=	"";
		String colVals	=	"";
		//info("0");
		web.window("/web:window[@title='Site:Search*']").waitForPage(null,true);
		//info("1");
		if(!"".equals(searchFor) )
		{
			web.selectBox("/web:window[@title='Site:Search*']/web:document[@index='0']/web:form[@name='criteria' or @index='0']/web:select[(@id='custSrch_name' or @name='custPartyType' or @index='0') and multiple mod 'False']").selectOptionByText(searchFor);
		}
		////info("2");
		if(!"".equals(searchForValue))
		{
			web.textBox("/web:window[@title='Site:Search*']/web:document[@index='0' or @name='thewindow']/web:form[@name='criteria' or @index='0']/web:input_text[@id='custPartyName' or @name='custPartyName']")
			.setText(searchForValue);			
			web.textBox("/web:window[@title='Site:Search*']/web:document[@index='0' or @name='thewindow']/web:form[@name='criteria' or @index='0']/web:input_text[@id='custPartyName' or @name='custPartyName']")
			.pressTab();	
			web.window("/web:window[@title='Site:Search*']").waitForPage(null,true);
			cols	=	"Customer Name";
			colVals	=	searchForValue;
		}
		//info("3");
		if(!"".equals(accountNumber))
		{
			web.textBox("/web:window[@title='Site:Search*']/web:document[@index='0' or @name='thewindow']/web:form[@name='criteria' or @index='0']/web:input_text[@id='custSrch_acctNo' or @name='custAccountNum' or @index='1']")
			.setText(accountNumber);	
			web.textBox("/web:window[@title='Site:Search*']/web:document[@index='0' or @name='thewindow']/web:form[@name='criteria' or @index='0']/web:input_text[@id='custSrch_acctNo' or @name='custAccountNum' or @index='1']")
			.pressTab();	
			web.window("/web:window[@title='Site:Search*']").waitForPage(null,true);
			if(!"".equals(cols))
			{
				cols 	= cols		+";"+	"Account Number";
				colVals = colVals	+";"+	accountNumber;
			}
			else
			{
				cols = "Account Number";
				colVals = accountNumber;				
			}
			
		}
		//info("4");
		web.button("/web:window[@title='Site:Search*']/web:document[@index='0' or @name='thewindow']/web:form[@name='criteria' or @index='0']/web:input_button[@name='Go' or @value='Go' ]")
		.click();	
		web.window("/web:window[@title='Site:Search*']").waitForPage(null,true);
		
		
		String colNames[]	=	 cols.split(";");
		String colValues[]	=	 colVals.split(";");
		//info("5");
		gENLIB.selectTableRadioButton("Select", colNames, colValues, "Select");
		if(web.button("/web:window[@title='Site:Search*']/web:document[@index='0']/web:form[@name='mTable' or @index='1']/web:input_button[@name='Select' or @value='Select' or @index='4']").exists())
			web.button("/web:window[@title='Site:Search*']/web:document[@index='0']/web:form[@name='mTable' or @index='1']/web:input_button[@name='Select' or @value='Select' or @index='4']").click();
		
		
	}	
	public void selectResources(String resources) throws Exception
	{
		if(!"".equals(resources))
		{
			if("Select All".equals(resources))
			{
				forms.checkBox("//forms:checkBox[(@name='SCCTRL_SELECT_ALL_0')]").check(true);
				think(2);
			}
			else
			{
				String rs[]	= resources.split(",");
				forms.checkBox("//forms:checkBox[(@name='SCCTRL_SELECT_ALL_0')]").check(false);
				think(2);
				int i	=	0;
				int j	=	0;
				while(true)
				{
					String currRec	=	forms.textField("//forms:textField[(@name='SCQUAL_AM_TERR_QUALIFIER_"+i+"')]").getText();
					if(currRec.equals(rs[j]))
					{
						forms.checkBox("//forms:checkBox[(@name='SCQUAL_AM_QUALIFIER_CHCK_"+i+"')]").check(true);
						think(2);
						j++;
						
						if(j==rs.length)
						{
							info("Selected all the resources: " + resources);
							break;
							
						}
					}
					i++;
					if(i==4)
					{
						i=3;
						forms.textField("//forms:textField[(@name='SCQUAL_AM_TERR_QUALIFIER_"+i+"')]").invokeSoftKey("DOWN");
						think(2);
						
						String newCurrRec	=	forms.textField("//forms:textField[(@name='SCQUAL_AM_TERR_QUALIFIER_"+i+"')]").getText();
						if(currRec.equals(newCurrRec))
						{
							reportFailure("Reached end of resources, cannot find any more , check your input data: " +resources);
							break;
						}
						
					}					
					
				}
				
			}
		}
	}	
	public void selectFormsSingleColValues(String xpaths,String srcValues) throws Exception
	{
		String paths[]	=	xpaths.split(",");
		String textFieldXpath	=	paths[0].substring(0,paths[0].lastIndexOf("_"));
		String checkBoxXpath	=	paths[1].substring(0,paths[1].lastIndexOf("_"));
		int maxVisibleLines		=	Integer.parseInt(paths[2]);
		
		
		if(!"".equals(srcValues))
		{
			
				String rs[]	= srcValues.split(",");
				//forms.checkBox("//forms:checkBox[(@name='SCCTRL_SELECT_ALL_0')]").check(false);
				think(2);
				int i	=	0;
				int j	=	0;
				while(true)
				{
					String currRec	=	forms.textField("//forms:textField[(@name='"+textFieldXpath+i+"')]").getText();
					if(currRec.equals(rs[j]))
					{
						forms.checkBox("//forms:checkBox[(@name='"+checkBoxXpath+i+"')]").check(true);
						think(2);
						j++;
						
						if(j==rs.length)
						{
							info("Selected all the resources: " + srcValues);
							break;
							
						}
					}
					i++;
					if(i==maxVisibleLines)
					{
						i=maxVisibleLines-1;
						forms.textField("//forms:textField[(@name='"+textFieldXpath+i+"')]").invokeSoftKey("DOWN");
						think(2);
						
						String newCurrRec	=	forms.textField("//forms:textField[(@name='"+textFieldXpath+i+"')]").getText();
						if(currRec.equals(newCurrRec))
						{
							reportFailure("Reached end of resources, cannot find any more , check your input data: " +srcValues);
							break;
						}
						
					}					
					
				}
				
			
		}
	}		
}

