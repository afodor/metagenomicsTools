package scripts.sonja2016;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

public class QuickAverages
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> annotationMap = getAnnotationMap();
		
		List<Holder> list = getAverageMap();
		
		for(Holder h : list)
			System.out.println(h.probeID + " " + h.average);
		
		writeResults(annotationMap, list);
	}
	
	private static String getTitleIfDefined(String annotation)
	{
		int index = annotation.indexOf("UG_TITLE=");
		
		if( index == -1)
			return "NA";
		
		int endIndex= annotation.indexOf("PROD")-1;
		
		if( endIndex < index)
			endIndex = annotation.length() -1;
		
		return annotation.substring(index+5, endIndex);
	}
	
	private static void writeResults(HashMap<String, String> annotationMap, 
			List<Holder> averageList) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getSonja2016Dir() + File.separator + "averagesWithAnnotations.txt")));
		
		writer.write("probesetIds\taverage\ttitleIfDefined\tfullAnnotation\n");
		
		for(Holder h : averageList)
		{
			writer.write(h.probeID + "\t");
			writer.write(h.average + "\t");
			
			String annotation = annotationMap.get(h.probeID);
			
			if( annotation == null)
				throw new Exception("No " + h.probeID);
			
			writer.write(getTitleIfDefined(annotation)+ "\t");
			
			writer.write(annotation + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static class Holder implements Comparable<Holder>
	{
		String probeID;
		Double average;
		
		@Override
		public int compareTo(Holder o)
		{
			return Double.compare(o.average,this.average);
		}
	}
	
	private static List<Holder> getAverageMap() throws Exception
	{
		HashMap<String, Double> map = new HashMap<String,Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getSonja2016Dir() + File.separator + 
					"ALDRICH_RMA_NORMALIZED.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if(map.containsKey(splits[0]))
				throw new Exception("No");
			
			double sum =0;
			int n=0;
			for( int x=1; x < splits.length; x++)
			{
				sum += Double.parseDouble(splits[x]);
				n++;
			}
			
			map.put(splits[0], sum / n);
		}
		
		List<Holder> list = new ArrayList<Holder>();
		
		for(String s : map.keySet())
		{
			Holder h = new Holder();
			h.probeID = s;
			h.average = map.get(s);
			list.add(h);
		}
		
		Collections.sort(list);
		
		return list;
		
	}
	
	static HashMap<String, String> getAnnotationMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getSonja2016Dir() + File.separator + "AllArrayData.txt"	)));
		
		reader.readLine();
		
		for(String s= reader.readLine() ; s!= null; s=reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], splits[splits.length-1].replaceAll("\"", ""));
		}
		
		return map;
	}
}
