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

public class PValuesAcrossBioinformaticsPipelines
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
				File.separator + "af_out" + File.separator + "pValuesAcrossBioinformaticsIDs.txt")));
		
		writer.write("bioinformaticsLab1\tbioinformaticsLab2\tsequencingLab\tusingNAextraction\t" + 
							"taxa\tsampleSize\tpValue\tmeanDifference\tfoldChange\tavgTaxa\n");
		
		Boolean[] boolVals = { true, false };
		
		for( int x=0; x < wetlabIds.size(); x++)
		{
			if( ! wetlabIds.get(x).equals("jravel") && 
						! wetlabIds.get(x).equals("dgevers") )
			{
				System.out.println( wetlabIds.get(x)  );
				for( Boolean b1 : boolVals)
				for(int y= 0; y < bioinformaticsIds.size()-1; y++)
					for( int z=y+1; z < bioinformaticsIds.size(); z++)
						for(String taxa : taxaHeaders)
							if( avgVals.get(taxa) > 0.01)
							{
								String bioinformatics1 = bioinformaticsIds.get(y);
								String bioinformaitcs2 = bioinformaticsIds.get(z);
								
								writer.write(bioinformatics1+ "\t" + bioinformaitcs2 + "\t" + 
								wetlabIds.get(x) + "\t" + b1 + "\t" +  taxa);
								
								int taxaID = RawDesignMatrixParser.getTaxaID(taxaHeaders,taxa );
								Holder h= 
										 getPValueAcrossSamples( 
													 map,
													mbqcIDs,
													wetlabIds.get(x),
													bioinformatics1,
													bioinformaitcs2,
													taxaID, 
													b1);
										
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
		
				
		writer.flush();  writer.close();

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
		HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap1 = 
				RawDesignMatrixParser.pivotBySampleID(map, drylabID1, wetlabID);

		HashMap<String, List<RawDesignMatrixParser>> mbqcIDMap2 = 
				RawDesignMatrixParser.pivotBySampleID(map, drylabID2, wetlabID);

		String extractionID = "NA";
		
		if ( useNA)
		{
			extractionID = PValuesByExtraction.getMostCommonExtraction(mbqcIDMap1);
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
					PValuesByExtraction.getAverageForExtractionID(innerList1, extractionID,taxaID);
			Double val2 = 
					PValuesByExtraction.getAverageForExtractionID(innerList2, extractionID,taxaID);

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
