import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	@FunctionLibrary("WEBTABLEOBJ") lib.ebsqafwk.WEBTABLEOBJ wEBTABLEOBJ;

	public void initialize() throws Exception {
	}

	/**
	 * Add code to be executed each iteration for this virtual user.
	 */
	public void run() throws Exception {

	}

	public void finish() throws Exception {
	}
	
	public void sop(String rs)
	{
		String debug	=	getVariables().get("DEBUG_MODE");
		if(debug==null || (debug!=null && debug.equals("OFF")))
		{
			// intentionally left it blank																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																													
		}
		else
			System.out.println(rs);
		
	}	
	
	public String findEmptyRowStart(String tableName,String colName) throws Exception{
		
		return wEBTABLEOBJ.findEmptyRowStart(tableName, colName);
	}
	
	/**
	 *  This is for finding table row, which is empty
	 *  
	 *  Example1: 
	 *  
	 * @param tableName
	 * @param colName
	 * @param compType
	 * @return
	 * @throws Exception
	 */
	public String findDynamicEmptyRowStart(String tableName,String colName, String compType) throws Exception{
		
		return findDynamicEmptyRowStart(tableName, colName, compType,"0");
	}
	
	public String findDynamicEmptyRowStart(String tableName,String colName, String compType, String compSequence) throws Exception{
		
		return wEBTABLEOBJ.findDynamicEmptyRowStart(tableName, colName, compType, compSequence);
	}
	
	private HashMap<String,String> convertactionsToHashMap(String[] singleAction){
		
		HashMap<String,String> action = new HashMap<String,String>();
		
		//Keyword ,object ,caption ,logical name, outputvar, function Name, params
		int actionFieldsLength = singleAction.length;
		
		action.put("keyword", singleAction[0].toUpperCase());
		action.put("object", singleAction[1].toUpperCase());
		action.put("displayname", singleAction[2]); 
		action.put("logicalname", singleAction[3]);
		action.put("outputvar", singleAction[4]);
		action.put("funcname", singleAction[5]);
		
		if(!"".equals(singleAction[3])){
			
			boolean numberExists = singleAction[3].matches("\\d*");
			
			if(numberExists){
				action.put("displayname", singleAction[3]); 
			}
		}
			
		if(actionFieldsLength > 6){
			
			for(int index=1,actionIndex=6; actionIndex < actionFieldsLength; index++, actionIndex++){
				action.put("value"+index, singleAction[actionIndex]);
			}
		}
		
		return action;
	}
	
	public String tableactions(String tableName, List <String[]> actions, HashMap<String, String> searchColumns)throws Exception {
		
		return tableactions(tableName, searchColumns, actions);
	}
	
	public String tableactions(String tableName, HashMap<String, String> searchColumns, List <String[]> actions)throws Exception {

		System.out.println("***************** Start of tableactions with Search Columns ********************");
		
		wEBTABLEOBJ.getWebTableObject(tableName);
		
		int rowNumber = findRowNumber( tableName,  searchColumns);
		
		rowNumber = rowNumber - 1;
		
		tableactions(tableName, rowNumber+"", actions);
		
		System.out.println("***************** End of tableactions with Search Columns ********************");
		
		return ""+rowNumber;//
	}
	
	public String  tableactions(String tableName, String rowNumber , List <String[]> actions)throws Exception {

		System.out.println("***************** Start of tableactions with Row Number ********************");
		
		this.sop("rowNumber :"+rowNumber);
		
		int rowNum = toInt(rowNumber)+1;
		
		for(int i=0; i<actions.size();i++){
			
			wEBTABLEOBJ.getWebTableObject(tableName);
			
			// Get the Actions
			String []singleAction = actions.get(i);
			
			// Convert to HashMap
			HashMap<String,String> action = convertactionsToHashMap(singleAction);
			
			if(! action.get("keyword").equals("WAIT")){
				wEBTABLEOBJ.waitForColumn(tableName);
			}
			
			
			if(action.get("keyword").equals("CHECK")){  // Verification of keyword
				
				if(action.get("object").equals("CHECKBOX")){ // Verification of Object
					
					if(action.get("value1").equalsIgnoreCase("on") || action.get("value1").equalsIgnoreCase("true") || action.get("value1").equalsIgnoreCase("yes")){
						
						this.checkOn(tableName, rowNum, action);
					}				
					else if(action.get("value1").equalsIgnoreCase("off") || action.get("value1").equalsIgnoreCase("false") || action.get("value1").equalsIgnoreCase("no")){
						
						this.checkOFF(tableName, rowNum, action);
					}
				}else{
					
					reportFailure("Invalid Action for CHECK "+action.get("object"));
				}
			}
			else if(action.get("keyword").equals("SELECT")){
				
				if(action.get("object").equals("RADIOBUTTON")){ // Selecting Radio button
												
					this.radioOn(tableName, rowNum, action);
				}
				else if(action.get("object").equals("LIST")){ // Select List Box
					
					this.selectList(tableName, rowNum, action);
					
					wEBTABLEOBJ.clearWebTableObject();
				}
				else if(action.get("object").equals("LISTBOX")){ // Select List Box
															
					this.selectList(tableName, rowNum, action);
					
					wEBTABLEOBJ.clearWebTableObject();
				}
				else{
					reportFailure("Invalid Action for SELECT"+action.get("object"));
				}
			}
			else if(action.get("keyword").equals("SETTEXT")){
				
				if(action.get("object").equals("EDIT")){ // Set TextBox
					
					this.setEditValue(tableName, rowNum, action);
				}
				else if(action.get("object").equals("TEXTAREA")){ // Set TextArea
					
					this.setTextArea(tableName, rowNum, action);
				}
				else{ 
					reportFailure("Invalid Action for SETTEXT"+action.get("object"));
				}
			}
			else if(action.get("keyword").equals("CLICK")){
				
				if(action.get("object").equals("IMAGE")){
					
					this.clickImage(tableName, rowNum, action);
				}
				else if(action.get("object").equals("LINK")){
					
					this.clickLink(tableName, rowNum, action);
				}else if(action.get("object").equals("BUTTON")){
					
					this.clickButton(tableName, rowNum, action);
				}
				else
				{
					reportFailure("Invalid Action for Click "+action.get("object"));
				}
			}
			
			else if(action.get("keyword").equals("VERIFY")){
				
				if(action.get("object").equals("EDIT")){
					
					String editValue = this.getEditValue(tableName, rowNum, action);
					
					this.sop("Edit Value in Cell:"+editValue);
					
					if((editValue!=null)&&(compareStrings(action.get("value1"),editValue))){
						
						info("EditBox value :"+editValue+"is as expected");
						
					}else{
						
						reportFailure("Expected Value:"+action.get("value1")+ "where as Actual Value is :"+editValue);
					}
				}
				else if(action.get("object").equals("TEXTAREA")){
					
					String editValue =this.getTextAreaValue(tableName, rowNum, action);

					this.sop("Text Area Value in Cell:"+editValue);
					
					if((editValue!=null)&&(compareStrings(action.get("value1"),editValue))){
						
						info("EditBox value :"+editValue+"is as expected");
					}else{
						reportFailure("Expected Value:"+action.get("value1")+ "where as Actual Value is :"+editValue);
					}
				}
				else if(action.get("object").equals("LIST")){
					
					String listValue =this.getListValue(tableName, rowNum, action);
					
					this.sop("Selected List Value in the Cell :"+listValue);
					
					if((listValue!=null)&&(compareStrings(action.get("value1"),listValue))){
						
						info("ListBox value :"+listValue+"is as expected");
						
					}else{
						reportFailure("Expected Value:"+action.get("value1")+ "where as Actual Value is :"+listValue);
					}
				}
				else if(action.get("object").equals("LISTBOX")){
					
					String listboxValue =this.getListValue(tableName, rowNum, action);
					
					this.sop("Selected List Value in the Cell :"+listboxValue);
					
					if((listboxValue!=null)&&(compareStrings(action.get("value1"),listboxValue))){
						
						info("ListBox value :"+listboxValue+"is as expected");
						
					}else{
						reportFailure("Expected Value:"+action.get("value1")+ "where as Actual Value is :"+listboxValue);
					}
				}
				else if(action.get("object").equals("CHECKBOX")){
					
					String checkValue =this.getCheckBoxOption(tableName, rowNum, action);
					
					this.sop("CheckBox Value in the Cell :"+checkValue);
					
					if((checkValue!=null)&&(compareStrings(action.get("value1").trim(),checkValue) )){
						
						info("Actual Checkbox state :"+checkValue+" is verified with expectged value :"+action.get("value1"));
						
					}else{
						reportFailure("Actual Checkbox state :"+checkValue+" is not matched with expected value :"+action.get("value1"));
					}
				}
				else if(action.get("object").equals("TEXT")){
					
					List<String> textValue =this.getCellData(tableName, rowNum, action);
					String expected = action.get("value1");
					
					String actualValue =  textValue.toString().trim();
					this.sop("Text Value in the Cell :"+actualValue);
					System.out.println("Text Value in the Cell :"+actualValue);
					
					boolean bFound = false;
					
					for(int cellItemIndex=0; cellItemIndex<textValue.size();cellItemIndex++){
						if(compareStrings(expected,textValue.get(cellItemIndex))){
							bFound = true;
							break;
						}
					}
					
					if(!bFound){
						
						String []expVals = expected.split(";");
						
						if((textValue!=null)&&(textValue.size()>0)){
							
							bFound = true;
							
							int firstIndex = 0;
							for(String exp : expVals){
								 int currentIndex =  actualValue.indexOf(exp,firstIndex);
								if(currentIndex>0){
									
									firstIndex = currentIndex;
									bFound = true;
									
								}else{
									
									bFound = false;
									break;
									
								}
								
							}
						}
					}
					
					if(bFound){
						
						info("Verified Text :"+expected+" is as expected");
					}
					else{
						
						reportFailure("Expected Text :"+action.get("value1")+ "  where as obtained text is :"+actualValue);
					}
				}

				else if(action.get("object").equals("LINK")){
					
					String linkValue =this.getLinkValue(tableName, rowNum, action);
					
					this.sop("Link Value in the Cell :"+linkValue);
					
					if((linkValue!=null)&&(compareStrings(action.get("value1"),linkValue))){
						
						info("Verified LINK :"+linkValue+"is as expected");
						
					}else{
						
						reportFailure("Expected LINK :"+action.get("value1")+ "where as obtained LINK is :"+linkValue);
					}
				}
				else if(action.get("object").equals("IMAGE")){
					
					String imageValue =this.getImageValue(tableName, rowNum, action);
					
					this.sop("Image Value in the Cell :"+imageValue);
					
					if((imageValue!=null)&&(imageValue.contains(action.get("value1")))){
						
						info("Verified Img :"+imageValue+"is as expected");
					}else{
						reportFailure("Expected Img :"+action.get("value1")+ "where as obtained Img alt is :"+imageValue);
					}
				}
				else{ 
					reportFailure("Invalid Action for VERIFY"+action.get("object"));
				}
			}
			
			else if(action.get("keyword").equals("GET")){
				
				if(action.get("object").equals("EDIT")){
					
					String editValue = this.getEditValue(tableName, rowNum, action);
					
					info("WebTable - Text Field Value in the Variable :"+action.get("outputvar")+" is :"+editValue);
					
					getVariables().set(action.get("outputvar"),editValue);
					
					gENLIB.setProperty(action.get("outputvar"),editValue);
				}
				else if(action.get("object").equals("TEXTAREA")){
					
					String textAreaValue =this.getTextAreaValue(tableName, rowNum, action);
					
					info("WebTable - Text Area Value in the Variable :"+action.get("outputvar")+" is :"+textAreaValue);
					
					getVariables().set(action.get("outputvar"),textAreaValue);
					
					gENLIB.setProperty(action.get("outputvar"),textAreaValue);
				}
				else if(action.get("object").equals("LIST")){
					
					String listValue =this.getListValue(tableName, rowNum, action);
					
					info("WebTable - Selected List Item Value in the Variable :"+action.get("outputvar")+" is :"+listValue);
					
					getVariables().set(action.get("outputvar"),listValue);
					
					gENLIB.setProperty(action.get("outputvar"),listValue);
				}
				else if(action.get("object").equals("LISTBOX")){
					
					String listBoxValue =this.getListValue(tableName, rowNum, action);
					
					info("WebTable - Selected List Item Value in the Variable :"+action.get("outputvar")+" is :"+listBoxValue);
					
					getVariables().set(action.get("outputvar"),listBoxValue);
					
					gENLIB.setProperty(action.get("outputvar"),listBoxValue);
				}
				else if(action.get("object").equals("CHECKBOX")){
					
					String checkboxValue =this.getCheckBoxOption(tableName, rowNum, action);
					
					info("WebTable - Checkbox value in the Variable :"+action.get("outputvar")+" is :"+checkboxValue);
					
					getVariables().set(action.get("outputvar"),checkboxValue);
					
					gENLIB.setProperty(action.get("outputvar"),checkboxValue);
				}
				else if(action.get("object").equals("TEXT")){
					
					List<String> textValue =this.getCellData(tableName, rowNum, action);
					
					info("WebTable - Text Value in the Variable :"+action.get("outputvar")+" is :"+textValue.toString());
					
					if(textValue.size() == 0){
						
						getVariables().set(action.get("outputvar"),"");
						
						gENLIB.setProperty(action.get("outputvar"),"");
						
					}else{
						
						getVariables().set(action.get("outputvar"),textValue.get(0));
						
						gENLIB.setProperty(action.get("outputvar"),textValue.get(0));
						
					}
				}
				
				else if(action.get("object").equals("LINK")){
					
					String linkValue =	this.getLinkValue(tableName, rowNum, action);
					
					info("WebTable - Link value in the Variable :"+action.get("outputvar")+" is :"+linkValue);
					
					getVariables().set(action.get("outputvar"),linkValue);
					
					gENLIB.setProperty(action.get("outputvar"),linkValue);
				}
				else{ 
					reportFailure("Invalid Action for GET"+singleAction[1]);
				}
			}
			else if(action.get("keyword").equals("FUNCTIONCALL")){
				
				if(action.get("object").equals("GENLIB")){
						
					if(action.get("funcname").equalsIgnoreCase("WEBSELECTLOV")){
						
						System.out.println("*********  Start of WebSelectLOV in Table ****************** ");
						
						int currentWindowIndex = Integer.parseInt(web.getFocusedWindow().getAttribute("index"));
						
						this.sop("Click WebSelectLOV Image in WebTable "+action.toString());
						this.clickLink(tableName, rowNum, action);
						
						// Waiting for the WebSelect LOV to load
						currentWindowIndex = currentWindowIndex+1;
						web.window("/web:window[@index='"+currentWindowIndex+"']").waitForPage(20,true);
						//Thread.sleep(15000);
						
						this.sop("Selecting specified value from WebSelectLov");
						gENLIB.webSelectLOV(null,action.get("value1"),action.get("value2"),action.get("value3"),action.get("value4"));
						
						wEBTABLEOBJ.clearWebTableObject();
						
						System.out.println("*********  End of WebSelectLOV in Table ****************** ");
					}
					else
					{
						reportFailure("Invalid value for FUNCTIONCALL: GENLIB"+action.get("displayname"));
					}
				}else if(action.get("object").equals("CRMLIB")){
						
					if(action.get("funcname").equalsIgnoreCase("CRMWEBSELECTLOV")){
						
						System.out.println("*********  Start of CRM WebSelectLOV in Table ****************** ");
						
						int currentWindowIndex = Integer.parseInt(web.getFocusedWindow().getAttribute("index"));
						
						HashMap<String,String> setAction = new HashMap<String,String>();
						setAction.put("displayname",action.get("displayname"));
						setAction.put("value1",action.get("value3"));
						
						this.setEditValue(tableName, rowNum, setAction);
						
						this.sop("Click WebSelectLOV Image in WebTable "+action.toString());
						this.clickLink(tableName, rowNum, action);
						
						// Waiting for the WebSelect LOV to load
						currentWindowIndex = currentWindowIndex+1;
						web.window("/web:window[@index='"+currentWindowIndex+"']").waitForPage(20,true);
						//Thread.sleep(15000);
						
						this.sop("Selecting specified value from WebSelectLov");
						gENLIB.webSelectLOV(null,action.get("value2"),action.get("value3"),action.get("value4"),action.get("value5"));
						
						System.out.println("*********  End of WebSelectLOV in Table ****************** ");
					}
					else
					{
						reportFailure("Invalid value for FUNCTIONCALL: GENLIB"+action.get("displayname"));
					}
				}
				else
				{
					//System.err.println("Inside else");
					reportFailure("Invalid value for FUNCTIONCALL"+action.get("object"));
				}
			
			}else if(action.get("keyword").equals("WAIT")){
				
				if(action.get("object").equals("WINDOW")){
					
					String windowName = action.get("logicalname");
					windowName = windowName.replaceAll("\"", "").replaceAll("'", "");
					
					if(windowName.contains("title=")){
						
						String[] title = windowName.split("=");
						windowName = title[1];
					}
					
					/*//"title='Manage User Profile'"
					if(windowName.startsWith("title=")){
						windowName = "@"+windowName;
					}else if(windowName.startsWith("@title=")){
						
					}else {
						
						if(windowName.startsWith("'")){
							windowName = "@title="+windowName;
						}else{
							windowName = "@title='"+windowName+"'";
						}
						
					}*/
					this.sop("wait window name :"+windowName);
					//this.sop("wait window name :"+Integer.parseInt(eval("{{WAIT_PAGE}}")));
					web.window("/web:window[@title='"+ windowName+"']").waitForPage(200,true);
					
				}else if(action.get("object").equals("NORMAL")){
					
					String extraTime = action.get("logicalname");
					
					if(extraTime == null){
						extraTime = "";
					}
					
					gENLIB.wait("20",extraTime);
				}
				
			}else if(action.get("keyword").equals("SETWINDOW")){
				
			}
			else{
				reportFailure("Invalid Action received:"+action.get("keyword"));
			}
			
			/*Wait */
			Thread.sleep(5000);
			
			System.out.println("***************** End of tableactions with Row Number ********************");
		}
		
		System.out.println("***************** Clearing WebTable Object ********************");
		
		wEBTABLEOBJ.clearWebTableObject();
		
		return ""+(rowNum-1);
	}
	
	
	public void clearWebTableObject() throws Exception{
		wEBTABLEOBJ.clearWebTableObject();
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
		
		web.textBox("//web:input_text[@index='"+requiredTxtField.getIndex()+"']").setText(searchValue);
		
		List<DOMElement>imgsList = tr.getElementsByTagName("img");
		List<DOMElement> requiredImgs = new ArrayList<DOMElement>();
		
		if(imgsList.size()>0){
			
			requiredImgs.get(textFieldIndex).click();
			
			}else{
				throw new Exception("No Images are present in row containing specified ImgName");
				
				
			}
		//spotnuru
		gENLIB.webSelectLOV(null, searchByOption, searchValue, colName, rowValue);
		
	}
	
	
	/**
	 *  Get Columns 
	 *  
	 * @param tableName
	 * @throws Exception
	 */
	public void getColumns(String tableName) throws Exception{
		
		wEBTABLEOBJ.getColumns(tableName);
	}
	
	public int getRowCount(String tableName) throws Exception{
		
		int rowCount = wEBTABLEOBJ.getRowCount(tableName);
		
		return rowCount;
	}
	
	
	public boolean verifyTableWithExactRowData(String tableName,String[] colNames, HashMap<String,List<String>> expectedTableData , String[] sExpComponents) throws Exception{
		
		boolean verifyResult = wEBTABLEOBJ.verifyTableWithExactRowData(tableName, colNames, expectedTableData, sExpComponents);
		
		return verifyResult;
	}
	
	
	public List<String> getCellData(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {

		return wEBTABLEOBJ.getCellData(tableName, searchColumns, actions);
	}
	
	public List<String> getCellData(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		return wEBTABLEOBJ.getCellData(tableName, rowNum, actions);
	}
	
		
	public void checkOn(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		wEBTABLEOBJ.checkOn(tableName, rowNum, actions);
	}
	
	public void checkOFF(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		wEBTABLEOBJ.checkOFF(tableName, rowNum, actions);
	}
	
	
	public void radioOn(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		wEBTABLEOBJ.radioOn(tableName, rowNum, actions);
	}
	
	
	public String getEditValue(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		return wEBTABLEOBJ.getEditValue(tableName, rowNum, actions);
	}
	
	public String getLinkValue(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		return wEBTABLEOBJ.getLinkValue(tableName, rowNum, actions);
	}
	
	
	public String getImageValue(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		return wEBTABLEOBJ.getImageValue(tableName, rowNum, actions);
	}
	
	
	public String getCheckBoxOption	(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {

		return "";
	}

	public String getCheckBoxOption	(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		return wEBTABLEOBJ.getCheckBoxOption(tableName, rowNum, actions);
	}
	
	public String getTextAreaValue(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		return wEBTABLEOBJ.getTextAreaValue(tableName, rowNum, actions);
	}
	
	public void setEditValue(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {

	}
	
	public void setEditValue(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {
		
		wEBTABLEOBJ.setEditValue(tableName, rowNum, actions);
	}
	
	public void setTextArea(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {
		
		wEBTABLEOBJ.setTextArea(tableName, rowNum, actions);
	}

 
	public void clickImage(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {
	
		wEBTABLEOBJ.clickImage(tableName, searchColumns, actions);
	}
	
	public void clickImage(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {
		
		wEBTABLEOBJ.clickImage(tableName, rowNum, actions);
	}


	public void clickImageByName(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {
		
		wEBTABLEOBJ.clickImageByName(tableName, rowNum, actions);
	}

	public void clickLink(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {
		wEBTABLEOBJ.clickLink(tableName, searchColumns, actions);
	}
	
	public void clickLink(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		wEBTABLEOBJ.clickLink(tableName, rowNum, actions);
	}
	
	public void clickButton(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {
		
		wEBTABLEOBJ.clickButton(tableName, rowNum, actions);
	}

	
	public void selectList(String tableName, int rowNum, HashMap<String,String>actions)throws Exception {
		
		wEBTABLEOBJ.selectList(tableName, rowNum, actions);
	}

	
	public String getListValue(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		return wEBTABLEOBJ.getListValue(tableName, rowNum, actions);
	}

	public boolean verifyRowData(String tableName, HashMap<String, String> searchColumns, String[] actions)throws Exception {

		return wEBTABLEOBJ.verifyRowData(tableName, searchColumns, actions);
	}
	
	public int findRowNumber(String tableName, HashMap<String, String> searchColumns) throws Exception{
	
		return wEBTABLEOBJ.findRowNumber(searchColumns, tableName);

	}
	

	public String [] getKeysFromHashMapValuesAreList(HashMap< String,List<String>> searchColumns){
		
		String colVal [] =(String[])searchColumns.keySet().toArray(new String[searchColumns.keySet().size()]);
		 
		return colVal;
	}
	
	public String [] getKeysFromHashMap(HashMap<String, String> searchColumns){
		
		String colVal [] =(String[])searchColumns.keySet().toArray(new String[searchColumns.keySet().size()]);
		 
		return colVal;
	}
	
	public String [] getValuesFromHashMap(HashMap<String, String> searchColumns){
		return (String[])searchColumns.values().toArray(new String[searchColumns.keySet().size()]);
		
	}
	
	@SuppressWarnings("unchecked") 
	public List<String> [] getValuesFromHashMapValuesAreList(HashMap< String,List<String>> searchColumns){
		
		return ( List<String>[])searchColumns.values().toArray(new ArrayList[searchColumns.keySet().size()]);
		
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
	
	
	
	
	
	
}
