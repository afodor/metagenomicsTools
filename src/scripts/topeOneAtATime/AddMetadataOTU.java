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

public class AddMetadataOTU
{
	
	public static void main(String[] args) throws Exception
	{
		HashSet<String> fileSet3=  AddMetadataMerged.getFileSet(3);
		HashSet<String> fileSet4 = AddMetadataMerged.getFileSet(4);
		
		File rawCounts = new File( ConfigReader.getTopeOneAtATimeDir() + File.separator +
				"diverticulosisOTUs" + File.separator +  "otus" + File.separator + 
				"tope_otu_asColumns.txt");
			
		OtuWrapper wrapper = new OtuWrapper( rawCounts );
		
		File logNormalFile= new File(  ConfigReader.getTopeOneAtATimeDir()
				+ File.separator + "merged" + 
				File.separator + "pivoted_" + 
		"otu" + "asColumnsLogNormal.txt" );
			
		wrapper.writeNormalizedLoggedDataToFile(logNormalFile.getAbsolutePath());
		
		File outFile = new File( ConfigReader.getTopeOneAtATimeDir()
					+ File.separator + "merged" +
					File.separator + "pivoted_" + 
		"otu" + "asColumnsLogNormalPlusMetadata.txt");
		
		addMetadata(wrapper, logNormalFile, outFile, false, fileSet3, fileSet4);	
		
	}
	
	
	
	private static void addMetadata( OtuWrapper wrapper, File inFile, File outFile,
				boolean fromR, HashSet<String> file3Set, HashSet<String> file4Set) throws Exception
	{
		HashMap<String, Nov2016MetadataParser> novMetaMap = Nov2016MetadataParser.getMetaMap();
		HashMap<String, Integer> caseControlMap = AddMetadataMerged.getCaseControlMap();
		HashMap<String, String> ticLocaitonMap = AddMetadataMerged.getTicLocationMap();
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("id\tkey\t");
		
		writer.write("waist\tticsCount\tage\tsex\tbmi\twhr\twbo\tbmi_CAT\tticLocation\t");
		
		writer.write("readNum\tisBlankControl\tnumberSequencesPerSample\tshannonEntropy\tcaseControl\tset\tread");
		
		String[] firstSplits = reader.readLine().split("\t");
		
		int startPos = fromR ? 0 : 1;
		
		for( int x=startPos; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "").replaceAll("\\.", "-").replace("D5", "DV");
			String sampleId = key;
			
			Nov2016MetadataParser novMeta = novMetaMap.get(key);
				
			writer.write(key+ "\t" + key + "\t");
			
			if( novMeta == null)
			{
				writer.write("\t\t\t\t\t\t\t\t");
			}
			else
			{
				//writer.write("waist\tticsCount\tage\tsex\tbmi\twhr\twbo\tbmi_CAT\t");
				
				writer.write( AddMetadataMerged.getStringOrNothing(novMeta.getWaist()) + "\t");
				writer.write( AddMetadataMerged.getStringOrNothing(novMeta.getTicsCount()) + "\t");
				writer.write( AddMetadataMerged.getStringOrNothing(novMeta.getAge()) + "\t");
				writer.write( AddMetadataMerged.getStringOrNothing(novMeta.getSex()) + "\t");
				writer.write( AddMetadataMerged.getStringOrNothing(novMeta.getBmi()) + "\t");
				writer.write(  AddMetadataMerged.getStringOrNothing(novMeta.getWhr()) + "\t");
				writer.write(  AddMetadataMerged.getStringOrNothing(novMeta.getWbo()) + "\t");
				writer.write(  AddMetadataMerged.getStringOrNothing(novMeta.getBmi_CAT()) + "\t");	
			}
			
			String location = ticLocaitonMap.get(key );
			
			if( location == null || location.length() == 0)
				writer.write("\t");
			else
				writer.write(location + "\t");
			
			System.out.println(key + " " +key.replaceAll("-", ".") );
			// todo: Check with Roshonda if these are in fact forward reads
			writer.write( "1" + "\t" + 
						( key.indexOf("DV-000-") != -1) + "\t" + 
					wrapper.getNumberSequences(key.replaceAll("-", ".").replace("DV", "D5") ) 
						+ "\t" + wrapper.getShannonEntropy(key.replaceAll("-", ".").replace("DV", "D5")) 
						+ "\t" );
			
			Integer val = caseControlMap.get( new StringTokenizer(key, "_").nextToken());
			
			if( val == null)
				writer.write("-1\t");
			else
				writer.write("" + val + "\t");
			
			writer.write( AddMetadataMerged.getIdOrThrow(sampleId +"_1" , file3Set, file4Set)+ "\t");
			
			writer.write( Integer.parseInt(key.split("_")[1]) + "");
				
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	

}
