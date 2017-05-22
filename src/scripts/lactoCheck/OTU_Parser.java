package scripts.lactoCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

public class OTU_Parser
{	
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLactoCheckDir() + File.separator + 
					"gaQiimeClosedRef.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			String taxaString = splits[splits.length-1];
			
			if(taxaString.contains("g__Lactobacillus"))
				System.out.println(splits[splits.length-1]);
		}
	}
}
