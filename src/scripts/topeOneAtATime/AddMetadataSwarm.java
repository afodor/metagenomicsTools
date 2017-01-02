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
import utils.ConfigReader;

public class AddMetadataSwarm
{
	public static void main(String[] args) throws Exception
	{
		HashSet<String> fileSet3=  AddMetadataMergedKraken.getFileSet(3);
		HashSet<String> fileSet4 = AddMetadataMergedKraken.getFileSet(4);
		
		OtuWrapper wrapper = new OtuWrapper( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "swarmOTUsAsColumnsWithSingles.txt");
			
		File logNormalizedFile = 
				new File( ConfigReader.getTopeOneAtATimeDir()
						+ File.separator + "swarmOTUsAsColumnsLogNorm.txt");
								
		wrapper.writeNormalizedLoggedDataToFile(logNormalizedFile);
		
		File outFile = new File( ConfigReader.getTopeOneAtATimeDir()
				+ File.separator + "swarmOTUsAsColumnsLogNormPlusMetadata.txt");
			
		addMetadata(wrapper, logNormalizedFile, outFile,false, fileSet3, fileSet4);
			
	}
	
	
	static void addMetadata( OtuWrapper wrapper, File inFile, File outFile,
				boolean fromR, HashSet<String> file3Set, HashSet<String> file4Set) throws Exception
	{
		HashMap<String, Nov2016MetadataParser> novMetaMap = Nov2016MetadataParser.getMetaMap();
		HashMap<String, Integer> caseControlMap = AddMetadataMerged.getCaseControlMap();
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("id\tkey\t");
		
		writer.write("waist\tticsCount\tage\tsex\tbmi\twhr\twbo\tbmi_CAT\t");
		
		writer.write("isBlankControl\tnumberSequencesPerSample\tshannonEntropy\tcaseContol\tset");
		
		String[] firstSplits = reader.readLine().split("\t");
		
		int startPos = fromR ? 0 : 1;
		
		for( int x=startPos; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			
			Nov2016MetadataParser novMeta = novMetaMap.get(key.split("_")[0] );
				
			writer.write(key+ "\t" + key.split("_")[0] + "\t");
			
			if( novMeta == null)
			{
				writer.write("\t\t\t\t\t\t\t\t");
			}
			else
			{
				//writer.write("waist\tticsCount\tage\tsex\tbmi\twhr\twbo\tbmi_CAT\t");
				
				writer.write( getStringOrNothing(novMeta.getWaist()) + "\t");
				writer.write( getStringOrNothing(novMeta.getTicsCount()) + "\t");
				writer.write( getStringOrNothing(novMeta.getAge()) + "\t");
				writer.write( getStringOrNothing(novMeta.getSex()) + "\t");
				writer.write( getStringOrNothing(novMeta.getBmi()) + "\t");
				writer.write(  getStringOrNothing(novMeta.getWhr()) + "\t");
				writer.write(  getStringOrNothing(novMeta.getWbo()) + "\t");
				writer.write(  getStringOrNothing(novMeta.getBmi_CAT()) + "\t");	
			}
			
			writer.write( 
						( key.indexOf("DV-000-") != -1) + "\t" + 
					wrapper.getNumberSequences(key) 
						+ "\t" + wrapper.getShannonEntropy(key) + "\t" );
			
			Integer val = caseControlMap.get( new StringTokenizer(key, "_").nextToken());
			
			if( val == null)
				writer.write("\t");
			else
				writer.write("" + val + "\t");
		
			if( splits[0].contains("set3"))
				writer.write("set3");
			else if ( splits[0].contains("set4"))
				writer.write("set4");
			else throw new Exception("Parsing error " + splits[0]);
				
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
	private static String getStringOrNothing(Object o)
	{
		if( o == null)
			return "";
		
		return o.toString();
	}
	
}
