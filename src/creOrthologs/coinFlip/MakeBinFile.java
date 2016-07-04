package creOrthologs.coinFlip;

import java.util.ArrayList;
import java.util.List;

public class MakeBinFile
{
	public static float increment =0.05f;
	
	public static class Holder
	{
		double sum=0;
		int n=0;
		float low;
		float high;
	}

	private static List<Holder> getHolderList() throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		float start = 0;
		
		while( start < 1.00f)
		{
			Holder h = new Holder();
			h.low = start;
			h.high = start + increment;
			start += increment;
			
			list.add(h);
		}
		
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = getHolderList();
		
		for(Holder h : list)
			System.out.println(h.low +" " + h.high);
	}
}
