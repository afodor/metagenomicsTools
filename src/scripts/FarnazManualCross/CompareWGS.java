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
		
		HashMap<String, SurgeryMetadataInterface> bs_metaMap = ParseBSInteface.parseMetaFile();
		
		File dataFile = new File(ConfigReader.getFarnazCrossDirBS() + File.separator+ 
					"humanN2_pathabundance_cpm.tsv");
		
		HashMap<String, Double> pValues0_1 = getPValuesWithin(bs_metaMap, dataFile, 0, 1);
		HashMap<String, Double> pValues0_6 = getPValuesWithin(bs_metaMap, dataFile, 0, 6);
		HashMap<String, Double> pValues1_6 = getPValuesWithin(bs_metaMap, dataFile, 1, 6);
		
		
		List<String> labels = new ArrayList<String>();
		labels.add("bs_0_vs_1");
		labels.add("bs_0_vs_6");
		labels.add("bs_1_vs_6");
		
		List< HashMap<String, Double> > pValues = new ArrayList<HashMap<String,Double>>();
		pValues.add(pValues0_1);
		pValues.add(pValues0_6);
		pValues.add(pValues1_6);
		
		writePValues(labels, pValues);
	
	}
	
	private static void writePValues( List<String> labels, List< HashMap<String, Double> > pValues) 
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigReader.getFarnazCrossDirBS() + 
				File.separator + "pivotedPValues.txt"));
		
		writer.write("id");
		
		for( int x=0; x < labels.size(); x++)
			writer.write("\t" + labels.get(x));
		
		writer.write("\n");
		
		HashSet<String> keys = new HashSet<String>();
		
		for( HashMap<String, Double> map : pValues )
			keys.addAll(map.keySet());
		
		for(String s : keys)
		{
			writer.write(s);
			
			for( HashMap<String, Double> map : pValues )
			{
				Double val = map.get(s);
				
				if( val == null)
					writer.write("\tNA");
				else
					writer.write("\t" + val);
			}
			
			writer.write("\n");
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
			System.out.println("Adding " + splits[0]);
			
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
				System.out.println("TTest failed " + av1.getAve() + " " + av2.getAve());
			}
			
			map.put(splits[0], pValue);
			
		}
		
		
		
		
		reader.close();
		
		return map;
	}
}
