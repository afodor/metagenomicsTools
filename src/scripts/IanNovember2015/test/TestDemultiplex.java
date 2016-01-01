package scripts.IanNovember2015.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import utils.ConfigReader;

public class TestDemultiplex
{
	private static HashMap<String, Integer> getBarcodeToSampleMap() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getIanNov2015Dir() + File.separator + 
				"MAP_HC_R1.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[1]))
				throw new Exception("No");
			
			map.put(splits[1], Integer.parseInt(splits[0]));
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> barcodeMap = getBarcodeToSampleMap();
		HashMap<String, Integer> expectedMap = getExpectedSubjectID();
		System.out.println("got " + expectedMap.size());
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getIanNov2015Dir() + File.separator + 
			"behavbugs_noindex_l001_r2_001" + File.separator +  "mcsmm" + 
					File.separator + "mcsmm.behavbugsn"	)));
		
		int numPassed = 0;
		int numNotFound =0;
		for(String s= reader.readLine(); s != null && s.trim().length() >0; s= reader.readLine())
		{
			String key = new StringTokenizer(s).nextToken();
			
			if( ! key.startsWith("@"))
				throw new Exception("Incorrect line " + key);
			
			String sequence = reader.readLine();
			
			for( int x=0; x < 2; x++)
			{
				String nextLine = reader.readLine();
				
				if( nextLine == null)
					throw new Exception("No");
			}
			
			Integer expectedFromBarcode = barcodeMap.get(sequence);
			
			if( expectedFromBarcode != null)
			{
				Integer expectedFromSeq = expectedMap.get(key);
				
				if(expectedFromSeq == null)
					throw new Exception("Could not find " + key);
				
				if( ! expectedFromBarcode.equals(expectedFromSeq))
					throw new Exception("Mismatch " + key + " " + expectedFromBarcode + " " + expectedFromSeq);
				
				numPassed++;
				
			}
			else
			{
				numNotFound++;
			}
			
			if( (numPassed + numNotFound) % 10000== 0)
				System.out.println("passed " + numPassed + " skipped "+ numNotFound);
		}
				
		System.out.println("passed " + numPassed + " skipped "+ numNotFound);
		System.out.println("global pass");
	}
	
	private static HashMap<String, Integer> getExpectedSubjectID() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		
		File topDir = new File(ConfigReader.getIanNov2015Dir() + File.separator + 
					"fastaFiles");
		
		for(String s : topDir.list())
		{
			System.out.println(s);
			StringTokenizer sTokenizer = new StringTokenizer(s, "_");
			sTokenizer.nextToken();
			
			String second = sTokenizer.nextToken();
			int expected = Integer.parseInt(second.substring(0, second.indexOf(".")));
			
			List<FastaSequence> list = 
					FastaSequence.readFastaFile(topDir.getAbsolutePath() + File.separator + s);
			
			for(FastaSequence fs : list)
			{
				String header = fs.getFirstTokenOfHeader();
				header = header.substring(0, header.lastIndexOf("_"));
				
				if( map.containsKey(header) && ! map.get(header).equals(expected))
					throw new Exception("No " + header +  " " + map.get(header) + " " + expected );
				
				map.put(header,expected);
			}
				
		}
				
		return map;
	}
}
