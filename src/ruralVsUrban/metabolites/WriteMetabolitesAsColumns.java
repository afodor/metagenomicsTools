package ruralVsUrban.metabolites;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;
import utils.TabReader;

public class WriteMetabolitesAsColumns
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"metabolites" + File.separator + "UNCH-01-15VW_scaled.txt")));
		
		List<String> subjects = new ArrayList<String>();
		List<String> urban_rural = new ArrayList<String>();
		
		for( int x=0; x < 4; x++)
			reader.readLine();
		
		urban_rural = parseAHeaderLine(reader.readLine());
		reader.readLine();
		subjects = parseAHeaderLine(reader.readLine());
		
		System.out.println(urban_rural);
		System.out.println(subjects);
		
		reader.readLine();
		
		HashMap<String, List<Double>> metMap = new LinkedHashMap<String, List<Double>>();
		
		for( String s= reader.readLine() ; s != null; s = reader.readLine() )
		{
			TabReader tReader = new TabReader(s);
			
			for( int x=0; x < 1; x++)
				tReader.nextToken();
			
			String key = tReader.nextToken().trim();
		
			for(int x=0; x < 11; x++)
				tReader.nextToken();
			
			if( key.length() == 0 || metMap.containsKey(key))
				throw new Exception("No");
			
			List<Double> list = new ArrayList<Double>();
			
			for(int x=0; x < 40; x++)
				list.add(Double.parseDouble(tReader.nextToken()));
			
			if( tReader.hasMore())
				throw new Exception("No");
			
			metMap.put(key,list);
			
		}
		
		reader.close();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"metabolites" + File.separator + "metabolitesAsColumns.txt")));
		
		writer.write("sample\tcategory");
		
		List<String> metabolites = new ArrayList<String>( metMap.keySet());
		
		for(String s : metabolites)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for( int x=0; x < 40; x++)
		{
			writer.write(subjects.get(x) + "\t" + urban_rural.get(x).replace("human ", "") );
			
			for(String s : metabolites)
				writer.write("\t" + metMap.get(s).get(x));
			
			writer.write("\n");
			
		}
		
		
		writer.flush();  writer.close();
	}
	
	
	private static List<String> parseAHeaderLine(String s ) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(s, "\t");
		
		List<String> list = new ArrayList<String>();
		
		sToken.nextToken();
		
		for( int x=0; x < 40; x++)
			list.add(sToken.nextToken());
		
		if( sToken.hasMoreTokens())
			throw new Exception("No");
		
		return list;
	}
	
	
}
