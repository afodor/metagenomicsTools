package scripts.farnaz.dada2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class PivotAllToOTUTable
{
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File("C:\\farnazDada2\\test.txt");
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		List<String> header = getSequenceHeaders(reader.readLine());
		
		for(String s: header)
			System.out.println(s);
		
		BufferedWriter writer = new BufferedWriter( new FileWriter(new File(
				"C:\\farnazDada2\\pivoted.txt")) );
		
		writer.write("sample");
		
		for( int x=0; x < header.size(); x++)
		{
			writer.write("\t" + "taxa_" + (x+1));
		}
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			
			StringTokenizer sToken = new StringTokenizer(s);
			
			while(sToken.hasMoreTokens())
				writer.write(sToken.nextToken() + (sToken.hasMoreTokens() ? "\t" : "\n") );
		}
		
		writer.flush();  writer.close();
		
		BufferedWriter writerFasta = new BufferedWriter(new FileWriter(new File(
				"C:\\farnazDada2\\allTogetherFasta.txt")));
		
		for( int x=0; x < header.size(); x++)
		{
			writerFasta.write(">taxa_" + (x+1) + "\n");
			writerFasta.write(header.get(x) + "\n");
		}
		
		writerFasta.flush();  writerFasta.close();
	}
	
	private static List<String> getSequenceHeaders(String s) throws Exception
	{
		s=s.replaceAll("\"", "");
		List<String> list = new ArrayList<>();
		
		StringTokenizer sToken = new StringTokenizer(s);
		
		while(sToken.hasMoreTokens())
			list.add(sToken.nextToken());
		
		return list;
	}
}
