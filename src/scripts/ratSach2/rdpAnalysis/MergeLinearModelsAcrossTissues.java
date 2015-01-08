package scripts.ratSach2.rdpAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class MergeLinearModelsAcrossTissues
{
	private static class Holder
	{
		Double pValueCecum;
		Double pValueColon;
	}
	
	public static void main(String[] args) throws Exception
	{
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			
			System.out.println(level);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getRachSachReanalysisDir()
				+ File.separator + "rdpAnalysis" 
				+ File.separator + "pValuesAcrossMixed_taxa_" + level +".txt"	)));
			
			OtuWrapper wrapper = new OtuWrapper(ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator + "sparseThreeColumn_" + level +  "_AsColumns.txt");
			
			double totalCounts = wrapper.getTotalCounts();
			
			writer.write("taxa\tcecum\tcolon\trelativeAbundance\ttaxaIfSigInEither\n");
			
			HashMap<String, Holder> map = getMap(level);
			
			for(String s : map.keySet())
			{
				Holder h=  map.get(s);
				
				if( h.pValueCecum != null && h.pValueColon != null)
				{
					String key = s;
					
					if( ! s.startsWith("Escherichia"))
						key =s.replaceAll("\\.", " ").replaceAll("\"", "");
					else
						key =s.replaceAll("\\.", "/").replaceAll("\"", "");
					
					writer.write( s + "\t" );
					writer.write( h.pValueCecum + "\t" + h.pValueColon + "\t");
					
					int index =wrapper.getIndexForOtuName(key);
					
					if( index == -1)
						throw new Exception("Could not find " + key);
					
					writer.write(( wrapper.getCountsForTaxa(key) / totalCounts) + "\t");
					
					if( Math.abs(h.pValueCecum)  >= 1 || Math.abs( h.pValueColon)  > 1 )
						writer.write(s + "\n");
					else
						writer.write("\n");
				}
			}
			
			
			writer.flush();  writer.close();
		}
	}
	
	private static HashMap<String, Holder> getMap(String level) throws Exception
	{
		HashMap<String, Holder> map = new LinkedHashMap<String, Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getRachSachReanalysisDir()
				+ File.separator + "rdpAnalysis" 
				+ File.separator + "pValuesForTime_taxa_Colon content_" + level + ".txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine() ; s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			if( splits.length != 5)
				throw new Exception("Parsing error " + s + " " + splits.length);
			
			String key = splits[0].replaceAll("\"","");
			
			if( map.containsKey(key))
				throw new Exception("Duplicate");
			
			Holder h = new Holder();
			h.pValueColon = getLogVal(splits);
			map.put(key,h);
		}
		
		
		reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getRachSachReanalysisDir()
				+ File.separator + "rdpAnalysis" 
				+ File.separator + "pValuesForTime_taxa_Cecal Content_" + level + ".txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine() ; s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			if( splits.length != 5)
				throw new Exception("Parsing error");
			
			String key = splits[0].replaceAll("\"","");
			
			Holder h= map.get(key);
		
			if( h== null)
			{
				h = new Holder();
				map.put(key, h);
			}
			
			h.pValueCecum = getLogVal(splits);
		}
		
		return map;
	}
	
	private static double getLogVal( String[] splits ) throws Exception
	{
		double pValue = Math.log10(Double.parseDouble(splits[4]));
		
		if( Double.parseDouble(splits[3]) > Double.parseDouble(splits[2]))
			pValue = -pValue;
		
		return pValue;
	}
}
