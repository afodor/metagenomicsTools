package scripts.GoranOct2015;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class InitialPivot
{
	public static void main(String[] args) throws Exception
	{
		for(int x=0; x <=5; x++)
			writeForALevel(x);
	}
	
	// 0 is OTU
	// 1 is phylum
	// 2 is class
	// 3 is order
	// 4 is family
	// 5 is genus
	public static void writeForALevel(int level) throws Exception
	{
		System.out.println(level);
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getGoranOct2015Dir() + File.separator + 
				"PC_0016 Metagenomics Study Report" + File.separator + "PC_0016 Data" 
						+ File.separator + "PC_0016 Seq_data.txt")));
		
		String taxa = "otu";
		
		if( level > 0 )
			taxa = NewRDPParserFileLine.TAXA_ARRAY[level];
		
		File outFile = new File(
				ConfigReader.getGoranOct2015Dir() + File.separator + 
				taxa + "_asColumns.txt");
		
		BufferedWriter writer= new BufferedWriter(new FileWriter(outFile));
	
		HashMap<String, List<Integer>> map = getTaxaMap(level);
		List<String> keys = new ArrayList<String>( map.keySet());
		Collections.sort(keys);
		
		String[] sampleNames = reader.readLine().split("\t");
		
		String[] interventionNames = reader.readLine().split("\t");
		
		List<String> namesPlusCategory = new ArrayList<String>();
		
		if( sampleNames.length != interventionNames.length)
			throw new Exception("No");
		
		for( int x=3; x < sampleNames.length; x++)
			namesPlusCategory.add(sampleNames[x] +"_" + interventionNames[x]);
		
		writer.write("sample");
		
		for(String s : keys)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for( int x=0; x < namesPlusCategory.size(); x++)
		{
			writer.write(namesPlusCategory.get(x));
			
			for(String key : keys)
			{
				List<Integer> list = map.get(key);
				writer.write("\t" + list.get(x));
			}

			writer.write("\n");
		}
		
		writer.flush(); writer.close();
		reader.close();
		
		OtuWrapper wrapper = new OtuWrapper(outFile);
		
		File loggedFile = new File( ConfigReader.getGoranOct2015Dir() + File.separator + 
				taxa + "_asColumnsLogNorm.txt");
		
		wrapper.writeNormalizedLoggedDataToFile(loggedFile);
	}
	
	private static HashMap<String, List<Integer>> getTaxaMap( int level) throws Exception
	{
		HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getGoranOct2015Dir() + File.separator + 
				"PC_0016 Metagenomics Study Report" + File.separator + "PC_0016 Data" 
						+ File.separator + "PC_0016 Seq_data.txt")));
		
		reader.readLine();  reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = null;
			
			if( level == 0  )
			{
				key = splits[0];
				if( map.containsKey(key))
					throw new Exception("No");
			}
			else
			{
				String[] rdpNames = splits[2].trim().replaceAll("\"", "").split(";");
				
				if( rdpNames.length != 6)
					throw new Exception("No");
				
				key = rdpNames[level];
				key = key.substring(0, key.indexOf("("));
			}
			
			List<Integer> list = map.get(key);
			
			if( list == null)
			{
				list = new ArrayList<Integer>();
				for( int x=3; x < splits.length; x++)
					list.add(0);
				map.put(key, list);
			}
			
			for( int x=3; x < splits.length; x++)
				list.set(x-3, list.get(x-3) + Integer.parseInt(splits[x]));
		}

		reader.close();
		
		return map;
	
	}
}
