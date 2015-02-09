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
					OtuWrapper unnormalizedWrapper,
					HashMap<String, Double> rarifiedMap ) throws Exception
	{
		HashMap<String, MappingFileLine> metaMap = MappingFileLine.getMap();
		HashMap<String, String> ratToCageMap = getCageMappings();
		writer.write("sampleID\tline\ttissue\tratID\tcage\tcondition\tnumSequences\tshannonDiversity\trarifiedRichness50\tunrarifiedRichness");
		
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
			writer.write( mfl.getCondition() + "\t");
			writer.write(unnormalizedWrapper.getCountsForSample(key) + "\t");
			writer.write(unnormalizedWrapper.getShannonEntropy(key) + "\t");
			
			Double rarified = rarifiedMap.get(key);
			
			writer.write( (rarified == null ? "NA\t" : rarified + "\t" )  );
			
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
	
	
	/*
	 * This is run after running r scripts.
	 */
	/*
	public static void main(String[] args) throws Exception
	{
		String[] levels = { "phylum","class","order","family","genus", "otu" };
		String[] tissues = { "Cecal Content", "Colon content" };
		
		for(String level : levels)
			for( String tissue : tissues )
		{
			System.out.println(level);
			BufferedReader reader = new BufferedReader(new FileReader(new File( 
					ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator + "pcoa_" + level + "_" + tissue + ".txt")));
			
			BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
					ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator + "pcoa_" + level + "_" + tissue +  "WithMetadata.txt" )));
			
			OtuWrapper wrapper = level.equals("otu") ? 
					new OtuWrapper(
							ConfigReader.getRachSachReanalysisDir() + File.separator + "rdpAnalysis" + File.separator + 
							"sparseThreeColumn_otu_AsColumns.txt") : 
					new OtuWrapper(ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator + "sparseThreeColumn_" + level + 
						"_AsColumns.txt");
			
			addSomeMetadata(reader, writer,false, wrapper);
			
			reader = new BufferedReader(new FileReader(new File( 
					ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator +
					"sparseThreeColumn_" + level +
						"_AsColumnsLogNormalized.txt" )));
			
			writer =new BufferedWriter(new FileWriter(new File(
					ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator +
					"sparseThreeColumn_" +  level +
						"_AsColumnsLogNormalizedPlusMetadata.txt" )));
			
			addSomeMetadata(reader, writer,true, wrapper);
		}
	}*/
	
	// This run prior to running r scripts
	public static void main(String[] args) throws Exception
	{
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
			
			HashMap<String, Double> rarifiedMap = 
					getRarifiedRichnessMap(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			addSomeMetadata(reader, writer,true, wrapper,rarifiedMap );
			
		}
	}
	
	private static HashMap<String, Double> getRarifiedRichnessMap(String level) throws Exception
	{
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getRachSachReanalysisDir() + File.separator + 
			"rdpAnalysis" + File.separator + "sparseThreeColumn_" + level + "rarified.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s=  reader.readLine())
		{
			String[] splits = s.split("\t");
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], Double.parseDouble(splits[1]));
		}
		
		return map;
	}
}
		
		
		/*
			
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
		}*/
