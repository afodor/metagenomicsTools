package scripts.farnazBarSurgery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class MetaParser1
{
	private final String sampleID;
	private final int timepoint;
	private final String patientID_timepoint;
	private final int run;
	private final Integer typeOfSurgery;
	private final String patientId;
	
	public int getTimepoint()
	{
		return timepoint;
	}

	public String getPatientID_timepoint()
	{
		return patientID_timepoint;
	}

	public int getRun()
	{
		return run;
	}

	public Integer getTypeOfSurgery()
	{
		return typeOfSurgery;
	}

	public String getPatientId()
	{
		return patientId;
	}

	public String getSampleID()
	{
		return sampleID;
	}
	
	private MetaParser1(String s ) throws Exception
	{
		String[] splits = s.split("\t");
		
		this.sampleID = splits[1];
		this.patientID_timepoint = splits[2];
		
		int lastIndex = this.patientID_timepoint.lastIndexOf("-");
		
		this.patientId =  this.patientID_timepoint.substring(0, lastIndex);
		
		int timepoint = Integer.parseInt(this.patientID_timepoint.substring(lastIndex+1, this.patientID_timepoint.length()));
		
		Integer spreadsheetTimepoint = Integer.parseInt(splits[3]);
		
		if( timepoint != spreadsheetTimepoint)
			throw new Exception("Spreadsheet mismatch");
		
		this.timepoint = timepoint;
		this.run = Integer.parseInt(splits[4]);
		
		if(  splits[7].equals("NA"))
			this.typeOfSurgery = null;
		else
			this.typeOfSurgery = Integer.parseInt(splits[7]);
			
		
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
