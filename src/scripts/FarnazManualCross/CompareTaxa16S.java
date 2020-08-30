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
		
		File dataFileBS = new File(ConfigReader.getFarnazCrossDirBS() + File.separator+ 
				"bs_taxa_transposed.txt");
		
		HashMap<String, Double> bs_pValues0_1 = CompareWGS.getPValuesWithin(bs_metaMap, dataFileBS, 0, 1);
		HashMap<String, Double> bs_pValues0_6 = CompareWGS.getPValuesWithin(bs_metaMap, dataFileBS, 0, 6);
		HashMap<String, Double> bs_pValues1_6 = CompareWGS.getPValuesWithin(bs_metaMap, dataFileBS, 1, 6);
		
		List<String> labels = new ArrayList<String>();
		labels.add("bs_0_vs_1");
		labels.add("bs_0_vs_6");
		labels.add("bs_1_vs_6");
		
		
		List< HashMap<String, Double> > pValues = new ArrayList<HashMap<String,Double>>();
		pValues.add(bs_pValues0_1);
		pValues.add(bs_pValues0_6);
		pValues.add(bs_pValues1_6);
		
		
		CompareWGS.writePValues(labels, pValues, "pValues_16S_BS.txt");
	
	}
}
