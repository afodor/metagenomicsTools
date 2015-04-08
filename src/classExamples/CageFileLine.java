package classExamples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CageFileLine
{
	private final String cage;
	private final String genotype;
	private final String time;
	private final String key;
	
	public String getCage()
	{
		return cage;
	}

	public String getGenotype()
	{
		return genotype;
	}

	public String getTime()
	{
		return time;
	}

	public String getKey()
	{
		return key;
	}

	public static HashMap<String, CageFileLine> getMetaMap() throws Exception
	{
		HashMap<String, CageFileLine> map = new LinkedHashMap<String, CageFileLine>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"D:\\classes\\Advanced_Stats_Spring2015\\cageData\\Jobins Library _updated_120311.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			CageFileLine cfl = new CageFileLine(s);
			
			if( map.containsKey(cfl))
				throw new Exception("No");
			
			map.put(cfl.key,cfl);
		}
		
		reader.close();
		
		return map;
	}
	
	private CageFileLine(String s) throws Exception
	{
		String[] splits = s.split("\t");
		this.key = splits[7] + "@" + splits[8];
		this.cage = "Cage" + splits[0].charAt(1) +"_" + splits[5];
		
		String timeVal = splits[4];
		
		if( timeVal.equals("PRE-AOM"))
			this.time = "PRE";
		else if (timeVal.equals("POST-AOM") )
			this.time = "POST";
		else this.time = timeVal;
		
		this.genotype = splits[5];
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, CageFileLine> map = getMetaMap();
		
		for(String s : map.keySet() )
			System.out.println(s + " "  + map.get(s).time);
	}
	
}
