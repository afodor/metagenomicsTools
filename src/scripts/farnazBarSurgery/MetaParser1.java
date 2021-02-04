package scripts.farnazBarSurgery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class MetaParser1
{
	private final String sampleID;
	private final Integer timepoint;
	//private final Integer typeOfSurgery;
	private final String patientId;
	private final Integer site;
	private final String sampleType;
	
	public Integer getTimepoint()
	{
		return timepoint;
	}

	
	public String getPatientId()
	{
		return patientId;
	}

	public Integer getSite()
	{
		return site;
	}


	public String getSampleType()
	{
		return sampleType;
	}


	public String getSampleID()
	{
		return sampleID;
	}
	
	private MetaParser1(String s ) throws Exception
	{
		String[] splits = s.split("\t");
		
		this.sampleID = splits[0];
		this.patientId = splits[1];
		
		if( ! splits[2].equals("NA"))
			this.timepoint = Integer.parseInt(splits[2]);
		else
			this.timepoint = null;
		
		
		if( ! splits[3].equals("NA"))
			this.site= Integer.parseInt(splits[3]);
		else
			this.site= null;
		
		this.sampleType = splits[4];
		
		
	}
	
	public static HashMap<String, Integer> getSurgeryType() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\BariatricSurgery_Analyses2021-main\\input\\Metadata\\TypeDateofSurgery-BiobehavioralR016-12-2020_FF.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 3)
				throw new Exception("No");
			
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, Integer.parseInt(splits[2]));
		}
		
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		//HashMap<String, MetaParser1> metaMap1 = getMetaMap1();
		
		HashMap<String, Integer> map = getSurgeryType();
		
		System.out.println( map.get("BIO-1-203"));
	}
	
	public static HashMap<String, MetaParser1> getMetaMap1() throws Exception
	{
		HashMap<String, MetaParser1> map = new HashMap<String, MetaParser1>();
		
		@SuppressWarnings("resource")
		BufferedReader reader= new BufferedReader(new FileReader("C:\\BariatricSurgery_Analyses2021-main\\input\\Metadata\\metadata.txt"));
		
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
