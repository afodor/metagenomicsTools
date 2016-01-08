package creOrthologs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.ConfigReader;

public class GetDistinctAlignedOrthologs
{
	public static class Holder
	{
		String lineName;
		String contingName;
		int startPos; 
		int endPos;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = getList();
		System.out.println(list.size());
	}
	
	public static List<Holder> getList() throws Exception
	{
		Random random = new Random();
		List<Holder> list = new ArrayList<Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
			"chs_11_plus_cards.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s =reader.readLine())
		{
			//System.out.println(s);
			String[] splits = s.split("\t");
			Holder h = new Holder();
			h.lineName = splits[0];
			
			if( h.lineName.trim().length() > 0 && splits[5].length() > 0 && splits[6].length() > 0 
						&& ! splits[5].equals("null") && ! splits[6].equals("null"))
			{

				h.contingName = splits[1].replaceAll("contig_", "");
				
				h.startPos = Integer.parseInt(splits[2]);
				h.endPos = Integer.parseInt(splits[3]);
				
				if( h.startPos > h.endPos)
				{
					int temp = h.startPos;
					h.startPos = h.endPos;
					h.endPos = temp;
				}
				
				boolean okToAdd = true;
				
				for( int x=0; okToAdd && x < list.size(); x++)
				{
					Holder oldHolder = list.get(x);
					
					if( h.startPos > oldHolder.startPos && h.startPos < oldHolder.endPos)
						okToAdd = false;
					
					if( h.endPos > oldHolder.startPos && h.endPos < oldHolder.endPos)
						okToAdd = false;
				}
				
				if( okToAdd && (Double.parseDouble(splits[5]) < 0.0001 ||
									Double.parseDouble(splits[6]) < 0.0001 || 	
											random.nextFloat() <= .25))
					list.add(h);
				
			}  // end if
		} // end for
			
		
		return list;	
	}
}
