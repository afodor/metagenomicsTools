package scripts.sonja2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

public class QuickAverages2
{
	public static void main(String[] args) throws Exception
	{
		
		
		List<Holder> list = getHolders();
		
		for(Holder h : list)
			System.out.println(h.probeID + " " + h.allAverage);
	
	}
	
	
	private static class Holder
	{
		String probeID;
		String annotation;
		Double allAverage;
		Double wtAverage;
		Double koAverage;
		int rankAll;
		int rankWt;
		int rankKO;
		int rankAllChannelsAndTransporters;
		int rankWtChannelsAndTransporters;
		int rankKOChannelsAndTransporters;
		String fileLine;
		boolean isChannelOrTransporter;
	}
	
	private static List<Holder> getHolders() throws Exception
	{
		HashMap<String, String> annotationMap = QuickAverages.getAnnotationMap();
		List<Holder> list = new ArrayList<Holder>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getSonja2016Dir() + File.separator + 
					"ALDRICH_RMA_NORMALIZED_JustCochlea.txt")));
		
		String header = reader.readLine();
		String[] headerSplits = header.split("\t");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			double allSum =0;
			double koSum =0;
			double wtSum =0;
			int koN =0;
			int wtN =0;
			for( int x=1; x < splits.length; x++)
			{
				Double val = Double.parseDouble(splits[x]);
				allSum += val;
				
				if( headerSplits[x].indexOf("KO") != -1 )
				{
					koSum+=val;
					koN++;
				}
				else if ( headerSplits[x].indexOf("WT") != -1)
				{
					wtSum += val;
					wtN++;
				}
				else throw new Exception("NO");
				
			}
			
			Holder h = new Holder();
			h.fileLine = s;
			h.probeID = splits[0];
			h.allAverage = allSum / (koN + wtN);
			h.koAverage = koSum / koN;
			h.wtAverage = wtSum/ wtN;
			h.annotation = annotationMap.get(h.probeID);
			if( h.annotation == null)
				throw new Exception("No");
			
			h.isChannelOrTransporter = h.annotation.toLowerCase().indexOf("channel") != - 1
						|| h.annotation.toLowerCase().indexOf("transporter") != -1;
			list.add(h);
			
			Collections.sort( list, new Comparator<Holder>()
			{
				@Override
				public int compare(Holder o1, Holder o2)
				{
					return Double.compare(o2.allAverage ,o1.allAverage);
				}
			} );
		}
		
		return list;
		
	}
}
	
	