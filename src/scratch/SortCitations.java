package scratch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortCitations
{
	private static class Holder implements Comparable<Holder>
	{
		String authors;
		String title;
		String publication;
		String volume;
		String number;
		int year =0;
		
		@Override
		// with some help from chatGPT
		public int compareTo(Holder o)
		{
			 int yearComparison = Integer.compare(o.year, this.year);
			
			 if( yearComparison == 0 )
				 return title.compareTo(o.title);
			 
			 return yearComparison;
			 
		}
	}
	
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = getList();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				"C:\\CV\\citationsSorted.txt")));
		
		for(Holder h : list)
		{
			writer.write(h.authors + " " + h.title + " " + h.publication);
			
			if( h.volume != null )
				writer.write( " " + h.volume);
			
			if( h.number != null)
				writer.write(":" + h.number);
			
			if( h.year > 0)
				writer.write(".  " + h.year);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		System.out.println(list.size());
	}
	
	private static List<Holder> getList() throws Exception
	{
		List<Holder> list = new ArrayList<SortCitations.Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\CV\\citationTabDeliminted.txt")));
		
		reader.readLine();
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			if( s.trim().length() > 0 )
			{
				Holder h = new Holder();
				
				String[] splits = s.split("\t",-1);
				
				h.authors = splits[0];
				h.title = splits[1];
				h.publication = splits[2];
				
				if( splits[3].trim().length() > 0 )
					h.volume = splits[3];
				
				if( splits[4].trim().length() > 0 )
					h.number = splits[4];
				
				if( splits[6].trim().length() > 0 )
					h.year = Integer.parseInt(splits[6]);
				
				list.add(h);
				
				
			}
		}
		
		reader.close();
		Collections.sort(list);
		return list;
	}
}
