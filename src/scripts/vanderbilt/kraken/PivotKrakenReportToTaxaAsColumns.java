package scripts.vanderbilt.kraken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import parsers.PivotOTUs;
import utils.ConfigReader;


public class PivotKrakenReportToTaxaAsColumns
{
	public static final String[] KRAKEN_LEVELS =  { "P", "C", "O", "F", "G", "S", "-" };
	public static final String[] RDP_LEVELS = 
		{ NewRDPParserFileLine.PHYLUM, 
		NewRDPParserFileLine.CLASS, 
			NewRDPParserFileLine.ORDER,
				NewRDPParserFileLine.FAMILY,
					NewRDPParserFileLine.GENUS,
						"species", "strain"};
	
	
	public static void main(String[] args) throws Exception
	{
		File topDir = new File(ConfigReader.getVanderbiltDir() + File.separator + 
				"krakenOut");
		
		for( int x=0; x < KRAKEN_LEVELS.length; x++)
		{
			HashMap<String, HashMap<String, Integer>> map = 
						new HashMap<String, HashMap<String,Integer>>();
			
			System.out.println(KRAKEN_LEVELS[x]);
			for(String s : topDir.list())
				if( s.startsWith("standardReport_for_Sample_") && s.indexOf("16S") == -1)
				{
					//System.out.println("NON 16S " + s);
					addToMap(new File(topDir.getAbsolutePath() + File.separator + s),
							map, KRAKEN_LEVELS[x]);
	
				}
					
			File outFile = new File(ConfigReader.getVanderbiltDir() + File.separator + 
					"spreadsheets" + File.separator + 
					"kraken_" + RDP_LEVELS[x] +"_taxaAsColumns.txt");
			PivotOTUs.writeResults(map, outFile.getAbsolutePath());
			
			OtuWrapper wrapper = new OtuWrapper(outFile);
			wrapper.writeNormalizedLoggedDataToFile(ConfigReader.getVanderbiltDir() 
					+ File.separator + 
					"spreadsheets" + File.separator + 
					"kraken_" + RDP_LEVELS[x] +"_taxaAsColumnsLogNorm.txt");
		}
		
		for( int x=0; x < KRAKEN_LEVELS.length; x++)
		{
			HashMap<String, HashMap<String, Integer>> map = 
						new HashMap<String, HashMap<String,Integer>>();
			
			System.out.println(KRAKEN_LEVELS[x]);
			for(String s : topDir.list())
			{
				//System.out.println(s);
				if( s.startsWith("standardReport_for_") && s.indexOf("16S") != -1)
				{
					//System.out.println("For 16S " + s);
					addToMap(new File(topDir.getAbsolutePath() + File.separator + s),
							map, KRAKEN_LEVELS[x]);
	
				}
			}
				
					
			File outFile = new File(ConfigReader.getVanderbiltDir() + File.separator + 
					"spreadsheets" + File.separator + 
					"kraken_" + RDP_LEVELS[x] +"_taxaAsColumnsFor16S.txt");
			PivotOTUs.writeResults(map, outFile.getAbsolutePath());
			
			OtuWrapper wrapper = new OtuWrapper(outFile);
			wrapper.writeNormalizedLoggedDataToFile(ConfigReader.getVanderbiltDir() 
					+ File.separator + 
					"spreadsheets" + File.separator + 
					"kraken_" + RDP_LEVELS[x] +"_taxaAsColumnsLogNormFor16S.txt");
		}
	}
	
	private static void addToMap(File file, HashMap<String, HashMap<String, Integer>> countMap,
					String level)
		throws Exception
	{
		String name = file.getName().replace("standardReport_for_Sample_", "").replace(".txt", "")
				.replace("_to16S", "").replace("standardReport_for_", "");
		String[] splits = name.split("_");
		String sampleID = splits[0];
		
		boolean needToTrim = true;
		
		if( splits[1].equals("all1") || splits[1].equals("all2"))
		{
			needToTrim = false;
			sampleID = sampleID + "_" + splits[1];
		}
			
		if( needToTrim)
		{
			if( splits[1].equals("MSHRM1"))
				sampleID = sampleID + "_all1";
			else if( splits[1].equals("MSHRM2"))
				sampleID = sampleID + "_all2";
			else throw new Exception("Unexpected " + splits[1]);
		}
		
		
		if( countMap.containsKey(sampleID))
			throw new Exception("Duplicate " + sampleID);
		
		HashMap<String, Integer> innerMap = new HashMap<String, Integer>();
		countMap.put(sampleID, innerMap);
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			sToken.nextToken();
			int counts = Integer.parseInt(sToken.nextToken());
			sToken.nextToken();
			String charString = sToken.nextToken();
			
			if( charString.equals(level))
			{
				sToken.nextToken();
				String taxaName = new String(sToken.nextToken());
				
				if (innerMap.containsKey(taxaName))
				{
					//System.out.println("Waring duplicate " + taxaName + " " 
						//		+ innerMap.get(taxaName));
					counts = counts + innerMap.get(taxaName);
				}
					
				innerMap.put(taxaName, counts);
			}
		}
		
		reader.close();
	}
}
