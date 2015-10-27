package scripts.IanOctober2015;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class BiomFileFormat
{
	/*
	 * The biom files don't seem to have taxa names, so this pivot is 
	 * not so useful...
	 */
	public static void main(String[] args) throws Exception
	{
		for(int i=2; i <=6; i++)
			pivotForALevel(i);
	}
	
	public static void pivotForALevel(int level) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getIanOct2015Dir() + 
				File.separator + "otu_table_L" + level +".biom")));
		
		StringTokenizer sToken = new StringTokenizer(reader.readLine(), "[]{,\":} ");
		
		Integer numRows = null;
		Integer numCols = null;
		String nextToken = sToken.nextToken();
		
		while( ! nextToken.equals("data"))
		{
			nextToken = sToken.nextToken();
			
			if( nextToken.equals("shape"))
			{
				numRows = Integer.parseInt(sToken.nextToken());
				numCols = Integer.parseInt(sToken.nextToken());
			}
		}
		
		System.out.println(numRows);
		System.out.println(numCols);
		
		Double[][] data = new Double[numRows][numCols];
		
		nextToken = sToken.nextToken().replaceAll("\"", "");
		while( ! nextToken.equals("rows"))
		{
			int aRow = Integer.parseInt(nextToken);
			int aCol = Integer.parseInt(sToken.nextToken().replaceAll("\"", ""));
			
			if( data[aRow][aCol] != null)
				throw new Exception("Parsing error " + aRow + " " + aCol);
			
			data[aRow][aCol] = Double.parseDouble(sToken.nextToken().replaceAll("\"", ""));
			
			nextToken = sToken.nextToken().replaceAll("\"", "");
		}
		
		
		List<String> rowNames = new ArrayList<String>();
		
		sToken.nextToken();
		
		String next=null;
		for( int x=0; x < numRows; x++)
		{
			
			String aName = sToken.nextToken();
			rowNames.add(aName.replaceAll("\"", ""));
			
			next = sToken.nextToken().replaceAll("\"", "");
			
			while( !next.equals("id") && !next.equals("columns") )
			{
				next = sToken.nextToken().replaceAll("\"", "");
			}
		}
		
		if( ! next.equals("columns") )
			throw new Exception("Parsing error " );
		
		List<String> colNames = new ArrayList<String>();
		
		for( int x=0; x < numCols; x++)
		{
			sToken.nextToken();
			colNames.add(sToken.nextToken().replaceAll("\"", ""));
			sToken.nextToken();
			sToken.nextToken();
		}
		
		if(sToken.hasMoreTokens())
			throw new Exception("Parsing error " + sToken.nextToken() );
		

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getIanOct2015Dir() + 
				File.separator + "Level_" + level + "_asColumns.txt")));
		
		writer.write("sample");
		
		for(String s: rowNames)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for( int x=0; x < colNames.size(); x++)
		{
			writer.write(colNames.get(x));
			
			for( int y=0; y < rowNames.size(); y++)
			{
				writer.write("\t" + (data[y][x] == null ? "0" : data[y][x]) );
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}
