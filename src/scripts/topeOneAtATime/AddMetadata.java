package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	public static HashMap<String, Integer> getCaseControlMap() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getTopeJan2016Dir() + File.separator + "tk_out_29Jan2016_corrected.txt"	)));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length > 2)
				throw new Exception("No");
			
			if( splits.length == 2)
			{
				if( map.containsKey(splits[0]))
					throw new Exception("No");
				
				map.put(splits[0], Integer.parseInt(splits[1]));
			}
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			OtuWrapper wrapper = new OtuWrapper( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "file3" + File.separator + "spreadsheets" + 
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumns.txt");
			
			File logNormalizedFile = new File(	ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "file3" + File.separator + "spreadsheets" + 
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormal.txt");
			
			File outFile = new File( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "file3" + File.separator + "spreadsheets" + 
					File.separator + "pivoted_" + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormalPlusMetadata.txt");
			
			addMetadata(wrapper, logNormalizedFile, outFile,false, "file3");
			
			/*
			logNormalizedFile = new File(ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "file3" + File.separator + "spreadsheets" + 
					File.separator + "mds_"+ NewRDPParserFileLine.TAXA_ARRAY[x] +  ".txt" );
			
			outFile = new File( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "file3" + File.separator + "spreadsheets" + 
					File.separator + "pivoted_" + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "mdsPlusMetadata.txt");
					*
			
			addMetadata(wrapper, logNormalizedFile, outFile,true);
			*/
			
		}
		
	}
	
	private static int getReadNum(String key) throws Exception
	{
		//System.out.println(key);
		String[] splits = key.split("_");
		
		int splitID = 2;
		int val = -1;
		
		try
		{
			 val = Integer.parseInt(splits[splitID]);
		}
		catch(Exception ex)
		{
			splitID = 1;
			val = Integer.parseInt(splits[splitID]);	
		}
		
		if( val != 1 && val != 4)
			throw new Exception("No ");
		
		return val;
	}
	
	private static void addMetadata( OtuWrapper wrapper, File inFile, File outFile,
				boolean fromR, String fileSet) throws Exception
	{
		HashMap<String, Integer> caseControlMap = getCaseControlMap();
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("id\tkey\treadNum\tisBlankControl\tnumberSequencesPerSample\tshannonEntropy\tcaseContol\tset\tread");
		
		String[] firstSplits = reader.readLine().split("\t");
		
		int startPos = fromR ? 0 : 1;
		
		for( int x=startPos; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			
			writer.write(key+ "\t" + key.split("_")[0] + "\t" +  getReadNum(key) + "\t" + 
						( key.indexOf("DV-000-") != -1) + "\t" + 
					wrapper.getNumberSequences(key) 
						+ "\t" + wrapper.getShannonEntropy(key) + "\t" );
			
			Integer val = caseControlMap.get( new StringTokenizer(key, "_").nextToken());
			
			if( val == null)
				writer.write("NA\t");
			else
				writer.write("" + val + "\t");
			
			writer.write(fileSet+ "\t");
			
			writer.write( Integer.parseInt(key.split("_")[1]) + "");
				
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
}
