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
				ConfigReader.getKlebDir() + File.separator +
				"distancesUpperTriangle.txt")));
		
		writer.write("xGenome\tyGenome\tdistance\n");
		
		//HashMap<Integer, Integer> threshMap = NumberVsEntropy.getPositionVsNumChangesMap();
		
		List<String> aList = new ArrayList<String>(map.keySet());
		Collections.sort(aList);
		
		for( int x=0; x < aList.size() -1; x++)
			for(int y=x+1; y < aList.size(); y++)
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
	
	
	private static HashMap<String, FastaSequence> getAsMap() throws Exception
	{
		HashMap<String, FastaSequence> map = new LinkedHashMap<String, FastaSequence>();
		
		List<FastaSequence> list = 
				FastaSequence.readFastaFile(ConfigReader.getKlebDir() +File.separator + 
						"all76.mfa");
		
		for(FastaSequence fs : list)
		{
			String key = fs.getFirstTokenOfHeader().replace("_V1", "");
			
			if( map.containsKey(key))
				throw new Exception("Duplicate key " + key);
			
			map.put(key, fs);
		}
		
		return map;
		
	}
		
	/*
	 * If positionToNumDiffMap is null it is ignored.
	 * 
	 * Otherwise, only columns that are indicated in the key of the map with a value <= threshold are included in 
	 * the SNP distance
	 */
	static int getNumDifferent( FastaSequence fs1, FastaSequence fs2, 
				HashMap<Integer, Integer> positionToNumDiffMap, int threshold ) throws Exception
	{
		int numDifferent =0;
		
		String seq1 = fs1.getSequence();
		String seq2 = fs2.getSequence();
		
		if( seq1.length() != seq2.length())
			throw new Exception("No");
		
		for( int x=0; x < seq1.length(); x++)
		{
			if( positionToNumDiffMap == null || 
					(positionToNumDiffMap.get(x) != null && positionToNumDiffMap.get(x) <= threshold ) )
			{
				if( seq1.charAt(x) != seq2.charAt(x))
				{
					numDifferent++;
					diffPositions.add(x);
				}
			}
		}
				
		return numDifferent;
	}
	
	static int getNumDifferent( FastaSequence fs1, FastaSequence fs2) throws Exception
	{
		return getNumDifferent(fs1, fs2, null, -1);
	}
	
}
