package scripts.ting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

public class PivotToOtuTable
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(new File(ConfigReader.getTingDir() 
				+ File.separator + "20170224_Casp11_DSS_16S_DeNovo.txt")));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(String s : topSplits)
			System.out.println(s);
		
		HashMap<String, List<Integer>> map = new HashMap<String,List<Integer>>();
		
		for(int x=0; x < 15; x++)
			reader.readLine();
		
		String s = reader.readLine();
		
		while( s.trim().length() > 0 )
		{
			System.out.println(s);
			
			String[] splits = s.split("\t");
			
			if(splits.length != topSplits.length)
				throw new Exception("No");
			
			String key = splits[0].replaceAll("\"", "");
			
			if( map.containsKey(key))
				throw new Exception("NO");
			
			List<Integer> list =new ArrayList<Integer>();
			
			map.put(key, list);
			list.add(-1);
			
			for( int x=1; x < splits.length; x++)
				list.add(Integer.parseInt(splits[x].replace("\"", "").replace(",", "")));
			
			s= reader.readLine();
		}
		
		reader.close();
	}
}
