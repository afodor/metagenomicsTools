package scripts.ting5Groups.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

public class CompareMetadataAndLogs
{
	private static List<String> getRowNames() throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTingDir() 
				+ File.separator + "5Groups" + File.separator + 
				"20170312_Casp11_DSS_5groups_16S_DeNovo_L6.txt")));
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			if( s.trim().length() == 0 )
				list.add("");
			else
				list.add(s.split("\t")[0].replaceAll("\"", ""));
		}
		
		return list;
	}
	
	private static HashMap<Integer, List<String>> getParsedMap() throws Exception
	{
		HashMap<Integer, List<String>> map = new HashMap<Integer,List<String>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTingDir() 
				+ File.separator + "5Groups" + File.separator + 
				"20170312_Casp11_DSS_5groups_16S_DeNovo_L6.txt")));
		
		
		String[] topLine = reader.readLine().split("\t");
		
		for( int x=1; x < topLine.length; x++)
		{
			int val = Integer.parseInt(topLine[x]);
			
			if( val != x)
				throw new Exception("No");
			
			map.put(val, new ArrayList<String>());
		}
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			for( int x=1; x < splits.length; x++)
				map.get(x).add(splits[x]);
		}

		reader.close();
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, List<String>> map = getParsedMap();
		checkAMetadataColumn(map, 1, 2);
		checkAMetadataColumn(map, 2, 3);
		checkAMetadataColumn(map, 3, 5);
		
		checkAMetadataColumn(map, 4, 6);
		checkAMetadataColumn(map, 5, 7);
		checkAMetadataColumn(map, 6, 8);
		
		checkAMetadataColumn(map, 9, 12);
		
		double sequenceDepth = getAverageSequenceDepth(map);
		System.out.println(sequenceDepth);
		

		List<String> rowNames = getRowNames();
		
		
		for(int x=11; x < 184; x++)
		{
			checkLogs(map, sequenceDepth, x, rowNames);
		}
		
		//List<String> list= map.get(1);
		//for(int x=0; x < list.size(); x++)
			//System.out.println(x + " " + rowNames.get(x) + " " +  list.get(x));
		
		System.out.println("Passed");
	}
	
	
	private static void checkLogs(HashMap<Integer, List<String>> map, double depth, int column,
			List<String> rowNames)
		throws Exception
	{
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTingDir() 
				+ File.separator + "5Groups" + File.separator + 
				"otuAsColumnsLogNorm_5GroupsPlusMetadata.txt"	)));
	
		List<Double> loggedList = new ArrayList<Double>();
		
		for(Integer i : map.keySet())
		{
			List<String> innerList = map.get(i);
			
			Integer val = Integer.parseInt(innerList.get(column+3).replace(",", "").replaceAll("\"", ""));
			double sum = Double.parseDouble(innerList.get(10).replace(",", "").replaceAll("\"", ""));
			
			double normVal =Math.log10(val/sum * depth + 1); 
			//System.out.println(val +" " + sum + " " + depth + " " + normVal);
			
			loggedList.add(normVal);
		}
		
		String topLine = reader.readLine();
		String aTaxa =topLine.split("\t")[column];
		String anotherTaxa = rowNames.get(column + 7);
		
		System.out.println(aTaxa + " " + anotherTaxa);
		
		if( !aTaxa.equals(anotherTaxa))
			throw new Exception("No");
		
		int index=0;
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			double valToCheck = Double.parseDouble(s.split("\t")[column]);
			
			//System.out.println(valToCheck + " " + loggedList.get(index));
			
			if( Math.abs(valToCheck- loggedList.get(index)) > 0.0001)
				throw new Exception("No");
			
			index++;
		}
		
		reader.close();
	}
	
	public static double getAverageSequenceDepth( HashMap<Integer, List<String>> map) throws Exception
	{
		double sum = 0;
		int n=0;
		
		for( Integer i : map.keySet())
		{
			//System.out.println(map.get(i).get(13));
			sum += Double.parseDouble(
					map.get(i).get(10).replace(".0", "").replace(",", "").replaceAll("\"", ""));
			n++;
		}
		
		
		
		return sum / n;
	}
		
	private static void checkAMetadataColumn(HashMap<Integer, List<String>> map,
						int metaColumnToCheck, int originalRow) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTingDir() 
				+ File.separator + "5Groups" + File.separator + 
				"otuAsColumnsLogNorm_5GroupsPlusMetadata.txt"	)));
		
		reader.readLine();
		
		for( String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			List<String> innerList = map.get(Integer.parseInt(splits[0]));
		
			String aVal = splits[metaColumnToCheck].replace(".0", "").replace(",", "").replaceAll("\"", "");
			String anotherVal = innerList.get(originalRow-2).replace(".0", "").replaceAll("\"","").replace(",", "");
			
			if( ! aVal.equals(anotherVal))
				throw new Exception("No match " + aVal + " " + anotherVal );
			
			//System.out.println(aVal + " " + anotherVal);
		}
		
		reader.close();
	}
}
