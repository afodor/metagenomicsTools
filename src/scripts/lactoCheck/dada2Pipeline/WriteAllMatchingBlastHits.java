package scripts.lactoCheck.dada2Pipeline;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import parsers.HitScores;
import utils.ConfigReader;

public class WriteAllMatchingBlastHits
{
	
	public static void main(String[] args) throws Exception
	{
		List<HitScores> hitList = HitScores.getAsList(ConfigReader.getLactoCheckDir() + File.separator + 
				"allTogetherFastaToSilvaByBlast.txt");
		
		HashMap<String,HashSet<String>> queryMap = new LinkedHashMap<>();
		HashSet<String> allTargets = new HashSet<>();
		
		for(HitScores hs : hitList)
		{
			if( hs.getAlignmentLength() >=90 & hs.getNumMismatches() <= 1 )
			{
				HashSet<String> inner = queryMap.get(hs.getQueryId());
				
				if( inner == null)
				{
					inner = new HashSet<>();
					queryMap.put(hs.getQueryId(), inner);
				}
				
				inner.add(hs.getTargetId());
				allTargets.add(hs.getTargetId());
			}
		}
		
		HashMap<String, String> headers = getSilvaHeaderLines(allTargets);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getLactoCheckDir() + File.separator + "silvaToDada2TopHitsMultiple.txt")));
		
		writer.write("queryID\ttargetID\theaderLine\n");
		
		for(String s : queryMap.keySet())
		{
			for(String s2 : queryMap.get(s))
			{
				String headerVal = headers.get(s2).substring(1);
				if( headerVal.indexOf("uncultured") == -1 && headerVal.indexOf("unidentified")== -1)
				{
					writer.write(s + "\t");
					writer.write(s2 + "\t");
					writer.write( headerVal+ "\n");
				}
			}
		}
		
		writer.flush();  writer.close();
	}
	
	private static HashMap<String, String> getSilvaHeaderLines(Set<String> queryIDS) throws Exception
	{
		HashMap<String, String> map = new LinkedHashMap<>();
		
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
		
		fsoat.close();
		
		return map;
	}
}
