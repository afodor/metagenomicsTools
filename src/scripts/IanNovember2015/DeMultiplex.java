package scripts.IanNovember2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class DeMultiplex
{
	private final String barcodeSequence;
	private final int sampleID;
	private final String description;
	
	private DeMultiplex(String s) throws Exception
	{
		String[] splits = s.split("\t");
		if(splits.length != 4)
			throw new Exception("No");
		
		this.sampleID = Integer.parseInt(splits[0]);
		this.barcodeSequence = splits[1];
		this.description = splits[3];
	}
	
	public static HashMap<String, DeMultiplex> getSampleID() throws Exception
	{
		HashMap<String, DeMultiplex> map = new HashMap<String,DeMultiplex>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getIanNov2015Dir() + File.separator + 
						"MAP_HC_R1.txt")));
		
		reader.readLine();
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			DeMultiplex dm = new DeMultiplex(s);
			
			if( map.containsKey(dm.barcodeSequence))
				throw new Exception("No");
			
			map.put(dm.barcodeSequence, dm);
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, DeMultiplex> sampleIDs = getSampleID();
		System.out.println(sampleIDs.size());
	}
}
