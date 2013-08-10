c

package scripts.pancreatitis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import parsers.OtuWrapper;
import pca.PCA;
import utils.ConfigReader;

public class PancreatitisPCAPivot
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(new File( 
				ConfigReader.getPancreatitisDir() + File.separator + 
		"erinHannaHuman_raw_phyForR.txt"), new HashSet<String>(), new HashSet<String>(),10);
		
		
		double[][] d=  wrapper.getUnnorlalizedAsArray();
		
		List<String> keys = new ArrayList<String>();
		for( String s : wrapper.getSampleNames() )
		{
			keys.add(s.replace("human", ""));
		}
		
		List<String> catHeaders = new ArrayList<String>();
		List<List<String>> categories = new ArrayList<List<String>>();
		File outFile = 
			new File(ConfigReader.getPancreatitisDir() + File.separator
					+ "PCA_PIVOT.txt");
		System.out.println("Writing " + outFile.getAbsolutePath());
		PCA.writePCAFile(keys, catHeaders, categories,
				d, outFile
				);
	}
}
