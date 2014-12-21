package mbqc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;	
import java.util.List;

import utils.Avevar;
import utils.ConfigReader;
import utils.StatisticReturnObject;
import utils.TTest;

public class PValuesByExtraction
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, RawDesignMatrixParser> map = RawDesignMatrixParser.getByFullId();
		List<String> bioinformaticsIds = RawDesignMatrixParser.getAllBioinformaticsIDs(map);
		
		//for(String s : bioinformaticsIds)
		//	System.out.println(s);
		
		List<String> taxaHeaders = RawDesignMatrixParser.getTaxaIds();
		
		//for(String s : taxaHeaders)
		//	System.out.println(s);
		
		List<String> mbqcIDs = RawDesignMatrixParser.getAllMBQCIDs(map);
		List<String> wetlabIds = RawDesignMatrixParser.getAllWetlabIDs(map);
		//System.out.println(wetlabIds);
		
		HashMap<String, Double> avgVals = RawDesignMatrixParser.getTaxaAverages(map, taxaHeaders);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getMbqcDir() +
				File.separator + "af_out" + File.separator + "pValuesNAVsNonNA.txt")));
		
		writer.write("bioinformaticsLab\tsequencingLab\ttaxa\tsampleSize\tpValue\tavgTaxa\n");
		
		for(String bio : bioinformaticsIds)
			for( String wet : wetlabIds)
				for(String taxa : taxaHeaders)
				{
					System.out.println(bio + "\t" + wet + "\t" + taxa );
					writer.write(bio + "\t" + wet + "\t" + taxa );
					int taxaID = RawDesignMatrixParser.getTaxaID(taxaHeaders,taxa );
					Holder h= getPValueForNAVsOther(map, mbqcIDs, wet, bio, taxaID);
					
					writer.write("\t" + h.sampleSize + "\t");
					
					if( h.pairedResults != null)
						writer.write(h.pairedResults.getPValue() + "\t");
					else
						writer.write("\t");
					
					writer.write(avgVals.get(taxa) + "\n");
				}
		
		writer.flush();  writer.close();

	}
	
	private static class Holder
	{
		StatisticReturnObject pairedResults=null;
		int sampleSize=0;
	}
	
	/*
	 * Returns the most common extraction id (the one in the most # of distinct mbqcIDs) that is not NA
	 */
	static String getMostCommonExtraction( HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap) throws Exception
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
	
	static Double getAverageForExtractionID(List<RawDesignMatrixParser> list, String extractionID,
			int taxaId)
	{
		List<Double> vals= new ArrayList<Double>();
		
		for( RawDesignMatrixParser rdmp : list)
		{
			//System.out.println(  rdmp.getExtractionWetlab() + " " + extractionID ); 
			if( rdmp.getExtractionWetlab().equals(extractionID))
			{
				List<Double> innerList= rdmp.getTaxaVals();
				
				if( innerList != null)
				{
					Double aVal = innerList.get(taxaId);
					
					if( aVal != null)
					{
						vals.add(aVal+ 0.00001);
					}
				}
				
			}
		
		}
			
		if( vals.size() ==0)
			return null;
		
		Avevar av = new Avevar(vals);
		return av.getAve();
	}
	
	/*
	 * For each sample, return a p-value for the null hypothesis that the distribution of NA
	 * is the same as the extraction protocol with the most total samples for that wetlabID 
	 * and drylabID
	 * 
	 * This is accomplished by paired test for samples with at least one NA and one other extraction.
	 * An average is taken across multiple samples for NA and other.
	 */
	private static Holder getPValueForNAVsOther( HashMap<String, RawDesignMatrixParser> map,
									List<String>  mbqcIDs,
									String wetlabID,
									String drylabID,
									int taxaID) throws Exception
	{
		 HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap = 
				RawDesignMatrixParser.pivotBySampleID(map, drylabID, wetlabID);
		
		 String extractionID = getMostCommonExtraction(mbqcIDMap);
		 
		 List<Double> extractionList = new ArrayList<Double>();
		 List<Double> naList = new ArrayList<Double>();
		 for( String s : mbqcIDMap.keySet() )
		 {
			 List<RawDesignMatrixParser> innerList = mbqcIDMap.get(s);
			 
			 Double extractionVal =  getAverageForExtractionID(innerList, extractionID, taxaID);
			 Double naVal = getAverageForExtractionID(innerList, "NA", taxaID);
			 
			 if(extractionVal != null && naVal != null)
			 {
				extractionList.add(extractionVal); 
				naList.add(naVal);
			 }
		 }
	
		 Holder h = new Holder();
		 h.sampleSize = extractionList.size();
		 
		 try
		 {
			h.pairedResults = TTest.pairedTTest(extractionList, naList);
		 }
		 catch(Exception ex)
		 {
			 
		 }
		 
		 return h;
	}
}
