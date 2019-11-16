package scripts.mattNov2019;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.StringTokenizer;

public class PivotMetabolites
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Double> map = getMetaboliteMap();
		
		for(String s : map.keySet())
			System.out.println(s + " "+  map.get(s));
		
		writePivot(map);
		System.out.println("Done");
	}
	
	private static void writePivot(HashMap<String, Double> map ) throws Exception
	{
		HashSet<String> tissueSet = new LinkedHashSet<>();
		HashSet<String> samples = new LinkedHashSet<>();
		HashSet<String> metabolites = new LinkedHashSet<>();
		
		for(String s : map.keySet())
		{
			String[] splits =s.split("@");
			if( splits.length != 3)
				throw new Exception();
			
			tissueSet.add(splits[2]);
			samples.add(splits[0]);
			metabolites.add(splits[1]);
		}
		
		// or could just us a tree set
		List<String> tissues = new ArrayList<>(tissueSet);
		Collections.sort(tissues);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\MattNov14\\pivotedMetabolites.txt"));
		
		writer.write("sample\tmetabolite");
		
		for(String s : tissues )
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(String sampleId : samples)
		{
			for(String metabolite : metabolites)
			{
				writer.write(sampleId + "\t" + metabolite + "\t");
				
				for(String t : tissues)
				{
					String key = sampleId + "@" + metabolite + "@" + t;
					Double val = map.get(key);
					
					writer.write( "\t" + (val == null ? "NA" : val ));
				}
								
				writer.write("\n");

			}
		}
		
		writer.flush();  writer.close();
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
