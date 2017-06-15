package scripts.lactoCheck;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;

public class PivotGreengenes
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getLactoCheckDir() + File.separator + "gaQiimeClosedRef.txt"	)));
		
		reader.readLine();
		
		String[] headers = reader.readLine().split("\t");
		
		List<String[]> list = new ArrayList<String[]>();
		
		for(String s= reader.readLine(); s != null; s =reader.readLine())
		{
			list.add(s.split("\t"));
		}
		
		reader.close();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getLactoCheckDir() + File.separator + "gaQiimeClosedRefColumnsAsTaxa.txt"	)));
		
		writer.write("sample");
		
		for(String[] sa : list)
			writer.write("\t" + sa[sa.length-1]);
		
		writer.write("\n");
		
		for( int x=1; x < headers.length-1; x++)
		{
			writer.write(headers[x]);
			
			for(String[] sa : list)
				writer.write("\t" + sa[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
}
