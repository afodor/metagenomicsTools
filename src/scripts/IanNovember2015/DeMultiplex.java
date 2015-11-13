package scripts.IanNovember2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

import parsers.FastQ;
import utils.ConfigReader;

public class DeMultiplex
{
	private final String barcodeSequence;
	private final int sampleID;
	private final String description;
	
	public String getDescription()
	{
		return description;
	}
	
	public int getSampleID()
	{
		return sampleID;
	}
	
	private DeMultiplex(String s) throws Exception
	{
		String[] splits = s.split("\t");
		if(splits.length != 4)
			throw new Exception("No");
		
		this.sampleID = Integer.parseInt(splits[0]);
		this.barcodeSequence = splits[1];
		this.description = splits[3];
	}
	
	public static HashMap<String, DeMultiplex> getBarcodeMap() throws Exception
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
		HashMap<String, DeMultiplex> sampleIDs = getBarcodeMap();
		System.out.println(sampleIDs.size());
		
		HashSet<Integer> sampleID = new HashSet<Integer>();
		
		for( DeMultiplex d : sampleIDs.values())
		{
			if( sampleID.contains(d.sampleID))
				throw new Exception("No");
			
			sampleID.add(d.sampleID);
		}
		
		System.out.println(sampleID);
		System.out.println(sampleID.size());
		
		
		BufferedReader reader = new BufferedReader(
					new FileReader(new File(
				ConfigReader.getIanNov2015Dir() + File.separator + 
				"behavbugs_noindex_l001_r2_001" +  File.separator + "mcsmm" + 
						File.separator + "mcsmm.behavbugsn")));
		
		long numRead =0;
		long numMatched =0;
		for( FastQ fastq = FastQ.readOneOrNull(reader); fastq != null; 
				fastq = FastQ.readOneOrNull(reader))
		{
			numRead++;
			
			for( String s : sampleIDs.keySet())
				if( fastq.getSequence().startsWith(s))
					numMatched++;
			
			if( numRead % 10000 == 0 )
				System.out.println(numRead  + " " + numMatched + " " + ((double)numMatched) / numRead);
		}
		
		System.out.println(numRead  + " " + numMatched + " " + ((double)numMatched) / numRead);
		reader.close();
	}
}
