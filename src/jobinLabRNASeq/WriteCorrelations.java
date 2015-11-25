package jobinLabRNASeq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;
import utils.Pearson;

public class WriteCorrelations
{
	private static final List<List<Double>> counts = new ArrayList<List<Double>>();
	private static final List<String> headers =new ArrayList<String>();
	
	public static void main(String[] args) throws Exception
	{
		populateLists();
		writeResults();
	}
	
	private static void writeResults() throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getJobinLabRNASeqDir() + File.separator +
				"correlationsForJmp.txt")));
		
		writer.write("comparison\tpearsonR\n");
		
		for( int x=0; x < headers.size() - 1; x++)
		{
			String xString = TimeAnnotations.getAnnotation(headers.get(x));
			
			for( int y=x+1; y < headers.size(); y++ )
			{
				String yString= TimeAnnotations.getAnnotation(headers.get(y));
				
				if( xString.compareTo(yString) < 0 )
					writer.write(xString + "@" + yString + "\t");
				else
					writer.write(yString + "@" + xString + "\t");
	
				System.out.println(counts.get(x).size() + " " + counts.get(y).size() );
				writer.write(Pearson.getPearsonR(counts.get(x), counts.get(y)) + "\n");
			}
		}
		
		writer.flush();  writer.close();
		
		
	}
	
	private static void populateLists() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getJobinLabRNASeqDir() + 
				File.separator + "pivotedSamplesAsColumnsR1Only.txt")));
		
		String firstLine = reader.readLine();
		
		StringTokenizer sToken = new StringTokenizer(firstLine);
		
		sToken.nextToken();
		
		while( sToken.hasMoreTokens())
		{
			headers.add(sToken.nextToken());
			counts.add(new ArrayList<Double>());
		}
			
		for( String s = reader.readLine(); 
					s != null;
						s = reader.readLine())
		{
			String[] splits= s.split("\t");
			
			if( splits.length - 1  != headers.size())
				throw new Exception("No");
			
			for( int x=1; x < splits.length; x++)
				counts.get(x-1).add( Math.log10( Integer.parseInt(splits[x]) + 1.0));
			
		}
		
		reader.close();	
	}
}
