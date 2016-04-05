package creOrthologs.kmers.chunk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;

public class pcaForChunk
{
	private static class Holder
	{
		String conting;
		int startPos;
		int endPos;		
		
		List<Double> pcas = new ArrayList<Double>();
	}
	
	private static void addNewAnnotations() throws Exception
	{
		
	}
	
	private static Holder findInListOrNull(List<Holder> list, String contig, int startPos, int endPos) 
				throws Exception
	{
		Holder h = null;
		
		for(Holder h2 : list)
		{
			if( h2.conting.equals(contig) && h2.startPos >= startPos && h2.endPos <= endPos )
			{
				if( h != null )
					throw new Exception("Duplicate");
				
				h = h2;
			}
		}
		
		return h;
	}
	
	private static List<Holder> getList() throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		BufferedReader reader =new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
						"chunks_pcoaAllContigsPlusMetadata.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{		
			String[] splits =s.split("\t");
			
			Holder h = new Holder();
			
			h.conting =splits[4].replace("contig_", "");
			h.startPos=  Integer.parseInt(splits[1]);
			h.endPos = Integer.parseInt(splits[2]);
			
			for( int x=6; x < splits.length; x++)
				h.pcas.add(Double.parseDouble(splits[x]));
			
			list.add(h);
		}
		
		
		return list;
	}
}
