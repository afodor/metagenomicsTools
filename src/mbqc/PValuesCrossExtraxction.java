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
import utils.TTest;
import mbqc.PValuesByExtraction.Holder;

public class PValuesCrossExtraxction
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, RawDesignMatrixParser> map = RawDesignMatrixParser.getByFullId();
		List<String> bioinformaticsIds = RawDesignMatrixParser.getAllBioinformaticsIDs(map);
		
		List<String> taxaHeaders = RawDesignMatrixParser.getTaxaIds();
				
		List<String> mbqcIDs = RawDesignMatrixParser.getAllMBQCIDs(map);
		List<String> wetlabIds = RawDesignMatrixParser.getAllWetlabIDs(map);
		
		HashMap<String, Double> avgVals = RawDesignMatrixParser.getTaxaAverages(map, taxaHeaders);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getMbqcDir() +
				File.separator + "af_out" + File.separator + "pValuesAcrossSamples.txt")));
		
		writer.write("bioinformaticsLab\tsequencingPlusExtraction1\tsequencingPlusExtraction2\t" + 
						"comparisonString\tnaFor1\tnaFor2\t" + 
							"naCategory\ttaxa\tsampleSize\tpValue\tmeanDifference\tfoldChange\tavgTaxa\n");
		
	
		HashSet<String> completed = new HashSet<String>();
		for( int x=0; x < wetlabIds.size(); x++)
			for (int y=x; y < wetlabIds.size(); y++)
			{
				if( ! wetlabIds.get(x).equals("jravel") && 
						! wetlabIds.get(y).equals("jravel") &&
							! wetlabIds.get(x).equals("dgevers") &&
								! wetlabIds.get(y).equals("dgevers"))
				{
					System.out.println( wetlabIds.get(x) + " "+ wetlabIds.get(y) );
					for(String bio : bioinformaticsIds)
						for(String taxa : taxaHeaders)
							if( avgVals.get(taxa) > 0.01)
							{
								 HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap1 = 
											RawDesignMatrixParser.pivotBySampleID(map, bio, wetlabIds.get(x));
									
								HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap2 = 
											RawDesignMatrixParser.pivotBySampleID(map, bio, wetlabIds.get(y));
								
								
								List<String> list1 = PValuesByExtraction.getAllExtractionNotNA(mbqcIDMap1);
								list1.add("NA");
								
								List<String> list2 = PValuesByExtraction.getAllExtractionNotNA(mbqcIDMap2);
								list2.add("NA");
								
								for(String extraction1: list1)
									for(String extraction2: list2)
										if( x != y || !extraction1.equals(extraction2) )
									{
										String comparisonID = getComparisonID(wetlabIds.get(x) + "_" + extraction1 
												,wetlabIds.get(y) + "_" +  extraction2 );
										
										if( ! completed.contains(comparisonID))
										{
											completed.add(comparisonID);
											
											writer.write(bio + "\t" + wetlabIds.get(x) + "_" + extraction1 + "\t" +
													wetlabIds.get(y) + "_" +  extraction2 +  "\t" +
													comparisonID + "\t"
											+ extraction1.equals("NA") + "\t" + extraction2.equals("NA")+ "\t"
													+ getCategory(extraction1, extraction2) + "\t"+ taxa );
											int taxaID = RawDesignMatrixParser.getTaxaID(taxaHeaders,taxa );
											Holder h= 
													getPValueAcrossSamples(map, 
															mbqcIDs, wetlabIds.get(x), wetlabIds.get(y), 
																bio, taxaID, extraction1, extraction2,
																mbqcIDMap1, mbqcIDMap2);
													
											writer.write("\t" + h.sampleSize + "\t");
												
											if( h.pairedResults != null)
											{
												writer.write(h.pairedResults.getPValue() + "\t" + h.meanDifference + "\t"
															+ h.foldChange + "\t");
											}
											else
											{
												writer.write("\t\t\t");
											}
											
												
												writer.write(avgVals.get(taxa) + "\n");
												
											writer.flush();
										}
									}
					}

				}
			}
		
				
		writer.flush();  writer.close();

	}
	
	static String getComparisonID(String s1, String s2)
	{
		if ( s1.compareTo(s2) >0 )
		{
			String temp =s1;
			s1 = s2;
			s2 = temp;
		}
		
		return s1 + "_" + s2;
	}
	
	
	private static String getCategory(String b1, String b2)
		throws Exception
	{
		if( b1.equals("NA") && b2.equals("NA"))
			return "NA_for_both";
		
		if( b1.equals("NA") && ! b2.equals("NA"))
			return "NA_for_first";
		
		if( ! b1.equals("NA") && b2.equals("NA"))
			return "NA_for_second";
					
		if( !b1.equals("NA") && !b2.equals("NA"))
			return "extraction_for_both";
		
		throw new Exception("Logic error");
	}
	
	private static Holder getPValueAcrossSamples( 
									HashMap<String, RawDesignMatrixParser> map,
									List<String>  mbqcIDs,
									String wetlabID1,
									String wetlabID2,
									String drylabID,
									int taxaID, 
									String extractionID1,
									String extractionID2,
									HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap1,
									HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap2) 
												throws Exception
	{
		 
		 List<Double> val1List= new ArrayList<Double>();
		 List<Double> val2List = new ArrayList<Double>();
		 
		 HashSet<String> mbqcIDsToSearch = new HashSet<String>();
		 mbqcIDsToSearch.addAll(mbqcIDMap1.keySet());
		 mbqcIDsToSearch.retainAll(mbqcIDMap2.keySet());
		 
		 for( String s : mbqcIDsToSearch)
		 {
			 List<RawDesignMatrixParser> innerList1 = mbqcIDMap1.get(s);
			 List<RawDesignMatrixParser> innerList2 =mbqcIDMap2.get(s);
			 
			 Double val1 =  
					PValuesByExtraction.getAverageForExtractionID(innerList1, extractionID1,taxaID);
			 Double val2 = 
					 PValuesByExtraction.getAverageForExtractionID(innerList2, extractionID2,taxaID);
			 
			 if(val1 != null && val2 != null)
			 {
				val1List.add(val1); 
				val2List.add(val2);
			 }
		 }
	
		 Holder h = new Holder();
		 h.sampleSize = val1List.size();
		 
		 try
		 {
			h.pairedResults = TTest.pairedTTest(val1List, val2List);
			h.meanDifference = new Avevar(val1List).getAve() -new Avevar(val2List).getAve();
			
			h.foldChange = (new Avevar(val1List).getAve() +0.00001) / 
					(new Avevar(val2List).getAve() +0.00001);
	
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
