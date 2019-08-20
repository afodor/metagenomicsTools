package scripts.tb_June2019;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class PivotToOTUTable
{
	public static void main(String[] args) throws Exception
	{
		 HashMap<String, HashMap<String,Integer>> map = 
				 getAllMap(new File("C:\\tb_RDP\\01_PearMergeReads\\output"));
		 
		 writePivotTable(new File("C:\\tb_RDP\\dada2_OUT\\pivotOut.txt"), 
				 new File("C:\\tb_RDP\\dada2_OUT\\fastaSeqs.txt"),
				 map);
	}
	
	private static void writePivotTable( File outFile, File fastaSumaryFile, HashMap<String, HashMap<String,Integer>> map)
	throws Exception
	{
		List<String> samples = new ArrayList<>(map.keySet());
		Collections.sort(samples);
		
		HashMap<String,Integer> seqs = new HashMap<String,Integer>();
		
		for(String s : samples)
		{
			HashMap<String, Integer> innerMap = map.get(s);
			
			for(String s2 : innerMap.keySet())
			{
				Integer count = seqs.get(s2);
				
				if( count ==null)
					count = 0;
				
				count += innerMap.get(s2);
				
				seqs.put(s2, count);
			}
		}
		
		List<Holder> list = new ArrayList<>();
		
		for(String s : seqs.keySet())
		{
			Holder h = new Holder();
			h.seq = s;
			h.count = seqs.get(s);
			list.add(h);
		}
		
		Collections.sort(list);
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(outFile));
		
		writer.write("sample");
		
		for(Holder h : list)
			writer.write("\t" + h.seq);
		
		writer.write("\n");
		
		for(String s : samples)
		{
			writer.write(s);
			
			HashMap<String, Integer> innerMap = map.get(s);
			
			for(Holder h : list)
			{
				Integer count = innerMap.get(h.seq);
				
				if( count == null)
					count =0;
				
				writer.write("\t" + count);
			}
			
			writer.write("\n");
		}
		
		
		writer.flush();  writer.close();
		
		writer = new BufferedWriter(new FileWriter(fastaSumaryFile));
		
		int index=1;
		for(Holder h : list)
		{
			writer.write(">seq" + index + "_" + h.count + "\n");
			writer.write(h.seq + "\n");
			index++;
		}
		
		
		writer.flush();  writer.close();
	}
	
	private static class Holder implements Comparable<Holder>
	{
		String seq;
		int count;
		
		@Override
		public int compareTo(Holder o)
		{
			return o.count - this.count;
		}
	}
	
	// outer key is sample
	private static HashMap<String, HashMap<String,Integer>> getAllMap(File topDir) throws Exception
	{
		HashMap<String, HashMap<String,Integer>>  map = new HashMap<>();
		
		String[] files =topDir.list();
		
		for(String s : files)
		{
			if( s.endsWith("_F.txt"))
			{
				String sample = s.replace("_F.txt", "");
				
				if( map.containsKey(sample))
					throw new Exception("No");
				
				File f = new File(topDir.getAbsolutePath() + File.separator + s);
				map.put(sample, getCountMap(f));
			}
		}
		
		return map;
	}
	
	private static HashMap<String, Integer> getCountMap(File f) throws Exception
	{
		HashMap<String, Integer> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		StringTokenizer sToken =new StringTokenizer(reader.readLine());
		
		List<String> seqs = new ArrayList<>();
		
		while(sToken.hasMoreTokens())
			seqs.add(sToken.nextToken().replaceAll("\"", ""));
		
		List<Integer> ints = new ArrayList<>();
		
		sToken = new StringTokenizer(reader.readLine());
		
		sToken.nextToken();
		
		while(sToken.hasMoreTokens())
			ints.add( Integer.parseInt(sToken.nextToken()));
			
		if( reader.readLine() != null)
			throw new Exception("No");
		
		if( seqs.size() != ints.size())
			throw new Exception("No");
		
		reader.close();
		
		for( int x=0; x < seqs.size(); x++)
			map.put(seqs.get(x), ints.get(x));
		
		return map;
	}
}
