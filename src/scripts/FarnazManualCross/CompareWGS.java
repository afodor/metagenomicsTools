package scripts.FarnazManualCross;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class CompareWGS
{
	
	public static void main(String[] args) throws Exception
	{
		
		HashMap<String, SurgeryMetadataInterface> bs_metaMap = ParseBSInteface.parseMetaFile();
		
		File dataFile = new File(ConfigReader.getFarnazCrossDirBS() + File.separator+ 
					"humanN2_pathabundance_cpm.tsv");
		
		HashMap<String, Double> pValues = getPValuesWithin(bs_metaMap, dataFile, 0, 1);
	}
	
	public static HashMap<String, Double> getPValuesWithin( 
			HashMap<String, SurgeryMetadataInterface> metaMap, 
				File dataFile, int timepoint1, int timepoint2) throws Exception
	{
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		List< SurgeryMetadataInterface > topLine = new ArrayList<SurgeryMetadataInterface>();
		
		BufferedReader reader = new BufferedReader(new FileReader(dataFile));
		
		String[] firstSplits = reader.readLine().split("\t");
		
		for( int x=1; x < firstSplits.length; x++ )
		{
			StringTokenizer sToken = new StringTokenizer(firstSplits[x], "_");
			String key = sToken.nextToken();
			
			if( key.equals("BS"))
				key = key + "_" + sToken.nextToken();
			
			SurgeryMetadataInterface smi = metaMap.get(key);
			
			if( smi == null)
				System.out.println("Could not find sample " +key);
			 
		}
		
		reader.close();
		
		return map;
	}
}
