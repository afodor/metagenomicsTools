package scripts.KeHospitalPivot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class MetaMapFileLine
{
	private final String sampleID;
	private final String patientID;
	private final int timepoint;
	private final String patientInOut;
	private final String donor;
	private final String bin;
	
	public String getBin()
	{
		return bin;
	}
		
	public String getSampleID()
	{
		return sampleID;
	}

	public String getPatientID()
	{
		return patientID;
	}

	public int getTimepoint()
	{
		return timepoint;
	}

	public String getPatientInOut()
	{
		return patientInOut;
	}

	public String getDonor()
	{
		return donor;
	}
	
	public static HashMap<String, MetaMapFileLine> getMetaMap() throws Exception
	{
		HashMap<String, MetaMapFileLine> map = new HashMap<>();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader("C:\\Ke_Hospital\\MetaWithInOutDonerTreatmentType.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			MetaMapFileLine mfl = new MetaMapFileLine(s);
			
			if( map.containsKey(mfl.sampleID))
				throw new Exception("Duplicate " + mfl.sampleID);
			
			map.put(mfl.sampleID, mfl);
		}
		
		reader.close();
		
		return map;
	}

	private MetaMapFileLine(String s )
	{
		String[] splits = s.split("\t");
		
		this.sampleID = splits[0];
		this.patientID = splits[1];
		this.timepoint = Integer.parseInt(splits[2]);
		this.bin = splits[6];
		this.patientInOut = splits[7];
		this.donor = splits[8];
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetaMapFileLine> map = getMetaMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s).getPatientInOut());
	}
}
