package scripts.joseeScramble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Scramble
{
	public static void main(String[] args) throws Exception
	{
		//File joseeDir =new File()
		
		HashMap<Integer, Integer> groupMap =getGroups();
		
		System.out.println(groupMap);
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
