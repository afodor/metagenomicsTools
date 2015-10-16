package ruralVsUrban.kathrynFinalTables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.HitScores;
import utils.ConfigReader;

public class KathrynOTUsToNCBI
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + 
						"ncbiRuralVsUrban.txt")));
		
		writer.write("meanRural\tmeanUrban\tpercentIdentity\tpValue\talignmentLength\thigherInRural\n");
		
		HashMap<String, HitScores> hitMap = 
		HitScores.getTopHitsAsQueryMap(ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + 
					"allForwardToNCBI_Oct15.txt");
		
		for( String s : hitMap.keySet())
			System.out.println( s + " "+ hitMap.get(s).getBitScore());
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + "otuModel_pValues_otu.txt"
				)));
		
		reader.readLine();
		
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		//	if( s.startsWith("X"))
		{
			String[] splits = s.split("\t");
			
			String key = "Consensus" +  splits[0].replace("X", "").replaceAll("\"", "");
			
			HitScores hs = hitMap.get(key);
			
			if( hs != null)
			{
				double meanUrban = (Double.parseDouble(splits[2]) + Double.parseDouble(splits[3])) / 2.0;
				double meanRural = (Double.parseDouble(splits[4]) + Double.parseDouble(splits[5])) / 2.0;
				
				writer.write(meanRural + "\t" + meanUrban + "\t" + hs.getPercentIdentity() + "\t"+ 
										Double.parseDouble(splits[8]) + "\t" + 
									hs.getAlignmentLength() + "\t" + (meanRural > meanUrban) + "\n");
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
