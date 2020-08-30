package scripts.FarnazManualCross;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import utils.Avevar;
import utils.ConfigReader;
import utils.TTest;

public class CompareWGS
{
	
	public static void main(String[] args) throws Exception
	{
		
		HashMap<String, SurgeryMetadataInterface> bs_metaMap = ParseBSMeta.parseMetaFile();
		HashMap<String, SurgeryMetadataInterface> palleja_metaMap = ParsePallejaMeta.parseMetaFile();
		
		File dataFileBS = new File(ConfigReader.getFarnazCrossDirBS() + File.separator+ 
					"humanN2_pathabundance_cpm.tsv");
		
		File dataFilePalleja = new File(ConfigReader.getFarnazCrossDirBS() + File.separator+ 
				"humanN2_pathabundance_cpm_Palleja.tsv");
		

		HashMap<String, Double> bs_pValues0_1 = getPValuesWithin(bs_metaMap, dataFileBS, 0, 1);
		HashMap<String, Double> bs_pValues0_6 = getPValuesWithin(bs_metaMap, dataFileBS, 0, 6);
		HashMap<String, Double> bs_pValues1_6 = getPValuesWithin(bs_metaMap, dataFileBS, 1, 6);
		
		HashMap<String, Double> palleja_pValues0_3 = getPValuesWithin( palleja_metaMap, dataFilePalleja, 0, 3);
		HashMap<String, Double> palleja_pValues0_12 = getPValuesWithin( palleja_metaMap, dataFilePalleja, 0, 12);
		HashMap<String, Double> palleja_pValues3_12 = getPValuesWithin( palleja_metaMap, dataFilePalleja, 3, 12);
		
		
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
		
		writePValues(labels, pValues);
	
	}
	
	private static void writePValues( List<String> labels, List< HashMap<String, Double> > pValues) 
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigReader.getFarnazCrossDirBS() + 
				File.separator + "pivotedPValues.txt"));
		
		for( int x=0; x < labels.size(); x++)
			writer.write(labels.get(x) + ( x == labels.size()-1 ? "\n" : "\t"));
		
		
		HashSet<String> keys = new HashSet<String>();
		
		for( HashMap<String, Double> map : pValues )
			keys.addAll(map.keySet());
		
		List<String> keyList = new ArrayList<String>(keys);
		
		for(int x=0; x < keyList.size(); x++)
		{
			boolean first = true;
			for( HashMap<String, Double> map : pValues )
			{
				
				if( ! first)
					writer.write("\t");
				
				Double val = map.get(keyList.get(x));
				
				if( val == null)
					writer.write("NA");
				else
					writer.write( "" + val);
				
				first = false;
			}
			
			writer.write( "\n");
			
		}
		
		writer.flush();  writer.close();
	}
	
	private static class Holder
	{
		Double timepoint1;
		Double timepoint2;
	}
	
	public static HashMap<String, Double> getPValuesWithin( 
			HashMap<String, SurgeryMetadataInterface> metaMap, 
				File dataFile, int timepoint1, int timepoint2) throws Exception
	{
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		List< SurgeryMetadataInterface > topLine = new ArrayList<SurgeryMetadataInterface>();
		
		@SuppressWarnings("resource")
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
			
			topLine.add(smi);
			 
		}
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			HashMap<String, Holder> patientToTimepointDataMap = new HashMap<String, CompareWGS.Holder>();
			
			String[] splits = s.split("\t");
			
			if( splits.length != firstSplits.length)
				throw new Exception("Parsing error");
			
			for( int x=1; x < splits.length; x++)
			{
				SurgeryMetadataInterface smi = topLine.get(x-1);
				
				if( smi != null)
				{
					boolean add = false;
					
					Holder h = patientToTimepointDataMap.get(smi.getParticipantID());
					
					if( h == null)
						h = new Holder();
					
					if(smi.getTimepoint() == timepoint1)
					{
						h.timepoint1 = Double.parseDouble(splits[x]);
						add = true;
					}
					else if (smi.getTimepoint() == timepoint2)
					{
						h.timepoint2 = Double.parseDouble(splits[x]);
						add = true;
					}
					
					if( add)
						patientToTimepointDataMap.put( smi.getParticipantID(), h );
				}
			}
			
			List<Double> list1 = new ArrayList<Double>();
			List<Double> list2 = new ArrayList<Double>();
			
			for( Holder h : patientToTimepointDataMap.values() )
			{
				if( h.timepoint1 != null && h.timepoint2 != null)
				{
					list1.add(h.timepoint1);
					list2.add(h.timepoint2);
				}
			}
			
			double pValue =0;
			Avevar av1 = new Avevar(list1);
			Avevar av2 = new Avevar(list2);
			
			
			try
			{

				pValue = Math.log10(TTest.pairedTTest(list1, list2).getPValue());

				if( av1.getAve() > av2.getAve() )
					pValue = -pValue;

			}
			catch(Exception ex)
			{
				//System.out.println("TTest failed " + av1.getAve() + " " + av2.getAve());
			}
			
			map.put(splits[0], pValue);
			
		}
		
		
		
		
		reader.close();
		
		return map;
	}
}
