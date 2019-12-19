package scripts.Pierce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import utils.ConfigReader;

public class MetadataParser
{
	private final int patientNum;
	private final String status;
	private final String sampleID;
	private final String stage;
	private final String treatmentType;
	private final String smokingStatus;
	private final String baseline_Antibiotics_3_mo; 
	private final String mucositis;
	private final String candidiasis;
	private final String hPV16or18;	
	private final int age;
	private final String fileLine;
	
	public int getPatientNum()
	{
		return patientNum;
	}

	public String getStatus()
	{
		return status;
	}

	public String getSampleID()
	{
		return sampleID;
	}

	public String getStage()
	{
		return stage;
	}

	public String getTreatmentType()
	{
		return treatmentType;
	}


	public String getSmokingStatus()
	{
		return smokingStatus;
	}


	public String getBaseline_Antibiotics_3_mo()
	{
		return baseline_Antibiotics_3_mo;
	}


	public String getMucositis()
	{
		return mucositis;
	}


	public String getCandidiasis()
	{
		return candidiasis;
	}


	public String gethPV16or18()
	{
		return hPV16or18;
	}


	public int getAge()
	{
		return age;
	}

	public String getFileLine()
	{
		return fileLine;
	}

	private MetadataParser(String s) throws Exception
	{
		//System.out.println(s);
		this.fileLine = s;
		String[] splits = s.split("\t");
		
		if( splits.length != 11)
			throw new Exception();
		
		this.patientNum =Integer.parseInt(splits[0]);
		this.status = splits[1];
		this.sampleID= splits[2];
		this.stage = splits[3];
		this.treatmentType = splits[4];
		this.smokingStatus =splits[5];
		this.baseline_Antibiotics_3_mo= splits[6];
		this.mucositis = splits[7];
		this.candidiasis = splits[8];
		this.hPV16or18 =splits[9];	
		this.age = Integer.parseInt(splits[10]);
	}
	
	public static String getTopMetaLine() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getPierce2019Dir() + File.separator+ "OPCStudySubset.txt"	)));
			
		String aLine = reader.readLine();
		
		reader.close();
		
		return aLine;
	}
	
	public static HashMap<String, MetadataParser> getMetaMap() throws Exception
	{
		HashMap<String, MetadataParser> map = new HashMap<String, MetadataParser>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getPierce2019Dir() + File.separator+ "OPCStudySubset.txt"	)));
		
		reader.readLine();
		
		for(String s =reader.readLine(); s != null; s= reader.readLine())
		{
			MetadataParser mp = new MetadataParser(s);
			
			if( map.containsKey(mp.sampleID))
				throw new Exception("No");
			
			map.put(mp.sampleID,mp);
		}
		
		reader.close();
		
		return map;
				
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParser>  map=MetadataParser.getMetaMap();
		
		for(String s : map.keySet())
			System.out.println( s + " " +  map.get(s).patientNum);
	}

}
