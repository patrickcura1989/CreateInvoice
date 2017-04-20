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
		
		
	

			 gENLIB.setProperty("WAIT_NORMAL","1");
			 gENLIB.setProperty("WAIT_OBJ","200");
			 gENLIB.setProperty("WAIT_PAGE","1");
			getScript("Create a interest invoice").run(1);
			
	}
	public void finish()throws Exception{
}
	
}
