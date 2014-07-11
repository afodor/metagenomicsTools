package bigDataScalingFactors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddDiseaseExtentToRiskSpreadsheet
{
	private static HashMap<String, String> getDiseaseExtentMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
					"July_StoolRemoved" + File.separator + "study_1939_mapping_file.txt"));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("Duplicate");
			
			map.put(splits[0], splits[47]);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getDiseaseExtentMap();
		
		for(String s : map.values())
			System.out.println(s);
		
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getBigDataScalingFactorsDir() 
				+ File.separator + "July_StoolRemoved" 
				+ File.separator + "risk_raw_countsTaxaAsColumnsAllButStool.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getBigDataScalingFactorsDir() 
				+ File.separator + "July_StoolRemoved" 
				+ File.separator + "risk_raw_countsTaxaAsColumnsAllButStoolWithExtent.txt"
				)));
		
		writer.write("sample\textent");
		
		for( String s : wrapper.getOtuNames())
			writer.write("\t" + s);
		
		writer.write("\n");
		
		
		for(int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			String sampleName = wrapper.getSampleNames().get(x);
			writer.write(sampleName);
			
			if( map.get(sampleName) == null)
				throw new Exception("No");
			
			String val = map.get(sampleName).toLowerCase();
			writer.write( "\t" + (val.equals("no") || val.equals("control") ? "control" : "case") );
			
			for(int y=0; y < wrapper.getOtuNames().size(); y++)
			{
				writer.write("\t" + wrapper.getDataPointsUnnormalized().get(x).get(y));
			}
			
			writer.write("\n");
		}

		writer.flush();  writer.close();
	}
}
