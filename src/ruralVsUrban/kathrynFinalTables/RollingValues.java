package ruralVsUrban.kathrynFinalTables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.ConfigReader;

public class RollingValues
{
	public static final int WINDOWS_SIZE = 25;
	
	
	private static class Holder implements Comparable<Holder>
	{
		double pValue;
		double prevelance;
		double rank;
		
		@Override
		public int compareTo(Holder o)
		{
			return Double.compare(this.pValue,o.pValue);
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> higherInRural = getHolders(true);
		List<Holder> higherInUrban = getHolders(false);
		
		for( Holder h : higherInRural)
			System.out.println(h.pValue + " " + h.prevelance);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + "rollingWindownPrevelance.txt")));
		
		writer.write("rank\tpValue\trollingPrevelance\thigherInRural\n");
		addResults(higherInRural, writer, true);
		addResults(higherInUrban, writer, false);
		
		writer.flush();  writer.close();
	}
	
	private static void addResults(List<Holder> list, BufferedWriter writer, boolean higherInRural) throws Exception
	{
		for( int x=0; x < list.size() - WINDOWS_SIZE; x++)
		{
			Holder h= list.get(x);
			
			writer.write(h.rank + "\t");
			writer.write(h.pValue + "\t");
			
			double sum =0;
			
			for( int y= x; y < x + WINDOWS_SIZE; y++)
				sum += list.get(y).prevelance;
				
			sum = sum / WINDOWS_SIZE;
			
			writer.write(sum  + "\t" );
			writer.write(higherInRural + "\n");
		}
		writer.flush();
	}
	
	private static List<Holder> getHolders(boolean higherInRural) throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + 
						"otusToMostWanted.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null ; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( Boolean.parseBoolean(splits[7]) == higherInRural)
			{
				Holder h= new Holder();
				h.prevelance = Double.parseDouble(splits[2]);
				h.pValue = Double.parseDouble(splits[3]);
				list.add(h);
			}
		}
		
		Collections.sort(list);
		
		for( int x=0; x < list.size(); x++)
			list.get(x).rank =x +1 ;
	
		reader.close();
		return list;
	}
}
