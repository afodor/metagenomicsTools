package scripts.ratSach2.rdpAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import scripts.ratSach2.MappingFileLine;
import utils.ConfigReader;

public class AddMetadata
{
	private static void addSomeMetadata(BufferedReader reader, 
					BufferedWriter writer,boolean skipFirst,
					OtuWrapper unnormalizedWrapper) throws Exception
	{
		HashMap<String, MappingFileLine> metaMap = MappingFileLine.getMap();
		HashMap<String, String> ratToCageMap = getCageMappings();
		writer.write("sampleID\tline\ttissue\tratID\tcage\tnumSequences\tshannonDiversity\tunrarifiedRichness");
		
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
			writer.write(mfl.getTissue() + "\t"  );
			writer.write(mfl.getRatID() + "\t");
			writer.write( ratToCageMap.get(mfl.getRatID()) +"\t");
			
			writer.write(unnormalizedWrapper.getCountsForSample(key) + "\t");
			writer.write(unnormalizedWrapper.getShannonEntropy(key) + "\t");
			writer.write(unnormalizedWrapper.getRichness(key) + "");
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x] );
			
			writer.write("\n");
		}
			
		writer.flush();  writer.close();
		
		reader.close();
	}
	
	public static HashMap<String, String> getCageMappings() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getRachSachReanalysisDir()+
				File.separator + "TTULyteCages.txt")));
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[1]))
				throw new Exception("NO");
			
			map.put(splits[1], splits[0]);
		}
		
		reader.close();
		return map;
	}
	
	
	public static void main(String[] args) throws Exception
	{
		/*
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
			
			OtuWrapper wrapper = new OtuWrapper(ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator + "sparseThreeColumn_" + NewRDPParserFileLine.TAXA_ARRAY[x] + 
						"_AsColumns.txt");
			
			addSomeMetadata(reader, writer,false, wrapper);
		}
		*/
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++ )
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			BufferedReader reader = new BufferedReader(new FileReader(new File( 
					ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator +
					"sparseThreeColumn_" +  NewRDPParserFileLine.TAXA_ARRAY[x] +
						"_AsColumnsLogNormalized.txt" )));
			
			BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
					ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator +
					"sparseThreeColumn_" +  NewRDPParserFileLine.TAXA_ARRAY[x] +
						"_AsColumnsLogNormalizedPlusMetadata.txt" )));
			
			OtuWrapper wrapper = new OtuWrapper(ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator + "sparseThreeColumn_" + NewRDPParserFileLine.TAXA_ARRAY[x] + 
						"_AsColumns.txt");
			
			addSomeMetadata(reader, writer,true, wrapper);
			
			String[] tissues = { "Cecal Content", "Colon content" };
			
			for(String tissue : tissues)
			{
				reader = new BufferedReader(new FileReader(new File(
						ConfigReader.getRachSachReanalysisDir()
						+ File.separator + "rdpAnalysis" 
						+ File.separator +
						"pcoa_" +  NewRDPParserFileLine.TAXA_ARRAY[x] + "_" + tissue  +".txt"
						)));
				
				writer = new BufferedWriter(new FileWriter(new File(
						ConfigReader.getRachSachReanalysisDir()
						+ File.separator + "rdpAnalysis" 
						+ File.separator +
						"pcoa_" +  NewRDPParserFileLine.TAXA_ARRAY[x] + "_" + tissue  +"PlusMetadata.txt"
						)));
				
				addSomeMetadata(reader, writer,false, wrapper);
				
			}
		}
	}
}
