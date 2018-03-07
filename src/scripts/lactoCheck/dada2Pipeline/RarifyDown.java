package scripts.lactoCheck.dada2Pipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;
import utils.ConfigReader;

public class RarifyDown
{
	private static final Random RANDOM = new Random(42);
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> map = getDepthMap();
		
		int rareNumber = Integer.MAX_VALUE;
		
		for(Integer i : map.values())
			if( i > 50)
				rareNumber =Math.min(rareNumber, i);
		
		System.out.println(rareNumber);
	
		File fastqDir = new File(ConfigReader.getLactoCheckDir() + File.separator + 
				"fastqDemultiplexed");
		
		File fastqOutDir = new File(ConfigReader.getLactoCheckDir() + File.separator + 
				"fastqDemultiplexedRarified");
		
		for(String s : map.keySet())
		{
			int readDepth = map.get(s);
			
			if(readDepth > 50)
			{
				System.out.println(s + " " + readDepth);
				HashSet<Integer> set = getIncludeSet(readDepth, rareNumber);
				
				int numWritten =0;
				
				BufferedReader reader =  new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( fastqDir + File.separator + s ) ) ));
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
						fastqOutDir.getAbsolutePath() + File.separator + 
						s.replace(".gz", ""))));
				
				int seqIndex =0;
				for( FastQ fq= FastQ.readOneOrNull(reader); fq != null; fq = FastQ.readOneOrNull(reader))
				{
					if( set.contains(seqIndex))
					{
						set.remove(seqIndex);
						numWritten++;
						fq.writeToFile(writer);
					}
					
					
					seqIndex++;
				}
				
				writer.flush(); writer.close();
				
				reader.close();
				
				if( numWritten != rareNumber)
					throw new Exception("No");
				
				if( ! set.isEmpty())
					throw new Exception("No");
			
			}
		}
	
	}
	
	private static HashSet<Integer> getIncludeSet(int depth, int setSize )
	{
		List<Integer> list= new ArrayList<>();
		
		for( int x=0; x < depth; x++)
			list.add(x);
		
		Collections.shuffle(list, RANDOM);
		
		HashSet<Integer> set= new HashSet<>();
		
		for( int x=0; x < setSize;x++)
			set.add(list.get(x));
		
		return set;
	}
	
	private static HashMap<String, Integer> getDepthMap() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLactoCheckDir() + File.separator + 
					"readSummary.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], Integer.parseInt(splits[1]));
		}
		
		return map;
	}
}
