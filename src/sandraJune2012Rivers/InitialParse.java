package sandraJune2012Rivers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import utils.ConfigReader;

public class InitialParse
{
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, Integer> countMap = new LinkedHashMap<Integer,Integer>();
		
		BufferedWriter lengthWriter = new BufferedWriter(new FileWriter(new File(ConfigReader.getSandraRiverJune2012Dir() + File.separator + 
		"lengths.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getSandraRiverJune2012Dir() + 
				File.separator + "sandraJuneForOtuThreePrimeTrim.fasta")));
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime
		(ConfigReader.getSandraRiverJune2012Dir() + 
					File.separator + "120110_reg1_sc" + File.separator + 
					"1.TCA.454Reads.fna");
		
		HashMap<String, Integer> map = SandraJune2012Metadata.getBarcodeToSampleMap();
		
		for(Integer i : map.values())
			countMap.put(i,0);
		
		List<String> list = new ArrayList<String>(map.keySet());
		
		int numScanned=0;
		int numMatched =0;
		
		for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence())
		{
			
			for( String seq : list )
				if( seq.indexOf("N") == -1 &&  fs.getSequence().startsWith(seq) && fs.getSequence().length() >=275)
				{
					String aSeq = fs.getSequence().substring(seq.length());
					
					int rightIndex = getRightIndex(aSeq);
					
					while( rightIndex != -1)
					{
						aSeq = aSeq.substring(0,rightIndex);
						rightIndex = getRightIndex(aSeq);
					}
					
					if( aSeq.length() >= 200)
					{

						numMatched++;
						
						writer.write(">" + fs.getFirstTokenOfHeader() + "_" + map.get(seq) + "\n");
						writer.write(aSeq+ "\n");
						lengthWriter.write(fs.getSequence().length() + "\n");
						
						countMap.put(map.get(seq), countMap.get(map.get(seq)) +1);
						
					}
					
				}
					
			numScanned++;
			
			System.out.println(numMatched + " " + numScanned);
		}
		
		writer.flush();  writer.close();
		
		for( int i : countMap.keySet())
			System.out.println(i + " " + countMap.get(i));
		
		lengthWriter.flush();  lengthWriter.close();
		
	}
	
	/*
	 * Requires 2 six mers to match
	 */
	private static int getRightIndex(String s)
	{
		String primerString = "CTGAGCCAGGATCAAACTCTGACTGAGCGGGCTGGCAAGGC";
		
		for(int x=4; x< primerString.length(); x++)
			if( s.endsWith(primerString.substring(0,x)))
					return s.lastIndexOf(primerString.substring(0,x));
		
		for(int x=5; x< primerString.length(); x++)
			if( s.endsWith(primerString.substring(1,x)))
					return s.lastIndexOf(primerString.substring(1,x));
				
		int oldVal = -1;
		float twoThirds = s.length()*2/3;
		
		for(int x=0; x < primerString.length() -6;x++)
		{
			int newIndex = s.lastIndexOf(primerString.substring(x,x+6));
			
			if( newIndex !=-1 && newIndex >= twoThirds )
			{
				if( oldVal==-1)
				{
					oldVal= newIndex;
				}
				else
				{
					if( newIndex > oldVal)
						return oldVal;
				}
					
			}
		}
		
		if( s.lastIndexOf("CTGAG") >= s.length() -15 )
			return s.lastIndexOf("CTGAG");
		
		if( s.lastIndexOf("TGAGC") >= s.length() -15)
			return s.lastIndexOf("TGAGC");
		
		return -1;
		
	}
}
