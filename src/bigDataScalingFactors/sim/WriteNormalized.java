package bigDataScalingFactors.sim;

import java.io.File;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class WriteNormalized
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(new File(
				ConfigReader.getBigDataScalingFactorsDir() 
				+ File.separator + "risk" 
				+ File.separator + "dirk" 
				+ File.separator + "resample" + File.separator + 
						"resampledMaxDepth.txt"	));
		
		
		wrapper.writeNormalizedDataToFile(new File(ConfigReader.getBigDataScalingFactorsDir() 
				+ File.separator + "risk" 
				+ File.separator + "dirk" 
				+ File.separator + "resample" + File.separator + 
						"resampledNormalizedMaxDepth.txt"));
		
		wrapper.writeRarifiedSpreadhseet(new File(ConfigReader.getBigDataScalingFactorsDir() 
				+ File.separator + "risk" 
				+ File.separator + "dirk" 
				+ File.separator + "resample" + File.separator + 
						"resampledRarifiedMaxDepth.txt"),false,4181);
	}
}
