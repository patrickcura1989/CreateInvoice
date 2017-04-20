import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;

import oracle.oats.scripting.modules.basic.api.IteratingVUserScript;
import oracle.oats.scripting.modules.basic.api.ScriptService;
import oracle.oats.scripting.modules.basic.api.Variables;
import oracle.oats.scripting.modules.formsFT.common.api.elements.AbstractWindow;
import oracle.oats.scripting.modules.webdom.api.elements.DOMBrowser;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.scripting.modules.webdom.api.elements.DOMTable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import oracle.oats.scripting.modules.applet.api.*;
import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.webdom.api.*;
import lib.*;

public class script extends IteratingVUserScript {
	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;
	@ScriptService oracle.oats.scripting.modules.applet.api.AppletService applet;
	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;

	/* Initialize Data for Fmwklib*/
	int SYNCTIME=-1;
	EBSBrowser ebs = null;
	EBS EBSLibrary;

	private Variables getvariable;
	//@ScriptService oracle.oats.scripting.modules.ebs.library.plugin.api.OracleLibrary EBSLibrary;
	@ScriptService oracle.oats.scripting.modules.datatable.api.DataTableService datatable;



	/* Initialize Data for Genlib*/
	DDT ddt;

	/* Initialize Data for palib*/
	String P_DEFAULT="-9001";

	/* Initialize Data for Sslib*/
	String BaseState="";
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
	
	
	public boolean refreshPaymentProcessRequest(String buttonName, String requestSourceColName, String requestSourceColValue, String referenceSourceColName, String referenceSourceColValue, String Status) throws Exception {
		 
		 info("********************* Start of Refresh WebItem ******************* ");
			
			boolean verified = false;
			String buttonXPath="";
			
			
			// Prepare Button XPath
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
			
			// ****** Search for Table ********
			
			// Select LOV
			//gENLIB.webSelectLOV("Search for Payment Process Request", "Payment Process Request Name", sourceColValue, "Payment Process Request Name", sourceColValue);
			
			
			// Click on Go Button
			//gENLIB.webClickButton("Go");
			
			// Expand Tree i.e Click Show link
			HashMap<String, String> searchColumns= new HashMap<String,String>();
			searchColumns.put(requestSourceColName, requestSourceColValue);
			
			HashMap<String, String> actions= new HashMap<String,String>();
			actions.put("displayname", "Details");
			
			wEBTABLELIB.clearWebTableObject();
			wEBTABLELIB.clickImage("Details", searchColumns, actions);
			Thread.sleep(5000);
			
			// Get Cell Data
			HashMap<String, String> searchColumns_reference= new HashMap<String,String>();
			searchColumns_reference.put(referenceSourceColName, referenceSourceColValue);
			
			HashMap<String, String> actions_reference= new HashMap<String,String>();
			actions_reference.put("displayname", "Status");
					
			wEBTABLELIB.clearWebTableObject();
			
			List<String> cellData = wEBTABLELIB.getCellData("Reference", searchColumns_reference, actions_reference);
			info("cellData123 :"+cellData.toString());
			
			int iterateCount = 0;
			while(!cellData.contains(Status)){ // targetColValue
				iterateCount++;
				
				info("Iteration Count in Refresh WebItem :"+iterateCount);
				web.button(buttonXPath).waitFor(20);
				web.button(buttonXPath).click();
				//delay(15);
				Thread.sleep(15000);
				
				//Get the Cell Value
				// Expand Tree i.e Click Show link
				HashMap<String, String> searchColumns1= new HashMap<String,String>();
				searchColumns1.put(requestSourceColName, requestSourceColValue);
				
				HashMap<String, String> actions1= new HashMap<String,String>();
				actions1.put("displayname", "Details");
				
				wEBTABLELIB.clearWebTableObject();
				wEBTABLELIB.clickImage("Details", searchColumns1, actions1);
				
				// Get Cell Data
				HashMap<String, String> searchColumns_reference1= new HashMap<String,String>();
				searchColumns_reference1.put(referenceSourceColName, referenceSourceColValue);
				
				HashMap<String, String> actions_reference1= new HashMap<String,String>();
				actions_reference1.put("displayname", "Status");
						
				wEBTABLELIB.clearWebTableObject();
				
				cellData = wEBTABLELIB.getCellData("Reference", searchColumns_reference1, actions_reference1);
				info("cellData456 :"+cellData.toString());
				
				if(iterateCount == 30){
					verified  = false;
					reportFailure("refreshWebItem : Unable to get value "+Status+" for the column status after "+(30*15)+" sec.");
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
	
	
	
	//------- START: Functions written by phani and lokesh for animesh requirement----------
	
	public void leaseOpenAccountingPeriod(String startDate, String endDate) throws Exception {
		 String startPeriod =startDate;
	        String endPeriod = endDate;
	       
	        String start[] =startPeriod.split("-");
	        String end[] =endPeriod.split("-");

	        if(toInt(start[1])<30){

	            startPeriod = "02-"+start[0]+"-"+(2000+ toInt(start[1]));
	        }else{
	            startPeriod = "02-"+start[0]+"-"+(1900+ toInt(start[1]));
	        }

	        if(toInt(end[1])<30){

	            endPeriod = "02-"+end[0]+"-"+(2000+ toInt(end[1]));
	        }else{
	            endPeriod = "02-"+end[0]+"-"+(1900+ toInt(end[1]));
	        }

	        System.err.println("startPeriod"+startPeriod);
	        System.err.println("endPeriod"+endPeriod);
	        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	        DateFormat reqFormat = new SimpleDateFormat("MMM-yy");

	        Date startdate = (Date)formatter.parse(startPeriod); ;
	        
	        System.err.println("startdate"+startdate.toString());
	        Date enddate = (Date)formatter.parse(endPeriod);
	        System.err.println("enddate"+enddate.toString());
	        Calendar calstart=Calendar.getInstance();
	        Calendar calend=Calendar.getInstance();
	        calstart.setTime(startdate);
	        calend.setTime(enddate);

	        int smonth = calstart.get(Calendar.MONTH);
	        int syear = calstart.get(Calendar.YEAR);
	        
	        int emonth = calend.get(Calendar.MONTH);
	        int eyear = calend.get(Calendar.YEAR);
	        
	        
	        
	            while((startdate.before(enddate))||((smonth == emonth)&&(syear == eyear))){


	          

	            startdate= calstart.getTime();
	            openPeriod(reqFormat.format(calstart.getTime()));
	            
	            calstart.add(Calendar.MONTH, 1);
	            
	             smonth = calstart.get(Calendar.MONTH);
		         syear = calstart.get(Calendar.YEAR);
		        
		         emonth = calend.get(Calendar.MONTH);
		         eyear = calend.get(Calendar.YEAR);
		        
	        } 
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
	
	
	public void openPeriod_obsolete(String period)throws Exception{
		
		String docPath = getCurrentWindowXPath()+"/web:document[@index='0']";
		String periodTextboxPath = getCurrentWindowXPath()+"/web:document[@index='0']/web:form[@index='0']";
		
		String periodXpath= periodTextboxPath +"/web:input_text[@id='PeriodNameLov']";

		//System.err.println("periodXpath"+periodXpath);
		
		web.textBox(periodXpath).setText(period);
		
		web.textBox(periodXpath).fireEvent("onkeyup");
		web.textBox(periodXpath).fireEvent("onchange");
		web.textBox(periodXpath).fireEvent("onchange");
		
		delay(10000);
		
		gENLIB.webSelectLOV("Search for Period Name","Period Name",period,"Period Name", period);
		
		delay(10000);
		
		/* Code for clicking and selecting LOV
		 * web.image(docPath+"/web:img[@alt='Search for Period Name']").getParent().click();
		
		delay(15000);
		
		String quickSelectImgPath=getCurrentWindowXPath()+"/web:document[@index='1']";
		//System.err.println("quickSelectImgPath"+quickSelectImgPath);
		
		web.image(quickSelectImgPath+"/web:img[@alt='Quick Select']").getParent().click();*/
				
		
		String goButtonXpath ="/web:window[@title='Accounting Periods']/web:document[@index='0']/web:form[ @index='0']/web:button[@value='Go']";
		web.button(goButtonXpath).click();		
		delay(25000);
		
		web.window("/web:window[ @title='Accounting Periods']").waitForPage(50, true);
		
		web.link(docPath+"/web:a[@id='PeriodStatusTblRN:N:0']").click();
		web.link(docPath+"/web:a[@id='PeriodStatusTblRN:N:0']").fireEvent("onclick");
		delay(20000);
			
		web.selectBox(docPath+"/web:form[ @index='0']/web:select[@id='PeriodStatusTblRN:PeriodStatusItem:0']").selectOptionByText("Open");
		web.selectBox(docPath+"/web:form[ @index='0']/web:select[@id='PeriodStatusTblRN:PeriodStatusItem:0']").fireEvent("onchange");
		delay(5000);
		web.button(docPath+"/web:form[ @index='0']/web:button[@id='ApplyBtn']").click();
		delay(10000);
	}	
	
	public void openPeriod(String period) throws Exception{
		
		String docPath = getCurrentWindowXPath()+"/web:document[@index='0']";
		String periodTextboxPath = getCurrentWindowXPath()+"/web:document[@index='0']/web:form[@index='0']";	
		String periodXpath= periodTextboxPath +"/web:input_text[@id='PeriodNameLov']";

		
		web.textBox(periodXpath).setText(period);
		
		web.textBox(periodXpath).fireEvent("onkeyup");
		web.textBox(periodXpath).fireEvent("onchange");
		web.textBox(periodXpath).fireEvent("onchange");
		
		delay(2000);
		
		gENLIB.webSelectLOV("Search for Period Name","Period Name",period,"Period Name", period);
		
		delay(5000);	
				
		gENLIB.webClickButton("Go");
				
		delay(9000);
		
		web.window("/web:window[ @title='Accounting Periods']").waitForPage(10, false);	
		
		gENLIB.webClickImage("Update");
		
		delay(8000);
		
		web.window("/web:window[ @title='Update Accounting Periods']").waitForPage(10, false);
		
		gENLIB.webSelectListBoxWithXPath(docPath+"/web:form[ @index='0']/web:select[@id='PeriodStatusTblRN:PeriodStatusItem:0']", "Open");
		
		delay(5000);
		
		gENLIB.webClickButton("Apply;0");
		
		delay(2000);
		
		web.window("/web:window[ @title='Accounting Periods']").waitForPage(10, false);
	}
	
	//------- END: Functions written by phani and lokesh for animesh requirement----------
	/* ----------------------------------------------------------- */

	//	1.
	//	Name of the Function: CheckForms
	//	Desc: Checks the existance of forms
	//	Base Location:O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Fmwklib

	public void CheckForms()throws Exception{
		try{
			if (forms.window("//forms:window[(@enabled='true')]").exists())
			{


				AbstractWindow[] strarr=forms.getAllOpenWindows();
				int val=strarr.length;
				String winname="";
				for(int icount=0;icount<=val-1;icount++)
				{
					winname=strarr[icount].getAttribute("name").toString();
					if(!winname.equalsIgnoreCase("NAVIGATOR"))
						forms.window("//forms:window[(@name='"+winname+"')]").close();;

				}
			}
		}
		catch(Exception e){

		}
	}
	//	2.
	//	Name of the Function: CloseForms
	//	Desc: This function closes Forms and Launches SSA Login page
	//	Base Location:O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Fmwklib
	/**
	 * This function closes Forms and Launches SSA Login page
	 * @return
	 * @throws Exception
	 */
	public int CloseForms()throws Exception
	{        
		int E_OK=1;


		ebs.oracle_close_all_browsers();

		ebs.navigateToURL(getVariables().get("oracle_php_url"));
		//web_browser_invoke(browser, oracle_php_url);

		if(!web.window("/web:window[@title='Login']").exists())
		{
			info("Could not return to login page.");
			E_OK=0;
			return E_OK;
		}
		getVariables().set("BaseState","");

		return(E_OK);
	}



	//	3.
	//	Name of the Function: SetBaseState
	//	Desc: This function sets the state of the application to the specified state
	//	param:	one String
	//	Base Location:O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Fmwklib


	/**
	 * This function sets the state of the application to the specified state.
	 * @param State
	 * # "<User>:<Responsibility>:<Project>"
       # "<User>:<Responsibility>"
       # "<User>"
       #  None
	 * @return 0 if successful. Negative Integer if fails.
	 * @throws Exception
	 */
	public int SetBaseState(String State)throws Exception{

		String OldState[];
		String NewState[];
		String TempState="",TemplateFlag="",TempPrj="";
		int OldLen, NewLen, TempLen;
		int rc=-1;
		OldState=getvariable.get("BaseState").split(":");
		NewState=State.split(":");
		if((NewState[3]).substring(0, 1)=="~")
		{
			NewState[3]=(NewState[3]).substring(1, (NewState[3]).length()-1);
			TemplateFlag="Y"; 
		}


		OldLen=OldState.length;
		NewLen=NewState.length;
		TempPrj=ReplaceIndex(NewState[3]);
		NewState[3]=TempPrj;
		if(OldState[1]==NewState[1]&&OldState[2]==NewState[2]&&OldState[3]==NewState[3])
		{

			if(NewLen==3 && NewState[3]=="")
			{
				NewLen=2;
			}
			switch (NewLen)
			{
			case 0:
				rc=0;
				break;
			case 1: 
			case 2:
				if(!web.window("/web:window[@title='Oracle Applications Home Page']").exists())
					web.link("/web:window[(@enabled='true')]/web:document[@index='0']/web:a[@text='"+getVariables().get("NavURL")+"']").click();
				rc=SetResponsibility(NewState[2],0);

				break;
			case 3:

				if(web.window("/web:window[@title='Oracle Applications Home Page']").exists())
				{
					//web.window("/web:window[@title='Oracle Applications Home Page']");
					if(web.link("/web:window[@title='Oracle Applications Home Page']/web:document[@index='0']/web:a[@text='"+NewState[3]+"']").exists()==true)
					{
						//set_window("Oracle Applications Home Page",TimeOut);
						web.link("/web:window[@title='Oracle Applications Home Page']/web:document[@index='0']/web:a[@text='"+NewState[3]+"']").click();
						//web_link_click("{ class: object, MSW_class: html_text_link, html_name: \""&NewState[3]&"\", location: 0}");

					}
				}
				else

					if(TemplateFlag.equalsIgnoreCase("Y"))
					{
						if(web.window("/web:window[@index='0' or @title='Project Template Setup']").exists())
						{
							//set_window("Project Template Setup",TimeOut);
							rc=0;
						}
						else
						{
							if(web.link("/web:window[(@enabled='true')]/web:document[@index='0']/web:a[@text='Project Template Setup']").exists())
							{

								web.link("/web:window[(@enabled='true')]/web:document[@index='0']/web:a[@text='Project Template Setup']").click();
								rc=0;
							}
							else
							{
								info("Could not return to Project Template Setup Page");
								rc=-1;
							}
						}
					}
					else
					{
						if(web.link("/web:window[(@enabled='true')]/web:document[@index='0']/web:a[@text='Project']").exists()){
							web.link("/web:window[(@enabled='true')]/web:document[@index='0']/web:a[@text='Project']").click();
							rc=0;
						}

						//if(obj_exists("{ class: object, MSW_class: html_text_link, html_name: \""&"SelectedHome"&"\", location: 0}")==E_OK)
						//{

						//rc=web_link_click("{ class: object, MSW_class: html_text_link, html_name: \""&"SelectedHome"&"\", location: 0}");

						//}
						//else 
						if(web.link("/web:window[(@enabled='true')]/web:document[@index='0']/web:a[@text='Home']").exists())
						{
							//set_window("Home",TimeOut);

							//web_event("{ class: object, MSW_class: html_text_link, html_name: \""&"Home"&"\", location: 1}","click");
							web.link("/web:window[(@enabled='true')]/web:document[@index='0']/web:a[@text='Home']").click();
						}
					}
				return(rc);
			}
		}
		else
		{
			switch (NewLen)
			{
			case 0:
				rc=LogOut(OldLen);
				if(rc==0)getvariable.set("BaseState","");
				break;
			case 1:
				rc=SetUser(OldState, NewState);
				if(rc==0)getvariable.set("BaseState",NewState[1]);
				else return(rc);
				break;
			case 2:
				if(OldState[1]!=NewState[1])
				{
					rc=SetUser(OldState, NewState);
				}
				else
				{

					if(web.window("/web:window[@title='Oracle Applications Home Page']").exists())
						web.link("/web:window[@title='Oracle Applications Home Page']/web:document[@index='0']/web:a[@text='"+getVariables().get("NavURL")+"']").click();

				}

				web.link("/web:window[@title='Oracle Applications Home Page']/web:document[@index='0']/web:a[@text='"+NewState[2]+"']").click();
				rc=0;
				Thread.sleep(2000);
				if(rc==0)getvariable.set("BaseState",NewState[1]+":"+NewState[2]);
				else return(rc);
				break;
			case 3:

				if(OldState[1]!=NewState[1])
				{
					rc=SetUser(OldState, NewState);
					TempState=NewState[1];
				}

				if(OldState[2]!=NewState[2])
				{

					if(web.window("/web:window[@title='Oracle Applications Home Page']").exists())
						web.link("/web:window[@title='Oracle Applications Home Page']/web:document[@index='0']/web:a[@text='"+getVariables().get("NavURL")+"']").click();
					rc=SetResponsibility(NewState[2], 1);
					Thread.sleep(2000);
					if(rc==0)TempState=NewState[1]+":"+NewState[2];else return(rc);
				}
				else if(OldState[2]!=NewState[2]||OldState[1]!=NewState[1])
				{
					rc=SetResponsibility(NewState[2], 1);
					Thread.sleep(2000);				
					if(rc==0)TempState=NewState[1]+":"+NewState[2];else return(rc);
				} 
				if(TempState.equalsIgnoreCase(NewState[1]+":"+NewState[2])&& !NewState[3].equalsIgnoreCase("")) 
				{

					if(web.link("/web:window[@title='Oracle Applications Home Page']/web:document[@index='0']/web:a[@text='"+NewState[3]+"']").exists())
					{
						web.link("/web:window[@title='Oracle Applications Home Page']/web:document[@index='0']/web:a[@text='"+NewState[3]+"']").click();

					}
					else

						if(TemplateFlag=="Y")
						{
							rc=getScript("Sslib").callFunction("TemplateHome",NewState[3]).hashCode();
						}
						else
						{
							rc=getScript("Sslib").callFunction("ProjectHome",NewState[3]).hashCode();
						}
				}
				else 
					if(TempState=="")
					{

						if(web.window("/web:window[@title='Oracle Applications Home Page']").exists())
							web.link("/web:window[@title='Oracle Applications Home Page']/web:document[@index='0']/web:a[@text='"+getVariables().get("NavURL")+"']").click();


						//if(objExists(NewState[3]) == E_OK){

						web.link("/web:window[@title='Oracle Applications Home Page']/web:document[@index='0']/web:a[@text='"+NewState[3]+"']").click();

					}

					else
						if(TemplateFlag=="Y")
						{
							rc=getScript("Sslib").callFunction("TemplateHome",NewState[3]).hashCode();
						}
						else
						{
							rc=getScript("Sslib").callFunction("ProjectHome",NewState[3]).hashCode();
						}

			}

			if(rc==0)
				if(TemplateFlag=="Y")
					getvariable.set("BaseState",NewState[1]+":"+NewState[2]+":~"+NewState[3]);
				else
					getvariable.set("BaseState",NewState[1]+":"+NewState[2]+":"+NewState[3]);			
			else return(rc);

		}


		return(rc);	


	}


	public int SetResponsibility(String RespName ,int Project) throws Exception
	{
		int rc=1;


		if(web.window("/web:window[@title='Oracle Applications Home Page']").exists()){
			rc=0;
		}

		ebs.oracle_navigator_select(RespName);

		return(rc);
	}

	public int LogOut(int State) throws Exception{

		if(web.link("/web:window[(@enabled='true')]/web:document[@index='0']/web:a[@text='Logout']").exists())
		{
			web.link("/web:window[(@enabled='true')]/web:document[@index='0']/web:a[@text='Logout']").click();
		}
		switch (State)
		{
		case 0:
			return(0);

		case 1:
			ebs.oracle_close_all_browsers();


			ebs.navigateToURL(getVariables().get("oracle_php_url"));

			web.window("/web:window[(@enabled='true')]").maximize();
			return(0);

		}
		return(-1);
	}

	public int SetUser(String OldState[],String NewState[]) throws Exception{
		int rc=-1;


		if(OldState[1]==NewState[1])
		{
			if(OldState[2]!="")
			{
				web.image("/web:window[@title='Oracle Applications Home Page']/web:image[@text='"+getVariables().get("NavURL")+"']").click();// chane this
				rc=0;
			}
		}
		else
		{
			if(OldState[1]!="")
			{
				rc=LogOut(1);

				rc=LogIn(NewState[1]);
			}
		}
		return(rc);

	}
	public int LogIn(String User) throws Exception{
		initEBS();
		String UidPwd[];
		int Counter,rc=-1;
		DOMBrowser WindowName;
		String ActualUsernameField,ActualPasswordField,UsernameField1,UsernameField2,PasswordField1,PasswordField2;



		Thread.sleep(2000);
		UidPwd=User.split("~");

		info("inside login");
		WindowName=web.getFocusedWindow();


		if(WindowName.getAttribute("title").equalsIgnoreCase( "Login - Microsoft Internet Explorer"))	 
		{
			for(Counter = 144; Counter<=144; Counter++)//#	For a Period of Twenty Four Hours The Env is Checked(In this case)...
			{				 


				//EBSLibrary.oracle_close_all_browsers();
				ebs.oracle_close_all_browsers();
				//EBSLibrary.oracle_launch_php_url();
				//.oracle_close_all_browsers();
				ebs.navigateToURL(getVariables().get("oracle_php_url"));

				Thread.sleep(2000);

				WindowName=web.getFocusedWindow();			

				if(WindowName.getAttribute("title").equalsIgnoreCase("Login - Microsoft Internet Explorer") || WindowName.getAttribute("title").equalsIgnoreCase("Login - Windows Internet Explorer"))//#"Cannot find server - Microsoft Internet Explorer" || WindowName == "500 Internal Server Error - Microsoft Internet Explorer" || win_exists("HTTP 500 Internal server error")==E_OK)
				{


					WindowName.maximize();
					Thread.sleep(2000);	

					break;										 
				}
				else Thread.sleep(2000);	


			}	 
		}	




		//#Added to handle the OAM login for adv config testing

		if(web.window("/web:window[ @title='Login']").exists())
		{
			web.textBox("/web:window[@title='Login']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName']/web:input_text[@id='unamebean' or @name='usernameField']").setText(UidPwd[0]);



			web.textBox("/web:window[ @title='Login']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName']/web:input_password[@id='pwdbean' or @name='passwordField']").setText(UidPwd[1]);	 

			web.button("/web:window[ @title='Login']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='SubmitButton' or @value='Login']").click();
			if(web.window("/web:window[@title='Oracle Applications Home Page']").exists()) rc=0;
			Thread.sleep(2000); 
		}
		/*#Added to handle the OAM login for adv config testing
			 else
			 {
					UsernameField1 = "{ class: object, MSW_class: html_edit, html_name: \""&"ssousername"&"\", location: 0}";
					PasswordField1 = "{ class: object, MSW_class: html_edit, html_name: \""&"password"&"\", location: 0}";
					UsernameField2 = "{ class: object, MSW_class: html_edit, html_name: \""&"usernameField"&"\", location: 0}";
					PasswordField2 = "{ class: object, MSW_class: html_edit, html_name: \""&"passwordField"&"\", location: 0}";				

				set_window("Login",TimeOut*20);		 
					if(obj_exists(UsernameField1)==E_OK)
						ActualUsernameField = UsernameField1; 
					else 
						if(obj_exists(UsernameField2)==E_OK)
							ActualUsernameField = UsernameField2; 		 

				if(obj_exists(PasswordField1)==E_OK) 
					ActualPasswordField = PasswordField1; 
				else 
					if(obj_exists(PasswordField2)==E_OK)
							ActualPasswordField = PasswordField2; 

				 set_window("Login",TimeOut*20);
				 rc=edit_set(ActualUsernameField, UidPwd[1]);

				 set_window("Login",TimeOut*20);	 
				 rc=edit_set(ActualPasswordField, UidPwd[2]);

				 set_window("Login",TimeOut*20);	 
				 rc=button_press("{class: push_button,MSW_class: html_push_button,html_name: \""&"Login"&"\", location: 0}");
				 web_sync(TimeOut*20); 
			}*/

		return(rc);

	}
	public void initEBS()throws Exception{
		browser.launch();
		ebs = new EBSBrowser(this);
		new Initialize (web,browser,forms,this,SYNCTIME);
	}


	//	4.
	//	Name of the Function: ReplaceIndex
	//	Desc: Replaces with index
	//	param:	one String
	//	Base Location:O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Fmwklib
	/**
	 * Replaces ** with Index
	 * @param strString
	 * @return
	 */
	public String ReplaceIndex(String strString)throws Exception
	{

		if(strString.substring(0, 2)=="**")
		{
			return(getVariables().get("Index")+strString.substring(2, strString.length()-2)); 
		}
		else
		{
			return(strString);
		}

	}




	//	5.
	//	Name of the Function: FormatStdDate 
	//	Desc: Changes the given date to desired format
	//	param:	2 (String, String)
	//	Base Location:O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	@SuppressWarnings("unchecked") public String FormatStdDate (String Date,String Format)throws Exception
	{
		HashMap months;
		String DateArray[];
		String val;


		months	=	 new HashMap();

		months.put("01", "JAN");
		months.put("1", "JAN");
		months.put("02", "FEB");
		months.put("2", "FEB");
		months.put("03", "MAR");
		months.put("3", "MAR");
		months.put("04", "APR");
		months.put("4", "APR");
		months.put("05", "MAY");
		months.put("5", "MAY");
		months.put("06", "JUN");
		months.put("6", "JUN");
		months.put("07", "JUL");
		months.put("7", "JUL");
		months.put("08", "AUG");
		months.put("8", "AUG");
		months.put("09", "SEP");
		months.put("9", "SEP");
		months.put("10", "OCT");
		months.put("11", "NOV");
		months.put("12", "DEC");


		if (Format.equalsIgnoreCase("DD-MON-YYYY"))
		{
			System.out.println(Date);
			return (Date);

		}
		else if (Format.equalsIgnoreCase("MM/DD/YYYY"))
		{
			DateArray =Date.split("/");
			val = (DateArray[1] + "-" +  months.get(DateArray[0]) + "-" + DateArray[2]);
			System.out.println(val);
			return(val);

		}
		else if (Format.equalsIgnoreCase("DD/MM/YYYY"))
		{
			DateArray =Date.split("/");
			val = (DateArray[0] + "-" + months.get(DateArray[1])+ "-" + DateArray[2]);
			System.out.println(val);
			return(val);

		}


		return "not a valid format";
	}


	//	6.
	//	Name of the Function: IsNumber
	//	Desc: Verifies whether given parameter is a number or not
	//	param:	one String
	//	Base Location:O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib


	public String IsNumber(String TestVar)throws Exception {

		try { 
			int x = Integer.parseInt(TestVar); 
			System.out.println("integer"); 
			return "Integer";
		} 
		catch(NumberFormatException nFE) { 
			System.out.println("String"); 
			return "String";
		} 

	}




	//	7.
	//	Name of the Function: CompareValue
	//	Desc: Compare the actual value with expected value
	//	param:	2(int ,int)
	//	Base Location:O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib
	public boolean CompareValue(int ActValue, int ExpValue)throws Exception 

	{

		int HiLimit, LowLimit;

		HiLimit=ExpValue+1;
		LowLimit=ExpValue;

		if(ActValue>=LowLimit&&ActValue<=HiLimit)
		{
			return true;
		}
		else if(ActValue-ExpValue>=-1&&ActValue-ExpValue<=1)
		{
			return true;
		}
		else
		{
			return false;
		}

	}


	//8.
	//Name of the Function: GetNextNumber
	//Desc: Gives the next number
	//param:	one String
	//Base Location:O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib
	public String GetNextNumber(String CurrentNumber)throws Exception 
	{
		int Number;
		int NewNumber;
		String NextNumber;
		int LastNumber;

		String str1=CurrentNumber.substring(0,2);
		System.out.println(str1);
		String str2=CurrentNumber.substring(2);
		LastNumber=Integer.parseInt(str2);
		Number = Integer.parseInt(str1);
		NewNumber=Number+1;

		if(NewNumber < 10)
		{
			NextNumber = "0"+NewNumber+LastNumber;
			//NextNumber=Integer.parseInt(str3);
			System.out.println(NextNumber);

		}
		else
		{
			NextNumber = Integer.toString(NewNumber)+Integer.toString(LastNumber);
			//NextNumber=Integer.parseInt(str4);
			System.out.println(NextNumber);

		}  
		return NextNumber;
	}


	//9.
	//Name of the Function: getRequestId
	//Desc: Takes the source and returns concurrent request id (source can be Note Window/Decision Window/Status bar)
	//param:	2(String, String)
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	public String getRequestId(String Source,String Request)throws Exception
	{
		String req_id;
		String req_text;
		//1. Source parameter option
		if (Source.equalsIgnoreCase("W_DECISION"))
		{
			forms.choiceBox("//forms:choiceBox").setFocus();
			req_text= forms.choiceBox("//forms:choiceBox").getAlertMessage();
			info("message"+req_text);
			String r_id = req_text.substring(req_text.indexOf("=")+1,req_text.indexOf(")"));
			req_id = r_id.trim();
			System.out.println("Request ID :"+req_id);
			if (Request.equalsIgnoreCase("P_REQUEST_YES"))
			{
				return req_id;
			}
			else if(Request.equalsIgnoreCase("P_REQUEST_NO"))
			{
				return req_text;
			}
			else
			{
				warn("Invalid Request Option");
				return "-1";
			}
		}
		else if (Source.equalsIgnoreCase("W_NOTE"))
		{
			forms.choiceBox("//forms:choiceBox").setFocus();
			req_text= forms.choiceBox("//forms:choiceBox").getAlertMessage();
			info(req_text);
			String tmp[];
			tmp=req_text.split("is");
			String r_id = tmp[1];
			req_id = r_id.trim();
			System.out.println("Request ID :"+req_id);
			if (Request.equalsIgnoreCase("P_REQUEST_YES"))
			{
				return req_id;
			}
			else if(Request.equalsIgnoreCase("P_REQUEST_NO"))
			{
				return req_text;
			}
			else
			{
				warn("Invalid Request Option");
				return "-1";
			}
		}
		else if (Source.equalsIgnoreCase("W_STATUS_BAR"))
		{

			req_text= forms.getStatusBarMessage();
			info(req_text);
			String tmp[];
			tmp=req_text.split("is");
			String r_id = tmp[1];
			req_id = r_id.trim();
			System.out.println("Request ID :"+req_id);
			if (Request.equalsIgnoreCase("P_REQUEST_YES"))
			{
				return req_id;
			}
			else if(Request.equalsIgnoreCase("P_REQUEST_NO"))
			{
				return req_text;
			}
			else
			{
				warn("Invalid Request Option");
				return "-1";
			}
		}
		else 
		{

			warn("Invalid Source Option");
			return "-1";	
		}

	}




	//10.
	//Name of the Function: FindInArray
	//Desc: Finds an item in the given array
	//param:	2 ( String array, String)
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	public boolean FindInArray(String FindArray[], String Item)throws Exception
	{ 

		int i;
		int Count=0;

		Count=FindArray.length;
		for(i=0;i<Count;i++)
		{
			if(FindArray[i].equalsIgnoreCase(Item))
			{
				return true;
			}

		}
		return false;
	}




	//11.
	//Name of the Function: ClearArray
	//Desc:	Clears the array
	//param:	1 (String array)
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib
	public String[] ClearArray(String Array[])throws Exception
	{
		int i; 
		int Count= Array.length;

		for(i = 0; i < Count; i++)
		{
			Array[i]=null;

		}
		return Array;	
	}

	//12.
	//Name of the Function: RPad
	//Desc:	Returns the Rpad value
	//param:	3 (String, int, String)
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib


	public String RPad(String Str, int Len, String PadStr)throws Exception 
	{
		int i; String NewStr;
		NewStr = Str;

		while(Len>NewStr.length())
		{
			NewStr = NewStr + PadStr;
		}
		if (NewStr.length()>Len)
		{
			NewStr = NewStr.substring(0,Len);
		}
		return(NewStr);
	} 

	//13.
	//Name of the Function: FormatDate 
	//Desc:
	//param:	2(String, String)
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	@SuppressWarnings("unchecked") public String FormatDate (String Date,String Format)throws Exception
	{
		info("format date");  
		HashMap months;
		String DateArray[];
		String val;

		months	=	 new HashMap();

		//	months.put("JAN", "01");
		//	months.put("FEB", "02");
		//	months.put("MAR", "03");
		//	months.put("APR", "04");
		//	months.put("MAY", "05");
		//	months.put("JUN", "06");
		//	months.put("JUL", "07");
		//	months.put("AUG", "08");
		//	months.put("SEP", "09");
		//	months.put("OCT", "10");
		//	months.put("NOV", "11");
		months.put("01", "JAN");
		months.put("1", "JAN");
		months.put("02", "FEB");
		months.put("2", "FEB");
		months.put("03", "MAR");
		months.put("3", "MAR");
		months.put("04", "APR");
		months.put("4", "APR");
		months.put("05", "MAY");
		months.put("5", "MAY");
		months.put("06", "JUN");
		months.put("6", "JUN");
		months.put("07", "JUL");
		months.put("7", "JUL");
		months.put("08", "AUG");
		months.put("8", "AUG");
		months.put("09", "SEP");
		months.put("9", "SEP");
		months.put("10", "OCT");
		months.put("11", "NOV");
		months.put("12", "DEC");



		DateArray =Date.split("-");


		//1.Format "DD-MON-YYYY" 
		if (Format.equalsIgnoreCase("DD-MON-YYYY"))
		{
			if (Date.contains("/"))
			{
				DateArray =Date.split("/");	
			}
			int len=(DateArray[2]).length();
			if (len<3)
			{
				DateArray[2]= "20"+DateArray[2];
			}
			if (IsNumber(DateArray[1]).equalsIgnoreCase("String"))
			{
				val = (DateArray[0] + "-" + (DateArray[1]) + "-" + (DateArray[2]));
				System.out.println(val);	
			}
			else
			{
				val = (DateArray[0] + "-" +  months.get(DateArray[1]) + "-" + (DateArray[2]));
				System.out.println(val);
			}
			return (val);

		}
		//2.Format "DD-MM-YY"
		else if (Format.equalsIgnoreCase("DD-MM-YY"))
		{

			val = (DateArray[0] + "-" +  months.get(DateArray[1]) + "-" + (DateArray[2].substring(2,4)));
			System.out.println(val);
			return(val);

		}
		//3.Format "MM/DD/YY"
		else if (Format.equalsIgnoreCase("MM/DD/YY"))
		{
			val =  (months.get(DateArray[1])+ "/" + DateArray[0] + "/" + (DateArray[2].substring(2,4)));
			System.out.println(val);
			return(val);

		}
		//4.Format "MM/DD/YYYY"
		else if (Format.equalsIgnoreCase("MM/DD/YYYY"))
		{
			val =  (months.get(DateArray[1])+"/"+ DateArray[0] + "/" + DateArray[2]);
			System.out.println(val);
			return(val);

		}
		//5.Format "DD/MM/YY"
		else if (Format.equalsIgnoreCase("DD/MM/YY"))
		{

			val =  (DateArray[0] + "/" + months.get(DateArray[1])+ "/" + DateArray[2].substring(2,4));
			System.out.println(val);
			return(val);

		}
		//6.Format "DD/MM/YYYY"
		else if (Format.equalsIgnoreCase("DD/MM/YYYY"))
		{
			val =  (DateArray[0] + "/" + months.get(DateArray[1])+ "/" + DateArray[2]);
			System.out.println(val);
			return(val);

		}
		//7.Format "YYYY-MM-DD"
		else if (Format.equalsIgnoreCase("YYYY-MM-DD"))
		{
			DateArray =Date.split("-");
			val =  (DateArray[2]+"-"+ months.get(DateArray[1])+ "-" + DateArray[0]);
			System.out.println(val);
			return(val);

		}
		return "not a valid format";
	}


	//14.
	//Name of the Function: StrLtrim
	//param:	one String
	//Desc: trims the given value
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	public String StrLtrim(String value)throws Exception
	{

		String strtrim= value.trim();
		return strtrim; 

	}


	//15.
	//Name of the Function: GetRequestID
	//Desc: gets the request id
	//param:	one String
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	public String GetRequestID (String Text)throws Exception
	{
		char req_text[]=Text.toCharArray();
		String Req_id= new String();
		int x =req_text.length;
		for (int i=0;i<x;i++)
		{
			if ((int)req_text[i]>=48&& (int)req_text[i]<=57)
			{
				Req_id=Req_id+req_text[i];
			}
		}
		return Req_id;

	}


	//16.
	//Name of the Function: GetUniqueIndex
	//Desc: gets two letters index
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	public String GetUniqueIndex()
	{
		String NewStr=new String();
		System.out.println(NewStr);
		Long time=System.currentTimeMillis();
		String STime1=time.toString();
		/*char[]STime=STime1.toCharArray();
	int Slength=STime.length;
	for (int i = 1; i< Slength ; i++)
	{
		 if((int)STime[i]>=64&& (int)STime[i]<=128)
			continue;

		if ((STime[i] != ':') && (STime[i] != '-')&& (STime[i]!= ' ') && (STime[i]!= '.')&& (STime[i])!= ',')
		{
			NewStr =NewStr + STime[i];                       // substr(STime,i,1) ;

		}
	}*/
		return STime1.substring(STime1.length()-2);
	}


	//17.
	//Name of the Function: GetNumber
	//Desc: gets number out of given String
	//param:	1 (String)
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	public int GetNumber (String Text)
	{
		String Text1=new String();
		int num;
		if(Text.indexOf(",")>=0)
		{
			Text1=Text.replaceAll(",", "");
		}
		if(Text1.indexOf("<")>=0)
		{
			num=(-1)*Integer.parseInt(Text1.substring(2,(Text1.length()-2)));
		}
		else
			num=Integer.parseInt(Text1);
		return num;
	}



	//18.
	//Name of the Function: Get_Add_Sub_Date
	//Desc: adds or substracts to the given date
	//param:	3(String, String, int)
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	public String Get_Add_Sub_Date(String date,String Add_Type, int Value) throws Exception
	{
		String Month[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};   
		String Temp_Day,Temp_Month, Temp_Year, Modified_Day,Modified_Month,Modified_year;
		String[] dateInformation=date.split("-");
		Calendar calendar=Calendar.getInstance();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		int monthNumber=0;
		Temp_Day=dateInformation[0];
		Temp_Month=dateInformation[1];
		Temp_Year=dateInformation[2];
		for(int i=0;i<Month.length;i++)
		{	System.out.println(Month[i]+ i);
		if(Month[i].equalsIgnoreCase(Temp_Month))
		{
			monthNumber=i;
			System.out.println(monthNumber);
			break;
		}
		}
		calendar.set(Integer.parseInt(Temp_Year),monthNumber,Integer.parseInt(Temp_Day));



		if("DD".equalsIgnoreCase(Add_Type))
			calendar.add(Calendar.DATE, Value);
		else 
			if("MM".equalsIgnoreCase(Add_Type))
				calendar.add(Calendar.MONTH,Value);
			else
				if("YYYY".equalsIgnoreCase(Add_Type))
					calendar.add(Calendar.YEAR,Value);
				else
					info("please enter correct add_dateType to modify (DD or MM or YYYY)");


		String[] ModifiedDateInfo=(dateFormat.format(calendar.getTime()).toString()).split("/");
		Modified_Day=ModifiedDateInfo[0];
		Modified_Month=Month[(Integer.parseInt(ModifiedDateInfo[1])-1)];
		Modified_year=ModifiedDateInfo[2];
		return(Modified_Day+"-"+Modified_Month+"-"+Modified_year);
	}



	//20.
	//Name of the Function: GetObjVal
	//Desc: gets the value from excel
	//param:	3 (String,String, String)
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	/**
	 * GetObjVal
	 * @param Object
	 * @param Step
	 * @param Instance
	 * @return
	 * @throws Exception
	 */
	public String GetObjVal(String Object,String Step,String Instance) throws Exception{
		info("get objval object is :- "+Object);    
		ddt		=	new DDT(datatable);	
		String TestTable=getVariables().get("TestTable");
		info("Test Table is "+TestTable);
		int TableRow=1, TableRowCount;
		String ReturnValue="";

		ddt.importSheet(TestTable, "Sheet1");
		TableRowCount=ddt.getRowCount(TestTable, "Sheet1");

		for(TableRow=1;TableRow<=TableRowCount;TableRow++)
		{
			ddt.setCurrentRow(TestTable, "Sheet1", TableRow);
			info("Step from sheet is "+ddt.getValue(TestTable,"Sheet1",  "Step"));
			if(ddt.getValue(TestTable,"Sheet1", "Object").equalsIgnoreCase(Object))
			{
				info("Inside if");
				if(ddt.getValue(TestTable,"Sheet1",  "Step").equalsIgnoreCase(Step))
				{
					//if(ddt.getValue(TestTable,"Sheet1",  "Instance").equalsIgnoreCase(Instance))

					ReturnValue=ddt.getValue(TestTable,"Sheet1", "Value");
					break;
				}

			}
		}

		if(ReturnValue.equalsIgnoreCase(""))
		{
			info("Value Not Found for "+Object);
			return("Object Value not found");
		}
		else
			return(ReturnValue);

	}



	//21.
	//Name of the Function: GetMessage
	//Desc: Takes the source and returns message(source can be  Note Window/Decision Window/Status bar)
	//param:	2 (int, int)
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	/**
	 * GetMessage
	 * @param Source
	 * @param Request
	 * @return
	 * @throws Exception
	 */
	public String GetMessage(int Source, int Request) throws Exception{


		String Message="";
		int RequestID=0; 
		int TimeOut;
		String CurrentWindow,ReqID;
		//CurrentWindow = GUI_get_window();

		TimeOut = 60;

		if(Source == -7001)
		{
			//set_window("Decision", TimeOut);
			Message=forms.textField("//forms:textField[(@name='Request submitted')]").getAttribute("value"); 
			RequestID = 1*Integer.valueOf(Message.substring(Message.indexOf("=")+2-1));
		}
		else if(Source == -7002)
		{
			//set_window("Note", TimeOut);
			Message=forms.textField("//forms:textField[(@name='NOTE_MESSAGE')]").getAttribute("value"); 
			RequestID = 1*Integer.valueOf(Message.substring(Message.indexOf("is")+2-1)); 


		}
		else if(Source == -7003)
		{

			think(20);	
			Message=forms.getStatusBarMessage(); 

			if(Message == "null"){
				Message = "";
			}

			//else if(Message.length()>1)RequestID = 1*Integer.valueOf(Message.substring(Message.indexOf("=")+1-1)); 


		}
		else
		{
			info("Invalid Source - GetMessage()"); 
			/*Not found:set_window(CurrentWindow)*/; 
			return("-10001"); 
		}

		if(Request == -7004)
		{
			info(Message);
			if(Message.length()>1)
				RequestID = Integer.valueOf(Message.substring((Message.indexOf("=")+1), Message.length()-1).trim());

			ReqID=Integer.toString(RequestID);
			return(ReqID);
		}
		else if(Request == -7005) 
		{

			return(Message);
		}else if(Request == -7007) 
		{

			return(Message);
		}
		else
		{
			info("Invalid Request Option - GetMessage()");

			return("-10001");
		}


	}



	//22.
	//Name of the Function: SetObjVal
	//Desc: sets the value into excel
	//param:	4 (String,String, String, String)
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	public int SetObjVal(String Object,String Value,String Step,String Instance) throws Exception{
		ddt		=	new DDT(datatable);	
		String TestTable= getVariables().get("TestTable");

		int TableRow=1,TableRowCount, rc;
		int ReturnValue=-1;

		ddt.importSheet(TestTable, "Sheet1",true,false);

		TableRowCount=ddt.getRowCount(TestTable, "Sheet1");
		info("Row Count :"+TableRowCount);

		//	Check for matching rows and replace value


		for(TableRow=1;TableRow<=TableRowCount;TableRow++)
		{
			ddt.setCurrentRow(TestTable,"Sheet1", TableRow);

			info("value at  :"+TableRow +" is :"+ddt.getValue(TestTable,"Sheet1", "Object"));
			if(ddt.getValue(TestTable,"Sheet1", "Object").equalsIgnoreCase(Object))
			{
				if(ddt.getValue(TestTable,"Sheet1",  "Step").equalsIgnoreCase(Step))
				{
					//if(ddt.getValue(TestTable,"Sheet1", "Instance")==Instance)  // Included ReturnValu=0; in the if condition

					ddt.setValue(TestTable,"Sheet1", "Value", Value);

					ReturnValue = 0;

				}
			}
		}

		//	If row dows not match with existing rows, create a new row

		if(ReturnValue != 0)
		{
			ddt.setCurrentRow(TestTable,"Sheet1", TableRowCount+1);
			//	Balakrishna			: 05-Jan-2006
			ddt.setValue(TestTable,"Sheet1", TableRowCount+1, "Object", Object);
			ddt.setValue(TestTable,"Sheet1", TableRowCount+1, "Value", Value);
			ddt.setValue(TestTable,"Sheet1", TableRowCount+1, "Step", Step);
			ddt.setValue(TestTable,"Sheet1", TableRowCount+1, "Instance", Instance);
			ReturnValue = 0;
		}

		ddt.exportSheet(TestTable);

		//ddt.exportSheet(TestTable);
		return(ReturnValue);


	}



	//23.
	//Name of the Function: getGlobalPrameter
	//param:	2 (String, int)
	//Desc: gets the global parameters  based on the given index eg: param1, param2, param3  and index is 3  the the returns value would be param3
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\Genlib

	public String getGlobalPrameter(String parameter,int index){
		String value="";
		String s[]=getVariables().get(parameter).split(",");
		try{
			value=s[index];
		}catch(ArrayIndexOutOfBoundsException e){
			value="";
		}
		return value;
	}


	//24.
	//Name of the Function: RunQuery
	//Desc: performs query run in Forms
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	public void RunQuery()throws Exception{

		if(forms.getStatusBarMessage().equalsIgnoreCase("Enter a query;  press Ctrl+F11 to execute, F4 to cancel.")){

			//String strkey="{F11}";
			//String VBSFilePath="O:\\EBS QA\\WR2OATS\\OATS_Script_manual\\VBS\\Execute.vbs";
			/*Run query*/
			forms.window("//forms:window[(@enabled='true')]").setFocus();

			boolean b =forms.window("//forms:window[(@enabled='true')]").isMenuEnabled("View|Query By Example|Run");
			if (b)
			{
				forms.window("//forms:window[(@enabled='true')]").selectMenu("View|Query By Example|Run");
			}
			else
			{
				forms.window("//forms:window[(@enabled='true')]").activate(true);

				forms.window("//forms:window[(@enabled='true')]").invokeSoftKey("EXECUTE_QUERY");

			}


			//Process P =Runtime.getRuntime().exec("wscript " + VBSFilePath + " KeyPress " + strkey);

		}
		else
		{
			info("Forms are not ready to run query");
		}

	}








	//25.
	//Name of the Function: EnterQuery
	//Desc: performs query enter in Forms
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	/**
	 *  Prepares the form for entering the query
	 * @param Win_Name
	 * @throws Exception
	 */
	public void EnterQuery()throws Exception{

		/*Enter query*/
		forms.window("//forms:window[(@enabled='true')]").setFocus();
		boolean b =forms.window("//forms:window[(@enabled='true')]").isMenuEnabled("View|Query By Example|Enter");
		if (b)
		{
			forms.window("//forms:window[(@enabled='true')]").selectMenu("View|Query By Example|Enter");
		}
		else
		{
			forms.window("//forms:window[(@enabled='true')]").activate(true);
			forms.window("//forms:window[(@enabled='true')]").invokeSoftKey("ENTER_QUERY");

		}
		if(forms.getStatusBarMessage().equalsIgnoreCase("Enter a query;  press Ctrl+F11 to execute, F4 to cancel.")){
			info("Forms are ready to run query"); 
		}else{
			info("Forms enter query process in not successfull"); 
		}

	}








	//26.
	//Name of the Function: CancelQuery
	//Desc: performs query cancel in Forms
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	/**
	 * Cancels the query
	 * @param Win_Name
	 * @throws Exception
	 */

	public void CancelQuery()throws Exception{

		String StatusMsg=forms.getStatusBarMessage();
		if(StatusMsg.equalsIgnoreCase("Enter a query;  press Ctrl+F11 to execute, F4 to cancel.")||StatusMsg.equalsIgnoreCase("FRM-40301: Query caused no records to be retrieved. Re-enter."))
		{
			/*Enter query*/
			forms.window("//forms:window[(@enabled='true')]").activate(true);
			boolean c= forms.window("//forms:window[(@enabled='true')]").isMenuEnabled("View|Query By Example|Cancel");
			if (c)
			{
				forms.window("//forms:window[(@enabled='true')]").selectMenu("View|Query By Example|Cancel");
			}
			else
			{
				forms.window("//forms:window[(@enabled='true')]").activate(true);
				forms.window("//forms:window[(@enabled='true')]").invokeSoftKey("EXIT");	 
			}
		}
		else
		{
			info("Forms are not in a status to cancel query"); 
		}

	}


	//27.
	//Name of the Function: ShowField
	//Desc: performs ShowField for given field name in Forms
	//param:	1 (String)
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	/**
	 * Shows a field
	 * @param Win_Name
	 * @param FieldName
	 * @throws Exception
	 * @return 1 if successful and 0 for fails

	 */
	public int ShowField(String FieldName)throws Exception{
		info("show field function");
		int Return_Val=0;
		String val="";
		/*Select Show Field option from menu*/
		forms.window("//forms:window[(@enabled='true')]").selectMenu("Folder|Show Field...");//ex=(@name='PROJECT_FOLDER')

		Thread.sleep(2000);
		if(forms.listOfValues("//forms:listOfValues").exists()){
			forms.listOfValues( "//forms:listOfValues").find(FieldName+"%");
			String ItemCount=forms.listOfValues( "//forms:listOfValues").getAttribute("itemCount").toString();
			if(ItemCount.equalsIgnoreCase("0"))
			{
				info("no items found");
				forms.listOfValues("//forms:listOfValues").clickCancel();
			}
			else
			{val=forms.listOfValues("//forms:listOfValues").getText(1);
			forms
			.listOfValues("//forms:listOfValues").select(val);
			;
			//forms.listOfValues("//forms:listOfValues").select(FieldName); 
			Return_Val=1;
			return Return_Val;
			}
		}
		else if(forms.choiceBox( "//forms:choiceBox").getAlertMessage().equalsIgnoreCase("There are no additional fields to show for this region.")){
			info("There are no additional fields to show for this region.");
			forms.choiceBox("//forms:choiceBox").clickButton("OK");
		}
		return Return_Val;
	}

	//28.
	//Name of the Function: HideField
	//Desc: performs HideField in Forms
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	/**
	 * Hides a Field 
	 * @param Win_Name
	 * @throws Exception
	 */
	public void HideField()throws Exception{
		/*Select Hide Field option from menu*/
		forms.window("//forms:window[(@enabled='true')]").selectMenu("Folder|Hide Field");
		if (forms.choiceBox("//forms:choiceBox").exists()){
			forms.choiceBox("//forms:choiceBox").clickButton("OK");
		}

	}

	//29.
	//Name of the Function: ClearRecord
	//Desc: Clears the currenct record
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	/**
	 * Clears the Record
	 * @param Win_Name
	 * @throws Exception
	 */
	public void ClearRecord()throws Exception{

		/*Select Clear Record option from menu*/
		forms.window("//forms:window[(@enabled='true')]").selectMenu("Edit|Clear|Record");

	}

	//30.
	//Name of the Function: SaveFolder
	//Desc: Saves the current display as the given folder Name
	//param:	1 (String)
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	/**
	 * Saves the current display as the given folder Name
	 * @param FolderName
	 * @throws Exception
	 */
	public void SaveFolder(String FolderName)throws Exception{

		/*Select Save As option from menu*/
		forms.window("//forms:window[(@enabled='true')]").selectMenu("Folder|Save As...");


		/*Save with FolderName*/	
		beginStep("Save Folder");
		{
			forms.textField("//forms:textField[(@name='FOLDER_CONTROL_NEW_FOLDER_NAME_0')]").setText(FolderName);

			forms.radioButton("//forms:radioButton[(@name='FOLDER_CONTROL_AUTOQUERY_ASK_0')]").setFocus();

			forms.checkBox("//forms:checkBox[(@name='FOLDER_CONTROL_INCLUDE_QUERY_0')]").check(false);

			forms.button("//forms:button[(@name='FOLDER_CONTROL_OK_SAVE_FOLDER_0')]").click();
		}
		endStep("Save Folder");


	}


	//31.
	//Name of the Function: DeleteRecord
	//Desc: Deletes the current record
	//Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	/**
	 * Deletes Current Record
	 * @throws Exception
	 */
	public void DeleteRecord()throws Exception{

		/*Select Delete option from menu*/
		forms.window("//forms:window[(@enabled='true')]").selectMenu("Folder|Delete...");

	}

	//	32.
	//	Name of the Function: ShowAllFields
	//	Desc: Shows all the fields passed into it in the form of an array
	//	param:	2 (String array, int)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	/**
	 * Shows all the fields passed into it in the form of an array. 
	 * @param FieldName 
	 *               - An array containing all the fields to be displayed.
	 * @param ParamNum 
	 *               - Contains the number of values contained in the ParamArray.
	 * @throws Exception
	 */
	public void ShowAllFields(String[] FieldName,int ParamNum)throws Exception{

		int i,ReturnValue;
		/*For loop to show required field*/
		for(i=1;i<=ParamNum;i++)
		{
			/*Call of ShowField function to show field*/
			ReturnValue=ShowField(FieldName[i]);
			if(ReturnValue==0)
			{
				info("Field Is Already Displayed");
			}
		}
	}

	//	33.
	//	Name of the Function: GetErrText
	//	Desc: gets the text from a Error window
	//	param:	1 (String)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	/**
	 * Gets The Error Text From the Error or Note Window. 
	 * @param Text
	 *            -The Captured Text Will be Stored 
	 * @throws Exception
	 */
	public void GetErrText(String Text)throws Exception{

		/*Check existance of error dialog and store message*/
		if(web.checkBox("//web:choiceBox").exists()){

			Text =web.checkBox("//web:choiceBox").getAttribute("Text");
		}
		else if (forms.choiceBox( "//forms:choiceBox").exists()){

			Text=forms.choiceBox( "//forms:choiceBox").getAlertMessage();
		}

		info(Text);
	}

	//	34.
	//	Name of the Function: NavProjectOption
	//	Desc: Navigates to the specified Project Option
	//	param:	1 (String)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	/**
	 * Navigates to the specified Project Option
	 * @param Option- The project option to navigate to. In case of child, use ":" as a seperator, without spaces
	 * @throws Exception
	 */
	public void NavProjectOption(String Option)throws Exception{
		int retValue, RecordCount , i=0, j, ArrayLen, RowNum, OptionText, res, window;
		int Scrolldiff;
		String CurrValue;
		String OptionArray[], TempArr[];
		OptionArray=Option.split(":");
		ArrayLen=OptionArray.length; 
		Robot r  = new Robot();

		if(forms.window("//forms:window[(@enabled='true')]").isMenuEnabled("View|Record|First"))
			forms.window("//forms:window[(@enabled='true')]").selectMenu("View|Record|First");

		for(j=0;j<=ArrayLen-1;j++){
			i=0;

			if(j==ArrayLen-1){

				while(i<=7){
					CurrValue=forms.textField( "//forms:textField[(@name='PROJECT_OPTIONS_OPTION_NAME_DISP_"+i+"')]").getAttribute("text");

					if (OptionArray[j].equalsIgnoreCase(CurrValue.trim())){
						forms.textField( "//forms:textField[(@name='PROJECT_OPTIONS_OPTION_NAME_DISP_"+i+"')]").setFocus();
						forms.button("//forms:button[(@name='OPTION_CONTROL_DETAIL_0')]").click();
						break;
					}

					Scrolldiff=forms.blockScroller("//forms:blockScroller").getMaximum()-forms.blockScroller("//forms:blockScroller").getValue();
					if (Scrolldiff>16 && i==7){
						i=0;
						forms.blockScroller("//forms:blockScroller").scrollTo(8);
						forms.textField( "//forms:textField[(@name='PROJECT_OPTIONS_OPTION_NAME_DISP_"+i+"')]").setFocus();
					}else if(Scrolldiff<=16 && i==7) {
						for(RowNum=1;RowNum<=Scrolldiff-8;RowNum++){
							forms.blockScroller("//forms:blockScroller").scrollDown();
						}
						i=0;
					}else i=i+1;

				}
				break;
			}else{

				OptionArray[j]="+ "+OptionArray[j];
				while(i<=7){
					CurrValue=forms.textField( "//forms:textField[(@name='PROJECT_OPTIONS_OPTION_NAME_DISP_"+i+"')]").getAttribute("text");

					if (OptionArray[j].equalsIgnoreCase(CurrValue.trim())){
						forms.textField( "//forms:textField[(@name='PROJECT_OPTIONS_OPTION_NAME_DISP_"+i+"')]").setFocus();
						r.keyPress(KeyEvent.VK_ENTER);
						r.keyRelease(KeyEvent.VK_ENTER);
						break;
					}

					Scrolldiff=forms.blockScroller("//forms:blockScroller").getMaximum()-forms.blockScroller("//forms:blockScroller").getValue();

					if (Scrolldiff>16 && i==7){
						i=0;
						forms.blockScroller("//forms:blockScroller").scrollTo(8);
						forms.textField( "//forms:textField[(@name='PROJECT_OPTIONS_OPTION_NAME_DISP_"+i+"')]").setFocus();
					}else if(Scrolldiff<=16 && i==7) {
						for(RowNum=1;RowNum<=Scrolldiff-8;RowNum++){
							forms.blockScroller("//forms:blockScroller").scrollDown();
						}
						i=0;
					}else{ 
						i=i+1;
						//forms.textField( "//forms:textField[(@name='PROJECT_OPTIONS_OPTION_NAME_DISP_"+i+"')]").setFocus();
					}
				}
			}

		}


	}

	//	35.
	//	Name of the Function: NavTaskNum
	//	Desc: Navigates to the specified Task Number (Invoke NavProjectOption ("Tasks") and then use this)
	//	param:	1 (String)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	/**
	 * Navigates to the specified Task Number (Invoke NavProjectOption("Tasks") and then use this)
	 * @param Option - The Task Number to navigate to
	 * @return 0 if successful, -1 - failure
	 * @throws Exception
	 */
	public int NavTaskNum(String Option)throws Exception 
	{

		int  RecordCount,rc=-1 , i=0, j, ArrayLen, TaskNum, Field0, Field1, OptionText;
		String retValue,ValCol;

		while (forms.textField("//forms:textField[(@name='TASKS_TASK_NUMBER_DISP_"+i+"')]").exists()){
			ValCol=forms.textField("//forms:textField[(@name='TASKS_EXPAND_COLLAPSE_"+i+"')]").getText();
			if(ValCol.equalsIgnoreCase("+"))
				forms.textField("//forms:textField[(@name='TASKS_EXPAND_COLLAPSE_"+i+"')]").click();	
			retValue=forms.textField("//forms:textField[(@name='TASKS_TASK_NUMBER_DISP_"+i+"')]").getAttribute("text");
			if(retValue.trim().equalsIgnoreCase(Option.trim())){
				forms.textField("//forms:textField[(@name='TASKS_TASK_NUMBER_DISP_"+i+"')]").setFocus();
				forms.button("//forms:button[(@name='TASKS_CONTROL_OPTIONS_0')]").click();
				rc=0;
				return rc;
			}
			i=i+1;
		}
		return rc;
	}	
	//	36.
	//	Name of the Function: NavTaskOption
	//	Desc: Navigates to the specified Task Option
	//	param:	1 (String)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	public void NavTaskOption(String Option)throws Exception{
		int retValue, RecordCount , i=0, j, ArrayLen, RowNum, OptionText, res, window;
		int Scrolldiff;
		String CurrValue;
		String OptionArray[], TempArr[];
		OptionArray=Option.split(":");
		ArrayLen=OptionArray.length; 
		Robot r  = new Robot();

		boolean b = forms.window("//forms:window[(@enabled='true')]").isMenuEnabled("View|Record|First");
		if (b)
		{            	 
			forms.window("//forms:window[(@enabled='true')]").selectMenu("View|Record|First");
		}
		else
		{
			forms.window("//forms:window[(@enabled='true')]").selectMenu("View|Record|Last");
			think(10);
			forms.window("//forms:window[(@enabled='true')]").selectMenu("View|Record|First");
			think(10);
		}

		for(j=0;j<=ArrayLen-1;j++){
			i=0;

			if(j==ArrayLen-1){

				while(i<=9){
					CurrValue=forms.textField( "//forms:textField[(@name='TASKS_OPTIONS_OPTION_NAME_DISP_"+i+"')]").getAttribute("text");

					if (OptionArray[j].equalsIgnoreCase(CurrValue.trim())){
						forms.textField( "//forms:textField[(@name='TASKS_OPTIONS_OPTION_NAME_DISP_"+i+"')]").setFocus();
						r.keyPress(KeyEvent.VK_ENTER);
						r.keyRelease(KeyEvent.VK_ENTER);
						break;
					}

					Scrolldiff=forms.blockScroller("//forms:blockScroller").getMaximum()-forms.blockScroller("//forms:blockScroller").getValue();
					if (Scrolldiff>=20 && i==9){
						i=0;
						forms.blockScroller("//forms:blockScroller").scrollTo(8);
						forms.textField( "//forms:textField[(@name='TASKS_OPTIONS_OPTION_NAME_DISP_"+i+"')]").setFocus();
					}else if(Scrolldiff<20 && i==9) {
						for(RowNum=1;RowNum<=Scrolldiff-10;RowNum++){
							forms.blockScroller("//forms:blockScroller").scrollDown();
						}
						i=0;
					}else i=i+1;

				}
				break;
			}else{

				OptionArray[j]="+ "+OptionArray[j];
				while(i<=9){
					CurrValue=forms.textField( "//forms:textField[(@name='TASKS_OPTIONS_OPTION_NAME_DISP_"+i+"')]").getAttribute("text");

					if (OptionArray[j].equalsIgnoreCase(CurrValue.trim())){
						forms.textField( "//forms:textField[(@name='TASKS_OPTIONS_OPTION_NAME_DISP_"+i+"')]").setFocus();
						r.keyPress(KeyEvent.VK_ENTER);
						r.keyRelease(KeyEvent.VK_ENTER);
						break;
					}

					Scrolldiff=forms.blockScroller("//forms:blockScroller").getMaximum()-forms.blockScroller("//forms:blockScroller").getValue();

					if (Scrolldiff>=20 && i==9){
						i=0;
						forms.blockScroller("//forms:blockScroller").scrollTo(8);
						forms.textField( "//forms:textField[(@name='TASKS_OPTIONS_OPTION_NAME_DISP_"+i+"')]").setFocus();
					}else if(Scrolldiff<20 && i==9) {
						for(RowNum=1;RowNum<=Scrolldiff-10;RowNum++){
							forms.blockScroller("//forms:blockScroller").scrollDown();
						}
						i=0;
					}else i=i+1;
				}
			}

		}

	} 

	//	37.
	//	Name of the Function: ValidateData
	//	Desc: Validates actual results data with expected results.
	//	param:	5 (String,String,int,int, String)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib


	/**
	 * Validates actual results data with expected results. 
	 * @param ExpVal - Path of the first file.
	 * @param ActualVal - Path of the second file.
	 * @param StartNo - Starting Line Number of the ExpFile
	 * @param EndNo - Corresponding Ending Line Number of the ExpFile 
	 * @param ShowAll
	 * @return 0 if successful, 1 - failure
	 * @throws Exception
	 */
	public int ValidateData(String ExpVal,String ActualVal,int StartNo,int EndNo,String ShowAll)throws Exception
	{
		String ParamArray[]=null;
		int ParamNum=0,value,return_val;


		//set_window("Oracle Applications - Vision Corporation", TimeOut);
		//handle this in future to check folder menu is enable obj_get_info("Folder_0","enabled",value);
		value=1;
		if(value==1)
		{		
			ParamArray=GetParamArray(ExpVal,ParamArray,ParamNum);
			ParamNum=ParamArray.length;
			if(ShowAll == "")
				ShowAllFields(ParamArray,ParamNum);

		}


		/* write this function in future ExportData(ActualVal);*/
		info("make ExportData(ActualVal) in future");
		return_val=CompareXLS(ExpVal,ActualVal,StartNo,EndNo);
		if(return_val==0)
		{
			return 0;
		}
		else
		{
			return return_val;
		}
	}


	//  	38.
	//  	Name of the Function: GetParamArray
	//  	Desc: Gets the parameter array and the number of elements in it
	//		param:	3 (String, String array, int)
	//  	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib

	/**
	 * Gets the parameter array and the number of elements in it.  
	 * @param FileName -Path of the xls file.
	 * @param ParamArray
	 * @param ParamNum
	 * @return ParamArray[]: Array containing all the column values.
	 * @throws Exception
	 */
	public String[] GetParamArray(String FileName,String ParamArray[],int ParamNum) throws Exception
	{
		String DataFileName;

		DataFileName=FileName;
		ParamArray=ddt.getColumns(FileName, "Sheet1");
		return(ParamArray);
	}




	//  	39.
	//  	Name of the Function: CompareXLS
	//  	Desc:	Compares the data passed through two excels
	//		param:	4 (String, String, int, int)
	//  	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib


	/**
	 * Compares the data passed through two excels. 
	 * @param ExpValue - Path of the first file.
	 * @param ActualValue - Path of the second file.
	 * @param StartNo
	 * @param EndNo
	 * @return 0 if successful, 1 - failure
	 * @throws Exception
	 */
	public int CompareXLS(String ExpValue, String ActualValue,int StartNo, int EndNo) throws Exception
	{
		String ExpValues, ActualValues, ExpColumnNames[]=null, ActColumnNames[]=null,Expfinal,ActFinal;

		int z,i, j, k,ExpParamList, ActParamList, ReturnVal1, ReturnVal2,Return;
		int Exp_RowCount, Act_RowCount, ExpParamNum=0, ActParamNum=0;
		//extern RSTART, RLENGTH, Index;

		z = 1;
		Return=0;
		ExpValues = ExpValue;
		ActualValues = ActualValue; 
		ExpColumnNames=GetParamArray(ExpValues,ExpColumnNames,ExpParamNum);
		ExpParamNum=ExpColumnNames.length;
		ActColumnNames=GetParamArray(ActualValues,ActColumnNames,ActParamNum);
		ActParamNum=ActColumnNames.length;
		//ReturnVal1 = ddt_open(ExpValues, DDT_MODE_READ);
		//ReturnVal2 = ddt_open(ActualValues, DDT_MODE_READ);
		ddt.importSheet(ExpValues, "Sheet1");
		ddt.importSheet(ActualValues, "Sheet1");
		Exp_RowCount=ddt.getRowCount(ExpValues, "Sheet1");
		Act_RowCount=ddt.getRowCount(ActualValues, "Sheet1");
		//ddt_get_row_count(ActualValues,Act_RowCount);
		if(StartNo==0)StartNo = 1;

		if(EndNo ==0)EndNo = Exp_RowCount;

		for(i=0;i<=ExpParamNum-1;i++)
		{
			for(k=0;k<=ActParamNum-1;k++)
			{
				if(ExpColumnNames[i]==ActColumnNames[k])
				{
					z = 1;
					for(j=StartNo;j<=EndNo;j++)
					{

						ddt.setCurrentRow(ExpValues, "Sheet1", j);
						ddt.setCurrentRow(ActualValues, "Sheet1", z);

						ActFinal=ddt.getValue(ActualValues,"Sheet1", ActColumnNames[k]);
						Expfinal=ddt.getValue(ExpValues,"Sheet1", ExpColumnNames[i]);
						if(ActFinal.contains(Expfinal)||ActFinal.equalsIgnoreCase(Expfinal)){
							info("Comparison Passed for "+ExpColumnNames[i]+"Exp:"+Expfinal+" Act:"+ActFinal);

						}else{
							info("Comparison Failed for "+ExpColumnNames[i]+"Exp:"+Expfinal+" Act:"+ActFinal);
							Return=1;
						}



						z++;
					}
				}
			}
		}

		//ddt_close(ExpValues);
		//ddt_close(ActualValues);

		return(Return);
	}


	//  	40.
	//  	Name of the Function: CheckRequestStatus
	//  	Desc: Checks the concurrent request status for given number of times
	//		param:	2 ( String, int)
	//  	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib


	public String CheckRequestStatus(String req_id, int NumberOfTries)throws Exception
	{
		String req_phase_val="";
		String req_stat_val="0";
		int flag_1=0,Retry=0;



		forms.window("//forms:window[(@name='NAVIGATOR')]").selectMenu("View|Requests");
		think(30);

		forms.radioButton("//forms:radioButton[(@name='JOBS_QF_WHICH_JOBS_SPECIFIC_JOBS_0')]").select();
		{
			think(10);
		}
		forms.textField("//forms:textField[(@name='JOBS_QF_REQUEST_ID_0')]").setText(req_id);
		{
			think(10);
		}
		forms.button(99, "//forms:button[(@name='JOBS_QF_FIND_0')]").click();

		for(Retry=0;Retry<NumberOfTries;Retry++)
		{


			forms.window(102, "//forms:window[(@name='JOBS')]").activate(true);
			{
				think(20);
			}

			forms.textField(114, "//forms:textField[(@name='JOBS_PHASE_0')]").setFocus();
			req_phase_val = forms.textField("//forms:textField[(@name='JOBS_PHASE_0')]").getText();

			{
				think(2.0);
			}
			forms.textField(115, "//forms:textField[(@name='JOBS_STATUS_0')]").setFocus();
			req_stat_val = forms.textField("//forms:textField[(@name='JOBS_STATUS_0')]").getText();
			//   				req_stat_val="Warning";
			{
				think(0.391);
			}

			if(req_phase_val.equals("Completed"))
			{
				if (req_stat_val.equals("Normal"))

				{	
					//   							flag_1=1;
					//   							System.out.println("Message got completed with : "+req_phase_val+" Phase "+req_stat_val+" Status ");
					//   							break;
					req_stat_val="1";
					break;
				}

				if (req_stat_val.equals("Error"))
				{
					//   							flag_1=1;
					//   							javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Message got completed with error","Error",javax.swing.JOptionPane.ERROR_MESSAGE);
					//   							System.out.println("Message got completed with : "+req_phase_val+" Phase "+req_stat_val+" Status ");
					req_stat_val="3";
					break;
				}
				if (req_stat_val.equals("Warning"))
				{
					//   							flag_1=1;
					//   							javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"Message got completed with warning","Warning",javax.swing.JOptionPane.WARNING_MESSAGE);
					//   							System.out.println("Message got completed with : "+req_phase_val+" Phase "+req_stat_val+" Status ");
					req_stat_val="2";
					break;
				}
				else
				{
					System.out.println("Request - " + req_id + " doesn't complete with a NORMAL status.");	

				}
				return req_stat_val;
			}
			forms.button(116, "//forms:button[(@name='JOBS_REFRESH_0')]").click();


		}
		if (!req_phase_val.equalsIgnoreCase("Completed"))
		{
			req_stat_val="Unfinished";
		}
		forms.window("//forms:window[(@name='JOBS')]").close();
		think(30);
		return 	req_stat_val;
	}


	//  	41.
	//  	Name of the Function: FireRequest
	//  	Desc: Submits the concurrent request
	//		param:	5( String, String array, String array, int, String)
	//  	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\palib


	public String []FireRequest(String Request,String Parameters[],String Prompt[], int NumberOfTries, String OU)throws Exception
	{
		String[] RequestIdStatus=new String[2];
		String ReqID="";
		boolean OUStatus;
		int ParamLength=0;
		String ObjPhysicalDescription="";
		String P_Default="";
		String RequestStatus="";	
		int count=0;
		forms.window("//forms:window[(@name='NAVIGATOR')]").selectMenu("View|Requests");
		forms.button("//forms:button[(@name='JOBS_QF_NEW_0')]").click();
		forms.button("//forms:button[(@name='WHAT_TYPE_OK_0')]").click();
		forms.textField("//forms:textField[(@name='WORK_ORDER_USER_CONCURRENT_PROGRAM_NAME_0')]").setText(Request);
		think(5);
		forms.textField("//forms:textField[(@name='WORK_ORDER_USER_CONCURRENT_PROGRAM_NAME_0')]").invokeSoftKey("NEXT_FIELD");
		think(30);
		if(forms.listOfValues("//forms:listOfValues").exists())
		{
			forms.listOfValues("//forms:listOfValues").find(OU);
		}
		if(OU != "")
		{

			if (forms.flexWindow("//forms:flexWindow").exists())
				forms.flexWindow("//forms:flexWindow").clickCancel();


		}
		think(60);
		if (forms.flexWindow("//forms:flexWindow").exists())
			forms.flexWindow("//forms:flexWindow").clickCancel();
		think(60);
		forms.window("//forms:window[(@name='WORK_ORDER')]").activate(true);
		OUStatus= forms.textField("//forms:textField[(@name='WORK_ORDER_OPERATING_UNIT_0')]").isEnabled();
		System.out.println(OUStatus);

		if(OUStatus==true && OU!="")
		{
			forms.textField("//forms:textField[(@name='WORK_ORDER_OPERATING_UNIT_0')]").setText(OU);
			forms.textField("//forms:textField[(@name='WORK_ORDER_PARAMETERS_0')]").setFocus();
			think(30);
			if (forms.flexWindow("//forms:flexWindow").exists())
			{

				forms.flexWindow(28, "//forms:flexWindow").setFocus();

			}
		}
		else
		{
			forms.textField("//forms:textField[(@name='WORK_ORDER_PARAMETERS_0')]").setFocus();
			think(30);
			if (forms.flexWindow("//forms:flexWindow").exists())
			{

				forms.flexWindow(28, "//forms:flexWindow").setFocus();

			}
		}
		forms.textField("//forms:textField[(@name='WORK_ORDER_PARAMETERS_0')]").setFocus();
		think(30);	
		ParamLength=Parameters.length;		
		if(ParamLength>0)
		{
			for (count=0;count<=ParamLength-1;count++)
			{
				//	 if((Parameters[i]).substring(1,1)!="#")

				forms.flexWindow("//forms:flexWindow").activate(true);
				if(Parameters[count] != "" && !Parameters[count].equals(P_DEFAULT))
				{
					System.out.println("count is"+count);				       
					System.out.println("prompt is"+ Prompt[count]);
					System.out.println("parameter is"+ Parameters[count]);
					forms.flexWindow("//forms:flexWindow").setText(Prompt[count], "", Parameters[count]);
				}
				info("inside for loop");
			}
		}

		else
		{
			if (forms.flexWindow("//forms:flexWindow").exists())
				forms.flexWindow("//forms:flexWindow").clickCancel();
		}
		if (forms.flexWindow("//forms:flexWindow").exists())
			forms.flexWindow("//forms:flexWindow").clickOk();
		if(Request.toUpperCase()==("Workflow Background Process").toUpperCase())
		{
			forms.window("//forms:window[(@name='Submit Request')]").activate(true);
			forms.button("//forms:button[(@name='WORK_ORDER_SCHEDULE_0')]").click();

			forms.window("//forms:window[(@name='Schedule')]").activate(true);
			forms.radioButton("//forms:radioButton[(@name='SCHEDULE_SCHEDULE_TYPE_PERIODICALLY_0')]").select();
			forms.list("//forms:list[(@name='SCHEDULE_INTERVAL_0')]").selectItem("Minute(s)");
			forms.textField("//forms:textField[(@name='SCHEDULE_AMOUNT_0')]").click();
			forms.textField("//forms:textField[(@name='SCHEDULE_AMOUNT_0')]").setText("4");
			forms.button("//forms:button[(@name='SCHEDULE_OK_0')]").click(); 
		}

		if(forms.window("//forms:choiceBox").exists())
		{
			forms.choiceBox("//forms:choiceBox").clickButton("OK");
		}

		forms.button("//forms:button[(@name='WORK_ORDER_SUBMIT_0')]").click();

		forms.choiceBox("//forms:choiceBox").setFocus();
		String req_text= forms.choiceBox("//forms:choiceBox").getAlertMessage();
		String r_id = req_text.substring(req_text.indexOf("=")+1,req_text.indexOf(")"));
		String req_id = r_id.trim();
		System.out.println("Request ID :"+req_id);
		ReqID=req_id;

		forms.choiceBox("//forms:choiceBox").clickButton("No");
		forms.window("//forms:window[(@name='JOBS_QF')]").close();

		RequestStatus=CheckRequestStatus(req_id, NumberOfTries);
		RequestIdStatus[0] = ReqID;
		RequestIdStatus[1] = RequestStatus;

		return RequestIdStatus;
	}

	//  	42.
	//  	Name of the Function: TemplateHome
	//  	Desc: This function Navigates to the Home page of given Template
	//		param:	1 (String)
	//  	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\sslib


	/**
	 * This function Navigates to the Home page of given Template
	 * @param ProjectTemplate
	 * @return
	 * @throws Exception
	 */
	public int TemplateHome(String ProjectTemplate) throws Exception
	{

		String Combo, Edit, Radio, Check, ListBox, Image, Link, Button,CurrentWindow;
		int rc=-1,testrc=-1;
		//extern TimeOut, BaseState;
		//set_window("Oracle Applications Home Page");

		web.link( "/web:window[ @title='Oracle Applications Home Page']/web:document[@index='0']/web:a[@text='Project Templates']").click();
		//Link="{ class: object, MSW_class: html_text_link, html_name: \""&"Project Templates"&"\", location: 0}";
		//rc= web_link_click(Link);
		if(web.window("/web:window[ @title='Project Templates']").exists()){
			rc=0;
		}
		else//(rc!=E_OK && getvar("batch")!="on")
		{

			info("ERR: Could not click "+"Project Templates"+" Errorcode: "+rc);
			//info("ERR: Could not click "+"Project Templates"+" Errorcode: "+rc);
		}
		//set_window("Project Templates"); 

		//Edit="{ class: object, MSW_class: html_edit, html_name: \""&"TemplateName"&"\", location: 0}";
		//CurrentWindow=GUI_get_window();
		//GUI_set_window(CurrentWindow);

		Thread.sleep(2000);
		//web_refresh();
		if(web.textBox( "/web:window[ @title='Project Templates']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@id='TemplateName' or @name='TemplateName']").exists())
		{
			web.textBox("/web:window[ @title='Project Templates']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@id='TemplateName' or @name='TemplateName']").setText(ProjectTemplate);

		}

		//Button="{class: push_button,MSW_class: html_push_button,html_name: \""&"Go"&"\", location: 0}";
		//rc=button_press(Button);
		web.button("/web:window[ @title='Project Templates']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@value='Go']").click();



		web.image("/web:window[@index='0' or @title='Project Templates']/web:document[@index='0']/web:img[@alt='Update' ]").click();
		/*Image="{ class: object, MSW_class: html_rect, html_name: \""&"Update"&"\", location: 0}";
  		rc=web_image_click(Image, 12,14);
  		if(rc!=E_OK && getvar("batch")!="on")
  		{

  			report_msg("ERR: Could not click "&"Update"&" Errorcode: "&rc);
  			pause("ERR: Could not click "&"Update"&" Errorcode: "&rc);
  		}*/

		if(web.window("/web:window[@index='0' or @title='Project Template Setup']").exists())
		{
			BaseState=BaseState+ProjectTemplate;
			testrc=0;
		}
		else
			testrc=-1;

		return(testrc);
	}


	//	43.
	//	Name of the Function: CheckView
	//	Desc: Checks for personalized view. Sets the view if found otherwise creates the view
	//	param:	2 (String ,String array)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\sslib

	/**
	 * Checks for personalized view. Sets the view if found otherwise creates the view.
	 * @param ViewName
	 * @param ColList
	 * @return
	 * @throws Exception
	 */
	public int CheckView(String ViewName,String ColList[]) throws Exception{
		//extern TimeOut;
		int rc=-1,ComboBox,number,ColListRowCount,ColListRow;

		String Combo, Edit, Radio, Check, ListBox, Image, Link, Button,CurrentWindow;


		if (web.selectBox("/web:window[@title='Project List']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='customizationsPoplist' or @name='customizationsPoplist') and multiple mod 'False']").exists()){
			web.selectBox("/web:window[@title='Project List']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='customizationsPoplist' or @name='customizationsPoplist') and multiple mod 'False']").selectOptionByText(ViewName);
		}else return rc;
		Thread.sleep(2000);
		web.button("/web:window[@title='Project List']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@value='Personalize']").click();

		if(!web.window("/web:window[@index='0' or @title='Personalize a Region - View']").exists()){

			info("ERR: Could not click "+"Personalize"+" Errorcode: "+rc);

		}
		/* Personalize a Region - View*/

		web.button("/web:window[ @title='Personalize a Region - View']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='CustTableViewCreate' or @value='Create View']").click();

		if(!web.window("/web:window[@index='0' or @title='Personalize a Region - Create View']").exists())
		{

			info("ERR: Could not click "+"Create View"+" Errorcode: "+rc);

		}
		/* Personalize a Region - Create View*/

		web.textBox("/web:window[@title='Personalize a Region - Create View']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@id='ViewName' or @name='ViewName']").setText(ViewName);
		web.textBox("/web:window[ @title='Personalize a Region - Create View']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:textarea[@id='ViewDesc' or @name='ViewDesc']").setText(ViewName);


		web.link("/web:window[@index='0' or @title='Personalize a Region - Create View']/web:document[@index='0']/web:a[@text='Remove All']").click();


		ColListRowCount=ColList.length;
		for(ColListRow=0;ColListRow<=ColListRowCount;ColListRow++)
		{

			web.selectBox("/web:window[@title='Personalize a Region - Create View']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='M__Id' or @name='ColumnsShuttle:leading') and multiple mod 'False']").selectOptionByText(ColList[ColListRow]);

			web.link( "/web:window[@index='0' or @title='Personalize a Region - Create View']/web:document[@index='0']/web:a[@text='Move']").click();		

		}

		web.link("/web:window[@title='Personalize a Region - Create View']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='CustViewApplyViewResults_uixr' or @value='Apply and View Results']").click();
		Thread.sleep(2000);
		if (!web.link("/web:window[@title='Personalize a Region - Create View']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='CustViewApplyViewResults_uixr' or @value='Apply and View Results']").exists()){
			rc=0;	
		}

		return rc;
	}

	//	44.
	//	Name of the Function: SetProjectBaseState
	//	Desc: It will navigate to given projects
	//	param:	1 (String)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\sslib


	/**
	 * 
	 * @param ProjectName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") public int SetProjectBaseState(String ProjectName) throws Exception
	{
		String CurrentWindow = "Search Projects";
		String ProjectTable = "/web:window[@title='Search Results']/web:document[@index='0']/web:table[@firstTableCell='Select']";

		int Looper,rc=-1,LOVTable,CheckField,ValueLength,iCount;
		String CellValue,RequiredField;
		String ProjectNameLink;
		int Found=-1;



		web.button("/web:window[@title='Search Projects']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='AdvancedSearchBtn' or @value='Advanced Search']").click();
		Thread.sleep(500);


		web.selectBox("/web:window[ @title='Search Projects']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='Condition_0' and @name='Condition_0') and multiple mod 'False']").selectOptionByText("starts with");
		web.textBox("/web:window[ @title='Search Projects']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@id='Value_0' or @name='Value_0']").setText(ProjectName);


		web.button("/web:window[@title='Search Projects']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@value='Go' and @index='4']").click();
		Thread.sleep(5000);

		LOVTable=web.table(ProjectTable).getRowCount();
		//tbl_get_rows_count(ProjectTable,LOVTable);


		for(Looper = 2;Looper <= LOVTable;Looper++)						
		{

			CellValue=web.table(ProjectTable).getCell(Looper, 2);
			ValueLength=CellValue.length();
			CheckField = ProjectName.length(); 
			RequiredField = CellValue.substring(0, CheckField);

			if(RequiredField.equalsIgnoreCase(ProjectName))
			{
				java.util.List ListofLink = null;
				ListofLink = web.table(ProjectTable).getElementsByTagName("a");
				//List<DOMElement> ListofLink = web.table(ProjectTable).getElementsByTagName("a");
				int Childlist=ListofLink.size();

				for(iCount=1;iCount<=Childlist;iCount++){
					if(((DOMElement) ListofLink.get(iCount)).getAttribute("text").equalsIgnoreCase(ProjectName)){
						((DOMElement) ListofLink.get(iCount)).click();
						Found = 1;
						rc=0;
						return(rc);	
					}
				}

			}

		}	

		if(Found!=1)
		{
			info("Project Not Found");
			return(rc);
		}
		return rc;


	}	

	//	45.
	//	Name of the Function: SetProjOrTempBaseState
	//	Desc: It will navigate to given Template of Prjects
	//	param:	2 (String, String)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\sslib



	public int SetProjOrTempBaseState(String ProjOrTemplateValue,String PorTorA) throws Exception
	{
		EBSBrowser ebs = null;
		int SYNCTIME = 500;
		ebs = new EBSBrowser(this);
		new Initialize (web,browser,forms,this,SYNCTIME);
		int Looper,rc=-1,LOVTable,CheckField,ValueLength,iCount;
		boolean VerifyProjTable;
		String ProjVal;
		//getScript("Fmwklib").callFunction("initEBS");
		String CurrentWindow = "Search Projects";
		String ProjectTable = "/web:window[@title='Search Results']/web:document[@index='0']/web:table[@firstTableCell='Select']";


		String CellValue,RequiredField;
		String ProjectNameLink;
		int Found=-1;

		if(PorTorA.equalsIgnoreCase("P") || PorTorA.equalsIgnoreCase("Project") || PorTorA.equalsIgnoreCase("Proj" )|| PorTorA.equalsIgnoreCase("PROJ") || PorTorA.equalsIgnoreCase("")){


			web.button("/web:window[@title='Search Projects']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='AdvancedSearchBtn' or @value='Advanced Search']").click();
			Thread.sleep(5000);


			web.selectBox("/web:window[ @title='Search Projects']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='Condition_0' and @name='Condition_0') and multiple mod 'False']").selectOptionByText("is");
			web.textBox("/web:window[ @title='Search Projects']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@id='Value_0' or @name='Value_0']").setText((String) getScript("GenLib").callFunction("CheckIndex",ProjOrTemplateValue));


			web.button("/web:window[@title='Search Projects']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@value='Go' and @index='4']").click();
			info("program in in mid ");
			Thread.sleep(500);
			ProjVal=((String) getScript("GenLib").callFunction("CheckIndex",ProjOrTemplateValue));
			info(ProjVal);
			WebTable ProjectTableObj = new WebTable("Status");

			VerifyProjTable=ProjectTableObj.verifyRowData("Project Name", ProjVal);
			if (VerifyProjTable==true)
			{

				ProjectTableObj.clickLink("Project Name", ProjVal, "Project Name");
				Found = 1;
				rc=0;
				return(rc);	
			}
			/*LOVTable=web.table(ProjectTable).getRowCount();
				//tbl_get_rows_count(ProjectTable,LOVTable);


				for(Looper = 2;Looper <= LOVTable;Looper++)						
				{

					CellValue=web.table(ProjectTable).getCell(Looper, 2);
					ValueLength=CellValue.length();
					CheckField = ((String) getScript("GenLib").callFunction("CheckIndex",ProjOrTemplateValue)).length(); 
						RequiredField = CellValue.substring(0, CheckField);

					if(RequiredField.equalsIgnoreCase((String) getScript("GenLib").callFunction("CheckIndex",ProjOrTemplateValue)))
					{
						java.util.List ListofLink = null;
						ListofLink = web.table(ProjectTable).getElementsByTagName("a");
					    //List<DOMElement> ListofLink = web.table(ProjectTable).getElementsByTagName("a");
					    int Childlist=ListofLink.size();

							for(iCount=1;iCount<=Childlist;iCount++){
								if(((DOMElement) ListofLink.get(iCount)).getAttribute("text").equalsIgnoreCase((String) getScript("GenLib").callFunction("CheckIndex",ProjOrTemplateValue))){
								  ((DOMElement) ListofLink.get(iCount)).click();
									Found = 1;
									rc=0;
									return(rc);	
								}
							}

					}

				}	*/

			if(Found!=1)
			{
				info("Project Not Found");
				return(rc);
			}
		}else
			if(PorTorA.equalsIgnoreCase("T") || PorTorA.equalsIgnoreCase("Template") || PorTorA.equalsIgnoreCase("Temp") || PorTorA.equalsIgnoreCase("TEMP"))
			{				
				web.textBox("/web:window[@title='Project Templates']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@id='TemplateName' or @name='TemplateName']").setText((String) getScript("GenLib").callFunction("CheckIndex",ProjOrTemplateValue));
				web.button("/web:window[ @title='Project Templates']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@value='Go']").click();
				Thread.sleep(5000);
				web.image("/web:window[@title='Project Templates']/web:document[@index='0']/web:img[@alt='Update']").click();
				rc=0;
				return(rc);
			}else
				if(PorTorA=="Alt" || PorTorA=="Alternate" || PorTorA=="ALT" || PorTorA=="A"){
					//web_refresh(CurrentWindow1)
					web.button("/web:window[@title='Search Projects']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='AlternativeSearchBtn' or @value='Alternative Search']").click();
					Thread.sleep(2000);
					web.button("/web:window[@title='Alternate Project Search']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='CriteriaRNAddCriteriaButton' and @value='Add Criteria']").click();

					web.selectBox("/web:window[@title='Add Criteria']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='M__Id' and @name='EGOADDCRITERIASHUTTLE:leading') and multiple mod 'True']").multiSelectOptionByText("^ Project:Project Name");
					web.link("/web:window[@title='Add Criteria']/web:document[@index='0']/web:a[@text='Move']").click();
					web.button( "/web:window[@title='Add Criteria']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='EgoApplyButton_uixr']").click();
					web.checkBox("/web:window[@title='Alternate Project Search']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_checkbox[@id='M__Id' and @name='CriteriaRNEgoCriteriaListTable:selected:0']").check(true);

					web.selectBox( "/web:window[@title='Alternate Project Search']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='CriteriaRNEgoCriteriaListTable:Varchar2:0' or @name='CriteriaRNEgoCriteriaListTable:Varchar2:0') and multiple mod 'False']").selectOptionByText("is");


					web.textBox( "/web:window[@title='Alternate Project Search']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@id='CriteriaRNEgoCriteriaListTable:ValueVarchar2:0' and @name='CriteriaRNEgoCriteriaListTable:ValueVarchar2:0']").setText((String) getScript("GenLib").callFunction("CheckIndex",ProjOrTemplateValue));
					web.button("/web:window[ @title='Alternate Project Search']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='SearchButton' and @value='Go' ]").click();
					web.link("/web:window[@index='0' or @title='Alternate Project Search']/web:document[@index='0']/web:a[@text='"+(String) getScript("GenLib").callFunction("CheckIndex",ProjOrTemplateValue)+"']").click();
					rc=0;
					return(rc);
				}
		return rc;						

	}	

	//	46.
	//	Name of the Function: SelectTask
	//	Desc: It selects the given Task value
	//	param:	1 (String)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\sslib



	public void SelectTask(String RequiredTaskValue) throws Exception{
		String TasksWindow = "Update Tasks";			
		String TasksTable = "/web:window[@title='Update Tasks']/web:document[@index='0']/web:table[@firstTableCell='Select']";
		String TaskColumnHeader = "Update";
		int TaskTableRowCount,TaskColumnLocation;						
		int RowNum1,RequiredColumn;
		String TaskName,TaskValue;
		String UpdateButton;


		/* Update Tasks*/


		if(web.link("/web:window[@title='Update Tasks']/web:document[@index='0']/web:a[@text='Expand All']").exists())
		{
			web.link("/web:window[@title='Update Tasks']/web:document[@index='0']/web:a[@text='Expand All']").click();
			Thread.sleep(5000);
		}				


		TaskTableRowCount=web.table(TasksTable).getRowCount();	


		//TaskColumnLocation = web.table(TasksTable).;
		TaskColumnLocation=4;
		RequiredColumn = TaskColumnLocation;		

		for(RowNum1 = 3; RowNum1 <= TaskTableRowCount; RowNum1++){ 			

			//web_obj_get_child_item(TasksTable,RowNum1,"#4","html_object",0,TaskName);
			TaskValue=web.table(TasksTable).getCell(RowNum1, RequiredColumn);


			if(TaskValue == RequiredTaskValue)
			{										

				/*Sourcecolname="Task Name";
						SourceColValue=RequiredTaskValue;  -- sourceColValueType=Text Field
						TargetColName="Update";TargetColcomponent=image;*/
				break;				
			}							

		}		//	Close of Inner For-Loop For Selecting a Task ...	




	}

	//	47.
	//	Name of the Function: SelectTemplateOption
	//	Desc: It selectst the given Template
	//	param:	1 (String)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\sslib


	public void SelectTemplateOption(String TemplateOption) throws Exception{

		int OptionsTable,icount;	
		int Arrlength,Looper,CheckField,ValueLength;
		String CellValue;		
		String RequiredField;	
		String TemplateOptionCheckBox,CurrentValue;
		String CurrentWindow1;
		String TemplateTable;
		String ClickToExpandButton,Found;
		String OptionArray[];


		CurrentWindow1 = "";
		TemplateTable  = "/web:window[@title='Project Template Setup']/web:document[@index='0']/web:table[@firstTableCell='Focus']";	
		OptionArray=TemplateOption.split(":");

		Arrlength=OptionArray.length;			


		//#pause("Please Insert the New code for Handling Multiple Non-Unique Template Options");


		/* Project Template Setup*/

		if(web.link("/web:window[@title='Project Template Setup']/web:document[@index='0']/web:a[@text='Expand All']").exists())
		{

			web.link("/web:window[@title='Project Template Setup']/web:document[@index='0']/web:a[@text='Expand All']").click();
			Thread.sleep(5000);
		}

		OptionsTable =web.table(TemplateTable).getRowCount();
		Looper = 2;
		for(icount=0;icount<=Arrlength-1;icount++){

			for(;Looper <= OptionsTable;Looper++)	/*For-Loop(Outer)*/					
			{

				CellValue=web.table(TemplateTable).getCell(Looper, 2);
				ValueLength=CellValue.trim().length();
				CheckField = OptionArray[icount].trim().length(); 
				//RequiredField = CellValue.substring(ValueLength-CheckField, CheckField);/// substr(CellValue,ValueLength-CheckField+1,CheckField);
				if(CellValue.trim().equalsIgnoreCase(OptionArray[icount].trim())){
					info(OptionArray[icount].trim());
					if(icount==Arrlength-1){
						info(OptionArray[icount].trim());
						/*click on update add code
								Colname=Option,targetcol=Update
								depending on variable tepmpate Option click on update image of table
						 */
						break;
					}
					break;
				}		
				info(Integer.toString(Looper));	
			}

		}


	}	

	//	48.
	//	Name of the Function: TemplateBaseState
	//	Desc: It will navigate to given Template
	//	param:	1 (String)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\sslib


	public boolean  TemplateBaseState(String Template) throws Exception
	{
		//extern TimeOut, BaseState, ProjectSuperUser;
		//auto rc,row;
		boolean rc=false;
		int Row;
		String XpathImage;


		//if (match(BaseState, Template)==0)
		if(getVariables().get("BaseState").indexOf(Template)<0)
		{
			SetBaseState(getVariables().get("ProjectSuperUser"));
			//set_window("Oracle Applications Home Page");
			web.window("/web:window[@title='Oracle Applications Home Page'or @index='0']").waitForPage(null);

			rc=web.link("/web:window[@index='0']/web:document[@index='0']/web:a[@text='Project Templates' or index='0']").exists();
			if(rc!=true)
			{info("ERR: Could not click "+"Project Templates "+" Errorcode: "+rc);

			JOptionPane.showMessageDialog(null,new String("ERR: Could not click Project Templates"),new String("Warning"),JOptionPane.WARNING_MESSAGE);
			}
			else
				web.link("/web:window[@index='0']/web:document[@index='0']/web:a[@text='Project Templates']").click();
			web.window("/web:window[@title='Project Templates'or @index='0']").waitForPage(null);
			ObjectClass oc=new ObjectClass(web);
			oc.searchTableObject("Select", "Name (Number)", Template, "Update", "image", 0);
			if(oc.isFound())
			{
				String xpath=oc.getImgXpath();
				web.image(xpath).click();

			} 

			else
			{

				info("ERR: Could not click "+"Update"+" Errorcode: "+rc);
				JOptionPane.showMessageDialog(null,new String("ERR: Could not click Update"),new String("Warning"),JOptionPane.WARNING_MESSAGE);
			}


			if(web.window("/web:window[@title='Project Template Setup'or @index='0']").exists())
			{
				String BaseState=getVariables().get("BaseState")+Template;
				getVariables().set("BaseState",BaseState);
				rc=true;
			}
			else
				rc=false;

		}

		else
		{
			if(web.link("/web:window[@index='0']/web:document[@index='0']/web:a[@text='Project Template Setup' ]").exists())

				web.link("/web:window[@index='0']/web:document[@index='0']/web:a[@text='Project Template Setup'or index='0']").click();

			else
			{

				info("ERR: Could not click "+"Project Template Setup"+" Errorcode: "+ rc);

				JOptionPane.showMessageDialog(null,new String("ERR: Could not click Update"),new String("Warning"),JOptionPane.WARNING_MESSAGE);
			}

			if(web.window("/web:window[@title='Project Template Setup'or @index='0']").exists())
				rc=true;
		}
		return(rc);

	}


	//	49.
	//	Name of the Function: ProjectHome
	//	Desc: It navigates to projects home page 
	//	param:	1 (String)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\sslib



	public void ProjectHome(String Project) throws Exception
	{
		web.link("/web:window[@title='Oracle Applications Home Page' or @index='0']/web:document[@index='0']/web:a[@text='Search Projects']").click();
		web.window("/web:window[@title='Search Projects' or @index='0']").waitForPage(null);
		web.button("/web:window[@title='Search Projects'or @index='0']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@id='AdvancedSearchBtn' or @value='Advanced Search' or @index='2']").click();
		web.window("/web:window[ @title='Search Projects'or @index='0']").waitForPage(null);
		web.selectBox("/web:window[@index='0' or @title='Search Projects']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:select[(@id='Condition_0' or @name='Condition_0' or @index='1') and multiple mod 'False']").selectOptionByText("starts with");
		web.textBox("/web:window[@index='0' or @title='Search Projects']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:input_text[@id='Value_0' or @name='Value_0' or @index='0']").setText(Project);
		web.button("/web:window[@index='0' or @title='Search Projects']/web:document[@index='0']/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']/web:button[@value='Go' or @index='4']").click();
		web.window("/web:window[@title='Search Results'or @index='0']").waitForPage(null);
		web.link("/web:window[@index='0' or @title='Search Results']/web:document[@index='0']/web:a[@text='"+Project+"' or @index='21']").click();	
	}



	//	50.
	//	Name of the Function: selectValue
	//	Desc: It selects a vlaue from search window
	//	param:	1 (String)
	//	Base Location:	O:\EBS QA\WR2OATS\GMS_OATS\12.2\Library\sslib


	/*************************************************************************************************************************************************
	 * 	FUNCTION: 			SelectValue
		CREATED BY: 		Chandrika Vepati 
		PURPOSE:  			Selecting the Required Value from the "Search and Select List of Values" Window
	 	INPUT PARAMS: 
			RequiredValue -	Value to be chosen from Table 
		OUTPUT PARAMS: 		None 
	 	RETURN VALUES: 		'0' if successful
	 				 : 		-1 if unsuccessful
	 * 
	 ************************************************************************************************************************************************/
	public boolean selectValue(String RequiredValue) throws Exception 
	{
		int selectValueTableRowsCount,selectValueTableColsCount,rowLooper,columnLooper,valueLength,checkField;
		String cellValue,requiredField;
		boolean returnValue;

		returnValue = false;

		String tableXpath = "/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:table[@firstTableCell='Select']";
		selectValueTableRowsCount = web.table(tableXpath).getRowCount();
		selectValueTableColsCount = web.table(tableXpath).getColumnCount();
		for(rowLooper=2;rowLooper<=selectValueTableRowsCount;rowLooper++)
		{
			for(columnLooper=3;columnLooper<=selectValueTableColsCount;columnLooper++)
			{
				cellValue = web.table(tableXpath).getCell(rowLooper, columnLooper);
				info(cellValue);
				valueLength = cellValue.length();
				if(cellValue.equalsIgnoreCase("No Items found"))
				{
					warn("Your Search Retrieved No Values.., Please Check for the Input Data Available..");
				}
				checkField = RequiredValue.length();
				if(valueLength>checkField&&valueLength-checkField<checkField)
					requiredField = cellValue.substring(valueLength-checkField, checkField);
				else
					requiredField=cellValue;
				if(requiredField.trim().equalsIgnoreCase(RequiredValue.trim()))
				{
					web.radioButton("/web:window[@title='Search and Select List of Values']/web:document[@index='1']/web:form[@id='_LOVResFrm' or @name='_LOVResFrm' or @index='0']/web:input_radio[@id='N1:N8:"+(rowLooper-2)+"' or (@name='N1:selected' and @value='"+(rowLooper-2)+"') or @index='"+(rowLooper-2)+"']").select();
					returnValue = true;
					info("The value : "+RequiredValue+" is selected.");
					rowLooper = selectValueTableRowsCount+1;
					break;
				}
			}
		}
		web.button("/web:window[@title='Search and Select List of Values' or @index='1']/web:document[@index='1']/web:button[@value='Select' or @index='4']").click();
		Thread.sleep(20000);
		if(returnValue==false)
		{
			warn(" The Required Value Not Found, Please Provide Exact Input Data\n (Including Case-Sensitivity...,)\n Thanks\n The Projects Team");
		}
		return returnValue;
	}

	
	public List<String> getColumnValuesfromExcelSheet(String inputFileName, String outputFileName, String worksheetName, String tableName, String columnHeader) throws Exception {
		
		int rowStart=0;
		int rowCount=0;
		int columnCount=0;
		int rowEnd=0;

			if(inputFileName!=null && !inputFileName.isEmpty() && outputFileName!=null && !outputFileName.isEmpty() && worksheetName!=null && !worksheetName.isEmpty() && tableName!=null && !tableName.isEmpty() && columnHeader!=null  && !columnHeader.isEmpty()) {
				
					formatXLSheet(inputFileName,outputFileName,worksheetName);                                      //output: formatted worksheet is stored in output folder.
				
					importFormattedXLsheet(outputFileName,worksheetName);                                           //output: imported datatable from the formated excel sheet.
				
					rowStart = findTablerow(worksheetName,tableName,columnHeader );                                 //output: gets the "rowStart" - row of the tableName.
				
					rowEnd   = findTableEndRow(worksheetName,rowStart,rowCount,columnCount,columnHeader);           //output: gets the "count" - number of rows of the tableName.
				
			        return retrieveTableColumnheadervalues( worksheetName,rowStart,rowEnd,columnCount,columnHeader);//output: Displays all values under columnHeader of a tableName.
					
			}
			
			else {
				
				   info("getColumnValuesfromExcel(): Please provide input parameters correctly");
				   
				   return null;
			}
		
		}
		
	private void formatXLSheet(String inputfileName,String outputfileName, String worksheetName) throws Exception{
			
			try{
				  POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(inputfileName));
				  HSSFWorkbook    wb = new  HSSFWorkbook(fs, true);
				  HSSFSheet    sheet = wb.getSheet(worksheetName);
				  
				  Iterator <Row> rows  = sheet.rowIterator ();		  
				 
				  while (rows.hasNext())
				  {
					    Row row = (Row) rows.next();	
					    Iterator <Cell> cells = row.cellIterator ();
					   
					   while (cells.hasNext ())
				       {
							  Cell cell =  (Cell) cells.next();		
							  String value="";
							  
							  switch (cell.getCellType ())
							  {
							  
							  	case HSSFCell.CELL_TYPE_NUMERIC :
							  					{ 
							  						
							  						if (DateUtil.isCellDateFormatted(cell))
							  							{
							  								SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
							  								String cellValue = sdf.format(cell.getDateCellValue());
							  								value+=cellValue;
							  								
							  							}else{
														     double d=cell.getNumericCellValue();
														     int integer=(int) Math.floor(d);
															     if((d-(double)integer)==0.0){													    
															    	 value=value+integer;
															     }
															     else{													    
															      value+=d;
															     }	
							  						 }
				                break;
				                }
				   
							   case HSSFCell.CELL_TYPE_STRING :
							   {
							    RichTextString richTextString = cell.getRichStringCellValue();							    
							    value=richTextString.getString ();
							    break;
							   }
				   
							   case HSSFCell.CELL_TYPE_FORMULA :
							   {							   
								   NumberFormat formatter = new DecimalFormat("0.00");
								   double d=cell.getNumericCellValue();
								   value+=formatter.format(d);							   
								   break;
							   }
							   
							   case HSSFCell.CELL_TYPE_BLANK :
							   {						   
							    break;
							   }
							   
							   default :
							   {						   
							    // types other than String and Numeric.
							    info ("Type not supported."+cell.getCellType ());						   
							    break;
							   }
				   
							}
					  cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					  cell.setCellValue(value);
					  }		  
				  }
				  
					  FileOutputStream fos=new FileOutputStream(outputfileName);
					  wb.write(fos);
					  info("Successfully completed formatting of:"+outputfileName);
					  fos.close();
					  }
			  
			  	 catch(Exception e){
			  		e.printStackTrace();
				  }
		
		}

	private void importFormattedXLsheet(String outputfileName, String worksheetName) throws Exception{
			
			datatable.importExcel(outputfileName);
			
			datatable.getGlobalDatatable();
			
			
		
		}            

	private int findTablerow(String worksheetName, String tableName, String columnHeader) throws Exception{
			
			int rowCount = datatable.getRowCount(worksheetName); 
			int columnCount= datatable.getColumnCount(worksheetName);	
			int currRow=0;
			info("rows="+rowCount);
			
			for(int i= 0;i<rowCount; i++)
			{
				for(int j=0;j<columnCount;j++) 
				{
					datatable.setCurrentRow(worksheetName, i);
					//info("current Row>"+datatable.getCurrentRow());
					String columnName =datatable.getColumn(worksheetName,j);
					//info("columnName="+columnName);				
						if(datatable.getEditValue(worksheetName,i,columnName)!=null){	
								//info("current value in ("+i+","+columnName+"):"+((String) datatable.getEditValue(worksheetName,i,columnName)).toString());
								if(((String) datatable.getEditValue(worksheetName,i,columnName)).toString().equalsIgnoreCase(tableName)){
									
									currRow=datatable.getCurrentRow();
									info("currRow5="+datatable.getCurrentRow());
									
									// call the method to find number of rows for the specified table
									
									i=rowCount;
									j=columnCount;
									break;
								}
						}
				}	 //end of second for loop
			} //end of first for loop
		return currRow;
		}                    

	private int findTableEndRow(String worksheetName,int rowStart, int rowCount, int columnCount, String columnHeader) throws Exception{
			
			int currRow;
			currRow=rowStart;
			int rowEnd=0;
			info("Printing currRow:"+currRow);
			for(int i=currRow;i<rowCount; i++)
				{
					
						int k=i+1;
						rowEnd = rowEnd+1;
						datatable.setCurrentRow(worksheetName,k);
						//info("current Row-->"+datatable.getCurrentRow());
						String columnName =datatable.getColumn(worksheetName,0);
						//info("columnName="+columnName);
						
							if(datatable.getEditValue(worksheetName,i+1,columnName)!=null){
									if(((String) datatable.getEditValue(worksheetName,i+1,columnName)).toString().equalsIgnoreCase("Test Scenario")){
										info("Number of rows for this table are:"+rowEnd);
										//call the method to print the values of the column name
																			
										i=rowCount;
										break;  // exit from this loop
									}
									else {
										
									}
							}
					
				} //end of for loop	
		return rowEnd;
		}
	private List<String> retrieveTableColumnheadervalues(String worksheetName,int rowStart, int count, int columnCount, String columnHeader) throws Exception{
			
			//String columnHeader="Agreement";
			int rowFinal;
			info("rowStart="+rowStart);
			info("rowCount="+count);
			info("columnCount="+columnCount);
			count=rowStart+count;
			
			List<String> ColArray = new ArrayList<String>();
			int p=0;
			
			for(int i=rowStart;i<count; i++)
			{
					for(int j=0;j<columnCount;j++) 
					{
						datatable.setCurrentRow(worksheetName, i);
						//info("current Row>"+datatable.getCurrentRow());
						String columnName =datatable.getColumn(worksheetName,j);
						//info("columnName="+columnName);				
						if(datatable.getEditValue(worksheetName,i,columnName)!=null){	
								//info("current value in ("+i+","+columnName+"):"+((String) datatable.getEditValue(worksheetName,i,columnName)).toString());
								if(((String) datatable.getEditValue(worksheetName,i,columnName)).toString().equalsIgnoreCase(columnHeader)){
									info("column found");
									info("Rowfinal="+datatable.getCurrentRow());
									rowFinal=datatable.getCurrentRow();
									
												for(int m=rowStart+1;m<count-1;m++) {
													datatable.setCurrentRow(worksheetName, m);
															
															// printing the column values
															
															if(datatable.getEditValue(worksheetName,m+1,columnName)!=null) {
																if(((String) datatable.getEditValue(worksheetName,m+1,columnName)).toString()!=null && !((String) datatable.getEditValue(worksheetName,m+1,columnName)).toString().isEmpty() ) {
																	if(((String) datatable.getEditValue(worksheetName,m+1,columnName)).toString().equalsIgnoreCase(columnHeader)){
																		info("Column Values for the column, "+columnHeader+" are:");
																		info("");
																		info(columnHeader);
																		info("-------------");
																	} 
																	else {
																		
																		info(((String) datatable.getEditValue(worksheetName,m+1,columnName)).toString());
																		String columnValue= (String) datatable.getEditValue(worksheetName,m+1,columnName).toString();
																		ColArray.add(columnValue);	
																		
																	}
																}
															}											
															
												}  // end of for loop
										
									j=columnCount;
									i=count;
									break;   // break after printing all column values
								
								}
						 } 
			
		            } // end of second for loop
		
	        }  //end of first for loop	
		/* Print colArray */
		/*for(String values: ColArray){
			info("Column value="+values);
		}*/
			return ColArray;
		}

	//Mallik
	// After getting Concurent request will press "View OutPut" Button ,will get one browser window,
	//if perticuler item selected then will get corresponding values in table that perticuler window   
	
	//	Name of the Function: MinMaxViewOutput
	//	Desc: MinMaxConcurentRequestForViewOutput
	//	param:	1 (String,String,String)


	public void MinMaxViewOutput(String item,String target_col,String target_col_Value) throws Exception {

		String str = web.document("/web:window[@title='http://*/OA_CGI/FNDWRR.exe?temp_id=*']/web:document[@index='0']").getHTML();
		info("flag value -->"+MinMaxConcurent.concurent(str,item,target_col,target_col_Value));

	}
	
	//Mallik
	//	Name of the Function: ImgVerfyCheckBox
	//	Desc: It navigates to 'Staffing Manager, Vision Services (USA)' responsibility ->'Projects: Resources'
	//	'Work Information' Tab
	//	param:	1 (String)
	public void webImgVerfyCheckBox(String imglabel) throws Exception {

		//1
		if (web.image("/web:window[@title='*']/web:document[@index='0']/web:td[@text='" +imglabel+"']/web:img[@alt='Read only Checkbox Not Checked']").exists())
		{
			info(imglabel+" -->Read only Checkbox Not Checked");
		}
		if (web.image("/web:window[@title='*']/web:document[@index='0']/web:td[@text='" +imglabel+"']/web:img[@alt='Read only Checkbox Checked']").exists())
		{
			info(imglabel+" -->Read only Checkbox Checked");
		}

	}
	
	

	
	//Mallik
	//	Name of the Function: webVerfyTblValueBasedOnLabel
	//	Desc: It navigates to 'Work Preferences: Review'
	//	param:	1 (String)
	
	public boolean webVerfyTblValueBasedOnLabel(String label_val,String current_value,String proposed_value)throws Exception
    {
        List<DOMElement> bodyList = web.document("/web:window[@index='0' or @title='*']/web:document[@index='0']").getElementsByTagName("body");
        DOMElement body = bodyList.get(0);
       
        List<DOMElement> thList = body.getElementsByTagName("th");
        DOMTable myWebTable = null;
       
        for(DOMElement th: thList){
           
            String colName = th.getAttribute("text");
           
            if((colName!= null)&&(colName.equals("Work in Current Location Only"))){
               
                myWebTable = (DOMTable)th.getParent().getParent().getParent();
               
            }
        }
        String key[] = new String[25];
        for (int i = 2,j=0; i < 7; j++,i++)
        {
            key[j]=myWebTable.getCell(i,1);
            info(key[j]);
        }
        String current[]=new String[25];
        for (int i = 2,j=0; i < 7; j++,i++) {
            current[j]=myWebTable.getCell(i,2);
            info(current[j]);
        }
        String proposed[]=new String[25];
        for (int i = 2,j=0; i < 7; j++,i++) {
            proposed[j]=myWebTable.getCell(i,3);
            info(proposed[j]);
        }
        String test[]={key[1],key[2],key[3],key[4],key[5]};
        String proposede[]={proposed[1],proposed[2],proposed[3],proposed[4],proposed[5]};
        String currente[]={current[1],current[2],current[3],current[4],current[5]};
        HashMap<String, String[]> map = new HashMap<String, String[]>();
         
         for(int i = 0; i<test.length; i++) {
             String temp10[] = {proposed[i],current[i]};
             map.put(test[i], temp10);
         }
         
         boolean flag=Tablevalues(map, label_val,current_value,proposed_value);
         return flag;
    }
	
    public boolean Tablevalues(HashMap<String, String[]> m,String test,String current_value,String proposed_value) throws Exception
    {
         
        String result[] = (String[]) m.get(test);
         System.out.println("result "+result);
         System.out.println("proposed :" + result[0] );
         
         
         if(result[1]==null||!(result[1].equalsIgnoreCase(current_value)))
         {
         System.out.println("Current :"+" ");
         return false;
         }
         else
         {
         System.out.println("Current :"+ result[1]);
         if(result[0]==null ||!(result[0].equalsIgnoreCase(proposed_value)))
         {
             System.out.println("proposed :"+" ");
             return false;
         }
             else
             {
             System.out.println("proposed :"+ result[0]);
             return true;
             }
         }
    }
	
	
	public void webVerfyTblValueBasedOnLabel1(String label_val)throws Exception
	{
		List<DOMElement> bodyList = web.document("/web:window[@index='0' or @title='*']/web:document[@index='0']").getElementsByTagName("body");
		DOMElement body = bodyList.get(0);
		
		List<DOMElement> thList = body.getElementsByTagName("th");
		DOMTable myWebTable = null;
		
		for(DOMElement th: thList){
			
			String colName = th.getAttribute("text");
			
			if((colName!= null)&&(colName.equals("Work in Current Location Only"))){
				
				myWebTable = (DOMTable)th.getParent().getParent().getParent();
				
			}
		}
		String key[] = new String[25];
		for (int i = 2,j=0; i < 7; j++,i++) 
		{
			key[j]=myWebTable.getCell(i,1);
			info(key[j]);
		}
		String current[]=new String[25];
		for (int i = 2,j=0; i < 7; j++,i++) {
			current[j]=myWebTable.getCell(i,2);
			info(current[j]);
		}
		String proposed[]=new String[25];
		for (int i = 2,j=0; i < 7; j++,i++) {
			proposed[j]=myWebTable.getCell(i,3);
			info(proposed[j]);
		}
		String test[]={key[1],key[2],key[3],key[4],key[5]};
		String proposede[]={proposed[1],proposed[2],proposed[3],proposed[4],proposed[5]};
		String currente[]={current[1],current[2],current[3],current[4],current[5]};
        HashMap<String, String[]> map = new HashMap<String, String[]>();
		 
		 for(int i = 0; i<test.length; i++) {
			 String temp10[] = {proposed[i],current[i]};
			 map.put(test[i], temp10);
		 }
		 
		 Tablevalues(map, label_val);
	}
	public void Tablevalues(HashMap<String, String[]> m,String test) throws Exception 
	{
		 
		 String result[] = (String[]) m.get(test);
		 info("proposed :" + result[0] );
		 if(result[1]==null)
			 info("Current :"+" ");
		 else
			 info("Current :"+ result[1]);
	}
	
	//Mallik
	//	Name of the Function: TableMearge
	//	Desc: It navigates to 'Staffing Manager, Vision Services (USA)' responsibility ->'Projects: Resources'
	// 	'Capacity' Tab
	//	param:	1 (String)
	
	public void webVerfyMergTblValBasedOnLabel(String label_val) throws Exception {
		ArrayList<String> day_list=new ArrayList<String>();
		ArrayList<String> a1=new ArrayList<String>();
		ArrayList<String> a2=new ArrayList<String>();
		day_list.add("Mon");
		day_list.add("Tue");
		day_list.add("Wed");
		day_list.add("Thu");
		day_list.add("Fri");
		day_list.add("Sat");
		day_list.add("Sun");
		for (int i = 4; i < 18; i++) {
			String capacity_list=web.table("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:table[@firstTableCell='Project' or @index='36']").getCell(3, i);
			a1.add(capacity_list);
		}
		for (int i = 4; i < 18; i++) {
			String Available_hrs_list=web.table("/web:window[@index='0' or @title='*']/web:document[@index='0']/web:table[@firstTableCell='Project' or @index='36']").getCell(4, i);
			a2.add(Available_hrs_list);
		}
		HashMap<String, ArrayList<String>> hm=new HashMap<String, ArrayList<String>>();
		hm.put("Capacity", a1);
		hm.put("Available Hours", a2);
		Table_merge(hm, label_val,day_list);

	}
	public void Table_merge(HashMap<String, ArrayList<String>> hm,String test,ArrayList<String> day_list) throws Exception 
	{
		ArrayList<String> al=(ArrayList<String>) hm.get(test);
		info(test+" \n------------");
		info("Week Starting For first week\n ----------------------");
		for (int i = 0; i < 7; i++) {
			info(day_list.get(i)+"---"+al.get(i));

		}
		info("Week Starting For Second week\n ----------------------");
		for (int i = 7,j=0; i < 14; j++,i++) {

			info(day_list.get(j)+"---"+al.get(i));

		}

	}
	
	
	
	//Mallik
	// After getting Concurent request will press "View OutPut" Button ,will get one browser window,
	//if perticuler item selected then will get corresponding values in table that perticuler window   
	
	//	Name of the Function: SchedulesViewOutput
	//	Desc: SchedulesConcurentRequestForViewOutput
	//	param:	1 (String,String,String)
	public void SchedulesViewOutput(String date,String target_col,String target_col_Value) throws Exception 
	{
		String str = web.document("/web:window[@title='http://*/OA_CGI/FNDWRR.exe?temp_id=*']/web:document[@index='0']").getHTML();
		str=str.replace("&lt;", "<");
		str=str.replace("&gt;", ">");
		info("flag value-->"+ShecduleConcurent.concurent(str,date,target_col,target_col_Value));
	}
	
	
}



class ShecduleConcurent {

	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;
	@ScriptService oracle.oats.scripting.modules.applet.api.AppletService applet;
	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;
	@ScriptService oracle.oats.scripting.modules.datatable.api.DataTableService datatable;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static String concurent(String content,String item_pass,String cor_value,String out1) throws Exception {
		File file = new File("filename.txt");
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		Map m1 = new HashMap();
		FileReader fr = new FileReader(file);
		BufferedReader br1 = new BufferedReader(fr);
		String strLine;
		int count =0;
		Scanner input = new Scanner(file);
		while (input.hasNextLine()) {
			String line = input.nextLine();
			count++;
		}
		System.out.println("count ===="+count);
		String line = "";
		int lineNo;
		String test="";

		for (lineNo = 1; lineNo < (count) &&  (line = br1.readLine())!=null; lineNo++) {
			if (lineNo > 6) {
				test=line	;
				String date="";
				String Currency_Code="";
				String Total_Cash="";
				String Normalization_Cash="";
				String Account_Revenue="";
				String Adjustment="";
				String Accrued_Asset="";
				ArrayList all_items=new ArrayList();
				if(test!=null)
				{
					if(!test.substring(1,11).trim().equals(""))
					{
						date = (test.substring(1, 11)).trim();				//0
						Currency_Code = test.substring(12, 22).trim();			//1
						Total_Cash  = test.substring(23, 37).trim();		//2
						Normalization_Cash  = test.substring(38, 52).trim();		//3
						Account_Revenue   = test.substring(53, 67).trim();		//4
						Adjustment   = test.substring(68, 82).trim();	//5
						Accrued_Asset   = test.substring(83, 97).trim();	//6
						String[] allItem_info={date,Currency_Code,Total_Cash,Normalization_Cash,Account_Revenue,Adjustment,
								Accrued_Asset};
						m1.put(date, allItem_info);
					}
				}
			}
		}

		String flag=ma1(m1, item_pass, cor_value,out1);

		return flag;

	}

	public static String ma1(Map m,String date,String cor_value,String out1)
	{
		Boolean flag=false;
		for (Iterator it = m.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			//		      System.out.println("keys ---"+key);
			if(date.equalsIgnoreCase(key))
			{
				System.out.println("if loop "+key);
				System.out.println(" value --->"+m.get(key));
				String args[]=(String[]) m.get(key);

				if(cor_value.equalsIgnoreCase("Currency Code"))
				{
					System.out.println("Date  	:"+date);
					System.out.println("Currency Code  : "+args[1]);
					if(out1.equalsIgnoreCase(args[1]))
					{
						flag=true;
					}
					System.out.println("flag --->"+flag);
				}
				if(cor_value.equalsIgnoreCase("Total Cash"))
				{
					System.out.println("Date  	:"+date);
					System.out.println("Total Cash  : "+args[2]);
					if(out1.equalsIgnoreCase(args[2]))
					{
						flag=true;
					}
				}
				if(cor_value.equalsIgnoreCase("Normalization Cash"))
				{
					System.out.println("Date  	:"+date);
					System.out.println("Normalization Cash   : "+args[3]);
					if(out1.equalsIgnoreCase(args[3]))
					{
						flag=true;
					}
				}
				if(cor_value.equalsIgnoreCase("Account Revenue"))
				{
					System.out.println("Date  	:"+date);
					System.out.println("Account Revenue    : "+args[4]);
					if(out1.equalsIgnoreCase(args[4]))
					{
						flag=true;
					}
				}
				if(cor_value.equalsIgnoreCase("Adjustment"))
				{
					System.out.println("Date  	:"+date);
					System.out.println("Adjustment    : "+args[5]);
					if(out1.equalsIgnoreCase(args[5]))
					{
						flag=true;
					}
				}
				if(cor_value.equalsIgnoreCase("Accrued Asset"))
				{
					System.out.println("Date  	:"+date);
					System.out.println("Accrued Asset  : "+args[6]);
					if(out1.equalsIgnoreCase(args[6]))
					{
						flag=true;
					}
				}
			}
		}

		return flag.toString();
	}
}


class MinMaxConcurent {

	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;
	@ScriptService oracle.oats.scripting.modules.applet.api.AppletService applet;
	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;
	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	@ScriptService oracle.oats.scripting.modules.datatable.api.DataTableService datatable;


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static String concurent(String content,String item_pass,String cor_value,String out1) throws Exception {
		File file = new File("filename.txt");
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		Map m1 = new HashMap();
		FileReader fr = new FileReader(file);
		BufferedReader br1 = new BufferedReader(fr);
		String strLine;
		int count =0;
		Scanner input = new Scanner(file);
		while (input.hasNextLine()) {
			String line = input.nextLine();
			count++;
		}
		System.out.println("count ===="+count);
		String line = "";
		int lineNo;
		String test="";

		for (lineNo = 1; lineNo < (count-3) &&  (line = br1.readLine())!=null; lineNo++) {
			if (lineNo > 44) {
				test=line	;
		String Item = "";
		String Category = "";
		String Min_Quantity = "";
		String Max_Quantity = "";
		String OnHand_Quantity = "";
		String Supplay_Quantity = "";
		String Demand_Quantity = "";
		String available_Quantity = "";
		String Minimum = "";
		String Maximum = "";
		String Multiple = "";
		String Reorder_Quantity = "";
		ArrayList all_items=new ArrayList();
		
		if(test!=null)
		{
			if(!test.substring(1,5).trim().equals(""))
			{
				Item = (test.substring(1, 21)).trim();				//0
				Category = test.substring(22, 34).trim();			//1
				Min_Quantity = test.substring(35, 48).trim();		//2
				Max_Quantity = test.substring(49, 62).trim();		//3
				OnHand_Quantity = test.substring(63, 76).trim();		//4
				Supplay_Quantity = test.substring(77, 90).trim();	//5
				Demand_Quantity = test.substring(91, 104).trim();	//6
				available_Quantity = test.substring(105, 118).trim();//7
				Minimum = test.substring(119, 132).trim();			//8
				Maximum = test.substring(133, 146).trim();			//9
				Multiple = test.substring(147, 160).trim();			//10
				Reorder_Quantity = test.substring(161, 174).trim();	//11
				String[] allItem_info={Item,Category,Min_Quantity,Max_Quantity,OnHand_Quantity,Supplay_Quantity,
						Demand_Quantity,available_Quantity,Minimum,Maximum,Multiple,Reorder_Quantity};
				m1.put(Item, allItem_info);
			}
		}
			}
		}
			String flag=ma1(m1, item_pass, cor_value,out1);
		return flag;
	}
	public static String ma1(Map m,String item,String cor_value,String out1)
	{
		Boolean flag=false;
		for (Iterator it = m.keySet().iterator(); it.hasNext();) {
		      String key = (String) it.next();
		      if(item.equalsIgnoreCase(key))
		      {
		    	  System.out.println("if loop "+key);
		    	  System.out.println(" value --->"+m.get(key));
		    	  String args[]=(String[]) m.get(key);
		    	  if(cor_value.equalsIgnoreCase("Category"))
		    	  {
		    		  System.out.println("ITEM  	:"+item);
		    		  System.out.println("Category  : "+args[1]);
		    		  if(out1.equalsIgnoreCase(args[1]))
		    		  {
		    			  flag=true;
		    		  }
		    		  System.out.println("flag --->"+flag);
		    	  }
		    	  if(cor_value.equalsIgnoreCase("Minimum Quantity"))
		    	  {
		    		  System.out.println("ITEM  	:"+item);
		    		  System.out.println("Min_Quantity  : "+args[2]);
		    		  if(out1.equalsIgnoreCase(args[2]))
		    		  {
		    			  flag=true;
		    		  }
		    	  }
		    	  if(cor_value.equalsIgnoreCase("Maximum Quantity"))
		    	  {
		    		  System.out.println("ITEM  	:"+item);
		    		  System.out.println("Max_Quantity  : "+args[3]);
		    		  if(out1.equalsIgnoreCase(args[3]))
		    		  {
		    			  flag=true;
		    		  }
		    	  }
		    	  if(cor_value.equalsIgnoreCase("On Hand Quantity"))
		    	  {
		    		  System.out.println("ITEM  	:"+item);
		    		  System.out.println("OnHand_Quantity  : "+args[4]);
		    		  if(out1.equalsIgnoreCase(args[4]))
		    		  {
		    			  flag=true;
		    		  }
		    	  }
		    	  if(cor_value.equalsIgnoreCase("Supplay Quantity"))
		    	  {
		    		  System.out.println("ITEM  	:"+item);
		    		  System.out.println("Supplay_Quantity  : "+args[5]);
		    		  if(out1.equalsIgnoreCase(args[5]))
		    		  {
		    			  flag=true;
		    		  }
		    	  }
		    	  if(cor_value.equalsIgnoreCase("Demand Quantity"))
		    	  {
		    		  System.out.println("ITEM  	:"+item);
		    		  System.out.println("Demand_Quantity  : "+args[6]);
		    		  if(out1.equalsIgnoreCase(args[6]))
		    		  {
		    			  flag=true;
		    		  }
		    	  }
		    	  if(cor_value.equalsIgnoreCase("available Quantity"))
		    	  {
		    		  System.out.println("ITEM  	:"+item);
		    		  System.out.println("available_Quantity  : "+args[7]);
		    		  if(out1.equalsIgnoreCase(args[7]))
		    		  {
		    			  flag=true;
		    		  }
		    	  }
		    	  if(cor_value.equalsIgnoreCase("Minimum"))
		    	  {
		    		  System.out.println("ITEM  	:"+item);
		    		  System.out.println("Minimum  : "+args[8]);
		    		  if(out1.equalsIgnoreCase(args[8]))
		    		  {
		    			  flag=true;
		    		  }
		    	  }
		    	  if(cor_value.equalsIgnoreCase("Maximum"))
		    	  {
		    		  System.out.println("ITEM  	:"+item);
		    		  System.out.println("Maximum  : "+args[9]);
		    		  if(out1.equalsIgnoreCase(args[9]))
		    		  {
		    			  flag=true;
		    		  }
		    	  }
		    	  if(cor_value.equalsIgnoreCase("Multiple"))
		    	  {
		    		  System.out.println("ITEM  	:"+item);
		    		  System.out.println("Multiple  : "+args[10]);
		    		  if(out1.equalsIgnoreCase(args[10]))
		    		  {
		    			  flag=true;
		    		  }
		    	  }
		    	  if(cor_value.equalsIgnoreCase("Reorder Quantity"))
		    	  {
		    		  System.out.println("ITEM  	:"+item);
		    		  System.out.println("Reorder_Quantity  : "+args[11]);
		    		  if(out1.equalsIgnoreCase(args[11]))
		    		  {
		    			  flag=true;
		    		  }
		    	  }
		      }
		    }

		return flag.toString();
	}
}
