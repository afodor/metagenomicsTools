package classExamples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class MungeFamilyCounts
{
	public static void main(String[] args) throws Exception
	{	
		HashMap<String, String> nameMap = newNameMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				new File("D:\\classes\\Advanced_Stats_Spring2015\\ninaData\\familyPivotedTaxaAsColumns.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File("D:\\classes\\Advanced_Stats_Spring2015\\ninaData\\familyPivotedTaxaAsColumnsNotNormalized.txt")));
		
		writer.write(reader.readLine()+ "\n");
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replace("Tope_", "").replace(".fas", "");
			String newName = nameMap.get(key);
			
			if( newName == null)
				throw new Exception("No");
			
			writer.write(newName);
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t"+ splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
	}
	
	private static HashMap<String,String> newNameMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"D:\\classes\\Advanced_Stats_Spring2015\\ninaData\\caseControlData.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String newName = s.split("\t")[0];
			
			String oldName = newName.replaceAll("case", "").replaceAll("control", "");
			
			if( map.containsKey(oldName))
				throw new Exception("No");
			
			map.put(oldName, newName);
		}
		
		return map;
	}
}
