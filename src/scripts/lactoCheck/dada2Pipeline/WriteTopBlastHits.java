package scripts.lactoCheck.dada2Pipeline;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
		
		HashSet<String> targets = new LinkedHashSet<>();
		
		for(String s : queryMap.keySet())
			targets.add(queryMap.get(s).getTargetId());
		
		HashMap<String, String> headers = getSilvaHeaderLines(targets);
		
		for(String s : headers.keySet())
			System.out.println(s + " " + headers.get(s));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getLactoCheckDir() + File.separator + "silvaToDada2TopHits.txt")));
		
		writer.write("queryID\ttargetID\talignmentLength\tpercentIdentity\tnumMismatches\teScore\theaderLine\n");
		
		for(String s : queryMap.keySet())
		{
			HitScores hs = queryMap.get(s);
			writer.write(hs.getQueryId() + "\t");
			writer.write(hs.getTargetId()+"\t");
			writer.write(hs.getAlignmentLength() + "\t");
			writer.write(hs.getPercentIdentity() + "\t");
			writer.write(hs.getNumMismatches() + "\t");
			writer.write(hs.getEScore() + "\t");
			writer.write(headers.get(hs.getTargetId()) + "\n");
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
