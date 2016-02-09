package ruralVsUrban.kathrynFinalTables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.HitScores;
import ruralVsUrban.mostWanted.MostWantedMetadata;
import utils.ConfigReader;

public class KathrynOTUToMostWantedPrevelance
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MostWantedMetadata> mostWantedMap = 
					MostWantedMetadata.getMap();
		
		for(String s : mostWantedMap.keySet() )
			System.out.println( s + " " + mostWantedMap.get(s).getMaxFraction());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + 
						"otusToMostWanted.txt")));
		
		writer.write("consensusID\ttarget\tmeanRural\tmeanUrban\tstoolPrevelance\tmaxFraction\tpValue\tpercentIdentity\talignmentLength\ttargetName\thigherInRural\trdpSummary\n");
		
		HashMap<String, HitScores> hitMap = 
		HitScores.getTopHitsAsQueryMap(ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + 
					"forwardToMostWanted16S.txt");
		
		for(String s : hitMap.keySet())
			System.out.println(s + " " + hitMap.get(s).getTargetId() + " "  + hitMap.get(s).getAlignmentLength());
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + "otuModel_pValues_otu.txt"
				)));
		
		reader.readLine();		
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = "Consensus" +  splits[0].replace("X", "").replaceAll("\"", "");
			
			HitScores hs = hitMap.get(key);
			
			if( hs != null)
			{
				MostWantedMetadata meta = mostWantedMap.get(hs.getTargetId());
				
				if( meta == null)
					throw new Exception("No");
				
				double meanUrban = (Double.parseDouble(splits[2]) + Double.parseDouble(splits[3])) / 2.0;
				double meanRural = (Double.parseDouble(splits[4]) + Double.parseDouble(splits[5])) / 2.0;
				
				writer.write(key +"\t" + hs.getTargetId() + "\t" +  meanRural + "\t" + meanUrban + "\t" + meta.getSubjectFractionStool() + "\t"+ 
										meta.getMaxFraction() + "\t" + 
										Double.parseDouble(splits[8]) + "\t" + hs.getPercentIdentity() + "\t" + 
									hs.getAlignmentLength() + "\t" + 
									hs.getTargetId() + "\t" + 	(meanRural > meanUrban) + "\t" + 
											meta.getRdpSummaryString() + "\n");
			}
			else
			{
				System.out.println("Could not find " + key);
			}
		}
		
		writer.flush(); writer.close();
		reader.close();
		
	}
}
