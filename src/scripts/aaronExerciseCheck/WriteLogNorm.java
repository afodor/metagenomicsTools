package scripts.aaronExerciseCheck;

import java.io.File;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class WriteLogNorm
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper( ConfigReader.getAaronExerciseDirectory() 
				+ File.separator + "genus.tsv");
		
		
		wrapper.writeNormalizedLoggedDataToFile(ConfigReader.getAaronExerciseDirectory() 
				+ File.separator + "genusLogNorm.tsv");
		
	}
}
