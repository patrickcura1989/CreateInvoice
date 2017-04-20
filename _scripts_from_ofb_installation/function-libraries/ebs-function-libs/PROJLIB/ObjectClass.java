/* 
 * Class Name: ObjectClass
 * Written by : Srinivas Potnuru ( spotnuru )
 * Last  Modified On: 14Jul2011
 * Last Modified By: Srinivas Potnuru
 * ---------------------------------------------------------------------------
 * Change History
 * ---------------------------------------------------------------------------
 * Date: 15-Jul-2011
 * Resource Edited file: spotnuru
 * Modified / Created Method: Check_Web_Link( String linkname, int locaiton)
 * Description: for HRMS conversio, to check if link exists with a specific location.
 * --------------------------------------------------------------------------- 
 * 
 * Date: 14-Jul-2011
 * Resource edited file: spotnuru
 * Modified / Created Method: HandleWebSecurity(String linkname) 
 * Description: for HRMS Conversion, to click on popped up dialog box on the first button.
 * ---------------------------------------------------------------------------
 * Date: 14-Jul-2011
 * Resource edited file: spotnuru
 * Modified / Created Method: SS_Image_Click(String linkname) 
 * Description: for HRMS Conversion
 * ---------------------------------------------------------------------------
 * Date: 14-Jul-2011
 * Given By: pkotta, piqbal
 * Resource edited file: spotnuru
 * Modified / Created Method: message(String str)
 * Description: to support the functions of  OpenScript ( Ex.: info )
 * ---------------------------------------------------------------------------
 * Date: 14-Jul-2011
 * Given By: pkotta, piqbal
 * Resource edited file: spotnuru
 * Modified / Created Constuctor: ObjectClass(WebDomService web,String docpath,IteratingVUserScript IVU)
 * Description: to support the functions of  OpenScript ( Ex.: info )
 * ---------------------------------------------------------------------------
 * Date: 14-Jul-2011
 * Given By: pkotta, piqbal
 * Resource edited file: spotnuru
 * Modified / Created Constuctor: ObjectClass(WebDomService web,IteratingVUserScript IVU)
 * Description: to support the functions of  OpenScript ( Ex.: info )
 * ---------------------------------------------------------------------------
 * Date: 14-Jul-2011
 * Resource edited file: Srinivas Potnuru ( spotnuru )  
 * Modified / Created Function: SearchFromLov() // Created this function with 2 parameters image name & search value
 * Description: This was written for hrms, conversion as they wanted for only 2 parameters, and image can be given as imagname;location
 * ---------------------------------------------------------------------------
 * Date: 14-Jul-2011
 * Resource edited file: Srinivas Potnuru ( spotnuru )  
 * Modified / Created Function: searchTableObject() // Modified
 * Description: This was written for hrms, conversion as they wanted search column to be passed as column number, 
 * required for function SearchFromLov() with 2 parameters.
 * ---------------------------------------------------------------------------
 * Date: 14-Jul-2011
 * Resource edited file: Pkotta, PIQbal, spotnuru   
 * Modified / Created Function: message(Str) // Modified
 * Description: To print message to results and console. 
 * ---------------------------------------------------------------------------
 */
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;
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

// Code added on 14-Jul-2011 , For accessing functions like info etc...
import oracle.oats.scripting.modules.basic.api.IteratingVUserScript;
import oracle.oats.scripting.modules.basic.api.ScriptService;


public class ObjectClass {
	IteratingVUserScript ivu;// Object for accessing the functions of all given by Openscript for each script.
	WebDomService web;
	String docpath;
	String form;
	DOMElement retObj;
	
	boolean searchSuccess;
	int tblcols,tblrows,tblstartrow;
	String olddocpath,oldform;
	boolean debug;
	String radioAttr;
	String checkAttr;
	DOMElement tableObj;
	DOMElement tableCell;
	DOMElement tableCellObject;
	String tableXPath;
	int tblRows,tblCols;
	@SuppressWarnings("unchecked") HashMap tblColNames;
	
	
	/*
	 * Constructor for new parameter ( IteratingVUserScript )
	 * Given By: pkotta, piqbal
	 * 
	 */
	ObjectClass(WebDomService web,IteratingVUserScript IVU)
	{
		this(web);
		ivu	=	IVU;
	}
	/*
	 * Constructor for new parameter ( IteratingVUserScript )
	 * Given By: pkotta, piqbal
	 * 
	 */	
	ObjectClass(WebDomService web,String docpath,IteratingVUserScript IVU)
	{
		this(web,docpath);
		ivu	=	IVU;
	}
	ObjectClass(WebDomService web)
	{
		this.web		=	web;
		olddocpath		=	this.docpath	=	"/web:window[@index='0']/web:document[@index='0']";
		oldform			=	this.form		=	"/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']";
		tblcols			=	0;
		tblrows			=	0;
		tblstartrow		=	0;
		searchSuccess	=	false;
		retObj			=	null;
		tableObj		=	null;
		debug			=	false;
		radioAttr		=	"";
		checkAttr		=	"";
		tableXPath		=	"";
		tblRows			=	0;
		tblCols			=	0;
		tableCell		=	null;
		tblColNames		=	null;
		tableCellObject	=	null;
	}
	

	ObjectClass(WebDomService web,String docpath)
	{
		this.web		=	web;
		olddocpath		=	this.docpath	=	docpath;
		oldform			=	this.form		=	"/web:form[@id='DefaultFormName' or @name='DefaultFormName' or @index='0']";
		tblcols			=	0;
		tblrows			=	0;
		tblstartrow		=	0;		
		searchSuccess	=	false;
		retObj			=	null;
		debug			=	false;
		tableObj		=	null;
		radioAttr		=	"";
		tableXPath		=	"";
		tblRows			=	0;
		tblCols			=	0;		
		tableCell		=	null;
		tblColNames		=	null;
		tableCellObject	=	null;
	}
	
	void setDebug(boolean val)
	{
		this.debug=val;
	}
	
	void showDebug(String str)
	{
		if(debug)
			System.out.println(str);
	}
	
	void showDebug(String show[])
	{
		if(debug)
		{
			for(int i=0;i<show.length;i++)
			{
				System.out.println("Value @ " +i + ":" + show[i]);
			}
		}
	}
	void showDebug(int str)
	{
		if(debug)
			System.out.println(""+str);
	}
	
	boolean isFound()
	{
		return searchSuccess;
	}
	void showInit()
	{
		System.out.println("docpath:"+docpath);
		System.out.println("form:"+form);
	}
	void setDocPath(String str)
	{
		this.olddocpath=this.docpath;
		this.docpath=str;
	}
	void restore()
	{
		this.docpath=this.olddocpath;
		this.form=this.oldform;
	}
	String getDocPath()
	{
		return this.docpath;
	}
	
	void setForm(String str)
	{
		this.oldform=this.form;
		this.form=str;
	}
	
	String getForm()
	{
		return this.form;
	}	
	void setCheckAttribute(String checkAttr)
	{
		this.checkAttr=checkAttr;
	}	
	String getCheckboxXpath() throws Exception
	{
		String ret;
		if(checkAttr.equals(""))	
			ret=docpath + form + "/web:input_checkbox[@name='"+retObj.getAttribute("name")+"']";
		else
			ret=docpath + form + "/web:input_checkbox[@"+checkAttr+"='"+retObj.getAttribute(checkAttr)+"']";
			
		showDebug(ret);
		return ret;
	}
	String getListboxXpath() throws Exception
	{
		String ret=docpath + form + "/web:select[@name='"+retObj.getAttribute("name")+"']";
		showDebug(ret);
		return ret;
	}	
	String getTableXpath() throws Exception
	{
		//web:window[@index='0' or @title='Suppliers']/web:document[@index='0']/web:table[@index='22']
		String ret="";
		if(tableObj!=null)
		{	
			ret=docpath +  "/web:table[@index='"+tableObj.getAttribute("index")+"']";
			showDebug(ret);
			tableXPath	=	ret;
		}
		return ret;
	}	
	String getImgXpath() throws Exception
	{
		String ret=docpath + form + "/web:img[@alt='"+retObj.getAttribute("alt")+"' or @id='"+retObj.getAttribute("id")+"']";
		showDebug(ret);
		return ret;		
	}
	String getBtnXPath() throws Exception
	{
		String ret=docpath + form + "/web:button[@value='"+retObj.getAttribute("value")+"']";
		showDebug(ret);
		return ret;	
	}
	String getLinkXPath() throws Exception
	{
		String ret=docpath +  "/web:a[@text='"+retObj.getAttribute("text")+"' or @index='"+retObj.getAttribute("index")+"']";
		showDebug(ret);
		return ret;	
	}
	String getTextXpath() throws Exception
	{
		String ret=docpath + form + "/web:input_text[@name='"+retObj.getAttribute("name")+"'or @index='"+retObj.getAttribute("index")+"']";
		showDebug(ret);
		return ret;		
	}
	//boolean 
	void setRadioAttribute(String radioAttr)
	{
		this.radioAttr=radioAttr;
	}
	String getRadioXpath() throws Exception
	{
		String ret;
		if(radioAttr.equals(""))	
			ret=docpath + form + "/web:input_radio[@name='"+retObj.getAttribute("name")+"']";
		else
			ret=docpath + form + "/web:input_radio[@"+radioAttr+"='"+retObj.getAttribute(radioAttr)+"']";
		showDebug(ret);
		return ret;		
	}	
	String getText() throws Exception
	{
		String ret=retObj.getAttribute("innerText");
		showDebug(ret);
		return ret;		
	}
	void click() throws Exception
	{
		if(retObj!=null)
			retObj.click();
		else
			System.out.println("Object Not Found");
			
	}

	String[]  getObjectType(String objType)
	{
		String [] str={"","",""};
		str=getObjectType(objType,false);
		return str;
	}
	String[] getObjectType(String objType,boolean isTable)
	{
		String [] str={"","",""};
		if(objType.equalsIgnoreCase("TEXT")||objType.equalsIgnoreCase("HTML_TEXT")||objType.equalsIgnoreCase("EDIT")||objType.equalsIgnoreCase("TEXTFIELD"))
		{
			str[0]="input";
			if(!isTable)
			{
				str[1]="name";
				str[2]="";				
			}
			else
			{
				str[1]="type";
				str[2]="text";				
			}	
				
		}
		else if(objType.equalsIgnoreCase("radio")||objType.equalsIgnoreCase("html_radio")||objType.equalsIgnoreCase("radio_button"))
		{
			str[0]="input";
			str[1]="value";
			str[2]="radio";
		}		
		else if(objType.equalsIgnoreCase("link")||objType.equalsIgnoreCase("html_text_link")||objType.equalsIgnoreCase("html_link"))
		{
			str[0]="a";
			str[1]="text";
			str[2]="";
		}		
		
		else if(objType.equalsIgnoreCase("check")||objType.equalsIgnoreCase("html_checkbox"))
		{
			str[0]="input";
			str[1]="type";
			str[2]="checkbox";
		}	
		else if(objType.equalsIgnoreCase("list")||objType.equalsIgnoreCase("combobox")||objType.equalsIgnoreCase("listbox")||objType.equalsIgnoreCase("html_list"))
		{
			str[0]="select";
			str[1]="";
			str[2]="";
		}	
		else if(objType.equalsIgnoreCase("image")||objType.equalsIgnoreCase("img")||objType.equalsIgnoreCase("html_rect"))
		{
			str[0]="img";
			if(!isTable)
				str[1]="alt";
			else
				str[1]="";
			str[2]="";
		}	
		else if(objType.equalsIgnoreCase("plain_text"))
		{
			str[0]="span";
			str[1]="";
			str[2]="";
		}	
		else if(objType.equalsIgnoreCase("BUTTON") || objType.equalsIgnoreCase("BTN"))
		{
			str[0]="button";
			str[1]="value";
			str[2]="";
		}		
		return str;
	}
	void searchObjectByLocation(String objtype,int loc,String name) throws Exception
	{
		
		searchSuccess=false;
		String searchAttrib="";
		String searchAttribVal="";
		String tags[]={"","",""};
		DOMElement obj=null;
		String tag="";
		
		tags=getObjectType(objtype);
		tag=tags[0];
		searchAttrib=tags[1]; 
		searchAttribVal=tags[2];
		
		showDebug("Tag: " + tag);
		showDebug("Atrribute: " + searchAttrib);
		List<DOMElement> ele = (List<DOMElement>) web.document(docpath).getElementsByTagName(tag);
		showDebug("number of elements found: " + ele.size());//debug
		DOMElement temp=null;
		
		int currentLocation = -1;
		boolean found = false;
		String getString="";
		for (int i = 0 ; i < ele.size(); i++) 
		{
			temp = (DOMElement) ele.get(i);
			getString=temp.getAttribute(searchAttrib);
			showDebug("VB:Link"+i+":"+getString);
			if(getString!=null && !"".equals(getString))
				showDebug("found : "+getString.trim().replaceAll("\\<.*?>","")); // debug
			showDebug("B:Link"+i+":"+getString);
			if (getString!=null && !getString.equals("") && name.equals(getString.trim().replaceAll("\\<.*?>",""))) 
			{//more attributes judgment can be added here
					showDebug("matched : "+temp.getAttribute(searchAttrib)); // debug
					currentLocation ++;
					if (loc == currentLocation) 
					{
							//temp.click();
							retObj=temp;
							found = true;
							searchSuccess=true;
							break;
					}
			}
			showDebug("A:Link"+i+":"+getString);
		}
		if (found == false)
			System.out.println("FAIL:element with location: " + loc + " not found");	
		else
			System.out.println("PASS:element with location: " + loc + " is found");
	}
	void searchTableObjectNext(String tablename,String SearchCol,String SearchText,String TargetCol,String TargetObjType,int objloc) throws Exception
	{
		searchTableObjectNext(0,tablename,SearchCol,SearchText,TargetCol,TargetObjType,objloc);
	}
	void searchTableObjectNext(int tblloc,String tablename,String SearchCol,String SearchText,String TargetCol,String TargetObjType,int objloc) throws Exception
	{
		while(!isFound())
		{
			searchTableObject(tblloc,tablename,SearchCol,SearchText,TargetCol,TargetObjType,objloc);
			
		}
	}
	void searchTableObject(String tablename,String SearchCol,String SearchText,String TargetCol,String TargetObjType,int objloc) throws Exception
	{
		searchTableObject(0,tablename,SearchCol,SearchText,TargetCol,TargetObjType,objloc);
	}
	@SuppressWarnings("unchecked") void searchTableObject(int tblloc,String tablename,String SearchCol,String SearchText,String TargetCol,String TargetObjType,int objloc) throws Exception
	{
		//String tablename="Select";
		//String SearchCol="*Member";
		//String SearchText="Brown, Ms. Casey";
		//String TargetCol="Approver";
		//String TargetObjType="check";
		//String TargetCol="Task";
		//String TargetObjType="text";

		//String TargetCol="Access";
		//String TargetObjType="list";
		//int objloc=0;
		searchSuccess=false;
		int tloc=-1;
		int iloc=0;
		List <DOMElement> ths = (List <DOMElement>) web.document(docpath).getElementsByTagName("th");
		String tags[]={"","",""};
		showDebug("number of elements: " + ths.size());//debug
		int iCounter=0,found=0;
		int iCols=0,iRows=0,startRow=0,dummRows=0,colRow=0,iSrcCol=0,iTargetCol=0;
		DOMElement tr,tbl;
		List<DOMElement> trs=null;
		for(int i=0;i<ths.size();i++)
		{	
			DOMElement th = (DOMElement) ths.get(i);

			startRow=0;
			dummRows=0;
			colRow=0;
			
			if(tablename.equals(th.getAttribute("innerText")) )
			{
				tloc++;
				if(tblloc!=tloc)
				{
					continue;
				}
				tr=th.getParent();// this will get the TR level objects					
				List <DOMElement> cols = (List <DOMElement>) tr.getElementsByTagName("th");
				tblcols=iCols=cols.size();
				
				List <DOMElement> childtrs = (List <DOMElement>) tr.getElementsByTagName("tr");
				List <DOMElement> childths = (List <DOMElement>) tr.getElementsByTagName("th");
				showDebug("child trs:"+childtrs.size());//debug
				showDebug("child ths:"+childths.size());//debug
				
				if(childtrs.size()>0)
				{	
					dummRows=childtrs.size();
					showDebug("Dummy rows:"+dummRows);//debug
				}
				tbl= tr.getParent();
				trs = (List <DOMElement>) tbl.getElementsByTagName("tr");
				tblrows=iRows = trs.size();//-dummRows;
				System.out.println("Rows: " + iRows);		//debug	
				System.out.println("Columns: " + iCols);//debug
				if(iCounter==iloc)
				{
					found=1;
					
					tblstartrow=startRow=dummRows+1;
					boolean isNumber	=	false; // 15-Jul-2011, for catching the exception for converting string to number
					int check =	0;
					for(int each=0;each<childths.size();each++)
					{
						showDebug("Col # "+each+":"+childths.get(each).getAttribute("innerText"));
						if(childths.get(each).getAttribute("innerText")!=null)
						{
							
							/*
							 * Added this code to check if on converting the string is it allowing to convert to number
							 */
							try
							{
								check	= Integer.parseInt(SearchCol);
								isNumber	=	true;
							}
							catch(Exception e)
							{
								showDebug("Number format Exception");
							}
							if(!isNumber)//for hrms conv @ 14-Jul-2011 by spotnuru, 15-Jul-2011
							{
								if(SearchCol.equals(childths.get(each).getAttribute("innerText").trim()))
								{
									iSrcCol=each;
								}	
							}
								
							if(TargetCol.equals(childths.get(each).getAttribute("innerText").trim()))
							{
								iTargetCol=each;
							}
						}
						
					}
					if(isNumber) //for hrms conv @ 14-Jul-2011 by spotnuru
					{
						iSrcCol=check;
					}
						
					break;						
				}	
				else	
					iCounter++;	
			}
		}
		if(found==1)
		{
			
			System.out.println("Table found");		
			System.out.println("Search Col Number:" + iSrcCol);
			System.out.println("Target Col Number:" + iTargetCol);
			int iOrgSrcCol,iOrgTargetCol;
			iOrgSrcCol=iSrcCol;
			iOrgTargetCol=iTargetCol;
			showDebug("Start Row:"+startRow);//debug
			showDebug("Total Rows:"+iRows);//debug
			for(int iStart=startRow;iStart<iRows;iStart++)
			{
				iSrcCol=iOrgSrcCol;
				showDebug("Scanning Row # "+iStart );
				List <DOMElement> tds = (List <DOMElement>) trs.get(iStart).getElementsByTagName("td");
				showDebug("Col Count:"+tds.size()+" iSrcCol:"+iSrcCol);//debug
				
				if(tds.size()<iCols)
				{
					showDebug("Different Row:"+iStart);
					continue;
				}
				showDebug("Innertext:"+tds.get(iSrcCol).getAttribute("innerText"));//debug
				
				int flg=0;
				String type="";
				int calc=0;
				int dummy	=	0;
				for(int dumb=0;dummy<iSrcCol;dummy++)
				{
					List <DOMElement> tds1 = (List <DOMElement>) tds.get(dumb).getElementsByTagName("td");
					showDebug(tds1.size());
					calc+=tds1.size()+1;
					if(tds1.size()>0)
						dumb+=tds1.size();
					else 
						dumb++;

				}
				showDebug("Calc:"+calc);
				iSrcCol=calc;
				String innerHtml=tds.get(iSrcCol).getAttribute("innerHtml");
				showDebug("Outside:"+tds.get(iSrcCol).getAttribute("innerHtml"));
				if(innerHtml.toLowerCase().contains("<select"))
				{
					type="select";
				}
				else if(innerHtml.toLowerCase().contains("<input"))
				{
					type="input";				
				}
				showDebug("Type:"+type);
				if(type!="" && tds.size()>iSrcCol)
				{
					showDebug("Entered object case"+tds.get(iSrcCol).getAttribute("innerHtml") );//debug
					List <DOMElement> childitems;
					childitems=tds.get(iSrcCol).getElementsByTagName(type);
					showDebug("Size:"+childitems.size());
					if(childitems.size()>0)
					{
						String test="";
						if(type=="input")
						{
							
							test=childitems.get(0).getAttribute("type");
							if(test.equalsIgnoreCase("hidden"))
							{
								type="";
							}	
							else
							{	
								test=childitems.get(0).getAttribute("value");
								showDebug("value:"+test);
							}
						}
						else
						{
							String rs[]=web.selectBox(docpath+form+"/web:select[@name='"+childitems.get(0).getAttribute("name")+"']").getSelectedText();
							showDebug("Value from select Box:"+rs[0]);//debug
							test=rs[0];
						}	
						if(SearchText.equals(test))
							flg=1;
					}		
				}

				showDebug("Source Col Text:\n"+tds.get(iSrcCol).getAttribute("innerText"));
				String comparetext=tds.get(iSrcCol).getAttribute("innerText");
				if((tds.size()>iSrcCol && comparetext!=null && SearchText.equals(comparetext.trim())) || flg==1)
				{
					showDebug("Search Found in row # "+iStart);//debug
					System.out.println("Search Found"); 
					String tag="",attrib="",attribval="";
					tags=getObjectType(TargetObjType,true);
					
					tag=tags[0];
					attrib=tags[1];
					attribval=tags[2];
					calc=0;
					iTargetCol=iOrgTargetCol;
					for(int dumb=0;dumb<iTargetCol;dumb++)
					{
						List<DOMElement> tds1=(List <DOMElement>)tds.get(dumb).getElementsByTagName("td");
						calc+=tds1.size();
						showDebug(tds1.size());
						if(tds1.size()>0)
						{	
							dumb+=tds1.size();
						}
					}
					iTargetCol=calc+iTargetCol;
					showDebug("Target:"+iTargetCol);
					showDebug("Tag:"+tag+" attrib"+ attrib + " attribval"+attribval);//debug
					List<DOMElement> items=(List <DOMElement>)tds.get(iTargetCol).getElementsByTagName(tag);
					showDebug("Target Column Text:\n"+tds.get(iTargetCol).getAttribute("innerHtml"));
					int checkloc=-1,ind=0;
					int objectfound=0;
					System.out.println("Items: "+ items.size()+" Tag:"+tag);//debug
					for ( ind=0;ind<items.size();ind++)
					{
						showDebug(items.get(ind).getAttribute(attrib));//debug
						if(!tag.equalsIgnoreCase("input") || (tag.equalsIgnoreCase("input")&&((DOMElement)items.get(ind)).getAttribute(attrib).equals(attribval)))
						{
							System.out.println("true condition");//debug
							checkloc++;
						}				
						if(objloc==checkloc)
						{
							objectfound=1;
							break;
						}
						
					}
					System.out.println("Index value:" + ind);//debug
					if(objectfound==1)
					{
						
						System.out.println("Object Found");
						retObj=items.get(ind);
						searchSuccess=true;
						break;

					}	
					else
					{
						System.out.println("Object is not found");
					}	
					
				}
			}	
			
		}
		else
		{
			System.out.println("Table not found");
		}		
	}
	// gets String array of text between 2 given strings 
	public String[] getText(String before,String after) throws Exception
	{
		
		ArrayList<String> al=new ArrayList<String>();
		//String docpath="/web:window[@index='0']/web:document[@index='0']";
		List <DOMElement> objs=(List <DOMElement>)web.document(docpath ).getElementsByTagName("body");
		String entiretext=objs.get(0).getAttribute("innerText");
		//String before="Internal Item Number";
		//String after="Price";
		int  startind=0,endind=0;
		int flag=0;
		int k=0;
		while(true )
		{
			startind=entiretext.indexOf(before,startind);
			endind=entiretext.indexOf(after,startind);
			showDebug(startind);//debug
			showDebug(endind);//debug
			if(startind<0 || endind<0)
			{
				break;
			}
			else
			{
				al.add(entiretext.substring(startind+before.length(),endind).trim());
				k++;
			}	
			showDebug(entiretext.substring(startind+before.length(),endind).trim());//debug
		
			startind++;
		}
		String[] rs=new String[al.size()];
		for(int i=0;i<al.size();i++)
			rs[i]=al.get(i);
		
		return rs; 
	}
	
	//checks if a particular string is available between 2 given strings
	public boolean checkText(String before,String after,String searchval) throws Exception
	{
		boolean found=false;
		//String docpath="/web:window[@index='0']/web:document[@index='0']";
		List <DOMElement> objs=(List <DOMElement>)web.document(docpath ).getElementsByTagName("body");
		String entiretext=objs.get(0).getAttribute("innerText");
		//String before="Internal Item Number";
		//String after="Price";
		int  startind=0,endind=0;
		int flag=0;
		
		while(true )
		{
			startind=entiretext.indexOf(before,startind);
			endind=entiretext.indexOf(after,startind);
			showDebug(startind);//debug
			showDebug(endind);//debug
			if(startind<0 || endind<0)
			{
				break;
			}
			else if(searchval.equalsIgnoreCase(entiretext.substring(startind+before.length(),endind).trim()))
			{
				found=true;
				break;
			}	
			showDebug(entiretext.substring(startind+before.length(),endind).trim());//debug
		
			startind++;
		}
		
		return found;
	}
	public String Rand(int size)
	{
		String retval="";
		Calendar now = Calendar.getInstance();
		String retStr=""+now.getTimeInMillis();
		retval=retStr.substring(retStr.length()-size,retStr.length());
		return retval;
	}
	@SuppressWarnings("unchecked") public boolean listBoxCheck(String obj,String exists[],String noexists[]) throws  Exception
	{

		String exists_pass="";
		String exists_fail="";
		String noexists_pass="";
		String noexists_fail="";
		HashMap actual=new HashMap();
		boolean checkfunction=true;
		
		if(!web.selectBox(obj).exists())
		{
			showDebug("Select object not found:"+obj);
			System.out.println("Object Not found.");
			return false;
			
		}	
		showDebug("Object found:"+obj);
		List <DOMElement> options = (List<DOMElement>)web.selectBox(obj).getElementsByTagName("option");
		
		showDebug("Number of elements:"+options.size());
		//retrieve elements from listbox
		for(int i=0;i<options.size();i++)
		{
			actual.put(options.get(i).getAttribute("innerText"),i);
			showDebug("Element "+i+":"+options.get(i).getAttribute("innerText"));
		}
		
		//check exists array			
		for(int i=0;i<exists.length;i++)
		{
			
			if(actual.containsKey(exists[i]))
				exists_pass+=exists[i]+",";
			else
				exists_fail+=exists[i]+",";
		}
		showDebug("After exists array");
		//check noexists array			
		for(int i=0;i<noexists.length;i++)
		{
			if(!actual.containsKey(noexists[i]))
				noexists_pass+=noexists[i]+",";
			else
				noexists_fail+=noexists[i]+",";
			
		}
		showDebug("After noexists array");
		if(exists_pass.length()>0)
		{
			exists_pass=exists_pass.substring(0,exists_pass.length()-1);
			System.out.println("PASS:Following are expected to be present and are present:\n"+exists_pass);
		}	

		if(exists_fail.length()>0)
		{
			exists_fail=exists_fail.substring(0,exists_fail.length()-1);
			System.out.println("FAIL:Following are expected to be present and are not present:\n"+exists_fail);
			checkfunction=false;
		}	
		
		if(noexists_pass.length()>0)
		{	
			noexists_pass=noexists_pass.substring(0,noexists_pass.length()-1);
			System.out.println("PASS:Following are expected not to be present and are not present:\n"+noexists_pass);
		}

		if(noexists_fail.length()>0)
		{
			noexists_fail=noexists_fail.substring(0,noexists_fail.length()-1);
			System.out.println("FAIL:Following are expected not to be present and are present:\n"+noexists_fail);
			checkfunction=false;
		}	
		
		return checkfunction;
	}
	public int getLastBrowser() throws Exception
	{
		showDebug("Func:getLastBrowser");
		int brwCnt=-1;
		if(web.window("/web:window[@index='"+(brwCnt+1)+"']").exists())
		{
			showDebug("Browser # " +(brwCnt+1) + " is found." );
			brwCnt++;
		}
		return brwCnt; 
	}
	public void selectFromLov(String img,String srcCol ,String srcVal) throws Exception
	{
		selectFromLov(-1,img,-1,srcCol,srcVal);
	}
	public void selectFromLov(String img,int imgIndex,String srcCol ,String srcVal) throws Exception
	{
		selectFromLov(-1,img,imgIndex,srcCol,srcVal);
	}
	public void selectFromLov(int brw,String img,String srcCol ,String srcVal) throws Exception
	{
		selectFromLov(brw,img,-1,srcCol,srcVal);
	}	
	public void selectFromLov(int brwcnt, String img, int imgindex, String srcCol, String srcVal) throws Exception
	{
		String imgxpath="";
		boolean searchimg=false;		
		/*
		if(brwcnt==-1)
			brwcnt=getLastBrowser();
		showDebug("Last Browser Index:"+brwcnt);
		
		if(brwcnt==-1)
		{
			System.out.println("No browsers opened.");
			return;
		}	
		
		 */
		if(imgindex!=-1)
		{
			searchObjectByLocation("img", imgindex, img);
			if(isFound())
				searchimg=true;
		}
		else if(!img.contains("{{") && !img.contains("["))
			imgxpath=docpath + "/web:img[@alt='"+img+"']";
		else
			imgxpath=img;
		
		
		showDebug("Image:"+imgxpath);
		
		
		if(searchimg)
			click();	
		else
			web.image(imgxpath).click();
		
		//String latestWindow="/web:window[@index='"+(brwcnt+1)+"' or @title='Search and Select List of Values']";
		String latestWindow="/web:window[@title='Search and Select List of Values']";
//		web.window(latestWindow).waitForPage(null);
		String latestDocpath=latestWindow+"/web:document[@index='1']";
		
		showDebug("1:Thread.sleep of 60000 msec");
		Thread.sleep(60000);
		
		if(!web.document(latestDocpath).exists())
		{
			latestDocpath=latestWindow+"/web:document[@index='0']";
		}
		String searchTextField=latestDocpath+"/web:form[@id='_LOVResFrm' or @name='_LOVResFrm' or @index='0']/web:input_text[@name='*searchText*']";
		String GoBtn=latestDocpath+"/web:form[@id='_LOVResFrm' or @name='_LOVResFrm' or @index='0']/web:button[@value='Go']";

		if(!web.button(GoBtn).exists())
		{
			showDebug(" 2:Thread.sleep of 60000 msec");
			Thread.sleep(30000);
		}
		
		web.textBox(searchTextField).click();
		web.textBox(searchTextField).setText(srcVal);
		web.button(GoBtn).click();
		web.window(latestWindow).waitForPage(null);
		showDebug("After entering value, Click Go");
		
		setDocPath(latestDocpath);
		searchTableObject("Select",srcCol ,srcVal,"Select","radio",0);
		String xpath=getRadioXpath();
		web.radioButton(xpath).select();
		searchObjectByLocation("button", 0, "Select");
		click();
		{Thread.sleep(10);}
		restore();
	}
	/*
	 * Added By : Srinivas Potnuru ( spotnuru )
	 * Date: 14-Jul-2011
	 * for HRMS Conversion
	 */
	public void selectFromLov(String img, String srcVal) throws Exception
	{
		String imgxpath="";
		boolean searchimg=false;		
		String[] aimg=	img.split(";");
		imgxpath=docpath + "/web:img[@alt='"+aimg[0]+"']";
		if(aimg.length>1)
		{
			searchObjectByLocation("img", Integer.parseInt(aimg[1]), aimg[0]);
			if(isFound())
				searchimg=true;
		}
		else if(!img.contains("{{") && !img.contains("["))
			imgxpath=docpath + "/web:img[@alt='"+img+"']";
		else
			imgxpath=img;		
		
		showDebug("Image:"+imgxpath);
		if(searchimg)
			click();
		else
			web.image(imgxpath).click();
		
		//String latestWindow="/web:window[@index='"+(brwcnt+1)+"' or @title='Search and Select List of Values']";
		String latestWindow="/web:window[@title='Search and Select List of Values']";
//		web.window(latestWindow).waitForPage(null);
		String latestDocpath=latestWindow+"/web:document[@index='1']";
		
		showDebug("1:Thread.sleep of 60000 msec");
		Thread.sleep(60000);
		
		if(!web.document(latestDocpath).exists())
		{
			latestDocpath=latestWindow+"/web:document[@index='0']";
		}
		String searchTextField=latestDocpath+"/web:form[@id='_LOVResFrm' or @name='_LOVResFrm' or @index='0']/web:input_text[@name='*searchText*']";
		String GoBtn=latestDocpath+"/web:form[@id='_LOVResFrm' or @name='_LOVResFrm' or @index='0']/web:button[@value='Go']";

		if(!web.button(GoBtn).exists())
		{
			showDebug(" 2:Thread.sleep of 60000 msec");
			Thread.sleep(30000);
		}
		
		web.textBox(searchTextField).click();
		web.textBox(searchTextField).setText(srcVal);
		web.button(GoBtn).click();
		web.window(latestWindow).waitForPage(null);
		showDebug("After entering value, Click Go");
		
		setDocPath(latestDocpath);
		
		searchTableObject("Select","2" ,srcVal,"Select","radio",0);//parsed this in actual function
		String xpath=getRadioXpath();
		web.radioButton(xpath).select();
		searchObjectByLocation("button", 0, "Select");
		click();
		{Thread.sleep(10);}
		restore();
	}
	
	/*
	 * Added By : Srinivas Potnuru ( spotnuru )
	 * Date: 14-Jul-2011
	 * for HRMS Conversion
	 */	
	public int SS_Link_Click(String linkname) throws Exception
	{
		int r	=0	;
		String []names	=	linkname.split(";");	
		if(names.length<=2)
		{
			web.link("/web:window[@index='0']/web:document[@index='0']/web:a[@text='"+names[0]+"']").waitFor();
			web.link("/web:window[@index='0']/web:document[@index='0']/web:a[@text='"+names[0]+"']").click();
		}
		else if(names.length>2)
		{
			//html_link
			searchObjectByLocation("html_link",Integer.parseInt(names[2]),names[0]);
			if(isFound())
				click();
			
			
		}

		
		ivu.setErrorRecovery(WebErrorRecovery.ERR_WAIT_FOR_PAGE_TIMEOUT_ERROR, ErrorRecoveryAction.Ignore);
		web.window("/web:window[@index='0']").waitForPage(null);
		ivu.setErrorRecovery(WebErrorRecovery.ERR_WAIT_FOR_PAGE_TIMEOUT_ERROR, ErrorRecoveryAction.Fail);		
		Thread.sleep(2000);
		
		return r;
	}
	/*
	 * Added By : Srinivas Potnuru ( spotnuru )
	 * Date: 14-Jul-2011
	 * for HRMS Conversion
	 */	
	public int SS_Image_Click(String linkname) throws Exception
	{
		int r	=0	;
		String []names	=	linkname.split(";");	
		String sPrefix,sActualData;
		showDebug("Value:"+ivu.getVariables().get("strPrefix"));
		int nStrLength	=	names[0].length();
		String name	=	"";
		sPrefix = names[0].substring(0, 3);
		showDebug("PREFIX:"+sPrefix);
		sActualData	=	names[0].substring(3,nStrLength);
		showDebug("Value:"+ivu.getVariables().get("strPrefix") + sActualData);
		if ( "str".equals(sPrefix))
			names[0] = ivu.getVariables().get("strPrefix") + sActualData;//strPrefix is global variable defined in inits
		if ("num".equals(sPrefix))
			names[0] = ivu.getVariables().get("numPrefix") + sActualData;
		if(names.length<=2)
		{
			web.link("/web:window[@index='0']/web:document[@index='0']/web:img[@alt='"+names[0]+"']").waitFor();
			web.link("/web:window[@index='0']/web:document[@index='0']/web:img[@alt='"+names[0]+"']").click();
		}
		else if(names.length>2)
		{
			//html_link
			searchObjectByLocation("image",Integer.parseInt(names[2]),names[0]);
			if(isFound())
				click();
		}
		ivu.setErrorRecovery(WebErrorRecovery.ERR_WAIT_FOR_PAGE_TIMEOUT_ERROR, ErrorRecoveryAction.Ignore);
		web.window("/web:window[@index='0']").waitForPage(null);
		ivu.setErrorRecovery(WebErrorRecovery.ERR_WAIT_FOR_PAGE_TIMEOUT_ERROR, ErrorRecoveryAction.Fail);	
		Thread.sleep(2000);
		
		return r;
	}	
	/*
	 * Added By : Srinivas Potnuru ( spotnuru )
	 * Date: 14-Jul-2011
	 * for HRMS Conversion
	 */	
	public void HandleWebSecurity() throws Exception
	{
		if(web.dialog("/web:dialog_unknown[ @index='0']").exists())
		{
			web.dialog("/web:dialog_unknown[ @index='0']").waitFor();
			web.dialog("/web:dialog_unknown[ @index='0']").clickButton(0);
		}
				
	}
	/*
	 * Added By : Srinivas Potnuru ( spotnuru )
	 * Date: 14-Jul-2011
	 * for HRMS Conversion
	 */	
	public boolean Check_Web_Link(String linkname,int location) throws Exception
	{
		boolean ret	=	false;	
		searchObjectByLocation("html_link",location,linkname);
		if(isFound())
		{
			ret	=	true;
			message("PASS: Link:" + linkname + " is found with location:" + location);
		}
		else
		{
			message("FAIL: Link:" + linkname + " is not found with location:" + location);
		}
		return ret;
	}
	boolean verifyText(String srcText) throws  Exception
	{
		boolean retval=false;
		List <DOMElement> objs=(List <DOMElement>)web.document(docpath ).getElementsByTagName("body");
		String entiretext=objs.get(0).getAttribute("innerText");
		if(entiretext.contains(srcText))
			retval=true;
		return retval;
	}

	@SuppressWarnings("unchecked") boolean getTableCell(int row,int col) throws Exception
	{
		boolean	cellFound	=	false;
		if(tableObj!=null)
		{
			List <DOMElement> trs = (List <DOMElement>)tableObj.getElementsByTagName("tr");
			List <DOMElement> items ;
			DOMElement tr	=	null;
			DOMElement td	=	null;
			int innerRows	=	0;
			int innerCols	=	0;
			int	currRow		=	0;
			int currCol	=	0;
			
			showDebug("TRS:"+trs.size());
			for(int i=0;i<trs.size();i++)
			{
				showDebug("Current i:"+i);
				showDebug("Current Row:"+currRow);
				if(currRow==row)
				{
					tr	=	trs.get(i);
					break;
				}				
				innerRows	=	0;
				innerRows	=	trs.get(i).getElementsByTagName("tr").size();
				showDebug("tr:"+i+" inner rows:"+innerRows);
				if(innerRows>0)
				{
					i	=	i	+ innerRows	;		
				}
				
				currRow++;

			}
			if(tr!=null)
			{
				showDebug("Found Row Content: "+tr.getAttribute("innerHtml"));	
				List <DOMElement> tds = (List <DOMElement>)tr.getElementsByTagName("td");
				showDebug("TDS:"+tds.size());
				currCol	=	0;
				for(int i=0;i<tds.size();i++)
				{
					showDebug("Current i:"+i);
					showDebug("Current Col:"+currCol);
					if(currCol==col)
					{
						td	=	tds.get(i);
						cellFound	=	true;
						break;
					}				
					innerCols	=	0;
					innerCols	=	tds.get(i).getElementsByTagName("td").size();
					showDebug("td:"+i+" inner cols:"+innerCols);
					if(innerCols>0)
					{
						i	=	i	+ innerCols	;		
					}
					
					currCol++;
				}				
				if(td!=null)
				{
					showDebug("Cell Found @ Row:"+row+",Col:"+col);
					tableCell=td;	
				}
				else
				{
					System.out.println("Column Not found.");
				}
			}
			else
			{
				System.out.println("Row Not Found");
			}
		}
		else
		{
			System.out.println("Table  Not Found");
		}
		return cellFound;		
	}
	String getTableCellData(int row,String col) throws Exception
	 {
		 String ret		=	"";
		 int searchcol	=	Integer.parseInt(tblColNames.get(col).toString());
		 ret			=	getTableCellData(row,searchcol);
		 return ret;
	 }
	 @SuppressWarnings("unchecked") String getTableCellData(int row,int col) throws Exception
	{
		String ret="";
		List <DOMElement> items ;
		getTableCell(row,col);
		if(tableCell!=null)
		{
			ret		=	tableCell.getAttribute("innerText");
			showDebug("Actual value:"+ret);
			if(ret==null)
			{
				showDebug("Found null");
				items	=	(List <DOMElement>)tableCell.getElementsByTagName("input");
				if(items.size()==1)
				{
					String type=items.get(0).getAttribute("type");
					showDebug("Object Type="+type);
					if("text".equals(type))
					{
						showDebug("Before Fetching Text Value");
						ret	=	items.get(0).getAttribute("value");
						showDebug("After Fetching Text Value:"+ret);
					}
					
				}
				else
				{
					ret	=	"";
					showDebug("Has more than one item, so did not get any value");
				}
			}
		}
		return ret;
	}
	boolean getTableChildObject(int row, String col,String objtype,int index) throws  Exception
	{
		boolean found	=	false;
		int searchcol	=	Integer.parseInt(tblColNames.get(col).toString());
		found			=	getTableChildObject(row, searchcol, objtype, index);
		return found;	
	}	 
	@SuppressWarnings("unchecked") boolean getTableChildObject(int row, int col,String objtype,int index) throws  Exception
	{
		searchSuccess	=	false;
		boolean found	=	false;
		getTableCell( row,col);
		retObj			=	null;
		
		
		if(tableCell!=null)
		{
			String [] tags	=	getObjectType(objtype,true);
			showDebug(tags);
			List <DOMElement> items = (List <DOMElement>) tableCell.getElementsByTagName(tags[0]);
			int actualcnt	=	-1;
			String tagtype	=	"";
			showDebug("Child items found:"+ items.size());
			for(int i=0;i<items.size();i++)
			{
				if(tags[1]!="" && tags[1].equals("type"))
				{
					tagtype	=	items.get(i).getAttribute(tags[1]);
					if(tags[2].equals(tagtype))
					{
						actualcnt++;
					}
				}
				else
				{
					actualcnt++;
				}
				if(actualcnt==index)
				{
					retObj			=	tableCellObject	=	items.get(i);
					found			=	searchSuccess	=	true;
					break;
				}
			}
		}
		else
		{
			System.out.println("Table Cell not found");
		}
		return found;	
	}
	public String getObjectValue(String objtype) throws Exception
	{
		String ret="";
		String xpath="";
		if(retObj!=null)
		{
			String tags[]=getObjectType(objtype,true);
			if("input".equals(tags[0]))
			{
				if("text".equals(tags[2]))
				{
					xpath	=	getTextXpath();
					ret		=	web.textBox(xpath).getAttribute("value");
				}
				else if("radio".equals(tags[2]))
				{
					xpath	=	getRadioXpath();
					ret		=	web.radioButton(xpath).getAttribute("selected");	
				}
				else if("checkbox".equals(tags[2]))
				{
					xpath	=	getCheckboxXpath();
					ret		=	web.checkBox(xpath).getAttribute("checked");
				}				
			}
			else if("a".equals(tags[0]) ||   "span".equals(tags[0]))
			{
				ret	=	getText();
			}
			else if("select".equals(tags[0]))
			{
				xpath	=	getListboxXpath();
				ret		=	web.selectBox(xpath).getSelectedText()[0];
			}
			else if("button".equals(tags[0]))
			{
				
				ret		=	"";
			}
			
		}
		else
		{
			System.out.println("Object Not Found");
		}
		return ret;
	}
	public boolean getTableObject(String tablename) throws Exception
	{
		return getTableObject(0, tablename);
	}
	
	@SuppressWarnings("unchecked") public boolean getTableObject(int tblloc,String tablename) throws Exception
	{
		
		boolean tblFound	=	false;
		int tloc=-1;
		int iloc=0;
		int iCounter=0,found=0;
		int iCols=0,iRows=0,startRow=0,dummRows=0,colRow=0,iSrcCol=0,iTargetCol=0;
		DOMElement tr,tbl,th=null;
		List<DOMElement> trs=null;		
		
		
		searchSuccess		=	false;
		tableObj			=	null;

		
		List <DOMElement> ths = (List <DOMElement>) web.document(docpath).getElementsByTagName("th");
		String tags[]={"","",""};
		showDebug("number of th elements: " + ths.size());//debug
		
		
		for(int i=0;i<ths.size();i++)
		{
			th = (DOMElement) ths.get(i);
			showDebug("th"+i+":"+ th.getAttribute("innerText"));//debug
			
			if(tablename.equals(th.getAttribute("innerText")))
			{
				tloc++;
				if(tloc<tblloc)
				{
					continue;
				}
				else
				{
					tblFound	=	true;
					showDebug("PASS: Table with name:"+tablename+" at location:"+tblloc+" is found" );//debug
					break;
				}
			}
		}
		if(tblFound)
		{
			DOMElement trfound	=	th.getParent();
			tableObj	=	th.getParent().getParent();			
			tableXPath	=	getTableXpath();
			tblRows		=	web.table(tableXPath).getRowCount();
			tblCols		=	web.table(tableXPath).getColumnCount();
			tblColNames	=	new HashMap(tblCols);
			List <DOMElement> heads = (List <DOMElement>) trfound.getElementsByTagName("th");
			for(int i=0;i<heads.size();i++)
			{
				String colname	=	heads.get(i).getAttribute("innerText");	
				tblColNames.put(colname,i);
				//showDebug("Column : "+ i +" : " + colname);
			}
			searchSuccess	=	true;
		}
		else
		{
			showDebug("FAIL: Table with name:"+tablename+" at location:"+tblloc+" is not found" );//debug
		}
		return tblFound;
	}
	public int getTableIndex() throws Exception
	{
		int tblind	=	0;
		if(tableObj!=null)
		{
				tblind	=	Integer.parseInt(tableObj.getAttribute("index"));
		}
		
		return tblind;
	}
	public int getTableRowCount() throws Exception
	{
		return tblRows;
	}
	public int getTableColCount() throws Exception
	{	
		return tblCols;
	}	
	
	public int getTableRowCount(boolean refresh) throws Exception
	{
		tblRows	=	0;
		if(!("".equals(tableXPath)))
		{
			tblRows	=	web.table(tableXPath).getRowCount();
		}
		return  tblRows;
	}
	public int getTableColCount(boolean refresh) throws Exception
	{
		tblCols	=	0;
		if(!("".equals(tableXPath)))
		{
			tblCols	=	web.table(tableXPath).getColumnCount();
		}		
		return  tblCols;
	}	
	public boolean TableTextExists(String searchStr) throws Exception
	{
		boolean retval=false;
		if(tableObj!=null)
		{
			String entiretext=tableObj.getAttribute("innerText");
			if(entiretext.contains(searchStr))
				retval=true;
		}	
		return retval;
	}	
	public String[] getColIndexes(String colnames[])
	{
		//int ret[]	=	 new int[colnames.length];//	=	int [colnames.length];
		String  ret[]	=	 new String[colnames.length];//	=	int [colnames.length];
		for(int i=0;i<colnames.length;i++)
		{
			ret[i]	=	 tblColNames.get(colnames[i]).toString();
			showDebug("Index of Columm:\""+colnames[i]+"\" is " + ret[i]);
		}
		return ret;
	}
	@SuppressWarnings("unchecked") public boolean TableCheck(int tblloc, String tblname,String colheads[],String objtypes[],String objvals[][]) throws Exception
	{
		boolean tablecheck 	=	false;
		boolean tblfound 	=	getTableObject(tblloc,tblname);
		tablecheck			=	tblfound;
		int collength		=	colheads.length;
		setDebug(true);
		//String colindexes[]	=	getColIndexes(colheads);
		
		int valrows			=	objvals.length;	
		int tblrows			=	tblRows;
		showDebug("Number of check rows:"+valrows);
		if(tblfound)
		{
			List <DOMElement> trs = (List <DOMElement>)tableObj.getElementsByTagName("tr");
			int latestrow	=	0;
			int currow		=	0;
			DOMElement tr	=	null;
			int innerRows	=	0;
			for(int i=1;i<=tblrows;i++)
			{
				for(int j=latestrow;j<trs.size();j++)
				{
					if(currow==i)
					{
						tr			=	trs.get(j);
						latestrow	=	j;
						break;
					}
					innerRows	=	0;
					innerRows	=	trs.get(j).getElementsByTagName("tr").size();
					showDebug("Inner Rows:" + innerRows);
					if(innerRows>	0)
					{
						j=j+innerRows;
					}
					currow++;
				}
				if(tr!=null)
				{
					List <DOMElement> 	tds = (List <DOMElement>)tr.getElementsByTagName("td");
					DOMElement			td	=	null;
					int curcol		=	0;
					int latestcol	=	0;
					int innerCols	=	0;
					for(int k=0;k<colheads.length;k++)
					{
						int colind	=	Integer.parseInt(tblColNames.get(colheads[k]).toString());
						for(int ind=latestcol;ind<tds.size();ind++)
						{
							if(curcol==colind)
							{
								latestcol	=	ind;
								td			=	tds.get(ind);
								retObj		=	td;
								break;
							}
							innerCols	=	0;
							innerCols	=	tds.get(ind).getElementsByTagName("td").size();
							showDebug("td:"+ind+" inner cols:"+innerCols);
							if(innerCols>0)
							{
								ind	=	ind	+ innerCols	;		
							}
							
							curcol++;
							
						}
						if(td!=null)
						{
							System.out.println(getObjectValue(objtypes[k]));
						}
					}
				}
			}
		}
		else
		{
			System.out.println("Table Not Found");
		}
		return tablecheck;
	}
	public boolean TableCheck( String tblname,String colheads[],String objtypes[],String objvals[][]) throws Exception
	{
		boolean tablecheck	=	TableCheck(0,tblname,colheads,objtypes,objvals);
		return tablecheck;
	}
	
	/**
	 * 
	 * @param title Title of the web page
	 * @param Hdr Header
	 * @author pkotta
	 */
	@SuppressWarnings("unchecked") public void selectToShowInfo(@Arg("title") String title, @Arg("Hdr") String Hdr) throws Exception {
		//"/web:window[@index='0' or @title='Create Work Order']/web:document[@index='0']/web:h2[@text='Operation Requirements']";
		String HdrT ="/web:window[@title='"+title+"']/web:document[@index='0']/web:h2[@text='"+Hdr+"']";
		String table ="/web:window[@title='"+title+"']/web:document[@index='0']/web:table[@index='"+web.element(HdrT).getParent().getParent().getParent().getIndex()+"']";
		List<DOMElement> myTableData = web.table(table).getElementsByTagName("img");
		
		Iterator<DOMElement> myitr = myTableData.iterator();
		
		while(myitr.hasNext())
		{
			DOMElement myimg = myitr.next();
//			info("index:"+myimg.getAttribute("index"));
//			info("index:"+myimg.getAttribute("alt"));
			if(myimg.getAttribute("alt").equals("Select to show information"))
			{
				web.image("/web:window[@title='"+title+"']/web:document[@index='0']/web:img[@alt='"+myimg.getAttribute("alt")+"' or @index='"+myimg.getAttribute("index")+"']").click();
				Thread.sleep(20000);
				
				if(web.image("/web:window[@title='"+title+"']/web:document[@index='0']/web:img[@index='"+myimg.getAttribute("index")+"']").getAttribute("alt").equals("Select to hide information"))
				{
					System.out.println(Hdr+" is expanded");
				}
				
			}else if(myimg.getAttribute("alt").equals("Select to hide information"))
			{
				
				System.out.println(Hdr+" is already expanded");
			}
			
		}
		
	}
	/**
	 * 
	 * @param title Title of the web page
	 * @param Hdr Header
	 * @author pkotta,piqbal
	 * 
	 */	
	
	public void message(String str) throws Exception
	{
		ivu.info(str);
	}
	/**
	 * 
	 * @param title Title of the web page
	 * @param Hdr Header
	 * @author pkotta
	 * 
	 */
	@SuppressWarnings("unchecked") public void selectToHideInfo(@Arg("title") String title, @Arg("Hdr") String Hdr) throws Exception {
		//"/web:window[@index='0' or @title='Create Work Order']/web:document[@index='0']/web:h2[@text='Operation Requirements']";
		String HdrT ="/web:window[@title='"+title+"']/web:document[@index='0']/web:h2[@text='"+Hdr+"']";
		String table ="/web:window[@title='"+title+"']/web:document[@index='0']/web:table[@index='"+web.element(HdrT).getParent().getParent().getParent().getIndex()+"']";
		List<DOMElement> myTableData = web.table(table).getElementsByTagName("img");
		
		Iterator<DOMElement> myitr = myTableData.iterator();
		
		while(myitr.hasNext())
		{
			DOMElement myimg = myitr.next();
//			info("index:"+myimg.getAttribute("index"));
//			info("index:"+myimg.getAttribute("alt"));
			if(myimg.getAttribute("alt").equals("Select to hide information"))
			{
				web.image("/web:window[@title='"+title+"']/web:document[@index='0']/web:img[@alt='"+myimg.getAttribute("alt")+"' or @index='"+myimg.getAttribute("index")+"']").click();
				Thread.sleep(20000);
				if(web.image("/web:window[@title='"+title+"']/web:document[@index='0']/web:img[@index='"+myimg.getAttribute("index")+"']").getAttribute("alt").equals("Select to show information"))
				{
					System.out.println(Hdr+" is Collapsed");
				}
			}else if(myimg.getAttribute("alt").equals("Select to show information"))
			{
				System.out.println(Hdr+" is already collapsed");
			}
			
		}
		
	}
}