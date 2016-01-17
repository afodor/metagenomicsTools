package creOrthologs.kmers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class CompareDistanceMatrices
{
	private static class Holder
	{
		double dist1;
		double dist2;
	}
	
	private static List<String> getNames() throws Exception
	{
		BufferedReader reader= new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"gatheredKmerMatrices" + File.separator +"allKey.txt")));
		
		
		List<String> list = new ArrayList<String>();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken= new StringTokenizer(s);
			sToken.nextToken();
			
			list.add(sToken.nextToken());
		}
		
		reader.close(); 
		
		return list;
	}
	
	private static void addToHolder(File file, List<Holder> list, boolean first ) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		int num = Integer.parseInt(reader.readLine().trim().replaceAll("\"", ""));
		
		int index =0;
		for( int x=0; x < num ; x++)
		{
			String s= reader.readLine();
			StringTokenizer sToken = new StringTokenizer(s);
			sToken.nextToken();
			
			for( int y=0; y < num ; y++)
			{
				if( first)
				{
					Holder h = new Holder();
					list.add(h);
					h.dist1 = Double.parseDouble(sToken.nextToken());
				}
				else
				{
					Holder h = list.get(index);
					h.dist2 = Double.parseDouble(sToken.nextToken());
					index++;
				}
			}
			
			if( sToken.hasMoreTokens())
				throw new Exception("no");
		}
		
		
		reader.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		File file1 = new File(ConfigReader.getCREOrthologsDir() + File.separator + 
				"gatheredKmerMatrices" + File.separator + "allDist.txt");
		
		File file2 = new File(ConfigReader.getCREOrthologsDir() + File.separator + 
				"gatheredKmerMatrices" + File.separator + "klebsiella_pneumoniae_chs_11.0_7000000220927538_302000_307001_dist.txt");
	
		List<Holder> list = new ArrayList<CompareDistanceMatrices.Holder>();
		
		addToHolder(file1, list, true);
		addToHolder(file2, list, false);
		
		writeResults(list);
	}
	
	private static void writeResults(List<Holder> list) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File(ConfigReader.getCREOrthologsDir() + File.separator + 
				"gatheredKmerMatrices" + File.separator + "comparison.txt")));
		
		writer.write("name1\tname2\tallDist\tsubDist\tbothPneu\n");
		
		int firstIndex=0;
		int secondIndex =0;
		
		List<String> names = getNames();
		
		for( Holder h : list)
		{
			
			writer.write( getThreeTokens(names.get(firstIndex)) + "\t" + getThreeTokens(names.get(secondIndex)) + "\t");
			
			firstIndex++;
			
			if( firstIndex == names.size())
			{
				firstIndex = 0;
				secondIndex++;
				
				System.out.println(secondIndex);
			}
			
			if( firstIndex < secondIndex)
			{
				writer.write( h.dist1  + "\t" + h.dist2 + "\t");
				
				writer.write( (names.get(firstIndex).indexOf("pneu") != -1 &&  
						names.get(secondIndex).indexOf("pneu") != -1)  + "\n");
			}			
		}
		
		writer.flush();  writer.close();
		
		if( firstIndex != 0)
			throw new Exception("no");
		
		if( secondIndex != names.size())
			throw new Exception("No " + secondIndex);
		
		
	}
	
	private static String getThreeTokens(String s)
	{
		String[] splits = s.split("_");
		return splits[0] + "_" + splits[1] + "_" + splits[2];
	}
}
