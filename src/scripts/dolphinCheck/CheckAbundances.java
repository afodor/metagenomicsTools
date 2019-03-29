package scripts.dolphinCheck;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.Avevar;

public class CheckAbundances
{
	private static HashMap<String, String> getBodySiteMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			"C:\\Thomas_Dolphin\\dolphinMetadata_withSampleID-CORRECTED.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			s=s.replaceAll("\"", "");
			String[] splits = s.split("\t");
			
			//System.out.println(s);
			if( ! s.startsWith("Water"))
			{
				map.put(splits[0], splits[6] );
			}
			else
			{
				map.put(splits[0], "water");
			}
		}
		
		reader.close();
		return map;
		
	}
	
	private static HashMap<String, String> bodySiteDecoded()
	{
		HashMap<String, String>  map =new HashMap<>();
		
		//Body site A is fecal, B is blowhole, C is blowhole plate, D is gastric, E is skin, and F is genital.
		
		map.put("A","fecal");
		map.put("B","blowhole");
		map.put("C", "blowhole_plate");
		map.put("D", "gastric");
		map.put("E" ,"skin"); 
		map.put("F", "genital");
		
		
		return map;
	}
	
	//outer key is body site; inner key is taxa relative abundance
	private static HashMap<String, HashMap<String,List<Double>>> getBodySiteToTaxa(String filter) throws Exception
	{
		HashMap<String, String> siteMap = getBodySiteMap();
		List<Long> rowSums = getRowSums(filter);
		
		HashMap<String, HashMap<String,List<Double>>> map = new HashMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\Thomas_Dolphin\\hierarch_merge.txt")));
		
		List<String> bodySites= new ArrayList<>();
		
		String topS = reader.readLine();
		String[] topSplits = topS.split("\t");
		
		for( int x= 4; x < topSplits.length; x++)
		{
			String key = new StringTokenizer(topSplits[x], "_").nextToken();
			String bodySite = siteMap.get(key);
			
			if( bodySite == null)
			{
				System.out.println("Could not find " + key);
			}
			else
			{
				System.out.println("Found " + key + " " + bodySite);
			}
				
			bodySites.add(bodySite);
		}
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != topSplits.length)
				throw new Exception("No");
			
			if( splits[3].equals(filter) )
			{
				for( int x=4; x < splits.length; x++)
				{
					String bodySite =  bodySites.get(x-4);
					
					if( bodySite != null)
					{
						HashMap<String,List<Double>> innerMap = map.get(bodySite);
						
						if( innerMap == null)
						{
							innerMap =new HashMap<>();
							map.put(bodySite, innerMap);
						}
						
						String taxa = splits[2];
						List<Double> taxaList = innerMap.get(taxa);
						
						if( taxaList == null)
						{
							taxaList = new ArrayList<>();
							innerMap.put(taxa,taxaList );
						}
						
						taxaList.add( Double.parseDouble(splits[x]) / rowSums.get(x-4) );

					}
					
				}
			}
		}
		
		reader.close();
		return map;
	}
	
	private static List<Long> getRowSums(String filter) throws Exception
	{
		List<Long> list= new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\Thomas_Dolphin\\hierarch_merge.txt")));
		
		String[] tops = reader.readLine().split("\t");
		
		for( int x=4;  x < tops.length; x++)
			list.add(0l);
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != tops.length)
				throw new Exception("No");
			
			if( splits[3].equals(filter))
			{
				for( int x=4; x < splits.length; x++)
					list.set(x-4, list.get(x-4)+ Long.parseLong(splits[x]));
			}
		}
		
		reader.close();
		
		return list;
	}
	
	private static void writeAverages(HashMap<String, HashMap<String,List<Double>>>  map, String filter ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter( new File("C:\\Thomas_Dolphin\\averageOut_" + filter + ".txt")));
		
		
		writer.write("bodySite\t" + filter + "\taverage\tsd\tsampleSize\n");
		
		for(String bodySite : map.keySet())
		{

			String bodySiteDecoded = bodySiteDecoded().get(bodySite) ;
			
			if(bodySiteDecoded== null)
				bodySiteDecoded= bodySite;
			
			HashMap<String,List<Double>> innerMap = map.get(bodySite);
			
			for(String taxa: innerMap.keySet())
			{
				List<Double> list= innerMap.get(taxa);
				Avevar av = new Avevar(list);
				
				if(av.getAve()>0)
					writer.write( bodySiteDecoded+ "\t" + av.getAve()+ "\t" + av.getSD() + "\t" + list.size() + "\n");
			}
			
		}
		
		writer.flush(); writer.close();
	}

	
	private static void writeResults(HashMap<String, HashMap<String,List<Double>>>  map, String filter ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter( new File("C:\\Thomas_Dolphin\\summedOut_" + filter + ".txt")));
		
		writer.write("bodySite\t" + filter + "\tfraction\n");
		
		for(String s : map.keySet())
		{
			HashMap<String,List<Double>> innerMap = map.get(s);
			
			for(String s2: innerMap.keySet())
			{
				List<Double> list= innerMap.get(s2);
				
				for(Double d : list)
				{
					String bodySite = bodySiteDecoded().get(s) ;
					
					if(bodySite == null)
						bodySite = s;
					
					writer.write( bodySite+ "\t" + s2 + "\t" + d + "\n");
				}
			}
			
		}
		
		writer.flush(); writer.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		String[] filters = {"phylum","genus"};
		
		for(String filter : filters)
		{
			HashMap<String, HashMap<String,List<Double>>>  bigMap = getBodySiteToTaxa(filter);
			writeResults(bigMap, filter);
			writeAverages(bigMap, filter);
		}
			
	}
}
