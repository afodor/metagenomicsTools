package mbqc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;	
import java.util.List;

import utils.Avevar;
import utils.ConfigReader;
import utils.StatisticReturnObject;
import utils.TTest;

public class PValuesByExtraction
{
	private static HashMap<String,String> getManualKitManufacturer()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("agoodman", "Omega biotek");
		map.put( "dlittman", "MO-BIO");
		map.put("dmills", "Zymo Research");
		map.put("ggloor", "Promega");
		map.put("jpetrosino", "MO-BIO");
		map.put("jravel", "In house");
		map.put("kjones", "unknown");
		map.put("pschloss", "MO-BIO");
		map.put("pturnbaugh", "MO-BIO");
		map.put("rburk", "two_methods_Qiaqen_and_Mo_Bio");
		map.put("rflores", "unknown");
		map.put("rknight", "MO-BIO");
		map.put("oshanks", "unknown");
		map.put("dgevers", "Chemagic DNA");
		
		return map;
	}
	
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
		
		HashMap<String,String> kitMap = getManualKitManufacturer();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getMbqcDir() +
				File.separator + "af_out" + File.separator + "pValuesNAVsNonNA.txt")));
		
		writer.write("bioinformaticsLab\tsequencingLab\textractionProtocol\tkitManufacturer\tseqPlusExtraction\ttaxa\tsampleSize\tpValue\tmeanDifference\tfoldChange\tavgTaxa\n");
		
		
		for(String bio : bioinformaticsIds)
			for( String wet : wetlabIds)
				for(String taxa : taxaHeaders)
					if( avgVals.get(taxa) >= 0.01 )
					{
						HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap = 
								RawDesignMatrixParser.pivotBySampleID(map, bio, wet);
						

						System.out.println(bio + "\t" + wet + "\t" + taxa );
						List<String> extractionProtocols = getAllExtractionNotNA(mbqcIDMap);
						
						for(String extraction : extractionProtocols)
						{
							String kit = kitMap.get(extraction);
							
							if( kit == null)
								throw new Exception("No " + extraction);
							
							writer.write(bio + "\t" + wet + "\t" + extraction +  "\t" + kit + "\t" + wet + "_" + extraction + "\t" + 
											taxa );
							int taxaID = RawDesignMatrixParser.getTaxaID(taxaHeaders,taxa );
							Holder h= getPValueForNAVsOther(map, mbqcIDs, wet, bio, taxaID, extraction,
											mbqcIDMap);
							
							writer.write("\t" + h.sampleSize + "\t");
							
							if( h.pairedResults != null)
								writer.write(h.pairedResults.getPValue() + "\t" + h.meanDifference + "\t" + h.foldChange + "\t");
							else
								writer.write("\t\t\t");
							
							writer.write(avgVals.get(taxa) + "\n");
						}
						
						
					}
		
		writer.flush();  writer.close();

	}
	
	static class Holder
	{
		StatisticReturnObject pairedResults=null;
		int sampleSize=0;
		Double meanDifference=null;
		Double foldChange = null;
	}
	
	static List<String> getAllExtractionNotNA( HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap )
		throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		for(String s : mbqcIDMap.keySet())
		{
			List<RawDesignMatrixParser> innerList = mbqcIDMap.get(s);
			
			for(RawDesignMatrixParser rdmp : innerList)
			{
				if( !rdmp.getExtractionWetlab().equals("NA") )
					set.add(rdmp.getExtractionWetlab());
			}
		}
		
		if( set.size() < 1)
			System.out.println(" WARNING: Could not find extraction lab " );
		
		List<String> list = new ArrayList<String>(set);
		Collections.sort(list);
		return list;
	}
	
	/*
	 * Returns the most common extraction id (the one in the most # of distinct mbqcIDs) that is not NA
	 */
	/*
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
	*/
	
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
						vals.add(aVal);
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
	 * is the same as the extraction protocol 
	 * 
	 * This is accomplished by paired test for samples with at least one NA and one other extraction.
	 * An average is taken across multiple samples for NA and other.
	 */
	private static Holder getPValueForNAVsOther( HashMap<String, RawDesignMatrixParser> map,
									List<String>  mbqcIDs,
									String wetlabID,
									String drylabID,
									int taxaID, 
									String nonNAExtractionProtocol,
									HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap) throws Exception
	{
		 
		 
		 List<Double> extractionList = new ArrayList<Double>();
		 List<Double> naList = new ArrayList<Double>();
		 for( String s : mbqcIDMap.keySet() )
		 {
			 List<RawDesignMatrixParser> innerList = mbqcIDMap.get(s);
			 
			 Double extractionVal =  getAverageForExtractionID(innerList, nonNAExtractionProtocol, taxaID);
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
			h.meanDifference = new Avevar(extractionList).getAve() -new Avevar(naList).getAve();
			
			h.foldChange = (new Avevar(extractionList).getAve() +0.00001) / 
							(new Avevar(naList).getAve() +0.00001);
			
			if( h.foldChange< 1)
			{
				h.foldChange= - Math.log( 1/h.foldChange)/Math.log(2);
			}
			else
			{
				h.foldChange = Math.log(h.foldChange) / Math.log(2);
			}

		 }
		 catch(Exception ex)
		 {
			 
		 }
		 
		 return h;
	}
}
