package scripts.farnazBarSurgery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class MetaParser1
{
	private final String sampleID;
	//private final int timepoint;
	//private final String patientID_timepoint;
	//private final Integer typeOfSurgery;
	
	public String getSampleID()
	{
		return sampleID;
	}
	
	private MetaParser1(String s )
	{
		String[] splits = s.split("\t");
		
		this.sampleID = splits[1];
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetaParser1> metaMap1 = getMetaMap1();
	}
	
	public static HashMap<String, MetaParser1> getMetaMap1() throws Exception
	{
		HashMap<String, MetaParser1> map = new HashMap<String, MetaParser1>();
		
		@SuppressWarnings("resource")
		BufferedReader reader= new BufferedReader(new FileReader("C:\\BariatricSurgery_Analyses2021-main\\input\\Metadata\\Metadata_BS_RYGB.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			MetaParser1 mp1 = new MetaParser1(s);
			
			if( map.containsKey(mp1.sampleID))
				throw new Exception("Duplicate " + mp1.sampleID);
			
			map.put(mp1.sampleID, mp1);
			
		}
		
		reader.close();
		return map;
	}
}
