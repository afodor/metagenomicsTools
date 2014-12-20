package mbqc;

import java.util.HashMap;
import java.util.HashSet;
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
		
		List<String> mbqcIDs = RawDesignMatrixParser.getAllMBQCIDs(map);
		List<String> wetlabIds = RawDesignMatrixParser.getAllWetlabIDs(map);
		System.out.println(wetlabIds);
		
		String sequencingLab = "agoodman";
		String bioinformaticsID = "jpetrosino";
		String taxa = "Proteobacteria";
		
		double aPValue = getPValueForNAVsOther(map, mbqcIDs, sequencingLab, bioinformaticsID, taxa);
		System.out.println(aPValue);
	}
	
	/*
	 * Returns the most common extraction id (the one in the most # of distinct mbqcIDs) that is not NA
	 */
	private static String getMostCommonExtraction( HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap) throws Exception
	{
		HashMap<String, Integer> extractionCounts = new HashMap<String, Integer>();
		
		for(String s : mbqcIDMap.keySet())
		{
			HashSet<String> extractionIDs = new HashSet<String>();
			List<RawDesignMatrixParser> innerList = mbqcIDMap.get(s);
			
			for(RawDesignMatrixParser rdmp : innerList)
			{
				if( !rdmp.getExtractionWetlab().equals("NA") && !extractionIDs.contains(rdmp.getExtractionWetlab()))
				{
					extractionIDs.add(rdmp.getExtractionWetlab());
					
					Integer count = extractionCounts.get(rdmp.getExtractionWetlab());
					
					if( count == null)
						count =0;
					
					count++;
					
					extractionCounts.put(rdmp.getExtractionWetlab(), count);
				}
			}
		}
		
		if(extractionCounts.size() == 0 )
			return null;
		
		String maxExtraction = null;
		int maxCount =-1;
		
		for(String s : extractionCounts.keySet())
		{
			int count = extractionCounts.get(s);
			
			if( count > maxCount)
			{
				maxCount = count;
				maxExtraction = s;
			}
		}
		
		return maxExtraction;
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
									List<String>  mbqcIDs,
									String wetlabID,
									String drylabID,
									String taxa) throws Exception
	{
		 HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap = 
				RawDesignMatrixParser.pivotBySampleID(map, drylabID, wetlabID);
		
		 String extractionID = getMostCommonExtraction(mbqcIDMap);
		 
		 System.out.println("Extraction= " + extractionID);
		
	     return 0;
	}
}
