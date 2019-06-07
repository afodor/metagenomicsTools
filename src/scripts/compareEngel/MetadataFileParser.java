package scripts.compareEngel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class MetadataFileParser
{
	private final String sampleID;
	private final Float eupct;  // european ancestry
	private final Float yripct;  // african ancestry
	private final int c_mrace; // maternal race
	private final int c_case2; // preterm/term
	
	public String getSampleID()
	{
		return sampleID;
	}

	public Float getEupct()
	{
		return eupct;
	}

	public Float getYripct()
	{
		return yripct;
	}

	public int getC_mrace()
	{
		return c_mrace;
	}

	public int getC_case2()
	{
		return c_case2;
	}

	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileParser> map =  getMetaMap();
		
		for(String s: map.keySet())
			System.out.println(s + " " + map.get(s).sampleID + " " + map.get(s).c_case2);
		
	}
	
	private final static String FILE_NAME = "PIN0108_MicrobCov_NoSNP_20190607.txt";
	
	private MetadataFileParser(String s,HashMap<String,Integer> topLineMap)
	{
		String[] splits = s.split("\t");
		
		this.sampleID = splits[0];
		this.eupct = getFloatOrNull(splits[topLineMap.get("EUpct")]);
		this.yripct = getFloatOrNull(splits[topLineMap.get("YRIpct")]);
		this.c_mrace= Integer.parseInt(splits[topLineMap.get("C_MRACE")]);
		this.c_case2= Integer.parseInt(splits[topLineMap.get("C_CASE2")]);
	}
	
	private static Float getFloatOrNull( String s )
	{
		if( s.length() >0)
			return Float.parseFloat(s);
		
		return null;
	}
	
	public static HashMap<String, MetadataFileParser> getMetaMap() throws Exception
	{
		HashMap<String, MetadataFileParser> map = new HashMap<>();
		
		HashMap<String,Integer> topLineMap= getColumnIndexMap();
		
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getEngelCheckDir() + File.separator + 
						FILE_NAME)));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			MetadataFileParser mfp = new MetadataFileParser(s, topLineMap);
			
			if( map.containsKey(mfp.sampleID))
				throw new Exception("Duplicate "  + mfp.sampleID);
			
			map.put(mfp.sampleID, mfp);
		}
		
		reader.close();
		
		return map;
	}
	
	private static HashMap<String, Integer> getColumnIndexMap()
		throws Exception
	{
		HashMap<String, Integer> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getEngelCheckDir() + File.separator + 
						FILE_NAME)));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=0; x< topSplits.length; x++)
		{
			if( map.containsKey(topSplits[x]))
				throw new Exception("Duplicate " + topSplits[x]);
			
			map.put(topSplits[x], x);
		}
		
		reader.close();
		return map;
	}
}
