package scripts.lactoCheck.dada2Pipeline;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import parsers.HitScores;
import utils.ConfigReader;

public class WriteTopBlastHits
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HitScores> queryMap = 
		HitScores.getTopHitsAsQueryMap(ConfigReader.getLactoCheckDir() + File.separator + 
				"allTogetherFastaToSilvaByBlast.txt");
	
		
		HashSet<String> targets = new HashSet<>();
		
		for(String s : queryMap.keySet())
			targets.add(queryMap.get(s).getTargetId());
		
		HashMap<String, String> headers = getSilvaHeaderLines(targets);
		
		for(String s : headers.keySet())
			System.out.println(s + " " + headers.get(s));
	}
	
	private static HashMap<String, String> getSilvaHeaderLines(Set<String> queryIDS) throws Exception
	{
		HashMap<String, String> map = new HashMap<>();
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(
				"C:\\silvaDatabase\\SILVA_132_SSURef_Nr99_tax_silva.fasta");
		
		for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs= fsoat.getNextSequence())
		{
			String key = fs.getFirstTokenOfHeader();
			
			if( queryIDS.contains(key))
			{
				map.put(key, fs.getHeader());
			}
		}
		
		return map;
	}
}
