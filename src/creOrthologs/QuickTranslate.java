package creOrthologs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

import parsers.FastaSequence;
import utils.ConfigReader;

public class QuickTranslate 
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, FastaSequence> map =
			FastaSequence.getFirstTokenSequenceMap(
					ConfigReader.getCREOrthologsDir() + 
					File.separator +"quickTranslation" + File.separator 
					+ "klebsiella_pneumoniae_chs_76.0.scaffolds.fasta");
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + 
				File.separator +"quickTranslation" + File.separator 
				+ "klebsiella_pneumoniae_chs_76.0.genes.gtf")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits =s.split("\t");
			
			FastaSequence fs = map.get(splits[0]);
			
			if( fs == null)
				throw new Exception("No");
			
			if( splits[2].equals("CDS"))
			{
				System.out.println(fs.getSequence().substring(
					Integer.parseInt(splits[3])-4, Integer.parseInt(splits[4])	));
				
				System.out.println();
			}
		}
		
		reader.close();
		
	}
}
