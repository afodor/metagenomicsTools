package claude.mouseCDiff;

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

public class ComparisonWith16S
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			"C:\\claudeSummary\\simonCrossRho\\kraken_max1\\allGenusFlat.txt"	)));
		writer.write("SampleType\tsampleID\tgenus\tgenusSum\tpValue\n");
		
		
		add16SMap(writer);
		
		writer.flush();  writer.close();
	}
	
	private static void add16SMap( BufferedWriter flatFile) throws Exception
	{
		// outer key is genus@
		HashMap<String, Double> map = new LinkedHashMap<String, Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				new File("C:\\claudeSummary\\simonCrossRho\\kraken_max1\\genus.txt")));
		
		
		reader.readLine();
		
		StringTokenizer sToken = new StringTokenizer(reader.readLine());
		List<String> sampleNames = new ArrayList<String>();
		
		sToken.nextToken();
		
		while(sToken.hasMoreTokens())
				sampleNames.add(sToken.nextToken().replaceAll("\"", "").trim());
		
		System.out.println(sampleNames);
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			sToken = new StringTokenizer(s, "\t");
			
			String tax = sToken.nextToken();
			
			if( tax.indexOf(";g__") != -1)
			{
				String genus = null;
				
				//from chatGPT
				for (String part : tax.split(";")) 
				{
				    if (part.startsWith("g__")) 
				    {
				        genus = part.substring(3);
				        break;
				    }
				}
				
				genus = genus.trim();
				
				if(genus.length() > 0 )
				{
					int x=0;
					while(sToken.hasMoreTokens())
					{
						String key = genus + "@" + sampleNames.get(x);
						Double oldVal = map.get(key);
						
						if(oldVal == null)
							oldVal = 0.0;
						
						Double newVal = Double.parseDouble(sToken.nextToken());
						
						newVal = newVal + oldVal;
						
						map.put(key, newVal);
						
					}
				}
			}
		}

		for(String s : map.keySet())
		{
			flatFile.write("16S_Dada2");
			
			sToken = new StringTokenizer(s, "@");
			String genus = sToken.nextToken();
			String sample = sToken.nextToken();
			
			flatFile.write("\t" + sample + "\t" + genus + "\t" + map.get(s) + "\tNA\n");
		}
		
		flatFile.flush();
		reader.close();
		System.out.println("Wrote 16s");
	}
}
