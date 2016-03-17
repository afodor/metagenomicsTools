package creOrthologs.kmers.chunk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.ConfigReader;

public class FindChunks
{
	//public static 
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = getList();
		
		for(Holder h : list)
			System.out.println(h.startPos + " " + h.spearman);
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
		double pearsonSum =0;
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
				
			}
			else
			{
				
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
