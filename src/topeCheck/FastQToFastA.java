package topeCheck;

import java.io.File;

import parsers.FastQ;
import utils.ConfigReader;

public class FastQToFastA
{
	public static void main(String[] args) throws Exception
	{
		File dir = new File(ConfigReader.getTopeCheckDir());
		
		for(String s : dir.list())
			if( s.endsWith(".fastq"))
			{
				FastQ.FastQToFastA(dir + File.separator + s, 
							dir + File.separator + s.replaceAll("fastq", "fasta"));
			}
	}
}
