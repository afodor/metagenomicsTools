package scratch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortCitations
{
	private static class Holder implements Comparable<Holder>
	{
		int year;
		String citation;
		
		@Override
		// with some help from chatGPT
		public int compareTo(Holder o)
		{
			 int yearComparison = Integer.compare(o.year, this.year);
			
			 if( yearComparison == 0 )
				 return citation.compareTo(o.citation);
			 
			 return yearComparison;
			 
		}
	}
	
	private static int getYear(String s ) throws Exception
	{
		for( int x=1990; x<=2025; x++)
			if( s.indexOf("" + x) != -1)
				return x;
		
		throw new Exception("Could not find year " + s);
	}
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = getList();
		
		for(Holder h : list)
		{
			System.out.println(h.citation);
			System.out.println();
		}
		
		System.out.println(list.size());
	}
	
	private static List<Holder> getList() throws Exception
	{
		List<Holder> list = new ArrayList<SortCitations.Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\CV\\citationsUnsorted.txt")));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			if( s.trim().length() > 0 )
			{
				Holder h = new Holder();
				h.citation = s;
				h.year = getYear(s);
				list.add(h);
			}
		}
		
		reader.close();
		Collections.sort(list);
		return list;
	}
}
