package scripts.farnazBarSurgery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class MetaParser2
{
	private final String patientID;
	
	public String getPatientID()
	{
		return patientID;
	}
	
	private MetaParser2(String s) throws Exception
	{
		String[] splits= s.split("\t");
		this.patientID =splits[0];
	}
	
	public static HashMap<String, MetaParser2> getMeta2Map() throws Exception
	{
		HashMap<String, MetaParser2> map = new HashMap<String, MetaParser2>();
		
		BufferedReader reader = new BufferedReader(new FileReader("C:\\BariatricSurgery_Analyses2021-main\\input\\Metadata\\reexternalredatarequest\\MASTER Microbiome paper_FF.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			
			MetaParser2 mp2 = new MetaParser2(s);
			
			if( map.containsKey(mp2.getPatientID()))
				throw new Exception("Duplicate");
			
			map.put(mp2.getPatientID(), mp2);
		}
		
		
		return map;
	}
}
