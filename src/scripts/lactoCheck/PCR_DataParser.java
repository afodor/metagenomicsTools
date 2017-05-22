package scripts.lactoCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class PCR_DataParser
{
	private final String group;
	private final double L_crispatus;
	private final double L_iners;
	private final double bglobulin;

	
	
	public String getGroup()
	{
		return group;
	}

	public double getL_crispatus()
	{
		return L_crispatus;
	}

	public double getL_iners()
	{
		return L_iners;
	}

	public double getBglobulin()
	{
		return bglobulin;
	}

	private PCR_DataParser(String s) throws Exception
	{
		String[] splits = s.split("\t");
		
		this.group = splits[0];
		this.L_crispatus = Double.parseDouble(splits[1]);
		this.L_iners = Double.parseDouble(splits[2]);
		this.bglobulin = Double.parseDouble(splits[3]);
	}
	
	public static HashMap<String, PCR_DataParser> getPCRData() throws Exception
	{
		 HashMap<String, PCR_DataParser>  map = new LinkedHashMap<String,PCR_DataParser>();
		
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getLactoCheckDir() + 
				File.separator + "qPCRVals.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( splits.length != 4)
				throw new Exception("No");
			
			PCR_DataParser pcr = new PCR_DataParser(s);
			
			if( map.containsKey(pcr.getGroup()))
				throw new Exception("No");
			
			map.put(pcr.getGroup(), pcr);
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, PCR_DataParser> map = 
				getPCRData();
		
		for(String s : map.keySet())
		{
			PCR_DataParser pcr = map.get(s);
			System.out.println(s + pcr.getL_crispatus() + " " + pcr.getL_iners() );
		}
			
	}
}
