package mbqc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import utils.StatisticReturnObject;
import utils.TTest;

public class PValuesAcrossBioinformaticsPipelines
{
	public static void main(String[] args) throws Exception
	{
		
	}

	private static class Holder
	{
		StatisticReturnObject pairedResults=null;
		int sampleSize=0;
	}

	private static Holder getPValueAcrossSamples( 
			HashMap<String, RawDesignMatrixParser> map,
			List<String>  mbqcIDs,
			String wetlabID,
			String drylabID1,
			String drylabID2,
			int taxaID, 
			Boolean useNA) throws Exception
	{
		/*
		HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap1 = 
				RawDesignMatrixParser.pivotBySampleID(map, drylabID1, wetlabID);

		HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap2 = 
				RawDesignMatrixParser.pivotBySampleID(map, drylabID2, wetlabID);

		String extractionID = "NA";
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

}*/

return null;
}
	
}
