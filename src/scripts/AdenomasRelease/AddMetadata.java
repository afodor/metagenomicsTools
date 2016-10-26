package scripts.AdenomasRelease;

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
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			File originalFile = new File(ConfigReader.getAdenomasReleaseDir() + File.separator + 
					"spreadsheets" + File.separator + "pivoted_" +  
					NewRDPParserFileLine.TAXA_ARRAY[x] + ".txt");
			
			File loggedFile = new File(ConfigReader.getAdenomasReleaseDir() + File.separator + 
					"spreadsheets" + File.separator + "pivoted_" +  
					NewRDPParserFileLine.TAXA_ARRAY[x] + "LogNormal.txt");
			
			OtuWrapper wrapper = new OtuWrapper(originalFile);

			File logNormalMetadata = new File(ConfigReader.getAdenomasReleaseDir() + File.separator + 
					"spreadsheets" + File.separator + "pivoted_" +  
					NewRDPParserFileLine.TAXA_ARRAY[x] + "LogNormalWithMetadata.txt");
			
			addMetadata(wrapper, loggedFile, logNormalMetadata, false);			
		}
	}
	
	private static void addMetadata( OtuWrapper originalWrapper, File inFile, File outFile, boolean fromR)
		throws Exception
	{
		System.out.println(inFile.getAbsolutePath());
		HashMap<String, String> metaMap = MetadataParser.getCaseControl();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("key\tcaseControl\tnumSequences\tshannonDiversity");
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x= (fromR ? 0 : 1); x < splits.length; x++ ) 
			writer.write("\t" + splits[x].replaceAll("\"", ""));
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			splits = s.split("\t");
			String key = splits[0].replaceAll("\"", "");
			System.out.println(key);
			writer.write(key+ "\t" + metaMap.get(key.replace("toRDP.txt", "")) + "\t" + 
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
