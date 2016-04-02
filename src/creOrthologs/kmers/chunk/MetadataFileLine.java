package creOrthologs.kmers.chunk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataFileLine
{
	private final String fileName;
	private final String contig;
	private final int start;
	private final int stop;
	private final String type;
	
	private MetadataFileLine(String s) throws Exception
	{
		String[] splits = s.split("\t");
		
		if( splits.length != 7)
			throw new Exception("No");
		
		this.fileName = splits[0];
		this.contig = splits[1];
		this.start = Integer.parseInt(splits[2]);
		this.stop = Integer.parseInt(splits[3]);
		this.type = splits[6];
	}
	
	
	
	public String getFileName()
	{
		return fileName;
	}

	public String getContig()
	{
		return contig;
	}

	public int getStart()
	{
		return start;
	}

	public int getStop()
	{
		return stop;
	}

	public String getType()
	{
		return type;
	}

	public static HashMap<String, MetadataFileLine> getAsMap() throws Exception
	{
		HashMap<String, MetadataFileLine> map = new HashMap<String,MetadataFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"chunkLog.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			MetadataFileLine mfl = new MetadataFileLine(s);
			
			if( map.containsKey(mfl.fileName))
				throw new Exception("No");
			
			map.put(mfl.fileName, mfl);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileLine> map = getAsMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() +
			File.separator +  "chunks_pcoaAllContigs.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir() +
				File.separator +  "chunks_pcoaAllContigsPlusMetadata.txt")));
		
		writer.write("fileName\tstart\tstop\tlength\tcontig\ttype\t" + reader.readLine() + "\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			String key = splits[0].replaceAll("\"", "").replaceAll("_dist.txt", "");
			
			MetadataFileLine mfl = map.get(key);
			
			if( mfl == null)
				throw new Exception("Could not find " + key);
			
			writer.write(key + "\t");
			writer.write(mfl.start + "\t" + mfl.stop + "\t" + (mfl.stop-mfl.start) + "\t" + 
			"contig_" +  mfl.contig + "\t" +
						mfl.type );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
			
		}
		writer.flush();  writer.close();
		reader.close();
	}
}
