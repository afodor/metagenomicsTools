package scripts.JamesKrakenParse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.Avevar;

public class CollectParentStats
{
	private static class Holder
	{
		List<String> childNames = new ArrayList<>();
		List<Double> childDepths =new ArrayList<>();
		List<Double> rSquaredValues =new ArrayList<>();
		Double parentAbundance; 	
		
	}
	
	public static void main(String[] args) throws Exception
	{
		String level = "phylum";
		
		//String level = "genus";
		
		HashMap<String, Holder> map = getParentStats(level);
		writeAverages(map, level);
	}
	
	private static void writeAverages(HashMap<String, Holder> map , String level) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\JamesKraken\\"+ level + "VanderbiltParentAverages.txt")));
		
		writer.write("parentName\tparentAbundance\tnumberOfChildren\tchildrenAvg\tchildrenVar\tchildTotal\tcorAvg\tcorSD\tchildrenNames\n");
		
		for( String s : map.keySet() )
		{
			
			Holder h = map.get(s);
			
			writer.write( s + "\t" +  h.parentAbundance + "\t" + h.childNames.size() + "\t" +  
						new Avevar(h.childDepths).getAve() + "\t" + new Avevar(h.childDepths).getSD() *new Avevar(h.childDepths).getSD());
			
			double sum =0;
			
			for( Double d : h.childDepths )
				sum += d;
			
			writer.write("\t" + sum);
			 		
			StringBuffer buff = new StringBuffer();
			
			for(String s2 : h.childNames)
				buff.append(s2+ ";");
			
			writer.write( "\t" + new Avevar(h.rSquaredValues).getAve() +"\t" + new Avevar(h.rSquaredValues).getSD() + "\t" 
								+ buff.toString() + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	/*
	 * Parent name is the key
	 */
	private static HashMap<String, Holder> getParentStats(String level) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(new File("C:\\JamesKraken\\"+ level + "Vanderbilt.txt")));
		
		HashMap<String, Holder> map = new HashMap<>();
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String parentName = splits[1];
			
			if( ! parentName.equals("NA"))
			{

				Holder h= map.get(parentName);
				
				if( h== null)
				{
					h = new Holder();
					map.put(parentName, h);
				}
				
				h.childNames.add(splits[0]);
				h.childDepths.add(Double.parseDouble(splits[2]));
				h.rSquaredValues.add(Double.parseDouble(splits[4]));
				
				double parentAbundance = Double.parseDouble(splits[3]);
				
				if( h.parentAbundance == null)
					h.parentAbundance = parentAbundance;
				
				
				if( h.parentAbundance != parentAbundance)
					throw new Exception("No " + h.parentAbundance + " "+ parentAbundance);
			}
			
		}
		
		reader.close();
		return map;
	}
}
