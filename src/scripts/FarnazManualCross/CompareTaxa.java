package scripts.FarnazManualCross;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import utils.ConfigReader;

public class CompareTaxa
{
	
	public static void main(String[] args) throws Exception
	{
		
		HashMap<String, SurgeryMetadataInterface> bs_metaMap = ParseBSMeta.parseMetaFile();
		HashMap<String, SurgeryMetadataInterface> palleja_metaMap = ParsePallejaMeta.parseMetaFile();
		
		File dataFileBS = new File(ConfigReader.getFarnazCrossDirBS() + File.separator+ 
					"bs_taxa_transposed.txt");
		
		File dataFilePalleja = new File(ConfigReader.getFarnazCrossDirBS() + File.separator+ 
				"palleja_Taxa_transformed.txt");

		HashMap<String, Double> bs_pValues0_1 = CompareWGS.getPValuesWithin(bs_metaMap, dataFileBS, 0, 1);
		HashMap<String, Double> bs_pValues0_6 = CompareWGS.getPValuesWithin(bs_metaMap, dataFileBS, 0, 6);
		HashMap<String, Double> bs_pValues1_6 = CompareWGS.getPValuesWithin(bs_metaMap, dataFileBS, 1, 6);
		
		HashMap<String, Double> palleja_pValues0_3 = CompareWGS.getPValuesWithin( palleja_metaMap, dataFilePalleja, 0, 3);
		HashMap<String, Double> palleja_pValues0_12 = CompareWGS.getPValuesWithin( palleja_metaMap, dataFilePalleja, 0, 12);
		HashMap<String, Double> palleja_pValues3_12 = CompareWGS.getPValuesWithin( palleja_metaMap, dataFilePalleja, 3, 12);
		
		
		List<String> labels = new ArrayList<String>();
		labels.add("bs_0_vs_1");
		labels.add("bs_0_vs_6");
		labels.add("bs_1_vs_6");
		labels.add("palleja_0_vs_3");
		labels.add("palleja_0_vs_12");
		labels.add("palleja_3_vs_12");
		
		
		List< HashMap<String, Double> > pValues = new ArrayList<HashMap<String,Double>>();
		pValues.add(bs_pValues0_1);
		pValues.add(bs_pValues0_6);
		pValues.add(bs_pValues1_6);
		
		pValues.add(palleja_pValues0_3);
		pValues.add(palleja_pValues0_12);
		pValues.add(palleja_pValues3_12);
		
		CompareWGS.writePValues(labels, pValues, "pValues_taxa_BS_vs_Palleja.txt");
	
	}
}
