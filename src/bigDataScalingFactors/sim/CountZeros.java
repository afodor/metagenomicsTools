package bigDataScalingFactors.sim;

import java.io.File;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class CountZeros
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(
				ConfigReader.getBigDataScalingFactorsDir() 
				+ File.separator + "risk" + 
				File.separator + "dirk" 
				+ File.separator + "resample" + File.separator + 
				"resampled.txt");
		
		int index = wrapper.getIndexForSampleName("sample_4181");
		
		int numZeros =0;
		
		for( int x=0; x < wrapper.getOtuNames().size(); x++)
			if( wrapper.getDataPointsUnnormalized().get(index).get(x)==0)
				numZeros++;
		
		System.out.println(numZeros + "  " + wrapper.getOtuNames().size());
	}
}
