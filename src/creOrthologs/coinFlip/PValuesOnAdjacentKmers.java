package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;

/*
 * Run WriteKmersWithDifferentAdjacentPValues and then BinNonAdjacentKMers 
 * before running this...
 */
public class PValuesOnAdjacentKmers
{
	private static class RangeHolder implements Comparable<RangeHolder>
	{
		Float lowConservation =null;
		Float highConservation = null;
		int n;
		double meanPValue;
		double sd;
		
		@Override
		public int compareTo(RangeHolder o)
		{
			return Double.compare(this.lowConservation, o.lowConservation );
		}
	}
	
	private static List<RangeHolder> getRanges() throws Exception
	{
		List<RangeHolder> list = new ArrayList<RangeHolder>();
		
		File topDir = new File(
		ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" + 
				File.separator + "ranges" );
		
		String[] files = topDir.list();
		
		for( String s : files)
		{
			File aFile = new File(topDir.getAbsolutePath() + File.separator + s);
			
			BufferedReader reader = new BufferedReader(new FileReader(aFile));
			
			RangeHolder rh = new RangeHolder();
			
			
			
			reader.close();
		}
		
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		
	}
}
