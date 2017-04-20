import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.scripting.modules.webdom.api.elements.DOMSelect;
import oracle.oats.scripting.modules.webdom.api.elements.DOMText;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.applet.api.*;

public class script extends IteratingVUserScript {
	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;
	@ScriptService oracle.oats.scripting.modules.applet.api.AppletService applet;
	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;

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
	/**
	 * 
	 * @param labelWithIndex
	 * @param valueToEnter
	 * @throws Exception
	 */
	public void webSetTextBasedOnLabel(String labelWithIndex, String valueToEnter)throws Exception {
		
		String value = "";
		
		DOMElement textFieldElement = webGetCompBasedOnLabel(labelWithIndex, "input");
		
		if(textFieldElement != null){
			
			textFieldElement.focus();
			
			((DOMText)textFieldElement).setText(valueToEnter);
			
		}
	}
	
	
	
	/**
	 *  Select List Box based on Label
	 *  		-- labelWithIndex (label;1,document=1,form=1) and label should be passed based on OATS recordings
	 *  
	 * @param labelWithIndex
	 * @param valueToselect
	 * @throws Exception
	 */
	public void webSelectListBoxBasedOnLabel(String labelWithIndex, String valueToselect)throws Exception {
		
		String value = "";
		
		DOMElement selectElement = webGetCompBasedOnLabel(labelWithIndex, "select");
		
		if(selectElement != null){
			
			selectElement.focus();
			
			((DOMSelect)selectElement).selectOptionByText(valueToselect);
			
			((DOMSelect)selectElement).fireEvent("blur");
		}
		
	}
	
	
	/**
	 * 
	 * @param labelWithIndex
	 * 			-- labelWithIndex (label;1,document=1,form=1) and label should be passed based on OATS recordings
	 * @return
	 * @throws Exception
	 */
	public String webGetLinkBasedOnLabel(String labelWithIndex)throws Exception 
	{
		String value = "";
		
		DOMElement linkElement = webGetCompBasedOnLabel(labelWithIndex, "a");
		
		if(linkElement != null){
			
			linkElement.focus();
			
			value =  linkElement.getAttribute("text");
			
			if(value != null){
				value = value.trim();
			}
		}
		
		this.sop("Link after the label :\""+labelWithIndex+"\" is :"+value);
		
		return value;
	}
	
	/**
	 * 
	 * @param labelWithIndex
	 * 			--  labelWithIndex (label;1,document=1,form=1) and label should be passed based on OATS recordings
	 * @return
	 * @throws Exception
	 */
	public String webGetTextBasedOnLabel(String labelWithIndex)throws Exception 
	{
		String[] components = {"td","div","span","h2"};
		
		String value = "";
		
		for(int index=0; index<components.length; index++){
			
			DOMElement textElement = webGetCompBasedOnLabel(labelWithIndex, components[index]);
			
			if(textElement != null){
				
				textElement.focus();
				
				value =  textElement.getAttribute("innertext");
				this.sop("type:"+components[index]+"    value:"+value);
				
				if(value != null){
					value = value.trim();
					break;
				}
			}
		}
		
		this.sop("Text after the label :\""+labelWithIndex+"\" is :"+value);
		
		return value;
	}
	
	
	public boolean webVerifyLinkBasedOnLabel(String labelWithIndex, String expectedValue) throws Exception {
		
		boolean linkExists = false;
		
		String actualValue = webGetLinkBasedOnLabel(labelWithIndex);
		
		linkExists = compareStrings(expectedValue, actualValue);
		
		if(linkExists){
			info("Actual Link :\""+actualValue+"\" is matched with Expected Link :\""+expectedValue+"\"");
		}else{
			warn("Actual Link :\""+actualValue+"\" is not matched with Expected Link :\""+expectedValue+"\"");
		}
		
		return linkExists;
	}
	
	public boolean webVerifyTextBasedOnLabel(String labelWithIndex, String expectedValue) throws Exception {
		
		boolean textExists = false;
		
		String actualValue = webGetTextBasedOnLabel(labelWithIndex);
		
		textExists = compareStrings(expectedValue, actualValue);
		
		if(textExists){
			info("Actual Text :\""+actualValue+"\" is matched with Expected Text :\""+expectedValue+"\"");
		}else{
			warn("Actual Text :\""+actualValue+"\" is not matched with Expected Text :\""+expectedValue+"\"");
		}
		
		return textExists;
	}
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean compareStrings(String expectedString, String actualString) {

		boolean stringMatched = false;

		Pattern pattern = Pattern.compile(expectedString);
		Matcher matcher = pattern.matcher(actualString);

		//System.out.println("expectedString :" + expectedString);
		
		if (matcher.matches()) {
			stringMatched = true;
		}

		return stringMatched;
	}
	
	public HashMap<String,String> getComponentDetails(String compTypeWithIndex) throws Exception{
		
		HashMap<String,String> compMapDetails = new HashMap<String,String>();
		
		int similarCompIndex = 0;
		int documentIndex = 0;
		int formIndex = 0;
		String expCompName = "";	
		
		compTypeWithIndex = Pattern.compile("\\,",Pattern.LITERAL).matcher(compTypeWithIndex).replaceAll(Matcher.quoteReplacement("~~"));
		
		String compDetails[] = compTypeWithIndex.split(",");
		
		for (int index = 0; index < compDetails.length; index++) {
			
			compDetails[index] = compDetails[index].replaceAll("~~", ",");
			
			// document index
			if (compDetails[index].trim().startsWith("document=")) {

				documentIndex = Integer.parseInt(compDetails[index].split("=")[1]);

			} else if (!compDetails[index].contains("document=") && !compDetails[index].contains("form=")) {

				String[] componentDetails = compDetails[index].split(";");

				// Similar Comp Index
				if (componentDetails.length > 1) {
					similarCompIndex = Integer.parseInt(componentDetails[1]);
				}

				// Comp Name
				expCompName = componentDetails[0];

			} else if (compDetails[index].trim().startsWith("form=")) {
				// form index
				formIndex = Integer.parseInt(compDetails[index].split("=")[1]);
			}
		}
		
		// Create Window Path
		String windowIndex = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();
		String windowPath = "/web:window[@index='" + windowIndex + "' or @title='" + title + "']";
		
		// Document Path
		String docPath = windowPath + "/web:document[@index='" + documentIndex + "']";
		
		// form Path
		String formPath = docPath+"/web:form[@index='" + formIndex + "']";
		
		
		compMapDetails.put("compname", expCompName);
		compMapDetails.put("compIndex", Integer.toString(similarCompIndex));
		compMapDetails.put("documentIndex", Integer.toString(documentIndex));
		compMapDetails.put("formIndex", Integer.toString(formIndex));
		compMapDetails.put("windowPath", windowPath);
		compMapDetails.put("docPath", docPath);
		compMapDetails.put("formPath", formPath);
		
		return compMapDetails;
	}
	
	
	
	
	public List<DOMElement> getComponents(String docPath, String tag, String expectedData) throws Exception{
		
		//List<DOMElement> compList = web.document(docPath).getElementsByTagName(components[compIndex], propertyTestList);
		List<DOMElement> actCompList =  new ArrayList<DOMElement>();
		List<DOMElement> compList = web.document(docPath).getElementsByTagName(tag);
		if(compList!=null)
			this.sop("number of comps found: "+ compList.size());
		else
			this.sop("number of comps found: 0");
		for(int index=0; index < compList.size(); index++){
			
			DOMElement comp = compList.get(index);
			
			String attribute = "text";
			
			if(tag.equalsIgnoreCase("td") || tag.equalsIgnoreCase("div") ||tag.equalsIgnoreCase("span") ||tag.equalsIgnoreCase("label") ){
				attribute = "innertext";
			}
			
			String compText = comp.getAttribute(attribute);
			
			if(compText != null){
				
				if(compText.trim().equals(expectedData)){
					actCompList.add(comp);
				}
			}
		}
		
		return actCompList;
	}
	
	
	public DOMElement webGetCompBasedOnLabel(String labelWithIndex, String compType)throws Exception 
	{
		// Label Components
		String[] components = {"td","div","span","h2","label"};
		//String[] components = {"span","td","div","h2","label"};
		
		// Link value
		DOMElement actualElement = null;
		
		// Get Label Details like compIndex, document Index, form Index, comp name, docPath, formPath
		HashMap<String,String> compDetails = getComponentDetails(labelWithIndex);

		int labelIndex 		= Integer.parseInt(compDetails.get("compIndex"));
		
		int documentIndex 	= Integer.parseInt(compDetails.get("documentIndex"));
		int formIndex 		= Integer.parseInt(compDetails.get("formIndex"));
		String expLabelName = compDetails.get("compname");
		String docPath 		= compDetails.get("docPath");
		String formPath 	= compDetails.get("formPath");	
			
		this.sop("****** Label Details ********************* ");
		this.sop("Label Name: \"" + expLabelName + "\"  Label Index: \"" + labelIndex + "\"  Document Index: \""
				+ documentIndex + "\"  Form Index: \"" + formIndex + "\"");

		
		PropertyTestList propertyTestList = new PropertyTestList();
		propertyTestList.add("innertext",	expLabelName);
		
		for(int compIndex=0; compIndex < components.length; compIndex++){
			
			// Get all the components which matches the lable
			//getComponents(docPath, components[compIndex]);
			//List<DOMElement> compList = web.document(docPath).getElementsByTagName(components[compIndex], propertyTestList);
			
			List<DOMElement> compList = getComponents(docPath, components[compIndex],expLabelName);
			if(compList!=null && compList.size()>0)
				this.sop("After get components:" + compList.size());
			else
				this.sop("After get components: 0");
			
			if(compList.size() > 0 && ((labelIndex+1) <= compList.size())){
				
				this.sop("Found label with Tag:"+ components[compIndex]);
				
				// Get the Label Component
				DOMElement labelComp = compList.get(labelIndex);
				
				// Get the Component Element
				DOMElement expCompelement = getNextElement(labelComp, compType);
				
				
				
				// Actual Element
				actualElement = expCompelement;
				
				break;
			}
		}
		
		return actualElement;
	}
	
	
	
	public DOMElement getNextElement(DOMElement component, String compType) throws Exception{
		
		DOMElement actualComp = null;
		
		DOMElement parentComp =  component.getParent();
		//this.sop("first Parent Tag :"+parentComp.getTag());
		//this.sop("Parnet HTML: :"+parentComp.getAttribute("innerHTML"));
		
		// going through next siblings
		DOMElement nextComp = component.getNextSibling(); 
		if(nextComp!=null)
		{
		//	this.sop("first Parent Sibling Tag :"+nextComp.getTag());
		//	this.sop("Next Sibling HTML: :"+nextComp.getAttribute("innerHTML"));
		}	
		else
		{
			//this.sop("No Next components for the label found component");
			//this.sop("Trying with parent");
			nextComp = parentComp.getNextSibling();
			if(nextComp!=null)
			{
				//this.sop("first Parent Sibling Tag :"+nextComp.getTag());
				//this.sop("Next Sibling HTML: :"+nextComp.getAttribute("innerHTML"));
			}			
		}	
		
		//DOMElement nextComp = component;
		while(nextComp != null){
			
			/*if(nextComp ){
				
			}*/
			
			// Check if the next comp matches link tag
			String nextCompTag = nextComp.getTag();
			//this.sop("nextCompTag :"+nextCompTag);
			if(nextComp.getTag().equalsIgnoreCase(compType)){// if sibling itself is a link
				
				actualComp = nextComp;
				
				return actualComp;
				/*if(compType.equalsIgnoreCase("a")){
					value = nextComp.getAttribute("text");
					//this.sop("value1:"+value);
					return nextComp;
				}*/
				
			}else
			{
				
				//this.sop("compType:"+compType);
				List<DOMElement> subElements = nextComp.getElementsByTagName(compType);
				//this.sop("comp size:"+subElements.size());
				
				if(subElements.size() > 0)
				{
					
					for(int index=0; index < subElements.size(); index++){
						
						String hidden = subElements.get(index).getAttribute("type");
						String compValue = subElements.get(index).getAttribute("text");
						//this.sop("compValue:"+compValue);
						
						if(hidden != null && hidden.equalsIgnoreCase("text") ){
								// code is not required, it should proceed component
						}else{
							if((hidden != null && hidden.equalsIgnoreCase("hidden") || compValue == null || compValue.equalsIgnoreCase(""))){
								continue;
							}
						}
						
						actualComp = subElements.get(index);
						
						return actualComp;
					}
				}
			}
			
			// Get Next Component
			nextComp = nextComp.getNextSibling();
		}
		
		return actualComp;
	}
}
