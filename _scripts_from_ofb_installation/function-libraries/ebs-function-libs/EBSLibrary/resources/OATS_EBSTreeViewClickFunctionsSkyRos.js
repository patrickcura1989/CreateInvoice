/*
How to use:
Run those javascript below, It will expand "Advanced Planning Administrator", expand "Admin", and then click "Instances". 
The 30000 is the interval between each clicking.

OATS_EBSTreeViewClickRespLink('respList',new Array("Advanced Planning Administrator","Admin","Instances"),30000);

*/

var OATS_EBSTreeViewIndex=0;
var OATS_EBSTreeViewLinkTextArray = new Array();
var OATS_CLICK_INTERVAL =  20000;
 
 function OATS_EBSTreeViewClickRespLink (respListId, linkTexts, clickIntervalMillisecs) //Entry
{
	try{
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewClickRespLink 1  clickIntervalMillisecs " + clickIntervalMillisecs);
		if (clickIntervalMillisecs){
			OATS_CLICK_INTERVAL = clickIntervalMillisecs;
		}
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewClickRespLink 2");
		var firstTreeViewNode = OATS_EBSTreeViewGetFirstTreeView(respListId);
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewClickRespLink 3  firstTreeViewNode " + firstTreeViewNode);
		if (firstTreeViewNode!=null)
		{
			OATS_EBSTreeViewLogger("OATS_EBSTreeViewClickRespLink 4  firstTreeViewNode.id " + firstTreeViewNode.id);
	    	OATS_EBSTreeViewExpandLinks(firstTreeViewNode.id, linkTexts);
		}
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewClickRespLink 5");
	}catch(ex)
	{
	    OATS_EBSTreeViewLogger(str.message);
	}
	OATS_EBSTreeViewLogger("OATS_EBSTreeViewClickRespLink 6");
}

function  OATS_EBSTreeViewGetFirstTreeView(respListId)
{
	try{
		var table = document.getElementById(respListId);
		return table.children[0].children[0].children[0].children[1];
	}catch(ex)
	{
	}
	return document.getElementById("treemenu1");
}


function OATS_EBSTreeViewExpandLinks(treeViewId, linkTexts)
{
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandLinks 1 treeViewId " + treeViewId);
		OATS_EBSTreeViewLinkTextArray = linkTexts;
		var UL_treeview = document.getElementById(treeViewId);
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandLinks 2");
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandLinks 3 UL_treeview: "+UL_treeview.tagName+" className:" + UL_treeview.className);
		if (linkTexts && linkTexts.length>0)
		{
			OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandLinks 4 linkTexts.length " + linkTexts.length);
			OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandLinks 5 OATS_CLICK_INTERVAL " + OATS_CLICK_INTERVAL);
			window.setTimeout(function(){OATS_EBSTreeViewExpandRoot(treeViewId);},OATS_CLICK_INTERVAL);
		}
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandLinks 6");
}

function OATS_EBSTreeViewExpandRoot(treeViewId)
{
	try{
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandRoot 1 treeViewId " + treeViewId);
		var UL_treeview = document.getElementById(treeViewId);
		var allRootMenu= UL_treeview.children;
		var expandChildren =  false;
		var rootmenuId = null;
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandRoot 2");
		for(var i=0;i< allRootMenu.length;++i)
		{
			var rootmenu = allRootMenu[i];
			OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandRoot 3 allRootMenu["+i+"]="+rootmenu);
			if (rootmenu.nodeType == 1 && OATS_EBSTreeViewGetNodeText(rootmenu) == OATS_EBSTreeViewLinkTextArray[0])
			{ 
			
				OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandRoot 4 ");
				if (OATS_EBSTreeViewNodeIsFolder(rootmenu))
				{
					OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandRoot 5 ");
					if (!OATS_EBSTreeViewNodeIsExpanded(rootmenu))
					{
						OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandRoot 6 ");
					    OATS_EBSTreeViewHighlight(rootmenu);
						rootmenu.click();
						expandChildren =  true;
						rootmenuId = rootmenu.id;
						OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandRoot 7 ");
						break;
					}
					// Handle rootmenu when expanded already.
					else if(OATS_EBSTreeViewNodeIsExpanded(rootmenu)){
						OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandRoot 6 ");
						OATS_EBSTreeViewHighlight(rootmenu);
						expandChildren =  true;
						rootmenuId = rootmenu.id;
						OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandRoot 7 ");
						break;
					}
				}else{
					OATS_EBSTreeViewHighlight(rootmenu);
					rootmenu.children[0].click();
					OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandRoot 8 ");
					break;
				}
			}
		}
		if (expandChildren)
		{
			window.setTimeout(function(){OATS_EBSTreeViewExpandNode(1, rootmenuId);},OATS_CLICK_INTERVAL);
		}
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandRoot 9");
	}catch(ex){
		OATS_EBSTreeViewLogger('OATS_EBSTreeViewExpandRoot('+treeViewId +"): "+ ex.message);
		if (ex.stack){
			OATS_EBSTreeViewLogger('OATS_EBSTreeViewExpandRoot('+treeViewId +"): " + ex.stack);
		}
	}
}

function OATS_EBSTreeViewExpandNode(index, parentMenuId)
{
	try{
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandNode 1 parentMenuId " + parentMenuId);
		var LI_parentMenu = document.getElementById(parentMenuId);
		OATS_EBSTreeViewListChildren(LI_parentMenu);
		var allChildMenu= LI_parentMenu.children[1].children;
		var expandChildren =  false;
		var childmenuId = null;
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandNode 2");
		for(var i=0;i< allChildMenu.length;++i)
		{
			var childmenu = allChildMenu[i];
			if (childmenu.nodeType == 1 && OATS_EBSTreeViewGetNodeText(childmenu) == OATS_EBSTreeViewLinkTextArray[index])
			{	 
				if (OATS_EBSTreeViewNodeIsFolder(childmenu))
				{
					if (!OATS_EBSTreeViewNodeIsExpanded(childmenu))
					{
					    OATS_EBSTreeViewHighlight(childmenu);
						childmenu.click();
						expandChildren =  true;
						childmenuId = childmenu.id;
						break;
					}else if (OATS_EBSTreeViewNodeIsExpanded(childmenu)){
						// Handle childmenu when expanded already.
						OATS_EBSTreeViewHighlight(childmenu);
						expandChildren = true;
						childmenuId = childmenu.id;
						break;
					}
				}else{
					OATS_EBSTreeViewHighlight(childmenu);
					childmenu.children[0].click();
					break;
				}
			}
		}
		if (expandChildren)
		{
			window.setTimeout(function(){OATS_EBSTreeViewExpandNode(index+1, childmenuId);},OATS_CLICK_INTERVAL);
		}
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewExpandNode 3");
	}catch(ex){
		OATS_EBSTreeViewLogger('OATS_EBSTreeViewExpandNode('+OATS_EBSTreeViewLinkTextArray[index]+',' + parentMenuId +"): "+ ex.message);
		if (ex.stack){
			OATS_EBSTreeViewLogger('OATS_EBSTreeViewExpandNode('+OATS_EBSTreeViewLinkTextArray[index]+',' + parentMenuId +"): " + ex.stack);
		}
	}
}

function OATS_EBSTreeViewListChildren(node)
{
	try{
		OATS_EBSTreeViewLogger('OATS_EBSTreeViewListChildren node.children.length '+ node.children.length);
		for(var i=0; i < node.children.length;++i)
		{
			OATS_EBSTreeViewLogger('OATS_EBSTreeViewListChildren node.children['+i+']');
			if (node.children[i].nodeType == 1){
				OATS_EBSTreeViewLogger('OATS_EBSTreeViewListChildren node.children['+i+']=' + node.children[i].tagName);
			}else{
			    OATS_EBSTreeViewLogger('OATS_EBSTreeViewListChildren node.children['+i+']=' + node.children[i].nodeName);
			}
		}
	}catch(ex)
	{
		OATS_EBSTreeViewLogger('OATS_EBSTreeViewListChildren ' + ex.message);
	}
}

function OATS_EBSTreeViewNodeIsFolder(LI_linkNode)
{
	try{
		return LI_linkNode.className=="submenu" ||  LI_linkNode.className =="rootmenu" ;
	}catch(ex)
	{
	}
	return false;
}

function OATS_EBSTreeViewNodeIsExpanded(node)
{
	try{
		var imgAlt=node.children[0].children[0].getAttribute('alt')
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewNodeIsExpanded "+imgAlt );
		if (imgAlt != null && imgAlt!=""){
			return imgAlt != 'expand';
		}
	}catch(ex)
	{
		OATS_EBSTreeViewLogger('OATS_EBSTreeViewNodeIsExpanded ' + ex.message);
	}
	return false;
}

function OATS_EBSTreeViewGetNodeText(LI_linkNode)
{
	try{
		return LI_linkNode.children[0].innerText;
	}catch(ex){
		OATS_EBSTreeViewLogger('OATS_EBSTreeViewGetNodeText('+LI_linkNode.outerHTML+') error.');
	}
	return "";
}

function OATS_EBSTreeViewLogger(str)
{
	if (webdom_debug){
		webdom_debug(str);
	}
}

function OATS_EBSTreeViewHighlight(LI_linkNode)
{
	try{
		OATS_EBSTreeViewLogger("OATS_EBSTreeViewHighlight " + OATS_EBSTreeViewGetNodeText(LI_linkNode));
		LI_linkNode.style.backgroundColor="#FF0066";
		LI_linkNode.scrollIntoView(true);
	}catch(ex){
		OATS_EBSTreeViewLogger('OATS_EBSTreeViewHighlight('+LI_linkNode.outerHTML+') error.');
	}
}
