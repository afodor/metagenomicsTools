package creOrthologs.igvOut;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Random;

import parsers.FastaSequence;
import utils.ConfigReader;

public class SamplePValues
{
	public static void main(String[] args) throws Exception
	{
		Random r = new Random();
		
		List<FastaSequence> list = FastaSequence.readFastaFile(ConfigReader.getBioLockJDir() + 
			File.separator + "resistantAnnotation" + File.separator + "refGenome" + 
					File.separator + "klebsiella_pneumoniae_chs_11.0.scaffolds.fasta");
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
			ConfigReader.getBioLockJDir() + File.separator + 
			"resistantAnnotation" + File.separator + "madeUpPValues.gwas"
			)));
		
		writer.write("CHR\tBP\tSNP\tP\n");
		
		for( FastaSequence fs : list)
		{
			String seq = fs.getSequence();
			String chr = fs.getFirstTokenOfHeader();
			
			for( int x=0; x < seq.length(); x++)
			{
				writer.write(chr + "\t");
				writer.write( (x+1) + "\t");
				writer.write( "SNP_"  + x + "\t"  );
				writer.write( 1.0 / r.nextInt(10000000) + "\n");
			}
			
		}
		
		writer.flush();  writer.close();
		
	}
}
