package scripts.LyteNov2016;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import scripts.LyteNov2016.mattFiles.MetadataParserFileLine;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];

			File unloggedFile = new File(ConfigReader.getLyteNov2016Dir() + 
					File.separator + "spreadsheets" + File.separator + 
					taxa +  "_taxaAsColumns.txt");
			
			File mdsIn = new File(ConfigReader.getLyteNov2016Dir() + 
						File.separator + "spreadsheets" + File.separator + 
						"pcoa_" + taxa +  ".txt");
			
			File mdsOut =new File(ConfigReader.getLyteNov2016Dir() + 
					File.separator + "spreadsheets" + File.separator + 
					"pcoa_" + taxa +  "plusMetadaa.txt");
		 
			addMetadata(unloggedFile, mdsIn, mdsOut, true);
			
			File loggedIn =new File(ConfigReader.getLyteNov2016Dir() + 
					File.separator + "spreadsheets" + File.separator + 
					"pivoted_" + taxa + "asColumnsLogNormal.txt");
			
			File loggedOut = new File(ConfigReader.getLyteNov2016Dir() + 
					File.separator + "spreadsheets" + File.separator + 
					"pivoted_" + taxa + "asColumnsLogNormalPlusMetadata.txt");
			
			addMetadata(unloggedFile, loggedIn, loggedOut, false);
			

			File unLoggedOut = new File(ConfigReader.getLyteNov2016Dir() + 
					File.separator + "spreadsheets" + File.separator + 
					"pivoted_" + taxa + "asColumnsUnloggedPlusMetadata.txt");
			
			addMetadata(unloggedFile, unloggedFile, unLoggedOut, false);
			
			
		}
	}
	
	
	private static void addMetadata(File unloggedFile, File inFile, File outFile, boolean fromR)
		throws Exception
	{
		HashMap<String, MetadataParserFileLine> metaMap = MetadataParserFileLine.getMetaMap();
		System.out.println(outFile.getAbsolutePath());
		OtuWrapper wrapper = new OtuWrapper(unloggedFile);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String[] topSplits = reader.readLine().replace("\"", "").split("\t");
		
		int startPos = fromR ? 0 : 1;
		
		writer.write("sample\tsampleID\treadNum\tnumSequences\tshannonDiveristy\t");
		writer.write("source\tanimal\texpControl\tdate\tsex\tcage");
		
		for( int x=startPos; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits =s.split("\t");
			
			String key = splits[0].replace(".fastatoRDP.txt.gz", "");
			writer.write(key);
			writer.write("\t" + key+ "\t" + 1 );
			writer.write("\t" + wrapper.getNumberSequences(splits[0]) + "\t" + 
								wrapper.getShannonEntropy(splits[0]) + "\t");
			
			System.out.println(key);
			MetadataParserFileLine mpfl = metaMap.get(key);
			
			writer.write(mpfl.getSource() + "\t" + mpfl.getAnimal() + "\t" + 
							mpfl.getExpControl() + "\t" + mpfl.getDate() + "\t" + 
								mpfl.getSex() + "\t" + mpfl.getCage());
			
			for(int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		reader.close();
		writer.flush();  writer.close();
	}
}
