package mbqc;

import java.util.HashMap;
import java.util.List;

public class PValuesByExtraction
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, RawDesignMatrixParser> map = RawDesignMatrixParser.getByFullId();
		List<String> bioinformaticsIds = RawDesignMatrixParser.getAllBioinformaticsIDs(map);
		
		for(String s : bioinformaticsIds)
			System.out.println(s);
		
		List<String> taxaHeaders = RawDesignMatrixParser.getTaxaIds();
		
		for(String s : taxaHeaders)
			System.out.println(s);
	}
	
	/*
	 * For each sample, return a p-value for the null hypothesis that the distribution of NA
	 * is the same as the extraction protocol with the most total samples for that wetlabID 
	 * and drylabID
	 * 
	 * This is accomplished by paired test for samples with at least one NA and one other extraction.
	 * An average is taken across multiple samples for NA and other.
	 */
	private static double getPValueForNAVsOther( HashMap<String, RawDesignMatrixParser> map,
									String wetlabID,
									String drylabID,
									String taxa)
	{
		return 0;
	}
}
