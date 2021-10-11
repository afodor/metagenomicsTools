package scripts.ahnVickieMouse;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import parsers.OtuWrapper;
import scripts.markSeqsAug2015.PivotAllLevels;

public class InitialPivot
{
	public static final String[] LEVELS = { "otu", "k", "p", "c", "o", "f", "g" };
	public static final String UNCLASSIFIED = "UNCLASSIFIED";
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(
				"C:\\AnhVickiMouseData\\OTU_Table.txt");
		
		List<String> sampleNames = PivotAllLevels.getSampleNames(inFile);
		
		for( String s : LEVELS)
		{
			System.out.println(s);
			HashMap<String, List<Long>> map = PivotAllLevels.getCounts(s, inFile);
			File outFile = new File("C:\\AnhVickiMouseData\\pivoted_" +s+".txt");
					
					PivotAllLevels.writeResults(s, sampleNames, map, outFile);
			
			OtuWrapper wrapper = new OtuWrapper(outFile);
			
			File logFile = new File("C:\\AnhVickiMouseData\\pivoted_" +s+"_logNorm.txt");
			
			wrapper.writeNormalizedLoggedDataToFile(logFile);
		}
	}
	
}
