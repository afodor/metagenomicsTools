package scripts.humanIowa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.OtuWrapper;
import utils.ConfigReader;
import utils.TabReader;

public class TestByRemerge
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getHumanIowa() + File.separator + 
				"reparseAug6_2018" + File.separator + "genus.tsv");
		
		File metaFile = new File(ConfigReader.getHumanIowa() + File.separator + 
				"reparseAug6_2018" + File.separator + "AAA_Shook_Anthony_Fodor_4.10.18.csv");
		
		HashMap<String, HashMap<String,Double>> preBMIMap = getPreBMIMap(metaFile);
		
		for(String s : preBMIMap.keySet())
		{
			System.out.println(s + " " + preBMIMap.get(s));
			
		}
			
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getHumanIowa() + File.separator + 
				"reparseAug6_2018" + File.separator + "AfMergedWithMeta.txxt")));
		
		writer.write("id\tsubject\tgroup\tbmi");
		
		for(String s : wrapper.getOtuNames())
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(String id : wrapper.getSampleNames())
		{
			int sampleNum = wrapper.getIndexForSampleName(id);
			StringTokenizer sToken = new StringTokenizer(id, "_");
			String subjectID = sToken.nextToken();
			String group = sToken.nextToken();
			writer.write(id + "\t" + subjectID + "\t" + group);
			
			HashMap<String, Double> innerMap = preBMIMap.get(subjectID);
			
			if(innerMap == null)
				throw new Exception("No " + subjectID);
			
			Double val = innerMap.get(group);
			
			/*
			String out= "NA";
			
			if( val != null)
				out = val.toString();
			*/
			
			writer.write("\t" + val);
			
			List<Double> dataList  = wrapper.getDataPointsNormalizedThenLogged().get(sampleNum);
			
			for(Double d : dataList)
				writer.write("\t"  + d);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
	}
	
	
	//outer key is going to be a subjectid (like 1002); inner key is one of "Act", "Con" or "Sed" 
	private static HashMap<String, HashMap<String,Double>> getPreBMIMap(File f) throws Exception
	{
		HashMap<String, HashMap<String,Double>> map = new HashMap<>();
		int controlIndex= getColumnIndex(f, "control_pre_BMI");
		int sedIndex = getColumnIndex(f, "sed_pre_BMI");
		int actIndex = getColumnIndex(f, "act_pre_BMI");
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split(",");
			String id = splits[0];
			
			if( id == null)
				throw new Exception("No");
			
			if( map.containsKey(id))
				throw new Exception("No");
			
			HashMap<String,Double> innerMap =new HashMap<>();
			map.put(id, innerMap);
			
			innerMap.put("Act", getNumOrNull(s,actIndex));
			innerMap.put("Con", getNumOrNull(s,controlIndex));
			innerMap.put("Sed", getNumOrNull(s,sedIndex));
		}
				
		reader.close();
		
		return map;
	}
	
	private static Double getNumOrNull(String s, int index)
	{
		s =s.replaceAll(",", "\t");
		
		TabReader tReader = new TabReader(s);
		
		for(int x=0; x < index; x++)
			tReader.nextToken();
		
		String returnS = tReader.nextToken().trim();
		
		if( returnS.length() == 0 )
			return null;
		
		return Double.parseDouble(returnS);
	}
	
	private static int getColumnIndex(File f, String query) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		String firstLine = reader.readLine();
		String[] splits = firstLine.split(",");
		//System.out.println(firstLine);
		
		for( int x=0; x < splits.length; x++)
			if( query.equals(splits[x]))
			{
				
			//	System.out.println(splits[x]);
				reader.close();
				return x;
			}
				
		throw new Exception("No " + query);
		
	}
	
}
