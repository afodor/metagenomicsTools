package dynamicProgramming;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class BlossumMatrix implements SubstitutionMatrix
{
	private final HashMap<String, Integer> map;
	
	// you can find the input for this at 
	// https://github.com/afodor/afodor.github.io/blob/master/classes/prog2015/Blosum50.txt
	public BlossumMatrix(String filepath) throws Exception
	{
		this.map = new HashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
		
		
		String[] top = new String[20];
		
		StringTokenizer topLine = new StringTokenizer(reader.readLine());
		
		for(int x=0; x < 20; x++)
			top[x] = topLine.nextToken();
		
		if( topLine.hasMoreTokens())
			throw new Exception("No " + topLine.nextToken());
		
		for( int x=0; x < 20; x++)
		{
			StringTokenizer nextLine = new StringTokenizer(reader.readLine());
			
			if( ! nextLine.nextToken().equals(top[x]))
				throw new Exception("No");
			
			for( int y=0; y < 20; y++ )
			{
				String key = top[x] + "@" + top[y];
				int val = Integer.parseInt(nextLine.nextToken());
				
				Integer oldVal = map.get(key);
				
				// check for symmetry
				String flippedKey = top[y] + "@" + top[x];
				Integer flippedVal = map.get(flippedKey);
				
				if( flippedVal != null && oldVal != null && flippedVal != oldVal)
					throw new Exception("No " + flippedVal + " " + oldVal);
				
				if( oldVal != null)
				{
					throw new Exception("No");
				}
				else
				{
					map.put(key, val);
				}
			}
			
			if( nextLine.hasMoreElements())
				throw new Exception("No");
		}
		
		String lastLine = reader.readLine() ;
		if( lastLine != null && lastLine.trim().length() != 0)
			throw new Exception("No ");
	}
	
	
	@Override
	public float getScore(char c1, char c2) throws Exception
	{
		String key = c1 + "@" + c2;
		
		if( map.containsKey(key))
			return (float) map.get(key);
		
		throw new Exception("Could not find " + c1 + " " + c2);
	}
	
	@Override
	public String getSubstitutionMatrixName()
	{
		// TODO Auto-generated method stub
		return "Blossom";
	}
}
