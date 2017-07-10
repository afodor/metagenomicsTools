package scripts.tingReProcessed.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import utils.ConfigReader;

public class CheckParse
{
	public static void main(String[] args) throws Exception
	{
		confirmPivot(0, 1);
		confirmPivot(1, 2);
		
		for( int x=3; x<=6; x++)
			confirmPivot(x, x);
		
		confirmPivot(7, 8);

		List<String> names =getNames();
		
		List<String> pivotNames = getNamesFromPivot();
		
		for( int x=0; x < pivotNames.size(); x++)
			System.out.println(pivotNames.get(x) + " " + getSkipNum(names, pivotNames.get(x)));
		
		
		System.out.println("pass");
	}
	
	
	
	private static List<String> getNames() throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		BufferedReader reader= new BufferedReader(new FileReader(new File(
				ConfigReader.getTingDir() + File.separator + "may_2017_rerun" + File.separator + 
					"20170512_Casp11_DSS_5groups_16S_DeNovo_NoPhiX_NoPrimerSeq_L6.txt")));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			list.add( s.trim().length() > 0 ? s.split("\t")[0] : "");
			
		}
			
		reader.close();
		
		return list;
	}
	

	private static List<String> getNamesFromPivot() throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTingDir() + File.separator + "may_2017_rerun" + File.separator + 
				"otuAsColumnsLogNorm_rerunPlusMetadata_L6.txt")));
		
		String[] splits=reader.readLine().split("\t");
		
		for(String s : splits)
			list.add(s);
		
		reader.close();
		
		return list;
	}
	
	
	private static int getSkipNum(List<String> names, String name) throws Exception
	{
		for( int x=0; x < names.size(); x++)
			if( names.get(x).equals(name) )
				return x-2;
		
		return -1;
	}
	
	private static void confirmPivot(int skipNumber, int columnNumber) throws Exception
	{
		HashMap<Integer, String> map = getMapFromPivot(columnNumber);
		HashMap<Integer, String> anotherMap = getMap(skipNumber);
		
		System.out.println(map);
		System.out.println(anotherMap);
		
		if( map.size() != anotherMap.size())
			throw new Exception("Fail");
		
		for( Integer i : map.keySet())
		{
			String anotherVal = anotherMap.get(i);
			
			if( anotherVal == null || !anotherVal.equals(map.get(i)))
			{
				Double oneD = Double.parseDouble(map.get(i).replace("%", ""));
				Double anotherD = Double.parseDouble(anotherVal.replace("%", ""));
				
				if( oneD.doubleValue() != anotherD.doubleValue())
					throw new Exception("Fail " + map.get(i) + " " + anotherMap.get(i));
			}
		}
	}
	
	private static HashMap<Integer, String> getMapFromPivot(int columnNumber) throws Exception
	{
		HashMap<Integer, String> map = new LinkedHashMap<Integer,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTingDir() + File.separator + "may_2017_rerun" + File.separator + 
				"otuAsColumnsLogNorm_rerunPlusMetadata_L6.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null ; s= reader.readLine())
		{
			String[] splits= s.split("\t");	
			
			Integer aVal = Integer.parseInt(splits[0]);
			
			if( map.containsKey(aVal))
				throw new Exception("No");
			
			map.put(aVal, splits[columnNumber]);
		}
		
		return map;
	}
	
	private static HashMap<Integer, String> getMap( int skipNumber) throws Exception
	{
		BufferedReader reader= new BufferedReader(new FileReader(new File(
				ConfigReader.getTingDir() + File.separator + "may_2017_rerun" + File.separator + 
					"20170512_Casp11_DSS_5groups_16S_DeNovo_NoPhiX_NoPrimerSeq_L6.txt")));
		
		String[] firstList = reader.readLine().split("\t");
		
		for( int x=0; x < skipNumber; x++) 
			reader.readLine();
		
		String[] secondList = reader.readLine().split("\t");
		System.out.println(secondList[0]);
		
		HashMap<Integer, String> map = new LinkedHashMap<Integer,String>();
		
		for( int x=1; x < firstList.length; x++)
		{
			Integer aVal= Integer.parseInt(firstList[x]);
			
			if( map.containsKey(aVal))
				throw new Exception("No");
			
			map.put(aVal, secondList[x]);
		}
		
		reader.close();
		
		return map;
	}
}
