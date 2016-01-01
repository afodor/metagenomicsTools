package scripts.IanNovember2015.test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import utils.ConfigReader;

public class TestDemultiplex
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> expectedMap = getExpectedSubjectID();
		System.out.println("got " + expectedMap.size());
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
