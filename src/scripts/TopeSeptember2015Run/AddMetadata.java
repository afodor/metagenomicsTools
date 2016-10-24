package scripts.TopeSeptember2015Run;

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
	public static void main(String[] args) throws Exception
	{

		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println( NewRDPParserFileLine.TAXA_ARRAY[x]);

			OtuWrapper wrapper = new OtuWrapper(ConfigReader.getTopeSep2015Dir() + File.separator +
					"spreadsheets" + File.separator 
					+NewRDPParserFileLine.TAXA_ARRAY[x] + "_taxaasColumns.txt");
			
			File logNormal = new File(
			ConfigReader.getTopeSep2015Dir() + File.separator +
			"spreadsheets" + File.separator + "pivoted_"
			+NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormal.txt");
			
			File logNormalMetadata = new File(ConfigReader.getTopeSep2015Dir() + File.separator +
					"spreadsheets" + File.separator + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormalPlusMetadata.txt");
			
			addMetadata(wrapper, logNormal, logNormalMetadata, false);
			
			File pcaFile = new File(ConfigReader.getTopeSep2015Dir() + File.separator +
					"spreadsheets" + File.separator + 
					"pcoa_" 
					+NewRDPParserFileLine.TAXA_ARRAY[x] + ".txt" );
			

			File pcaFileMeta = new File(ConfigReader.getTopeSep2015Dir() + File.separator +
					"spreadsheets" + File.separator + 
					"pcoa_" 
					+NewRDPParserFileLine.TAXA_ARRAY[x] + "WithMetadata.txt" );
			
			addMetadata(wrapper, pcaFile, pcaFileMeta, true);
			
		}
	}
	
	private static void addMetadata( OtuWrapper originalWrapper, File inFile, File outFile, boolean fromR)
		throws Exception
	{
		HashMap<String, MetadataParser> metaMap = MetadataParser.getMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("key\tprimers\tsample\treadNumber\tcaseControl\tnumSequences\tshannonDiversity");
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x= (fromR ? 0 : 1); x < splits.length; x++ ) 
			writer.write("\t" + splits[x].replaceAll("\"", ""));
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			splits = s.split("\t");
			
			StringTokenizer pToken = new StringTokenizer(splits[0].replaceAll("\"", ""),"_");
			
			String key = splits[0].replaceAll("\"", "");
			String sample = pToken.nextToken().replaceAll("\"", "");
			
			writer.write(key + "\t" + pToken.nextToken( ) + "\t" +  sample + "\t" 
			+   key.substring(key.lastIndexOf("_")+1, key.lastIndexOf("_")+2) + "\t");
			System.out.println( key + " " +  sample);
			
			MetadataParser mdp = metaMap.get(sample);
			Integer val = mdp == null ? null : mdp.getCaseControl() ;
			
			writer.write( ( val == null ? "NA" : val ) + "\t" + 
						originalWrapper.getNumberSequences(key) + "\t" + 
							originalWrapper.getShannonEntropy(key));
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
}
