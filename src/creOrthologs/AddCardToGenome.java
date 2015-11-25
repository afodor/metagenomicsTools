package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import parsers.HitScores;
import utils.ConfigReader;

public class AddCardToGenome
{
	public static void main(String[] args) throws Exception
	{
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
		
		for(HitScores hs : list ) 
		{
			writer.write("\t" + hs.getTargetId() + "\t" + hs.getTargetStart() 
			+ "\t" + hs.getTargetEnd()+ "\t" + 
					hs.getBitScore()  + "\t1e-30\t1e-30\t1e-30\t" +
			hs.getQueryId() + "\t" + 
			hs.getEScore() + "\tcardDatabase\n");
			
		}
		
		reader.close();
		writer.flush(); writer.close();
	}
}
