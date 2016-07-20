package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.ConfigReader;

public class BinNonAdjacentKmers
{
	private static class Holder implements Comparable<Holder>
	{
		String geneName;
		float conservationVal;
		float pVal;
		
		@Override
		public int compareTo(Holder o)
		{
			return Float.compare(this.conservationVal, o.conservationVal);
		}
	}
	
	private static void writeRanges(List<Holder> list ) throws Exception
	{
		int numToInclude = list.size()/10;
		int numToDo= -1;
		int index =1;
		BufferedWriter writer = null;
		
		for( Holder h  : list)
		{
			if( numToDo <= 0 )
			{
				numToDo = numToInclude;
				
				if( writer != null)
				{
					writer.flush();  writer.close();
				}
				
				writer = new BufferedWriter(new FileWriter(new File(
						ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" + 
									File.separator + "ranges" + File.separator + "splits_"+ index + ".txt")));
				writer.write("geneName\tlogPValues\tconservation\n");
				
				index++;	
					
			}
			
			writer.write( h.geneName + "\t" +  h.pVal + "\t" + h.conservationVal + "\n");
			numToDo--;
			
		}
		
		writer.flush();  writer.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = getHolders();
		System.out.println(list.size());
		writeRanges(list);
	}
	
	private static List<Holder> getHolders() throws Exception
	{
		List<Holder> list=  new ArrayList<Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" + 
						File.separator + "nonRedundantPValsVsCons_ResVsSucNoDupes.txt"	)));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			Holder h = new Holder();
			list.add(h);
			
			String[] splits = s.split("\t");
			h.geneName = splits[0];
			h.pVal = Float.parseFloat(splits[4]);
			h.conservationVal = Float.parseFloat(splits[5]);
		}
		
		
		reader.close();
		
		Collections.sort(list);
		return list;
	}
}
