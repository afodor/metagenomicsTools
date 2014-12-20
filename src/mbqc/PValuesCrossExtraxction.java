package mbqc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import utils.ConfigReader;
import utils.StatisticReturnObject;
import utils.TTest;

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
		
		writer.write("bioinformaticsLab\tsequencingLab1\tsequencingLab2\tnaFor1\tnaFor2\t" + 
							"taxa\tsampleSize\tpValue\tavgTaxa\n");
		
		Boolean[] bArray = { true, false}; 
		
		for( int x=0; x < wetlabIds.size()-1; x++)
			for (int y=x+1; y < wetlabIds.size(); y++)
			{
				if( ! wetlabIds.get(x).equals("jravel") && 
						! wetlabIds.get(y).equals("jravel") &&
							! wetlabIds.get(x).equals("degevers") &&
								! wetlabIds.get(y).equals("dgevers"))
				{
					System.out.println( wetlabIds.get(x) + " "+ wetlabIds.get(y) );
					for(String bio : bioinformaticsIds)
						for(String taxa : taxaHeaders)
							for( Boolean b1 : bArray)
								for(Boolean b2 : bArray)
								{
									writer.write(bio + "\t" + wetlabIds.get(x) + "\t" +
											wetlabIds.get(y) + "\t" + 
									b1 + "\t" + b2 + "\t"+ taxa );
									int taxaID = RawDesignMatrixParser.getTaxaID(taxaHeaders,taxa );
									Holder h= 
											getPValueAcrossSamples(map, 
													mbqcIDs, wetlabIds.get(x), wetlabIds.get(y), 
														bio, taxaID, b1, b2);
											
									writer.write("\t" + h.sampleSize + "\t");
										
									if( h.pairedResults != null)
										writer.write(h.pairedResults.getPValue() + "\t");
									else
										writer.write("\t");
										
										writer.write(avgVals.get(taxa) + "\n");
										
									writer.flush();
					}

				}
			}
		
				
		writer.flush();  writer.close();

	}
	
	

	private static class Holder
	{
		StatisticReturnObject pairedResults=null;
		int sampleSize=0;
	}
	
	
		private static Holder getPValueAcrossSamples( 
									HashMap<String, RawDesignMatrixParser> map,
									List<String>  mbqcIDs,
									String wetlabID1,
									String wetlabID2,
									String drylabID,
									int taxaID, 
									Boolean useNAFor1,
									Boolean useNAFor2) throws Exception
	{
		 HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap1 = 
				RawDesignMatrixParser.pivotBySampleID(map, drylabID, wetlabID1);
		
		 HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap2 = 
				RawDesignMatrixParser.pivotBySampleID(map, drylabID, wetlabID2);
		 
		 String extractionID1 = "NA";
		 String extractionID2 = "NA";
		 
		 if ( ! useNAFor1)
		 {
			 extractionID1 = PValuesByExtraction.getMostCommonExtraction(mbqcIDMap1);
		 }
		 
		 if( ! useNAFor2)
		 {
			 extractionID2 = PValuesByExtraction.getMostCommonExtraction(mbqcIDMap2);
		 }
		 
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
		 }
		 catch(Exception ex)
		 {
			 
		 }
		 
		 return h;
	}
}
