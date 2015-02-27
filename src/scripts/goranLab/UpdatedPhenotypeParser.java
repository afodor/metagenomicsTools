package scripts.goranLab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;
import utils.TabReader;

public class UpdatedPhenotypeParser
{
	private final Double sugbev_plusjuice;
	
	public Double getSugbev_plusjuice()
	{
		return sugbev_plusjuice;
	}
	
	private UpdatedPhenotypeParser(String s) throws Exception
	{
		TabReader tReader = new TabReader(s);
		
		for( int x=0; x < 23; x++)
			tReader.nextToken();
		
		String aVal = tReader.nextToken().trim();
		
		if( aVal.length() == 0 )
			this.sugbev_plusjuice = null;
		else
			this.sugbev_plusjuice = Double.parseDouble(aVal);
	}
	
	public static HashMap<Integer, UpdatedPhenotypeParser> getMetaMap() throws Exception
	{
		HashMap<Integer, UpdatedPhenotypeParser> map =new LinkedHashMap<Integer, UpdatedPhenotypeParser>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getGoranTrialDir()  + File.separator + 
				"UpdatedPhenotypeSANSOL022515.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			int aVal = Integer.parseInt(splits[0]);
			
			if( map.containsKey(aVal) )
				throw new Exception("No");
			
			UpdatedPhenotypeParser upp = new UpdatedPhenotypeParser(s);
			
			map.put(aVal, upp);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, UpdatedPhenotypeParser> map =getMetaMap();
		
		for(Integer i : map.keySet())
			System.out.println(i + " " + map.get(i).getSugbev_plusjuice());
	}
}
