package scripts.mattNov2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class PivotMetabolites
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Double> map = getMetaboliteMap();
		
		for(String s : map.keySet())
			System.out.println(s + " "+  map.get(s));
	}
	
	/*
	 * Key is sampleID@metabolite@tissue
	 */
	private static HashMap<String, Double> getMetaboliteMap() throws Exception
	{
		HashMap<String, Double> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\MattNov14\\metaboliteOnly.tsv.txt")));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			
			String[] splits = s.split("\t");
			
			if( splits.length != topSplits.length)
				throw new Exception("No");
			
			if(! splits[1].equals("NA"))
			{
				for( int x=2; x < splits.length; x++ )
				{
					if( ! splits[x].equals("NA"))
					{

						String aName = topSplits[x].replaceAll("\"", "").trim();
						String tissueName = ComparePValues.getFirstName(topSplits[x]).trim();
						String metaboliteName = aName.replace(tissueName, "").replace("(ug/G)", "").trim();
						
						String key = splits[0]+ "@" +  metaboliteName +"@" + tissueName;
						
						double parsedVal= Double.parseDouble(splits[x]);
						
						Double val = map.get(key);
						
						if( val == null)
							val = parsedVal;
						
						if( val.doubleValue() != parsedVal )
							throw new Exception( val + " " + parsedVal + " " + key );
						
						map.put(key, val);
					}
				}
			}
		}
		
		reader.close();
		return map;
	}
}
