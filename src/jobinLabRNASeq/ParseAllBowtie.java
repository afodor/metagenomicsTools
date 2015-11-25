package jobinLabRNASeq;

import java.io.File;
import java.util.StringTokenizer;

import parsers.SamHit;

import utils.ConfigReader;

public class ParseAllBowtie
{

/*
 * After running bowtie against a genome,
 * collect the .sam output with parsers.SamHit
 * Then run ParseAllBowtie and then this class
 */
	public static void main(String[] args) throws Exception
	{
		File topDir = new File(ConfigReader.getJobinLabRNASeqDir());
		
		for(String s : topDir.list())
			if( s.endsWith(".sam.gz"))
			{
				//System.out.println(s);
				String newName = new StringTokenizer(s, "\\.").nextToken();
				SamHit.writeHitsToFile( topDir.getAbsolutePath() + File.separator +  s,
						topDir.getAbsolutePath() + File.separator +  newName + ".txt");
			}
	}
}
