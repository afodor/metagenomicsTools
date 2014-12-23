package scripts.ratSach2.rdpAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import scripts.ratSach2.MappingFileLine;
import utils.ConfigReader;

public class AddMetadata
{
	private static void addSomeMetadata(BufferedReader reader, 
					BufferedWriter writer,boolean skipFirst) throws Exception
	{
		HashMap<String, MappingFileLine> metaMap = MappingFileLine.getMap();
		writer.write("sampleID\tline\ttissue");
		
		if( !skipFirst)
		{
			writer.write("\t" + reader.readLine() + "\n");
		}
		else
		{
			String[] splits = reader.readLine().split("\t");
			for( int x=1; x < splits.length; x++)
				writer.write("\t"  + splits[x]);
			
			writer.write("\n");
		}
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");

			String key = splits[0].trim().replaceAll("\"", "");
			writer.write(key+ "\t");
			MappingFileLine mfl = metaMap.get(key);
			writer.write(mfl.getLine() + "\t");
			writer.write(mfl.getTissue()  );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x] );
			
			writer.write("\n");
		}
			
		writer.flush();  writer.close();
		
		reader.close();
	}
	
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++ )
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			BufferedReader reader = new BufferedReader(new FileReader(new File( 
					ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator + "pcoa_" + NewRDPParserFileLine.TAXA_ARRAY[x] + ".txt" )));
			
			BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
					ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator + "pcoa_" + NewRDPParserFileLine.TAXA_ARRAY[x] + "WithMetadata.txt" )));
			
			addSomeMetadata(reader, writer,false);
		}
		
		
	}
}
