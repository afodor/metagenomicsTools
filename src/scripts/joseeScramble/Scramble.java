package scripts.joseeScramble;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class Scramble
{
	public static void main(String[] args) throws Exception
	{
		File joseeDir =new File("C:\\JoseeScramble");
		
		HashMap<Integer, Integer> groupMap =getGroups();
		
		System.out.println(groupMap);
		
		BufferedReader reader = new BufferedReader(new FileReader(joseeDir + File.separator + 
				"Biofilm project DNA extraction sample list.txt"));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(joseeDir + File.separator + 
				"Biofilm project DNA extraction sample listWithBatch.txt"));
		
		writer.write(reader.readLine()  +"\tbatch\n");
		
		for(String s = reader.readLine() ; s != null && s.trim().length() > 0 ; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			int id = Integer.parseInt(splits[0]);
			
			Integer group = groupMap.get(id);
			
			if ( group == null)
				throw new Exception("No");
			
			writer.write(s + "\t" + group + "\n");
			
			groupMap.remove(id);
		}
		
		writer.flush();
		writer.close();
		
		if( groupMap.size() != 0)
			throw new Exception("No");
	}
	
	private static HashMap<Integer, Integer> getGroups() throws Exception
	{
		List<Integer> list = new ArrayList<Integer>();
		
		for( int x=1; x <=112; x++)
			list.add(x);
		
		Collections.shuffle(list, new Random(324214));
		
		HashMap<Integer, Integer> groups = new HashMap<Integer, Integer>();
		
		int groupNum =1;
		int numAssigned =0;
		
		for( Integer i : list)
		{
			groups.put(i, groupNum);
			numAssigned++;
			
			if( numAssigned == 16)
			{
				groupNum++;
				numAssigned = 0;
			}
		}
		
		return groups;
	}
	
	
}
