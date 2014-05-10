package kleb;

import java.io.File;
import java.util.HashMap;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import probabilisticNW.ProbSequence;
import utils.ConfigReader;

public class SimpleClusterToPhyloXML
{
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, ProbSequence> probSeqMap = getAsProbSequences();
	}
	
	private static HashMap<Integer, ProbSequence> getAsProbSequences()
		throws Exception
	{
		HashMap<Integer, ProbSequence> map = new HashMap<Integer, ProbSequence>();
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(ConfigReader.getKlebDir()+
				File.separator + "alignmentOnlyDifferingPositions.txt");
		
		for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs=fsoat.getNextSequence())
		{
			Integer key = Integer.parseInt( fs.getFirstTokenOfHeader().replaceAll("_V1", ""));
			System.out.println(key);
			if( map.containsKey(key))
				throw new Exception("Parsing error");
			
			ProbSequence probSeq = new ProbSequence(fs.getSequence(), "" + key);
			map.put(key,probSeq);
		}
		
		return map;
	}
}
