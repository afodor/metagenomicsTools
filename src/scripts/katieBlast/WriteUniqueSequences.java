package scripts.katieBlast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import parsers.HitScores;
import utils.ConfigReader;

public class WriteUniqueSequences
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HitScores> map = HitScores.getTopHitsAsTargetMap(new File( 
			ConfigReader.getKatieBlastDir() + File.separator + "2YAJToGut.txt"));
		
		System.out.println(map.size());
		
		HashSet<String> postFilter=  new LinkedHashSet<>();
		
		for( HitScores hs : map.values() )
			if( hs.getPercentIdentity() >=25 && hs.getEScore() <= 1e-10)
				postFilter.add(hs.getTargetId());
		
		System.out.println( postFilter.size() );
		
		HashMap<String, FastaSequence> seqMap = getSequenceMap(postFilter);
		
		System.out.println(seqMap.size());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getKatieBlastDir() + File.separator + "matching_2YAJ.txt")));
		
		for(FastaSequence fs : seqMap.values())
		{
			writer.write(">" + fs.getHeader() + "\n");
			writer.write( fs.getSequence() + "\n");
		}
		
		writer.flush();  writer.close();
		
	}
	
	private static HashMap<String, FastaSequence> getSequenceMap(HashSet<String> set) throws Exception
	{
		HashMap<String, FastaSequence>  map = new LinkedHashMap<>();
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(ConfigReader.getKatieBlastDir() + File.separator + 
				"all.fst");
		
		for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence() )
		{
			if(  set.contains(fs.getFirstTokenOfHeader()))
			{
				map.put(fs.getFirstTokenOfHeader(), fs);
			}
		}
		
		return map;
	}
	
}
