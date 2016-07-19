package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.ConfigReader;

public class BinNonAdjacentKmers
{
	private static class Holder implements Comparable<Holder>
	{
		float conservationVal;
		float pVal;
		
		@Override
		public int compareTo(Holder o)
		{
			return Float.compare(this.conservationVal, o.conservationVal);
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = getHolders();
		System.out.println(list.size());
	}
	
	private static List<Holder> getHolders() throws Exception
	{
		List<Holder> list=  new ArrayList<Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" + 
						File.separator + "nonRedundantPValsVsCons_ResVsSuc.txt"	)));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			Holder h = new Holder();
			list.add(h);
			
			String[] splits = s.split("\t");
			h.pVal = Float.parseFloat(splits[4]);
			h.conservationVal = Float.parseFloat(splits[5]);
		}
		
		
		reader.close();
		
		Collections.sort(list);
		return list;
	}
}
