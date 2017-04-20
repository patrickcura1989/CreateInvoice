import java.util.HashMap;


public class TableObject {
	String tblName;
	HashMap <String ,Integer> cols;
	String tblXpath;
	int foundRow;
	public TableObject(String obj)
	{
		String arr[]=	obj.split("##");
		//(String tblName, HashMap <String ,Integer> cols,String tblXpath,int foundRow)
		this.tblName	=	arr[0].substring(1,arr[0].length()-1);
		this.tblXpath	=	arr[2].substring(1,arr[2].length()-1);
		this.foundRow	=	Integer.parseInt(arr[3].substring(1,arr[3].length()-1));
		this.cols		=	stringToCols(arr[1].substring(1,arr[1].length()-1));		
		
		System.out.println("Table Name:"+this.tblName);
		System.out.println("Table Xpath:"+this.tblXpath);
		System.out.println("Found Row:"+this.foundRow);
		
	}
	public TableObject(String tblName, HashMap <String ,Integer> cols,String tblXpath,int foundRow)
	{
		this.tblName	=	tblName;
		this.cols	=	cols;
		this.tblXpath	=	tblXpath;
		this.foundRow	=	foundRow;
	}
	
	public String getTableName()
	{
		return this.tblName;
	}
	public String getTblXpath()
	{
		return this.tblXpath;
	}
	public   HashMap <String ,Integer> getCols()
	{
		return this.cols;
	}
	public int getFoundRow()
	{
		return foundRow;
	}
	public HashMap<String,Integer> stringToCols(String colString)
	{
		HashMap<String,Integer> cols=	new HashMap<String,Integer>();
		
		String arr[]	=	colString.split(";;");
		for(int i=0;i<arr.length;i++)
		{
			String 	key	=	arr[i].substring(0,arr[i].indexOf("=>"));
			String	obj	=	arr[i].substring(arr[i].indexOf("=>")+2);
			System.out.println("(Key,Value)=>("+key+","+obj+")");
			cols.put(key, Integer.parseInt(obj));
		}
		return cols;
	}
	public String colsToString()
	{
		String ret="";
		Object[][] twoDarray = new String[this.cols.size()][2];

		Object[] keys 	= this.cols.keySet().toArray();
		Object[] values = this.cols.values().toArray();

		for (int row = 0; row < twoDarray.length; row++) {
			ret	=	ret + keys[row]+"=>" +values[row] + ";;";
		}		
		if(!"".equals(ret))
			ret	=	ret.substring(0,ret.length()-2);
		
		return ret;
	}
	public String toString()
	{
		//TableObject(String tblName, HashMap <String ,Integer> cols,String tblXpath,int foundRow)
		String complete	=	"("+this.tblName+")##("+colsToString()+")##("+this.tblXpath+")##("+this.foundRow+")"; 
		System.out.println("Values:"+ complete);
		return complete;
		
	}

}
