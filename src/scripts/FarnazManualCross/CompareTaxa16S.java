package scripts.FarnazManualCross;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import utils.ConfigReader;

public class CompareTaxa16S
{
	
	public static void main(String[] args) throws Exception
	{
		
		HashMap<String, SurgeryMetadataInterface> bs_metaMap = ParseBS16S.parseMetaFile();
		
		HashMap<String, SurgeryMetadataInterface> assal_metaMap = ParseAssalMeta.parseMetaFile();
		
		File dataFileBS =  new File(ConfigReader.getFarnazCrossDirBS() + File.separator+ 
						"bs_16S_transposed.txt");
				
		
		File dataFileAssal = new File(ConfigReader.getFarnazCrossDirBS() + File.separator+ 
				"assal_16S_transposed.txt");
		
		HashMap<String, Double> bs_pValues0_1 = CompareWGS.getPValuesWithin(bs_metaMap, dataFileBS, 0, 1);
		HashMap<String, Double> bs_pValues0_6 = CompareWGS.getPValuesWithin(bs_metaMap, dataFileBS, 0, 6);
		HashMap<String, Double> bs_pValues1_6 = CompareWGS.getPValuesWithin(bs_metaMap, dataFileBS, 1, 6);
		
		HashMap<String, Double> assal_pValues0_3 = CompareWGS.getPValuesWithin(assal_metaMap, dataFileAssal, 0, 3);
		HashMap<String, Double> assal_pValues0_12 = CompareWGS.getPValuesWithin(assal_metaMap, dataFileAssal, 0, 12);
		HashMap<String, Double> assal_pValues3_12 = CompareWGS.getPValuesWithin(assal_metaMap, dataFileAssal, 3, 12);
		
		
		List<String> labels = new ArrayList<String>();
		labels.add("bs_0_vs_1");
		labels.add("bs_0_vs_6");
		labels.add("bs_1_vs_6");
		labels.add("assal_0_vs_3");
		labels.add("assal_0_vs_12");
		labels.add("assal_3_vs_12");
		
		
		List< HashMap<String, Double> > pValues = new ArrayList<HashMap<String,Double>>();
		pValues.add(bs_pValues0_1);
		pValues.add(bs_pValues0_6);
		pValues.add(bs_pValues1_6);
		pValues.add(assal_pValues0_3);
		pValues.add(assal_pValues0_12);
		pValues.add(assal_pValues3_12);
		
		
		CompareWGS.writePValues(labels, pValues, "pValues_16S_BS_Vs_Assal.txt");
	
	}
}
