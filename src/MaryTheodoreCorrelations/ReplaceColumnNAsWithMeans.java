package MaryTheodoreCorrelations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import utils.Avevar;

public class ReplaceColumnNAsWithMeans
{
	/**
	 * THIS CODE HAS BUGS
	 */
	public static void main(String[] args) throws Exception
	{
		File inFile = new File( "C:\\MaryTheodoreQuick\\blah1.txt");
		
		List<Double> colMeans = getColumnMeans(inFile);
		
		for(Double d : colMeans)
			System.out.println(d);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\MaryTheodoreQuick\blah2.txt")));
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		writer.write(reader.readLine() + "\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			for(int x=0; x < splits.length; x++)
			{
				String aVal = splits[x].trim();
				
				if( aVal.length() > 0 )
				{
					writer.write(aVal);
				}
				else
				{
					writer.write("" + colMeans.get(x));
				}
				
				if( x < splits.length -1 )
					writer.write("\t");
			}
			
			writer.write("\n");
		}
		
		reader.close();
		writer.flush(); writer.close();
	}
	
	private static List<Double> getColumnMeans(File file) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		List<List<Double>> list = new ArrayList<>();
		
		String[] topLineSplits = reader.readLine().split("\t");
		
		for(String s : topLineSplits)
			list.add(new ArrayList<>());
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			for(int x=0; x < splits.length; x++)
			{
				String aVal = splits[x].trim();
				
				if( aVal.length() > 0 )
				{
					List<Double> innerList = list.get(x);
					innerList.add(Double.parseDouble(aVal));
				}
					
			}
		}
		
		List<Double> returnList = new ArrayList<>();
		
		for(List<Double> innerList : list)
			returnList.add(new Avevar(innerList).getAve());
		
		reader.close();
		
		return returnList;
	}
}
