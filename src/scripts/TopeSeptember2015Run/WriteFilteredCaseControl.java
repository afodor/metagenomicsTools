package scripts.TopeSeptember2015Run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

import utils.ConfigReader;

public class WriteFilteredCaseControl
{
	public static void main(String[] args) throws Exception
	{
		HashSet<String> set = getIncludeSet();
		
		for(String s : set)
			System.out.println(s);
	}
	
	private static HashSet<String> getIncludeSet() throws Exception
	{
		HashMap<String, Integer> counts = new HashMap<String,Integer>();
		HashMap<String, String> sampleToKey = new HashMap<String,String>();
		HashSet<String> observedKeys = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getTopeSep2015Dir() + File.separator + 
				"spreadsheets" + File.separator + "pcoa_phylumWithMetadata.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits =s.split("\t");
			String key = splits[0];
			
			if( observedKeys.contains(key))
				throw new Exception("Parsing error");
			
			observedKeys.add(key);
			
			String sample = splits[2];
			
			int readNumber = Integer.parseInt(splits[3]);
			
			if( readNumber != 1 && readNumber != 4)
				throw new Exception("Parsing error");
			
			if( readNumber == 1 && ! splits[4].equals("NA"))
			{
				int caseControl = Integer.parseInt(splits[4]);
				
				if( caseControl != 0 && caseControl != 1 && caseControl != -1)
					throw new Exception("Parsing error");
				
				if( caseControl != -1 )
				{
					int numSequences = Integer.parseInt(splits[5].replace(".0", ""));
					
					if( numSequences < 0 )
						throw new Exception("Parsing error");
					
					Integer oldSequences = counts.get(sample);
					
					if( oldSequences == null || numSequences > oldSequences)
					{
						counts.put(sample, numSequences);
						sampleToKey.put(sample,key);
					}
				}
			}
			
		}
		
		return new HashSet<>( sampleToKey.values());
	}
}
