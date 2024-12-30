package scratch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class SortCitations
{
	private static class Holder
	{
		String year;
		String citation;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = getList();
		
		for(Holder h : list)
		{
			System.out.println(h.citation);
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
			Holder h = new Holder();
			h.citation = s;
			list.add(h);
		}
		
		reader.close();
		return list;
	}
}
