package scripts.ahnVickieMouse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class MetadataFileLine
{
	//Diet	Disease_Status	Sample_Type	Menopause	Pair	Study #	Study Year

	private final String sampleName;
	private final String diet;
	private final String diseaseStatus;
	private final String sample_Type;
	private final String menopause;
	private final Integer pair;
	private final int studyNumber;
	private final int studyYear;
	
	public String getSampleName()
	{
		return sampleName;
	}

	public String getDiet()
	{
		return diet;
	}

	public String getDiseaseStatus()
	{
		return diseaseStatus;
	}

	public String getSample_Type()
	{
		return sample_Type;
	}

	public String getMenopause()
	{
		return menopause;
	}

	public Integer getPair()
	{
		return pair;
	}

	public int getStudyNumber()
	{
		return studyNumber;
	}

	public int getStudyYear()
	{
		return studyYear;
	}

	private MetadataFileLine(String s)
	{
		String[] splits = s.split("\t");
		
		this.sampleName = splits[0];
		this.diet = splits[3];
		this.diseaseStatus = splits[4];
		this.sample_Type = splits[5];
		this.menopause = splits[6];
		
		if( splits[7].equals("NA"))
			this.pair = null;
		else
			this.pair = Integer.parseInt(splits[7]);
		this.studyNumber = Integer.parseInt(splits[8]);
		this.studyYear = Integer.parseInt(splits[9]);
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileLine> map = getMetaMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s).getStudyYear());
	}
	
	@SuppressWarnings("resource")
	public static HashMap<String, MetadataFileLine> getMetaMap() throws Exception
	{
		HashMap<String, MetadataFileLine>  map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\AnhVickiMouseData\\mouse_metadata.txt")));
		
		reader.readLine();
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			MetadataFileLine mfl = new MetadataFileLine(s);
			
			if( map.containsKey(mfl.sampleName))
				throw new Exception("No");
			
			map.put(mfl.sampleName, mfl);
		}
		
		reader.close();
		return map;
	}
}
