package scripts.topeOneAtATime.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class CheckMetadata
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, List<String>> map = getExpected();
		
		for(String s : map.keySet())
			System.out.println(s);

		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			File toCheck = 
			 new File(ConfigReader.getTopeOneAtATimeDir()
						+ File.separator + "merged" +
						File.separator +  "mds_"+ NewRDPParserFileLine.TAXA_ARRAY[x] +  "PlusMetadata.txt" );
			
			System.out.println(toCheck.getAbsolutePath());
			
			BufferedReader reader = new BufferedReader(new FileReader(toCheck));
			
			reader.readLine();
			
			for(String s= reader.readLine(); s != null; s= reader.readLine())
			{
				String[] splits = s.split("\t");
				//System.out.println("Search :" + splits[1]);
				List<String> innerList = map.get(splits[1].replaceAll("\"", ""));
				
				if( innerList != null)
				{
					System.out.println(splits[1]);
					
					for( int y=0; y< innerList.size() ; y++)
					{
						String expected = innerList.get(y);
						
						if( expected.length() ==0)
							expected = "NA";
						
						
						System.out.println(expected + " " + splits[y+2]);
						
						if( !expected.equals(splits[y+2]))
						{

							Double d1= Double.parseDouble(expected);
							Double d2= Double.parseDouble(splits[y+2]);
							
							if ( d1.doubleValue() != d2.doubleValue())
								throw new Exception("fail");
						}
							
					}
				}
			}
			
			reader.close();
				
		}
		
		System.out.println("global pass");
	}
	
	private static HashMap<String, List<String>> getExpected() throws Exception
	{
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
			ConfigReader.getTopeOneAtATimeDir() + File.separator + 
				"tk_out_22Nov2016.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			
			if(map.containsKey(key))
				throw new Exception("No");
			
			List<String> list = new ArrayList<String>();
			
			map.put(key, list);
			
			for( int x=1; x <= 8; x++)
				list.add(splits[x]);
			
		}
		
		return map;
	}
}
