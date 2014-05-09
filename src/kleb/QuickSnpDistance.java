package kleb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import parsers.FastaSequence;
import utils.ConfigReader;

public class QuickSnpDistance
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, FastaSequence> map = getAsMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getKlebDir() + File.separator + "distances.txt")));
		
		writer.write("xGenome\tyGenome\tdistance\n");
		
		List<String> aList = new ArrayList<String>(map.keySet());
		Collections.sort(aList);
		
		for( int x=0; x < aList.size() -1; x++)
			for(int y=x+1 ; y < aList.size(); y++)
			{
				System.out.println(x + " " + y);
				writer.write(aList.get(x) + "\t");
				writer.write(aList.get(y) + "\t");
				writer.write(getNumDifferent(map.get(aList.get(x)), map.get(aList.get(y))) + "\n");
				writer.flush();
			}
		
		writer.flush();  writer.close();
	}
	
	private static HashMap<String, FastaSequence> getAsMap() throws Exception
	{
		HashMap<String, FastaSequence> map = new HashMap<String, FastaSequence>();
		
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
	
	private static int getNumDifferent( FastaSequence fs1, FastaSequence fs2 ) throws Exception
	{
		int numDifferent =0;
		
		String seq1 = fs1.getSequence();
		String seq2 = fs2.getSequence();
		
		if( seq1.length() != seq2.length())
			throw new Exception("No");
		
		for( int x=0; x < seq1.length(); x++)
			if( seq1.charAt(x) != seq2.charAt(x))
				numDifferent++;
		
		return numDifferent;
	}
	
}
