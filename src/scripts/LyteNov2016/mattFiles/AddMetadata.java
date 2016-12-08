package scripts.LyteNov2016.mattFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];

			File unloggedFile = new File(ConfigReader.getLyteNov2016Dir() + 
					File.separator + "mattFiles" + File.separator + 
					taxa + "File.txt");
			
			File mdsIn = new File(ConfigReader.getLyteNov2016Dir() + 
						File.separator + "mattFiles" + File.separator + 
						"pcoa_" + taxa +  ".txt");
			
			File mdsOut =new File(ConfigReader.getLyteNov2016Dir() + 
					File.separator + "mattFiles" + File.separator + 
					"pcoa_" + taxa +  "plusMetadaa.txt");
		 
			addMetadata(unloggedFile, mdsIn, mdsOut, true);
		}
	}
	
	private static void addMetadata(File unloggedFile, File inFile, File outFile, boolean fromR)
		throws Exception
	{
		System.out.println(outFile.getAbsolutePath());
		OtuWrapper wrapper = new OtuWrapper(unloggedFile);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String[] topSplits = reader.readLine().replace("\"", "").split("\t");
		
		int startPos = fromR ? 0 : 1;
		
		writer.write("sample\tsampleID\treadNum\tnumSequences\tshannonDiveristy");
		
		for( int x=startPos; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits =s.split("\t");
			
			writer.write(splits[0]);
			StringTokenizer sToken = new StringTokenizer(splits[0], "_");
			writer.write("\t" + sToken.nextToken() + "\t" + sToken.nextToken() );
			writer.write("\t" + wrapper.getNumberSequences(splits[0]) + "\t" + 
								wrapper.getShannonEntropy(splits[0]));
			
			for(int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		reader.close();
		writer.flush();  writer.close();
	}
}
