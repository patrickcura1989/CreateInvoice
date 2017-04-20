import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;


public class TestConcurent {


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void concurrent(String content,String item_pass,String cor_value) throws Exception {
		File file = new File("filename.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		System.out.println("Done-->"+file.getAbsolutePath());
		Map<String, String[]> m1 = new HashMap<String, String[]>();
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
				System.out.println(lineNo+"Line: " + line);
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
				ArrayList<String> all_items=new ArrayList<String>();
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

		currespondentValues(m1, item_pass, cor_value);
	}


	public static void currespondentValues(Map<String, String[]> m,String item,String cor_value)
	{
		for (Iterator<String> it = m.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			System.out.println("keys ---"+key);
			if(item.equalsIgnoreCase(key))
			{
				System.out.println("if loop "+key);
				System.out.println(" value --->"+m.get(key));
				String args[]=(String[]) m.get(key);

				if(cor_value.equalsIgnoreCase("Category"))
				{
					System.out.println("ITEM  	:"+item);
					System.out.println("Category  : "+args[1]);
				}
				if(cor_value.equalsIgnoreCase("Minimum Quantity"))
				{
					System.out.println("ITEM  	:"+item);
					System.out.println("Min_Quantity  : "+args[2]);
				}
				if(cor_value.equalsIgnoreCase("Maximum Quantity"))
				{
					System.out.println("ITEM  	:"+item);
					System.out.println("Max_Quantity  : "+args[3]);
				}
				if(cor_value.equalsIgnoreCase("On Hand Quantity"))
				{
					System.out.println("ITEM  	:"+item);
					System.out.println("OnHand_Quantity  : "+args[4]);
				}
				if(cor_value.equalsIgnoreCase("Supplay Quantity"))
				{
					System.out.println("ITEM  	:"+item);
					System.out.println("Supplay_Quantity  : "+args[5]);
				}
				if(cor_value.equalsIgnoreCase("Demand Quantity"))
				{
					System.out.println("ITEM  	:"+item);
					System.out.println("Demand_Quantity  : "+args[6]);
				}
				if(cor_value.equalsIgnoreCase("available Quantity"))
				{
					System.out.println("ITEM  	:"+item);
					System.out.println("available_Quantity  : "+args[7]);
				}
				if(cor_value.equalsIgnoreCase("Order Quantity"))
				{
					System.out.println("ITEM  	:"+item);
					System.out.println("Minimum  : "+args[8]);

					System.out.println("Maximum  : "+args[9]);

					System.out.println("Multiple  : "+args[10]);
				}
				if(cor_value.equalsIgnoreCase("Reorder Quantity"))
				{
					System.out.println("ITEM  	:"+item);
					System.out.println("Reorder_Quantity  : "+args[11]);
				}
			}
		}
	}
}
