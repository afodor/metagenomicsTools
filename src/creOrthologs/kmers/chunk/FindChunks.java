package creOrthologs.kmers.chunk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.ConfigReader;

public class FindChunks
{
	public static final double INITIATION_THRESHOLD = 0.85f;
	public static final double EXTENSION_THRESHOLD = 0.9f;
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = getList();
		List<ChunkHolder> chunks = getChunks(list);
		writeChunks(chunks);
 	}
	
	private static boolean positionIsInChunk(List<ChunkHolder> list, int position)
	{
		for(ChunkHolder ch : list)
			if( ch.firstStart >= position && ch.lastStart <= position)
				return true;
		
		return false;
	}
	
	private static void writeChunks( List<ChunkHolder> list ) throws Exception
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
			"pneuOnlyChunks_" + nf.format(INITIATION_THRESHOLD) + "_" 
					+  nf.format(EXTENSION_THRESHOLD) + ".txt")));
		
		writer.write("InitialStart\tEndStart\tlength\taverage\n");
		
		for(ChunkHolder ch : list)
		{
			writer.write(ch.firstStart + "\t");
			writer.write(ch.lastStart + "\t");
			writer.write(ch.n + "\t");
			writer.write( ch.spearmanSum / ch.n + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	
	private static List<Holder> getList() throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
				"initialConstrainedMap_pneu_only.txt")));
		
		reader.readLine();
		
		for(String s =reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 3)
				throw new Exception("No");
			
			Holder h = new Holder();
			list.add(h);
			h.startPos = Integer.parseInt(splits[0]);
			h.spearman= Double.parseDouble(splits[2]);
		}
		
		Collections.sort(list);
		return list;
	}
	
	private static class ChunkHolder
	{
		private int firstStart;
		private int lastStart;
		double spearmanSum =0;
		int n=0;
	}
	
	private static List<ChunkHolder> getChunks( List<Holder> list )
	{
		List<ChunkHolder> chunks = new ArrayList<ChunkHolder>();
		
		ChunkHolder currentChunk = null;
		
		int lastIndex = 0;
		
		while(lastIndex < list.size())
		{
			Holder thisHolder = list.get(lastIndex);
			if( currentChunk == null)
			{
				if(thisHolder.spearman <= INITIATION_THRESHOLD)
				{
					currentChunk = new ChunkHolder();
					chunks.add(currentChunk);
					currentChunk.firstStart = thisHolder.startPos;
					currentChunk.lastStart = thisHolder.startPos;
					currentChunk.n = 1;
					currentChunk.spearmanSum += thisHolder.spearman;
					
					int lookBack = lastIndex -1;
					
					boolean stop = false;
					while(lookBack > 0 && ! stop)
					{
						Holder previous = list.get(lookBack);
						
						if( previous.spearman <= EXTENSION_THRESHOLD && 
								! positionIsInChunk(chunks, previous.startPos))
						{
							currentChunk.firstStart = previous.startPos;
							currentChunk.n = currentChunk.n + 1;
							currentChunk.spearmanSum += thisHolder.spearman;
							lookBack--;
						}
						else
						{
							stop = true;
						}
					}
				}
			}
			else
			{
				if( thisHolder.spearman <= EXTENSION_THRESHOLD)
				{
					currentChunk.lastStart = thisHolder.startPos;
					currentChunk.n = currentChunk.n + 1;
					currentChunk.spearmanSum+= thisHolder.spearman;
				}
				else
				{
					currentChunk = null;
				}
			}
			
			lastIndex++;
		}
		
		return chunks;
	}
	
	private static class Holder implements Comparable<Holder>
	{
		int startPos;
		double spearman;
		
		@Override
		public int compareTo(Holder o)
		{
			return this.startPos - o.startPos;
		}
	}
}
