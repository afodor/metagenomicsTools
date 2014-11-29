package ruralVsUrban.abundantOTU;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.HitScores;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class MergeBestBlastToPValues
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer= new BufferedWriter(new FileWriter(new File(ConfigReader.getChinaDir()+
				File.separator + "abundantOTU" + File.separator + "abundantOTUMergedToSilva.txt")));
		
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getChinaDir() + 
				File.separator + "abundantOTU" + File.separator + 
				"abundantOTUForwardTaxaAsColumns.txt");
		
		writer.write("otuID\tpValue\tmeanRural\tmeanUrban\truralDivUrban\ttargetId\tqueryAlignmentLength\tpercentIdentity\tbitScore\tnumSequences\n");
		
		HashMap<String, HitScores> topHitMap = 
		HitScores.getTopHitsAsQueryMap(ConfigReader.getChinaDir() + File.separator + 
				"abundantOTU" + File.separator + "forwardAbundantOTUToSilva.txt.gz");
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getChinaDir()+
				File.separator + "abundantOTU" + File.separator + "pValuesFromMixedLinearModel.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = "Consensus" + splits[1].replaceAll("X", "").replaceAll("\"", "");
			System.out.println(key);
			writer.write(key + "\t");
			writer.write(splits[0] + "\t");
			writer.write(splits[2] + "\t");
			writer.write(splits[3] + "\t");
			writer.write(splits[4] + "\t");
			
			HitScores hs = topHitMap.get(key);
			writer.write(hs.getTargetId() + "\t");
			writer.write(hs.getQueryAlignmentLength() + "\t");
			writer.write(hs.getPercentIdentity() + "\t");
			writer.write(hs.getBitScore() + "\t");
			writer.write(wrapper.getCountsForTaxa(splits[1].replaceAll("X", "").replaceAll("\"", "")) + "\n");
		}
		writer.flush(); writer.close();
	}
}
 