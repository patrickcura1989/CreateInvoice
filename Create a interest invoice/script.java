import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.formsFT.api.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lib.*;

public class script extends IteratingVUserScript {
	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;
	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;
	@ScriptService oracle.oats.scripting.modules.datatable.api.DataTableService datatable;
	
	@FunctionLibrary("GENLIB") lib.ebsqafwk.GENLIB gENLIB;
	@FunctionLibrary("WEBTABLELIB") lib.ebsqafwk.WEBTABLELIB wEBTABLELIB;
	@FunctionLibrary("EBSLibrary") lib.oracle.oats.scripting.ebs.EBSLibrary eBSLibrary;
	
	public void initialize() throws Exception 
	{	
		//Initializing all required objects
		browser.launch();
                getVariables().set("CURR_SCRIPT_FOLDER",getScriptPackage().getScriptPath().replace(getScriptPackage().getScriptName()+".jwg",""));
	}
	

			  
     
     /**
      * Add code to be executed each iteration for this virtual user.
      */

	public void run() throws Exception 
	{	
		
		
	
		 beginStep("Prompt_URL");{
			eBSLibrary.oracle_prompt_url();
			gENLIB.oracle_prompt_jtt_url();
		 }endStep("Prompt_URL");

		 beginStep("Login_OAF");{
			eBSLibrary.oracle_launch_php_url();
			eBSLibrary.oracle_php_login("operations","welcome");
		 }endStep("Login_OAF");

		 beginStep("Navigate_OAF");{
			eBSLibrary.oracle_homepage_nav("Payables, Vision Operations (USA)","Setup,Options","Payables Options");
		 }endStep("Navigate_OAF");

		 beginStep("Update_Setup_Payable_Options");{
			gENLIB.formSelectLOV("AP_SYSTEM_PARAMETERS_QF_OPERATING_UNIT_0","Vision Operations");
			forms.button("//forms:button[(@name='AP_SYSTEM_PARAMETERS_QF_FIND_0')]").click();
			forms.tab("//forms:tab[(@name='ALTERNATE_REGIONS')]").select("Interest");
			forms.checkBox("//forms:checkBox[(@name='AP_SYSTEM_PARAMETERS_AUTO_CALCULATE_INTEREST_FLAG_0')]").check(true);
			forms.textField("//forms:textField[(@name='AP_SYSTEM_PARAMETERS_INTEREST_TOLERANCE_AMOUNT_0')]").setText("0.00");
			forms.textField("//forms:textField[(@name='AP_SYSTEM_PARAMETERS_INTEREST_CODE_COMB_CONC_0')]").setText("01-000-1230-0000-000");
			forms.textField("//forms:textField[(@name='AP_SYSTEM_PARAMETERS_INTEREST_CODE_COMB_CONC_0')]").invokeSoftKey("NEXT_FIELD");
			forms.textField("//forms:textField[(@name='AP_SYSTEM_PARAMETERS_INTEREST_ACCTS_PAY_CONC_0')]").setText("01-000-1320-0000-000");
			forms.textField("//forms:textField[(@name='AP_SYSTEM_PARAMETERS_INTEREST_ACCTS_PAY_CONC_0')]").invokeSoftKey("NEXT_FIELD");
			forms.window("//forms:window[(@name='AP_SYSTEM_PARAMETERS')]").selectMenu("File|Save");
		 }endStep("Update_Setup_Payable_Options");

		 beginStep("Close_Current_Form");{
			gENLIB.closeForm();
		 }endStep("Close_Current_Form");

		 beginStep("Navigate_Forms");{
			eBSLibrary.oracle_form_initial_condition();
			eBSLibrary.oracle_navigator_select("Setup,Options,Payables System Setup");
		 }endStep("Navigate_Forms");

		 beginStep("Update_Setup_Payable_System");{
			forms.checkBox("//forms:checkBox[(@name='AP_PRODUCT_SETUP_AUTO_CALCULATE_INTEREST_FLAG_0')]").check(true);
			forms.window("//forms:window[(@name='AP_PRODUCT_SETUP')]").selectMenu("File|Save");
		 }endStep("Update_Setup_Payable_System");

		 beginStep("Close_Current_Form");{
			gENLIB.closeForm();
		 }endStep("Close_Current_Form");

		 beginStep("Navigate_Forms");{
			eBSLibrary.oracle_form_initial_condition();
			eBSLibrary.oracle_navigator_select("Setup,Payment,Interest Rates");
		 }endStep("Navigate_Forms");

		 beginStep("Define_Interest_Rates");{
		beginStep("iterate");{
			 String setLine="0";
			gENLIB.setProperty("setLine",setLine);
			forms.textField("//forms:textField[(@name='INTEREST_PERIODS_ANNUAL_INTEREST_RATE_"+getVar("{{setLine}}")+"')]").setText("10");
			forms.textField("//forms:textField[(@name='INTEREST_PERIODS_START_DATE_"+getVar("{{setLine}}")+"')]").setText("04-JAN-2004");
			forms.textField("//forms:textField[(@name='INTEREST_PERIODS_END_DATE_"+getVar("{{setLine}}")+"')]").setText(gENLIB.getSysDate("DEFAULT","dd-MMM-yyyy",+30));
		}endStep("iterate");
			forms.window("//forms:window[(@name='INTEREST_PERIODS')]").selectMenu("File|Save");
		 }endStep("Define_Interest_Rates");

		 beginStep("Close_Current_Form");{
			gENLIB.closeForm();
		 }endStep("Close_Current_Form");

		 beginStep("Navigate_Forms");{
			eBSLibrary.oracle_form_initial_condition();
			eBSLibrary.oracle_navigator_select("Invoices,Entry,Invoices");
		 }endStep("Navigate_Forms");

		 beginStep("Create_Invoice_Header");{
		beginStep("iterate");{
			 String setLine="0";
			gENLIB.setProperty("setLine",setLine);
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_OPERATING_UNIT_"+getVar("{{setLine}}")+"')]").setText("Vision Operations");
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_INVOICE_TYPE_"+getVar("{{setLine}}")+"')]").setText("Standard");
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_VENDOR_NAME_"+getVar("{{setLine}}")+"')]").setText("Advanced Network Devices");
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_VENDOR_NUMBER_"+getVar("{{setLine}}")+"')]").setText("1013");
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_VENDOR_SITE_CODE_"+getVar("{{setLine}}")+"')]").setText("CHICAGO");
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_INVOICE_DATE_"+getVar("{{setLine}}")+"')]").setText(gENLIB.getSysDate("DEFAULT","dd-MMM-yyyy",-10));
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_INVOICE_NUM_"+getVar("{{setLine}}")+"')]").setText("InviS-1");
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_INVOICE_CURRENCY_CODE_"+getVar("{{setLine}}")+"')]").setText("USD");
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_INVOICE_AMOUNT_DSP_"+getVar("{{setLine}}")+"')]").setText("1000");
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_GL_DATE_"+getVar("{{setLine}}")+"')]").setText(gENLIB.getSysDate("DEFAULT","dd-MMM-yyyy",0));
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_PAYMENT_CURRENCY_CODE_"+getVar("{{setLine}}")+"')]").setText("USD");
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_TERMS_NAME_"+getVar("{{setLine}}")+"')]").setText("Immediate");
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_IBY_PAYMENT_METHOD_"+getVar("{{setLine}}")+"')]").setText("Check");
		}endStep("iterate");
			forms.window("//forms:window[(@name='INVOICES_SUM_FOLDER_WINDOW')]").selectMenu("File|Save");
		 }endStep("Create_Invoice_Header");

		 beginStep("Create_Invoice_Line");{
			forms.tab("//forms:tab[(@name='TAB_INVOICE_SUM_REGIONS')]").select("Lines");
		beginStep("iterate");{
			 String setLine="0";
			gENLIB.setProperty("setLine",setLine);
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_LINE_NUMBER_"+getVar("{{setLine}}")+"')]").setText("1");
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_LINE_TYPE_"+getVar("{{setLine}}")+"')]").setText("Item");
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_AMOUNT_DISP_"+getVar("{{setLine}}")+"')]").setText("750");
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_DEFAULT_ACCOUNT_"+getVar("{{setLine}}")+"')]").setText("01-110-7440-0000-000");
			gENLIB.formShowField("INVOICES_SUM_FOLDER_WINDOW","Generate Distributions");
			forms.list("//forms:list[(@name='LINE_SUM_FOLDER_GENERATE_DISTRIBUTIONS_"+getVar("{{setLine}}")+"')]").selectItem("Yes");
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_GL_DATE_"+getVar("{{setLine}}")+"')]").setText(gENLIB.getSysDate("DEFAULT","dd-MMM-yyyy",0));
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_TAX_CLASSIFICATION_CODE_"+getVar("{{setLine}}")+"')]").setText("");
		}endStep("iterate");
			forms.window("//forms:window[(@name='INVOICES_SUM_FOLDER_WINDOW')]").selectMenu("File|Save");
		 }endStep("Create_Invoice_Line");

		 beginStep("Verify_Inv_Distributions");{
			forms.tab("//forms:tab[(@name='TAB_INVOICE_SUM_REGIONS')]").select("2 Lines");
		beginStep("iterate");{
			HashMap <String,String>search86934 = new HashMap<String,String>();
			search86934.put("LINE_SUM_FOLDER_LINE_NUMBER_0","1");
			String setLine=gENLIB.formTableSearch(search86934,5);
			gENLIB.setProperty("setLine",setLine);
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_LINE_NUMBER_"+getVar("{{setLine}}")+"')]"
).setFocus();
		}endStep("iterate");
			forms.button("//forms:button[(@name='LINE_SUM_CONTROL_DISTRIBUTIONS_0')]").click();
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_INVOICE_LINE_NUMBER_0')]","1");
		beginStep("iterate");{
			HashMap <String,String>search29385 = new HashMap<String,String>();
			search29385.put("D_SUM_FOLDER_DISTRIBUTION_LINE_NUMBER_0","1");
			String setLine=gENLIB.formTableSearch(search29385,14);
			gENLIB.setProperty("setLine",setLine);
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_DISTRIBUTION_LINE_NUMBER_"+getVar("{{setLine}}")+"')]","1");
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_LINE_TYPE_"+getVar("{{setLine}}")+"')]","Item");
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_AMOUNT_"+getVar("{{setLine}}")+"')]","750.00");
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_DIST_CODE_COMBINATION_DISP_"+getVar("{{setLine}}")+"')]","01-110-7440-0000-000");
		}endStep("iterate");
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_DISTRIBUTION_CLASS_DSP_0')]","Preview");
			forms.window("//forms:window[(@name='DIST_SUMMARY')]").selectMenu("File|Save");
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_DISTRIBUTION_CLASS_DSP_0')]","Saved");
		 }endStep("Verify_Inv_Distributions");

		 beginStep("Close_Current_Form");{
			gENLIB.closeForm();
		 }endStep("Close_Current_Form");

		 beginStep("Create_Invoice_Line");{
			forms.tab("//forms:tab[(@name='TAB_INVOICE_SUM_REGIONS')]").select("Lines");
		beginStep("iterate");{
			 String setLine="1";
			gENLIB.setProperty("setLine",setLine);
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_LINE_NUMBER_"+getVar("{{setLine}}")+"')]").setText("2");
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_LINE_TYPE_"+getVar("{{setLine}}")+"')]").setText("Item");
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_AMOUNT_DISP_"+getVar("{{setLine}}")+"')]").setText("250");
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_DEFAULT_ACCOUNT_"+getVar("{{setLine}}")+"')]").setText("01-110-7680-0000-000");
			forms.list("//forms:list[(@name='LINE_SUM_FOLDER_GENERATE_DISTRIBUTIONS_"+getVar("{{setLine}}")+"')]").selectItem("Yes");
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_GL_DATE_"+getVar("{{setLine}}")+"')]").setText(gENLIB.getSysDate("DEFAULT","dd-MMM-yyyy",0));
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_TAX_CLASSIFICATION_CODE_"+getVar("{{setLine}}")+"')]").setText("");
		}endStep("iterate");
			forms.window("//forms:window[(@name='INVOICES_SUM_FOLDER_WINDOW')]").selectMenu("File|Save");
		 }endStep("Create_Invoice_Line");

		 beginStep("Verify_Inv_Distributions");{
			forms.tab("//forms:tab[(@name='TAB_INVOICE_SUM_REGIONS')]").select("2 Lines");
		beginStep("iterate");{
			HashMap <String,String>search34024 = new HashMap<String,String>();
			search34024.put("LINE_SUM_FOLDER_LINE_NUMBER_0","2");
			String setLine=gENLIB.formTableSearch(search34024,5);
			gENLIB.setProperty("setLine",setLine);
			forms.textField("//forms:textField[(@name='LINE_SUM_FOLDER_LINE_NUMBER_"+getVar("{{setLine}}")+"')]"
).setFocus();
		}endStep("iterate");
			forms.button("//forms:button[(@name='LINE_SUM_CONTROL_DISTRIBUTIONS_0')]").click();
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_INVOICE_LINE_NUMBER_0')]","2");
		beginStep("iterate");{
			HashMap <String,String>search60012 = new HashMap<String,String>();
			search60012.put("D_SUM_FOLDER_DISTRIBUTION_LINE_NUMBER_0","1");
			String setLine=gENLIB.formTableSearch(search60012,14);
			gENLIB.setProperty("setLine",setLine);
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_DISTRIBUTION_LINE_NUMBER_"+getVar("{{setLine}}")+"')]","1");
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_LINE_TYPE_"+getVar("{{setLine}}")+"')]","Item");
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_AMOUNT_"+getVar("{{setLine}}")+"')]","250.00");
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_DIST_CODE_COMBINATION_DISP_"+getVar("{{setLine}}")+"')]","01-110-7680-0000-000");
		}endStep("iterate");
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_DISTRIBUTION_CLASS_DSP_0')]","Preview");
			forms.window("//forms:window[(@name='DIST_SUMMARY')]").selectMenu("File|Save");
			gENLIB.formVerifyEdit("//forms:textField[(@name='D_SUM_FOLDER_DISTRIBUTION_CLASS_DSP_0')]","Saved");
		 }endStep("Verify_Inv_Distributions");

		 beginStep("Close_Current_Form");{
			gENLIB.closeForm();
		 }endStep("Close_Current_Form");

		 beginStep("Create_Validate_Invoice");{
			forms.tab("//forms:tab[(@name='TAB_INVOICE_SUM_REGIONS')]").select("1 General");
			forms.button("//forms:button[( @name='INV_SUM_CONTROL_ACTIONS_0' )]").click();
			// wait window is not required for forms;
			forms.checkBox("//forms:checkBox[( @name='INV_SUM_ACTIONS_APPROVE_0' )]").check(true);
			forms.button("//forms:button[( @name='INV_SUM_ACTIONS_OK_BUTTON_0' )]").click();
			// wait window is not required for forms;
			forms.button("//forms:button[( @name='INV_SUM_CONTROL_ACTIONS_0' )]").click();
			// wait window is not required for forms;
			forms.checkBox("//forms:checkBox[( @name='INV_SUM_ACTIONS_CREATE_ACCOUNTING_0' )]").check(true);
			forms.radioButton("//forms:radioButton[( @name='INV_SUM_ACTIONS_ACCOUNTING_DETAILS_FINAL_POST_0' )]").select();
			forms.button("//forms:button[( @name='INV_SUM_ACTIONS_OK_BUTTON_0' )]").click();
			forms.choiceBox("//forms:choiceBox").clickButton("OK");
		 }endStep("Create_Validate_Invoice");

		 beginStep("Close_All_Forms");{
			gENLIB.closeForms();
		 }endStep("Close_All_Forms");

		 beginStep("Navigate_Forms");{
			eBSLibrary.oracle_form_initial_condition();
			eBSLibrary.oracle_navigator_select("Payments,Entry,Payments");
		 }endStep("Navigate_Forms");

		 beginStep("Create_Inv_Payment_Hdr_AP");{
		beginStep("iterate");{
			 String setLine="0";
			gENLIB.setProperty("setLine",setLine);
			gENLIB.formSelectLOV("name='PAY_SUM_FOLDER_CURRENT_VENDOR_NAME_"+getVar("{{setLine}}")+"'","Advanced Network Devices");
			gENLIB.formSelectLOV("","CHICAGO");
			forms.textField("//forms:textField[( @name='PAY_SUM_FOLDER_CHECK_DATE_"+getVar("{{setLine}}")+"' )]").setText(gENLIB.getSysDate("DEFAULT","dd-MMM-yyyy",0));
			gENLIB.formSelectLOV("PAY_SUM_FOLDER_CURRENT_BANK_ACCOUNT_NAME_"+getVar("{{setLine}}")+"","BofA-204");
			gENLIB.formSelectLOV("name='PAY_SUM_FOLDER_PAYMENT_PROFILE_NAME_"+getVar("{{setLine}}")+"'","Standard Check Format");
			String documentNumber1=forms.textField("//forms:textField[(@name='PAY_SUM_FOLDER_CHECK_NUMBER_"+getVar("{{setLine}}")+"')]").getText();
			gENLIB.setProperty("documentNumber1",documentNumber1);
			 info("documentNumber1  ::::::>"+documentNumber1);
		}endStep("iterate");
		 }endStep("Create_Inv_Payment_Hdr_AP");

		 beginStep("Create_Invoice_Pymnt_Adjst_Inv");{
			forms.button("//forms:button[( @name='PAY_CONTROL_ENTER_INVOICES_0' )]").click();
			// wait window is not required for forms;
		beginStep("iterate");{
			 String setLine="0";
			gENLIB.setProperty("setLine",setLine);
			gENLIB.formSelectLOV("name='ADJ_INV_PAY_INVOICE_NUM_"+getVar("{{setLine}}")+"'","InviS-1");
		}endStep("iterate");
			forms.window("//forms:window[(@name='ADJ_INV_PAY')]").clickToolBarButton("Save");
			forms.window("//forms:window[( @name='ADJ_INV_PAY' )]"
).close();
			// wait window is not required for forms;
			String DocumentNumber1=forms.textField("//forms:textField[(@name='PAY_SUM_FOLDER_CHECK_NUMBER_0')]").getText();
			gENLIB.setProperty("DocumentNumber1",DocumentNumber1);
			 info("DocumentNumber1  ::::::>"+DocumentNumber1);
			forms.button("//forms:button[(@name='PAY_CONTROL_ACTIONS_0')]").click();
			forms.checkBox("//forms:checkBox[(@name='PAY_ACTIONS_CREATE_ACCOUNTING_0')]").check(true);
			forms.radioButton("//forms:radioButton[(@name='PAY_ACTIONS_ACCOUNTING_DETAILS_FINAL_0')]").select();
			forms.button("//forms:button[(@name='PAY_ACTIONS_OK_BUTTON_0')]").click();
			forms.choiceBox("//forms:choiceBox").clickButton("OK");
		 }endStep("Create_Invoice_Pymnt_Adjst_Inv");

		 beginStep("Verify_Payments_Common");{
		beginStep("iterate");{
			 String setLine="0";
			gENLIB.setProperty("setLine",setLine);
			gENLIB.formVerifyEdit("//forms:textField[(@name='VIEW_INV_PAY_INVOICE_NUM_"+getVar("{{setLine}}")+"')]","InviS-1");
			gENLIB.formVerifyEdit("//forms:textField[(@name='VIEW_INV_PAY_INVOICE_AMOUNT_"+getVar("{{setLine}}")+"')]","1,000.00");
			gENLIB.formVerifyEdit("//forms:textField[(@name='VIEW_INV_PAY_AMOUNT_"+getVar("{{setLine}}")+"')]","1,000.00");
		}endStep("iterate");
		 }endStep("Verify_Payments_Common");

		 beginStep("Menu_Select_Form");{
			gENLIB.formMenuSelect("Tools|View Accounting");
		 }endStep("Menu_Select_Form");

		 beginStep("Verify_View_Accounting");{
			web.window("/web:window[@title='Subledger Journal Entry Lines']").waitForPage(Integer.parseInt(getVar("{{WAIT_PAGE}}")),true);
		beginStep("iterate");{
			 String setLine="0";
			gENLIB.setProperty("setLine",setLine);
			List <String[]> action35537 = new ArrayList<String[]>();
			action35537.add( new String[]{"VERIFY","TEXT","Account","","","","01-000-1110-0000-000"});
			action35537.add( new String[]{"VERIFY","TEXT","Accounting Class","","","","Cash Clearing"});
			action35537.add( new String[]{"VERIFY","TEXT","Accounted CR","","","","1,000.00"});
			setLine=wEBTABLELIB.tableactions("Select",setLine,action35537);
			gENLIB.setProperty("setLine",setLine);
		}endStep("iterate");
		 }endStep("Verify_View_Accounting");

		 beginStep("Verify_View_Accounting");{
			web.window("/web:window[@title='Subledger Journal Entry Lines']").waitForPage(Integer.parseInt(getVar("{{WAIT_PAGE}}")),true);
		beginStep("iterate");{
			 String setLine="1";
			gENLIB.setProperty("setLine",setLine);
			List <String[]> action2625 = new ArrayList<String[]>();
			action2625.add( new String[]{"VERIFY","TEXT","Account","","","","01-000-2210-0000-000"});
			action2625.add( new String[]{"VERIFY","TEXT","Accounting Class","","","","Liability"});
			action2625.add( new String[]{"VERIFY","TEXT","Accounted DR","","","","1,000.00"});
			setLine=wEBTABLELIB.tableactions("Select",setLine,action2625);
			gENLIB.setProperty("setLine",setLine);
		}endStep("iterate");
			web.link("/web:window[@title='Subledger Journal Entry Lines']/web:document[@index='0']/web:a[ @text='Close Window']"
).click();
		 }endStep("Verify_View_Accounting");

		 beginStep("Close_OAF_Page");{
		 }endStep("Close_OAF_Page");

		 beginStep("Close_All_Forms");{
			gENLIB.closeForms();
		 }endStep("Close_All_Forms");

		 beginStep("Navigate_Forms");{
			eBSLibrary.oracle_form_initial_condition();
			eBSLibrary.oracle_navigator_select("Invoices,Entry,Invoices");
		 }endStep("Navigate_Forms");

		 beginStep("Verify_Invoice_WB");{
			forms.window("//forms:window[(@name='INVOICES_SUM_FOLDER_WINDOW')]").selectMenu("View|Query By Example|Enter");
			forms.textField("//forms:textField[(@name='INV_SUM_FOLDER_INVOICE_NUM_0')]").setText("InviS-1");
			forms.window("//forms:window[(@name='INVOICES_SUM_FOLDER_WINDOW')]").selectMenu("View|Query By Example|Run");
		beginStep("iterate");{
			 String setLine="0";
			gENLIB.setProperty("setLine",setLine);
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_OPERATING_UNIT_"+getVar("{{setLine}}")+"')]","Vision Operations");
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_INVOICE_TYPE_"+getVar("{{setLine}}")+"')]","Standard");
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_VENDOR_NAME_"+getVar("{{setLine}}")+"')]","Advanced Network Devices");
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_VENDOR_NUMBER_"+getVar("{{setLine}}")+"')]","1013");
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_VENDOR_SITE_CODE_"+getVar("{{setLine}}")+"')]","CHICAGO");
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_INVOICE_DATE_"+getVar("{{setLine}}")+"')]",gENLIB.getSysTime("DEFAULT","CC dd-MMM-yyyy",-10,0,0));
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_INVOICE_AMOUNT_DSP_"+getVar("{{setLine}}")+"')]","1,000.00");
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_GL_DATE_"+getVar("{{setLine}}")+"')]",gENLIB.getSysTime("DEFAULT","CC dd-MMM-yyyy",0,0,0));
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_PAYMENT_CURRENCY_CODE_"+getVar("{{setLine}}")+"')]","USD");
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_IBY_PAYMENT_METHOD_"+getVar("{{setLine}}")+"')]","Check");
		}endStep("iterate");
		 }endStep("Verify_Invoice_WB");

		 beginStep("Verify_Invoice_Line");{
			forms.tab("//forms:tab[(@name='TAB_INVOICE_SUM_REGIONS')]").select("2 Lines");
		beginStep("iterate");{
			HashMap <String,String>search35993 = new HashMap<String,String>();
			search35993.put("LINE_SUM_FOLDER_LINE_NUMBER_0","1");
			String setLine=gENLIB.formTableSearch(search35993,5);
			gENLIB.setProperty("setLine",setLine);
			gENLIB.formVerifyEdit("//forms:textField[(@name='LINE_SUM_FOLDER_LINE_NUMBER_"+getVar("{{setLine}}")+"')]","1");
			gENLIB.formVerifyEdit("//forms:textField[(@name='LINE_SUM_FOLDER_LINE_TYPE_"+getVar("{{setLine}}")+"')]","Item");
			gENLIB.formVerifyEdit("//forms:textField[(@name='LINE_SUM_FOLDER_AMOUNT_DISP_"+getVar("{{setLine}}")+"')]","750.00");
			gENLIB.formVerifyEdit("//forms:textField[(@name='LINE_SUM_FOLDER_DEFAULT_ACCOUNT_"+getVar("{{setLine}}")+"')]","01-110-7440-0000-000");
		}endStep("iterate");
		 }endStep("Verify_Invoice_Line");

		 beginStep("Verify_Invoice_General");{
			forms.tab("//forms:tab[(@name='TAB_INVOICE_SUM_REGIONS')]").select("1 General");
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_APPROVAL_STATUS_DISPLAY_0')]","Validated");
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_WFAPPROVAL_STATUS_DSP_0')]","Not Required");
			gENLIB.formVerifyEdit("//forms:textField[(@name='INV_SUM_FOLDER_HOLDS_COUNT_DISPLAY_0')]","0");
		 }endStep("Verify_Invoice_General");

		 beginStep("Close_All_Forms");{
			gENLIB.closeForms();
		 }endStep("Close_All_Forms");

		 beginStep("Exit_Application");{
			eBSLibrary.oracle_close_all_browsers();
			gENLIB.wait(getVar("{{WAIT_NORMAL}}"),"");
		 }endStep("Exit_Application");

	
	
	}
	public void finish()throws Exception{
}
	

			public String getVar(String key)
{
		try
                                          {
		key=key.substring(2, key.length()-2);
		String value="";
		value=getVariables().get(key);
		if(value==null)
		value=gENLIB.getProperty(key);

		return value;
                                          }
		catch(Exception e){
		return null;
		}
}
}
