package scripts.sonja2016;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
		writeResults(list);
	}
	
	
	private static class Holder
	{
		String probeID;
		String annotation;
		Double allAverage;
		Double wtAverage;
		Double koAverage;
		int rankAll=0;
		int rankWt=0;
		int rankKO=0;
		int rankAllChannelsAndTransporters=0;
		int rankWtChannelsAndTransporters=0;
		int rankKOChannelsAndTransporters=0;
		String fileLine;
		boolean isChannelOrTransporter;
	}
	
	private static void writeResults(List<Holder> list)  throws Exception
	{
		System.out.println("Writing");
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getSonja2016Dir() + File.separator + "cochleaSummary.txt")));
		
		writer.write(			"probeID\tisChannelOrTransporter\tAndrea_KO-Coch_Mouse430_2.CEL\tAndrea_WT-Coch_Mouse430_2.CEL\t" + 
						"Sonja_KO_COCH_A_Mouse430_2.CEL\tSonja_KO_COCH_B_Mouse430_2.CEL\t" + 
						"Sonja_WT_COCH_A_Mouse430_2.CEL\tSonja_WT_COCH_B_Mouse430_2.CEL\t" + 
						 "allAverage\twtAverage\tkoAverage\trankAll\trankWt\trankKo\trankAllChannelsOrTransporters\t"
						 + "rankWTChannelsOrTransporters\trankKoChannelsOrTransporters\tannotation\n");
		
		for(Holder h : list )
		{
			writer.write(h.probeID);
			
			writer.write("\t" + h.isChannelOrTransporter );
			
			String[] splits = h.fileLine.split("\t");
			
			for( int x=1; x <=6; x++)
				writer.write("\t" + Double.parseDouble(splits[x]));
			
			writer.write("\t" + h.allAverage);
			writer.write("\t" + h.wtAverage);
			writer.write("\t" + h.koAverage);
			
			writer.write("\t" + h.rankAll);
			writer.write("\t" + h.rankWt);
			writer.write("\t" + h.rankKO);
			

			writer.write("\t" + h.rankAllChannelsAndTransporters);
			writer.write("\t" + h.rankWtChannelsAndTransporters);
			writer.write("\t" + h.rankKOChannelsAndTransporters);
			
			writer.write(h.annotation + "\n");
		}
		
		writer.flush();  writer.close();
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
			System.out.println(splits[0]);
			
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
			
			int rank=0;
			int rankChannel =0;
			
			for( Holder h2 : list)
			{
				rank++;
				
				if( h2.isChannelOrTransporter)
					rankChannel++;
				
				h.rankAll= rank;
				h.rankAllChannelsAndTransporters = rankChannel;
			}
			
			Collections.sort( list, new Comparator<Holder>()
			{
				@Override
				public int compare(Holder o1, Holder o2)
				{
					return Double.compare(o2.wtAverage,o1.wtAverage);
				}
			} );
			
			rank=0;
			rankChannel =0;
			
			for( Holder h2 : list)
			{
				rank++;
				
				if( h2.isChannelOrTransporter)
					rankChannel++;
				
				h.rankWt= rank;
				h.rankWtChannelsAndTransporters= rankChannel;
			}
			
			Collections.sort( list, new Comparator<Holder>()
			{
				@Override
				public int compare(Holder o1, Holder o2)
				{
					return Double.compare(o2.koAverage,o1.koAverage);
				}
			} );
			
			rank=0;
			rankChannel =0;
			
			for( Holder h2 : list)
			{
				rank++;
				
				if( h2.isChannelOrTransporter)
					rankChannel++;
				
				h.rankKO= rank;
				h.rankKOChannelsAndTransporters= rankChannel;
			}
		}
		
		return list;
		
	}
}
	
	