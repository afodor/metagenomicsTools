package kleb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import parsers.FastaSequence;
import utils.ConfigReader;

public class QuickSnpDistance
{
	private static HashSet<Integer> diffPositions = new HashSet<Integer>();
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, FastaSequence> map = getAsMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getKlebDir() + File.separator + "distancesOnlySubset.txt")));
		
		writer.write("xGenome\tyGenome\tdistance\n");
		
		List<String> aList = new ArrayList<String>(map.keySet());
		Collections.sort(aList);
		
		for( int x=0; x < aList.size() ; x++)
			for(int y=0; y < aList.size(); y++)
				{
					System.out.println(x + " " + y);
					writer.write(aList.get(x) + "\t");
					writer.write(aList.get(y) + "\t");
					writer.write(getNumDifferent(map.get(aList.get(x)), map.get(aList.get(y))) 
								+ "\n");
					writer.flush();
				}
		
		writer.flush();  writer.close();
		
		//writeDiffPositionsFiles( new ArrayList<FastaSequence>( map.values()));
	}
	
	private static void writeDiffPositionsFiles( List<FastaSequence> list ) throws Exception
	{
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(ConfigReader.getKlebDir() + 
				File.separator + "alignmentOnlyDifferingPositions.txt")));
		
		for(FastaSequence fs : list)
		{
			writer.write(fs.getHeader() + "\n");
			
			String seq = fs.getSequence();
			for( int x=0; x < seq.length(); x++)
				if( diffPositions.contains(x))
					writer.write("" + seq.charAt(x));
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static HashMap<String, FastaSequence> getAsMap() throws Exception
	{
		HashMap<String, FastaSequence> map = new LinkedHashMap<String, FastaSequence>();
		
		List<FastaSequence> list = 
				FastaSequence.readFastaFile(ConfigReader.getKlebDir() +File.separator + 
						"Klebs.mfa");
		
		for(FastaSequence fs : list)
		{
			String key = fs.getFirstTokenOfHeader().replace("_V1", "");
			
			if( map.containsKey(key))
				throw new Exception("Duplicate key " + key);
			
			map.put(key, fs);
		}
		
		return map;
		
	}
		
	static int getNumDifferent( FastaSequence fs1, FastaSequence fs2 ) throws Exception
	{
		int numDifferent =0;
		
		String seq1 = fs1.getSequence();
		String seq2 = fs2.getSequence();
		
		if( seq1.length() != seq2.length())
			throw new Exception("No");
		
		for( int x=0; x < seq1.length(); x++)
			if( seq1.charAt(x) != seq2.charAt(x))
			{
				numDifferent++;
				diffPositions.add(x);
			}
				
		return numDifferent;
	}
	
}
