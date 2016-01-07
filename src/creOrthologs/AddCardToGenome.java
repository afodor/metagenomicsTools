package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;

import parsers.FastaSequence;
import parsers.HitScores;
import utils.ConfigReader;

public class AddCardToGenome
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> fastaMap = getFastaMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
			"annotatedHitsDir" + File.separator + 
			"klebsiella_pneumoniae_chs_11.0topHits.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
			"chs_11_plus_cards.txt")));
		
		writer.write(reader.readLine() + "\tcardsEscore\tsource\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			writer.write( s + "\t\tmbdgOrtholog\n");
		}
		
		List<HitScores> list = HitScores.getAsList(ConfigReader.getCREOrthologsDir() + 
			File.separator + "cardToChs11Blast.txt"	);
		
		double val = Math.log10(1e-65);
		
		for(HitScores hs : list ) 
		{
			writer.write("\t\t\t\t" + "contig_" +  hs.getTargetId() + "\t" + hs.getTargetStart() 
			+ "\t" + hs.getTargetEnd()+ "\t" + 
					hs.getBitScore()  + "\t" + val +"\t" + val + "\t" + val +"\t" +
			fastaMap.get(hs.getQueryId()) + "\t" + 
			hs.getEScore() + "\tcardDatabase\n");
			
			System.out.println(hs.getQueryId() + " " + fastaMap.get(hs.getQueryId()));
			
		}
		
		reader.close();
		writer.flush(); writer.close();
	}
	
	private static HashMap<String, String> getFastaMap() throws Exception
	{
		HashMap<String, String> fastaMap = new HashMap<String,String>();
		
		List<FastaSequence> list = FastaSequence.readFastaFile(ConfigReader.getCREOrthologsDir()+
				File.separator + "ARmeta-genes.fa");
		
		for(FastaSequence fs : list)
			fastaMap.put(fs.getFirstTokenOfHeader(), fs.getHeader());
		
		return fastaMap;
	}
}
