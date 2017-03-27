package scripts.ting5Groups.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_ADDPeer;

import utils.ConfigReader;

public class CompareMetadata
{
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
			
			System.out.println(aVal + " " + anotherVal);
		}
		
		reader.close();
	}
}
