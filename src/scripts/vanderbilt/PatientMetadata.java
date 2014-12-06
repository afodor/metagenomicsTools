package scripts.vanderbilt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class PatientMetadata
{
	private final String sample;
	private final String studyID;
	private final String treatment;
	
	public String getSample()
	{
		return sample;
	}

	public String getStudyID()
	{
		return studyID;
	}

	public String getTreatment()
	{
		return treatment;
	}

	public PatientMetadata(String s) throws Exception
	{
		String[] splits = s.split("\t");
		this.sample = splits[0];
		this.studyID = splits[5];
		this.treatment = splits[6];
	}
	
	public static HashMap<String, PatientMetadata> getAsMap() throws Exception
	{
		HashMap<String, PatientMetadata> map = new HashMap<String, PatientMetadata>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getVanderbiltDir() + File.separator + "mapping_file.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			PatientMetadata pm = new PatientMetadata(s);
			if( map.containsKey(pm.sample))
				throw new Exception("No");
			
			map.put(pm.sample, pm);
		}
		
		return map;
	}
	
}
