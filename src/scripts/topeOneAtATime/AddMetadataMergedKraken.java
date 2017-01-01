package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import parsers.OtuWrapper;
import scripts.TopeSeptember2015Run.AddMetadataForKraken;
import utils.ConfigReader;

public class AddMetadataMergedKraken
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
		HashSet<String> fileSet3=  getFileSet(3);
		HashSet<String> fileSet4 = getFileSet(4);
		
		for( int x=0; x < AddMetadataForKraken.TAXA_ARRAY.length; x++)
		{
			String taxa=  AddMetadataForKraken.TAXA_ARRAY[x];
			System.out.println(taxa);
			
			OtuWrapper wrapper = new OtuWrapper( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "krakenMerged" + 
					File.separator + "pivoted_" + 
					"diverticulosis_merged_kraken_" + taxa + ".txt");
			
			File logNormalizedFile = new File(	ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "krakenMerged" + 
					File.separator + "pivoted_" + 
					"diverticulosis_merged_kraken_" + taxa + "LogNormal.txt");
			
			File outFile = new File( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "krakenMerged" + 
					File.separator + "pivoted_" + 
					"diverticulosis_merged_kraken_" + taxa + "LogNormalPlusMetadata.txt");
			
			addMetadata(wrapper, logNormalizedFile, outFile,false, fileSet3, fileSet4);
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
	
	static HashSet<String> getFileSet(int fileNum) throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		File file3Dir =new File(ConfigReader.getTopeOneAtATimeDir() + File.separator + 
				"File" + fileNum + File.separator + "fastaOut");
		
		for( String s : file3Dir.list())
		{
			set.add(new StringTokenizer(s, "_").nextToken());
		}
		
		return set;
	}
	
	
	@SuppressWarnings("unused")
	private static String getIdOrThrow(String sampleID, HashSet<String> file3Set, HashSet<String> file4Set)
		throws Exception
	{
		if( file3Set.contains(sampleID)  && file4Set.contains(sampleID))
			throw new Exception("Sample id in both " + sampleID);
		
		if( file3Set.contains(sampleID) )
			return "File3";
		
		if( file4Set.contains(sampleID))
			return "File4";
		
		throw new Exception("File id in neither " + sampleID);
	}
	
	private static void addMetadata( OtuWrapper wrapper, File inFile, File outFile,
				boolean fromR, HashSet<String> file3Set, HashSet<String> file4Set) throws Exception
	{
		HashMap<String, Integer> caseControlMap = getCaseControlMap();
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("id\tcaseContol");
		
		String[] firstSplits = reader.readLine().split("\t");
		
		int startPos = fromR ? 0 : 1;
		
		for( int x=startPos; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			Integer val = caseControlMap.get( new StringTokenizer(key, "_").nextToken());
			int readNum = getReadNum(key);
			
			if( readNum ==1 && val != null && (val==0 || val == 1))
			{
				writer.write(key + "\t" + val);
				
				for( int x=1; x < splits.length; x++)
					writer.write("\t" + splits[x]);
				
				writer.write("\n");
			}	
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
	/*  The full metadata spreadsheet 
	private static void addMetadata( OtuWrapper wrapper, File inFile, File outFile,
				boolean fromR, HashSet<String> file3Set, HashSet<String> file4Set) throws Exception
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
			String sampleId = new StringTokenizer(key, "_").nextToken();
			
			writer.write(key+ "\t" + key.split("_")[0] + "\t" +  getReadNum(key) + "\t" + 
						( key.indexOf("DV-000-") != -1) + "\t" + 
					wrapper.getNumberSequences(key) 
						+ "\t" + wrapper.getShannonEntropy(key) + "\t" );
			
			Integer val = caseControlMap.get( new StringTokenizer(key, "_").nextToken());
			
			if( val == null)
				writer.write("NA\t");
			else
				writer.write("" + val + "\t");
			
			writer.write(getIdOrThrow(sampleId, file3Set, file4Set)+ "\t");
			
			writer.write( Integer.parseInt(key.split("_")[1]) + "");
				
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	*/
	
}
