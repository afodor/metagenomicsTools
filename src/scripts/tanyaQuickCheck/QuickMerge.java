package scripts.tanyaQuickCheck;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import parsers.OtuWrapper;
import utils.TabReader;

public class QuickMerge
{
	//public static String[] LEVELS = {"phylum"};
	
	
	public static String[] LEVELS = {"phylum", "class", "order", "family", "genus",  "species"};
	
	public static void main(String[] args) throws Exception
	{
		writeMetFilesMerged("neg");
		writeMetFilesMerged("pos");
	}
	
	public static void writeMetFilesMerged(String prePostString) throws Exception
	{
		File preFile =  new File( "C:\\tanyaQuickRep\\" + prePostString + "_pre.txt");
		
		HashMap<String, String> metaboliteLines =  getMetaboliteLines(preFile);
		
		for(String level : LEVELS)
		{
			System.out.println(level);
			OtuWrapper wrapper = new OtuWrapper("C:\\tanyaQuickRep\\tanya_kraken_"+ level +  ".txt");
			
			File loggedFile =  new File( "C:\\tanyaQuickRep\\tanya_kraken_"+ level +  "logNorm.txt");
			
			wrapper.writeLoggedDataWithTaxaAsColumns(loggedFile);
			
			writeMergedFile(loggedFile, metaboliteLines, preFile, level, prePostString);
		}
		
	}
	
	private static String getTopLine(File file) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(file));
	
		String s= reader.readLine();
		
		reader.close();
		
		return s;
	}
	
	private static void writeMergedFile( File loggedFile, HashMap<String, String> metaboliteLines , File metaboliteFile, String level, String prePostString )
		throws Exception
	{
		String[] topLineMetabolites = getTopLine(metaboliteFile).split("\t");
		
		BufferedReader reader = new BufferedReader(new FileReader(loggedFile));
		
		BufferedWriter  writer = new BufferedWriter(new FileWriter( new File("C:\\tanyaQuickRep\\" + level + "_logged_" + prePostString + ".txt")));
		
		writer.write("id");
		
		String[] firstTaxaLine = reader.readLine().split("\t");
		
		for( int x=1; x < firstTaxaLine.length; x++)
			writer.write("\t" + firstTaxaLine[x]);
		
		for( int x=1; x < topLineMetabolites.length; x++)
			writer.write("\t" + topLineMetabolites[x]);
		
		writer.write("\n");

		int found =0;
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String metaboliteLine = metaboliteLines.get(splits[0]);
			
			if( metaboliteLine == null)
			{
			//	System.out.println("Could not find " + splits[0]);
				
			}
			else
			{
				//System.out.println("Found" + splits[0]);
				found++;
				
				writer.write(splits[0]);
				
				String[] taxaSplits = s.split("\t");
				
				for( int x=1; x < taxaSplits.length; x++)
					writer.write("\t" + splits[x]);
				
				String[] metSplits = metaboliteLine.split("\t");
				
				for( int x=1; x < metSplits.length; x++)
					writer.write("\t" +  metSplits[x]);
					
				writer.write("\n");
			}
		}
		
		System.out.println("Found " + found);
		writer.close();
		
		reader.close();
	}
	
	/*
	 * Key is going to be the microbiome id; 
	 * the values is going to be the entire line of the metabolite file
	 */
	private static HashMap<String, String> getMetaboliteLines(File file) throws Exception
	{
		HashMap<String, String> idMap = getMetaMap();
		
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		int numFound =0;
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0];
			
			String microbeID = idMap.get(key);
			
			if( microbeID == null) 
			{
				//System.out.println("Could not find " +key );
			}
			else
			{
				///System.out.println("Found " + microbeID);
				numFound++;
				
				map.put(microbeID, s);
			}
				
		}
		
		//System.out.println("Found " + numFound);
		reader.close();
		
		return map;
		
	}
	
	private static HashMap<String, String> getMetaMap() throws Exception
	{
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\tanyaQuickRep\\alderete_k99_metadata_30dec18.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			TabReader tr = new TabReader(s);
			
			String value= tr.getNext();
			tr.getNext();
			
			String key = tr.getNext();
			
			String[] parts  = value.split("\\.");
			
			value = parts[0];
			
			map.put(key, value);
		}
		
		reader.close();
		return map;
	}
}
