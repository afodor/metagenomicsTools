package jobinLabRNASeq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import parsers.GffParser;

import utils.ConfigReader;

/*
 * After running bowtie against a genome,
 * collect the .sam output with parsers.SamHit
 * Then run ParseAllBowtie and then this class
 */
public class FindGeneForMappedSequence
{
	public static final String INTER_GENIC = "INTER_GENIC";
	
	private static class Holder
	{
		@SuppressWarnings("unused")
		String locusTag;
		int[] counts;
	}
	
	public static HashMap<String, GffParser> getGeneFileMap() throws Exception
	{
		HashMap<String, GffParser> map = 
			new HashMap<String, GffParser>();
		
		File topDir = new File(ConfigReader.getJobinLabRNASeqDir());
		for(String s : topDir.list())
		{
			if( s.endsWith(".gff"))
			{
				String key = s.replace(".gff", "");
				
				if( map.containsKey(key))
					throw new Exception("No");
				
				map.put(key, new GffParser( topDir.getAbsolutePath() + 
												File.separator + s));
			}
		}
		
		return map;
	}
	
	public static List<File> getOutputFiles() throws Exception
	{
		List<File> list = new ArrayList<File>();
		File topDir = new File(ConfigReader.getJobinLabRNASeqDir());
		for(String s : topDir.list())
		{
			if( s.endsWith("output.txt.gz"))
			{
				list.add(new File( topDir.getAbsolutePath() + File.separator + 
										s));
			}
		}
		
		return list;
		
	}
	
	private static void writeResults(HashMap<String, Holder> holderMap,
										List<File> outputFiles)
				throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"pivotedSamplesAsColumns.txt")));
		
		writer.write("locusTag");
		
		for(File f : outputFiles)
			writer.write("\t" + f.getName().replace("_output.txt.gz", ""));
		
		writer.write("\n");
		
		for( String s : holderMap.keySet() )
		{
			writer.write(s);
			
			Holder h = holderMap.get(s);
			
			for( int x=0; x < outputFiles.size(); x++)
				writer.write("\t" + h.counts[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> holderMap = new HashMap<String, Holder>();
		HashMap<String, GffParser> geneFileMap = getGeneFileMap();
		List<File> outputFiles = getOutputFiles();
		
		for( int x=0; x < outputFiles.size(); x++)
			addAFile(outputFiles.get(x), holderMap, geneFileMap, x, outputFiles.size());
		
		writeResults(holderMap, outputFiles);
	}
	
	private static void addAFile(File file, HashMap<String, Holder> holderMap,
			HashMap<String, GffParser> geneFileMap, 
			int index, int listSize) throws Exception
	{
		System.out.println(file.getName() +" " + index + " " + listSize);
		BufferedReader reader = new BufferedReader( new InputStreamReader( 
				new GZIPInputStream( new FileInputStream( file))));
	
		reader.readLine();
		
		int count =0;
		for(String s= reader.readLine(); 
					s != null;
						s= reader.readLine())
		{
			String[] splits= s.split("\t");
			
			StringTokenizer sToken = new StringTokenizer(splits[1], "|");
			
			sToken.nextToken();sToken.nextToken();sToken.nextToken();
			String targetName = sToken.nextToken().replace(".1", "");
			
			GffParser gffParser = geneFileMap.get(targetName);
			
			if( gffParser == null)
				throw new Exception("No");
			
			String locusTag = gffParser.getGeneLocusTag(Integer.parseInt(splits[2]));
			
			if( locusTag == null)
				locusTag = INTER_GENIC;
			
			Holder h = holderMap.get(locusTag);
			
			if( h== null)
			{
				h = new Holder();
				h.locusTag = locusTag;
				h.counts = new int[listSize];
				holderMap.put(locusTag,h);
			}
			h.counts[index]++;
			
			if( ++count % 1000000 ==0)
				System.out.println("\t\t" + count);
		}
	}
}
