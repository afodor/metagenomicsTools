package scripts.TopeSeptember2015Run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataParser
{
	private final String studyID;
	private final int caseControl;
	
	public String getStudyID()
	{
		return studyID;
	}
	
	public int getCaseControl()
	{
		return caseControl;
	}
	
	private MetadataParser(String s)
	{
		String[] splits = s.split("\t");
		
		this.studyID = splits[0];
		
		if (splits[1].trim().length() == 0 )
			this.caseControl = -1;
		else
			this.caseControl = Integer.parseInt(splits[1]);
	}
	
	public static HashMap<String, MetadataParser> getMap() throws Exception
	{
		HashMap<String, MetadataParser> map = new HashMap<String, MetadataParser>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getTopeSep2015Dir() +File.separator + 
			"Copy of DHSV Illumina Sets 1 and 2 Metadata.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine() ; s != null; s =reader.readLine())
		{
			MetadataParser mp = new MetadataParser(s);
			
			//if( map.containsKey(mp.studyID) && ! mp.studyID.equals("Bacteria Pool")
				//				&& ! mp.studyID.equals("5BM-007") && 
					//			! mp.studyID.equals("DHSV Pool") && ! mp.studyID.equals("Mouse 13"))
			//	throw new Exception("No " + mp.studyID);
			
			map.put(mp.studyID, mp);
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParser> map = getMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s).caseControl);
	}
}
