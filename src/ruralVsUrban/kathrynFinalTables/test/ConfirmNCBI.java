package ruralVsUrban.kathrynFinalTables.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;


import utils.ConfigReader;

public class ConfirmNCBI
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> expectedMap = getExpectedBestVal();
		System.out.println("Got " + expectedMap.size());
		HashMap<String, String> reparsedMap = getFullTopHit();
		System.out.println("Got " + reparsedMap.size());
		
		for(String s : expectedMap.keySet())
			if( ! reparsedMap.get(s).equals(expectedMap.get(s)))
				throw new Exception("Failed");
		
		System.out.println("Map comparison passed");
		
		checkColumns(8, 5);
	}
	
	private static class Holder
	{
		String target;
		double bitScore;
	}
	
	private static void checkColumns(int kwColumns, int outputColumn) throws Exception
	{
		HashMap<String, Double> kwVals = new HashMap<String, Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + "otuModel_pValues_otu.txt"
				)));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( splits.length != 17)
				throw new Exception("No");
			
			if( kwVals.containsKey(splits[0].replaceAll("\"", "").replace("X", "Consensus")))
				throw new Exception("Fail");
			
			if( splits[0].startsWith("\"X"))
			kwVals.put(splits[0].replaceAll("\"", "").replace("X", "Consensus"),
					Double.parseDouble(splits[kwColumns]));
		}
		
		reader.close();
		
		HashMap<String, Double> outputVals = new HashMap<String, Double>();
		
		reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + 
					"ncbiRuralVsUrban.txt"
				)));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( splits.length != 8)
				throw new Exception("No");
			
			if( Boolean.parseBoolean(splits[7]) && 
					Double.parseDouble(splits[3]) > Double.parseDouble(splits[2]))
				throw new Exception("Fail");
			

			if( !Boolean.parseBoolean(splits[7]) && 
					Double.parseDouble(splits[2]) > Double.parseDouble(splits[3]))
				throw new Exception("Fail");
			
			if(outputVals.containsKey(splits[0]))
				throw new Exception("Fail");
			
			outputVals.put(splits[0],Double.parseDouble(splits[outputColumn]));
		}
		System.out.println("got " + kwVals.size() + " " + outputVals.size());
		
		if( kwVals.size() != outputVals.size())
			throw new Exception("Fail");
		
		reader.close();
		
		if( ! kwVals.keySet().equals(outputVals.keySet()))
			throw new Exception("Fail");
		
		for(String s : kwVals.keySet())
			if( Math.abs(kwVals.get(s)- outputVals.get(s))> 0.001)
				throw new Exception("Fail");
		
		System.out.println("Pass column comparison");
	}
	
	private static HashMap<String, String> getFullTopHit() throws Exception
	{
		HashMap<String, Holder> holderMap = new HashMap<String,Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigReader.getChinaDir() + File.separator + 
		"Kathryn_update_NCBI_MostWanted" + File.separator + 
			"allForwardToNCBI_Oct15.txt"));
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 12)
				throw new Exception("No");
			
			Holder h = holderMap.get(splits[0]);
			
			if( h == null || Double.parseDouble(splits[11].trim()) > h.bitScore)
			{
				h = new Holder();
				h.target = splits[1];
				h.bitScore = Double.parseDouble(splits[11].trim());
				holderMap.put( splits[0], h );
			}
		}
		
		reader.close();
		
		HashMap<String, String> map = new HashMap<String,String>();
		
		for(String s : holderMap.keySet())
			map.put(s, holderMap.get(s).target);
			
		
		return map;
	}
	
	private static HashMap<String, String> getExpectedBestVal() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + 
						"ncbiRuralVsUrban.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], splits[1]);
		}
		
		reader.close();
		return map;
	}
}
