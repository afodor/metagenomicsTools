package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import utils.Avevar;
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
		List<Float> pValues = new ArrayList<Float>();
		
		@Override
		public int compareTo(RangeHolder o)
		{
			return Double.compare(this.lowConservation, o.lowConservation );
		}
	}
	
	private static class GeneHolder implements Comparable<GeneHolder>
	{
		String geneName;
		String chr;
		List<Float> ranks = new ArrayList<Float>();
		double average;
		int n;
		
		@Override
		public int compareTo(GeneHolder o)
		{
			return Double.compare(this.average, o.average);
		}
	}
	
	private float getRankProportion( float conservation, float pValue, List<RangeHolder> list ) throws Exception
	{
		for(RangeHolder rh : list)
		{
			if( conservation>= rh.lowConservation && conservation<= rh.highConservation)
			{
				double rank = Math.abs(Collections.binarySearch(rh.pValues, pValue));
				rank = rank / rh.pValues.size();
				return (float) rank;
			}
		}
		
		throw new Exception("Could not find " + conservation);
	}
	
	private static List<GeneHolder> getGenes(List<RangeHolder> rangeList) throws Exception
	{
		HashMap<String, GeneHolder> map = new HashMap<String,GeneHolder>();
		List<GeneHolder> geneList = new ArrayList<GeneHolder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" + 
					File.separator + "nonRedundantPValsVsCons_ResVsSucNoDupes.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String geneId = splits[0];
			float conservation = Float.parseFloat(splits[5]);
			
			if( conservation < 0.85)
			{
				GeneHolder gh = map.get(geneId);
				
				if( gh == null)
				{
					gh = new GeneHolder();
					gh.geneName = geneId;
					gh.chr = splits[1];
					map.put(geneId, gh);
				}
				
				if( ! gh.chr.equals(splits[1]))
					throw new Exception("Parsing error");
				
				float pValue = Float.parseFloat(splits[4]);
				gh.ranks.add(pValue);
			}
			
			
		}
		
		for( GeneHolder gh : map.values())
		{
			Avevar av = new Avevar(gh.ranks);
			gh.average = av.getAve();
			gh.n = gh.ranks.size();
		}
		
		geneList.addAll(map.values());
		Collections.sort(geneList);
		
		return geneList;
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
			
			reader.readLine();

			
			for(String s2 = reader.readLine();s2 != null; s2 = reader.readLine())
			{
				
				String[] splits =s2.split("\t");
				float pValue = Float.parseFloat(splits[1]);
				float conservation = Float.parseFloat(splits[2]);
				
				if( rh.highConservation == null)
				{
					rh.highConservation = conservation;
					rh.lowConservation = conservation;
				}
				
				if( conservation > rh.highConservation)
					rh.highConservation = conservation;
				
				if( conservation < rh.lowConservation)
					rh.lowConservation = conservation;
				
				rh.pValues.add(pValue);
				
			}
			
			Collections.sort(rh.pValues);
			
			reader.close();
		}
		
		Collections.sort(list);
		return list;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<RangeHolder> rangeList =getRanges();
		List<GeneHolder> geneList = getGenes(rangeList);
	}
}
