package creOrthologs.hmms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

public class PosteriorDecode
{
	private static class Holder implements Comparable<Holder>
	{
		double pValue;
		int position;
		
		@Override
		public int compareTo(Holder o)
		{
			return this.position - o.position;
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"annotatedHitsDir" + File.separator + "klebsiella_pneumoniae_chs_11.0topHits.txt");
		
		HashMap<String, List<Holder>> map = getProbs(inFile);
		
		for(String s : map.keySet() )
		{
			System.out.println(s);
			
			for(Holder h : map.get(s))
				System.out.println("\t" + h.position + " " + h.pValue);
		}
	}
	
	//key is the contig id
	public static HashMap<String, List<Holder>> getProbs(File inFile) throws Exception
	{
		HashMap<String, List<Holder>> map = new HashMap<String, List<Holder>>();
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
						if( splits[6].trim().length() > 0 )
			{
				String key = splits[1];
				
				List<Holder> list = map.get(key);
				
				if( list == null)
				{
					list = new ArrayList<Holder>();
					map.put(key, list);
				}
				
										
				Holder h=  new Holder();
				h.position = Math.min(Integer.parseInt(splits[2]), Integer.parseInt(splits[3]));
				h.pValue = Double.parseDouble(splits[6]);
				
				list.add(h);
			}
			
			
		}
		
		for( List<Holder> list : map.values())
		{
			Collections.sort(list);
		}
			
		reader.close();
		return map;
	}
	
}
