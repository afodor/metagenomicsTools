package scripts.luthurJan2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class MetadataFileParser
{
	private final String sampleID;
	private final String mouseID;
	private final String sex;
	private final String cage;
	private final String diet;
	private final String challenge;
	private final String age;
	private final String dayOnDiet;
	private final String dayAfterFMT;
	private final String daysPostChallenge;
	private final String experiment;
	
	
	public String getSampleID()
	{
		return sampleID;
	}

	public String getMouseID()
	{
		return mouseID;
	}

	public String getSex()
	{
		return sex;
	}

	public String getCage()
	{
		return cage;
	}

	public String getDiet()
	{
		return diet;
	}

	public String getChallenge()
	{
		return challenge;
	}

	public String getAge()
	{
		return age;
	}

	public String getDayOnDiet()
	{
		return dayOnDiet;
	}

	public String getDayAfterFMT()
	{
		return dayAfterFMT;
	}

	public String getDaysPostChallenge()
	{
		return daysPostChallenge;
	}
	
	public String getExperiment()
	{
		return experiment;
	}

	private MetadataFileParser(String s) throws Exception
	{
		String[] splits =s.split("\t");
		
		this.sampleID =splits[0];
		this.mouseID = splits[5];
		this.sex= splits[8];
		this.experiment = splits[3];
		this.cage =splits[13];
		this.diet = splits[14];
		this.challenge = splits[15];
		this.age = splits[16];
		this.dayOnDiet = splits[17];
		this.dayAfterFMT = splits[18];
		this.daysPostChallenge = splits[19];
	}
	
	public static HashMap<String, MetadataFileParser> getMap() throws Exception
	{
		HashMap<String, MetadataFileParser> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getLuthurJan2019Dir() + 
			File.separator + "data" + File.separator + "luther.mdmf.2018.02.20.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			MetadataFileParser mfp = new MetadataFileParser(s);
			
			if( map.containsKey(mfp.sampleID))
				throw new Exception("Duplicate");
			
			map.put(mfp.sampleID,mfp);
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileParser> map =getMap();
		
		for(String s: map.keySet())
			System.out.println(s + " " + map.get(s).daysPostChallenge);
	}
}
