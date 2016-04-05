package creOrthologs.kmers.chunk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
	
	public static void main(String[] args) throws Exception
	{
		addNewAnnotations();
		System.out.println("Uniqe = " + numUnique);
		System.out.println("Duplicate = "  + numDuplicate);
	}
	
	private static void addNewAnnotations() throws Exception
	{
		List<Holder> holders= getList();
		
		for(Holder h : holders)
			System.out.println(h.conting + " " + h.startPos+ " " + h.endPos+ " " + h.pcas.get(0));
		
		BufferedReader reader =new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
				"chs_11_plus_cards.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
			"chs_11_plus_cardsWithChunkPCA.txt"
				)));
		
		writer.write(reader.readLine());
		
		for( int x=0; x < 10; x++)
			writer.write("\tPCA" + (x+1));
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			int startPos =Integer.parseInt(splits[5]);
			int endPos = Integer.parseInt(splits[6]);
			
			if( startPos > endPos)
			{
				int temp = startPos;
				startPos = endPos;
				endPos = temp;
			}
				
			Holder h = findInListOrNull(holders, splits[4], startPos, endPos);
			
			writer.write(s);
			
			for( int x=0;x  < 10; x++)
			{
				writer.write("\t" + ( h == null ? "" : h.pcas.get(x) ) );
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
	
	private static int numDuplicate =0 ;
	private static int numUnique =0;
	
	private static Holder findInListOrNull(List<Holder> list, String contig, int startPos, int endPos) 
				throws Exception
	{
		Holder h = null;
		int overlap =0;
		
		for(Holder h2 : list)
		{
			if( h2.conting.equals(contig) && startPos >= h2.startPos && endPos <= h2.endPos )
			{
				if( h != null )
				{
					System.out.println("Duplicate");
					numDuplicate++;
				}
				else
				{
					System.out.println("Unique");
					numUnique++;
				}
					
				h = h2;
			}
		}
		
		if( h == null)
			System.out.println("Could not find " + contig + " " + startPos + " " + endPos);
		
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
			
			h.conting =splits[4];
			h.startPos=  Integer.parseInt(splits[1]);
			h.endPos = Integer.parseInt(splits[2]);
			
			for( int x=6; x < splits.length; x++)
				h.pcas.add(Double.parseDouble(splits[x]));
			
			list.add(h);
		}
		
		
		return list;
	}
}
