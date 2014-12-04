package ruralVsUrban.abundantOTU;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.FastaSequence;
import parsers.HitScores;
import parsers.OtuWrapper;
import ruralVsUrban.mostWanted.MostWantedMetadata;
import utils.ConfigReader;

public class MergeBestBlastToPValuesForAbundantOTU
{
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, FastaSequence> fastaMap = 
				FastaSequence.getFirstTokenSequenceMap(ConfigReader.getChinaDir() + File.separator + 
						"ncbi" + File.separator + "ncib16.fasta");
		
		BufferedWriter writer= new BufferedWriter(new FileWriter(new File(ConfigReader.getChinaDir()+
				File.separator + "mostWanted" + File.separator + "abundantOTUMostWanted.txt")));
		
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getChinaDir() + 
				File.separator + "abundantOTU" + File.separator + 
				"abundantOTUForwardTaxaAsColumns.txt");
		
		
		writer.write("otuID\tpValue\tadjustedp\thigherIn\tmeanRural\tmeanUrban\truralDivUrban\ttargetId\tqueryAlignmentLength\tpercentIdentity\tbitScore\tnumSequences\t" + 
		"mostWantedID\tmostWantedPriority\tmaxFraction\tstoolSubjectFraction\trelativeAbundanceStool\tgoldGlobalMostWanted\tgoldGlobalBestHit\trdpMetadata\t" + ""
				+ "ncbiPercentIdentity\tncbiHeader\tgreengenesPercentIdentity\tgreengenesIdentifier\n");
		HashMap<String, MostWantedMetadata> mostMetaMap = MostWantedMetadata.getMap();
		
		HashMap<String, HitScores> topHitMap = 
		HitScores.getTopHitsAsQueryMap(ConfigReader.getChinaDir() + File.separator + 
				"mostWanted" + File.separator + "forwardToMostWanted16S.txt.gz");
		
		HashMap<String, HitScores> ncbiMap= 
				HitScores.getTopHitsAsQueryMap(ConfigReader.getChinaDir() + File.separator + 
						"ncbi" + File.separator + "forwardTo16S.txt.gz");
		
		HashMap<String, HitScores> greengensMap= 
				HitScores.getTopHitsAsQueryMap(ConfigReader.getChinaDir() + File.separator + 
						"greengenes" + File.separator + "forwardToNamed.txt.gz");
		
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getChinaDir()+
				File.separator + "abundantOTU" + File.separator + "pValuesFromMixedLinearModel.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = "Consensus" + splits[1].replaceAll("X", "").replaceAll("\"", "");
			System.out.println(key);
			
			
			if(! key.equals("ConsensusunRarifiedRichness") 
					&& ! key.equals("ConsensusshannonEveness") && 
						!key.equals("ConsensusshannonDiversity"))
			{
				
				writer.write(key + "\t");
				writer.write(splits[0] + "\t");
				writer.write(splits[5] + "\t");
				
				writer.write( (Double.parseDouble(splits[2])  >
								Double.parseDouble(splits[3]) ? "rural" : "urban" ) + "\t");
				
				writer.write(splits[2] + "\t");
				writer.write(splits[3] + "\t");
				writer.write(splits[4] + "\t");
				
				HitScores hs = topHitMap.get(key);
				
				if( hs != null)
				{
					writer.write(hs.getTargetId() + "\t");
					writer.write(hs.getQueryAlignmentLength() + "\t");
					writer.write(hs.getPercentIdentity() + "\t");
					writer.write(hs.getBitScore() + "\t");
				}
				else
				{
					writer.write("NA\t0\t0\t0\t");
				}
				
				writer.write(wrapper.getCountsForTaxa(splits[1].replaceAll("X", "").replaceAll("\"", "")) + "\t");
				
				if( hs != null)
				{
					MostWantedMetadata mostMeta = mostMetaMap.get(hs.getTargetId());
					
					if( mostMeta == null)
						throw new Exception("Could not find " + hs.getTargetId());
					
					writer.write(hs.getTargetId() + "\t");
					writer.write(mostMeta.getPriority() + "\t");
					writer.write(mostMeta.getMaxFraction() + "\t");
					writer.write(mostMeta.getSubjectFractionStool() + "\t");
					writer.write(mostMeta.getRelativeAbundanceStool() + "\t");
					writer.write(mostMeta.getGoldGlobal() + "\t");
					writer.write(mostMeta.getGoldGlobalBestHit() + "\t");
					writer.write(mostMeta.getRdpSummaryString() + "\t");
				}
				else
				{
					writer.write("NA\tNA\t0\t0\t0\t0\tNA\tNA\t");
				}
				
				writer.write(ncbiMap.get(key).getPercentIdentity() + "\t");
				writer.write(fastaMap.get(ncbiMap.get(key).getTargetId()).getHeader().substring(1) + "\t");
				writer.write(greengensMap.get(key).getPercentIdentity() + "\t");
				writer.write(greengensMap.get(key).getTargetId() + "\n");
			}
		}
		writer.flush(); writer.close();
	}
}
 