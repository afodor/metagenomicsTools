package scripts.lactoCheck.OtuPick;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class PivotOutDada2Results
{
	public static void main(String[] args) throws Exception
	{
		pivotForDirectory("Escherichia_Shigella" );
	}
	
	public static void pivotForDirectory(String directoryName) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getLactoCheckDir() + 
				File.separator + directoryName + File.separator + "dada2Out.txt")));
		
		List<String> sequences = new ArrayList<String>();
		
		StringTokenizer sToken = new StringTokenizer(reader.readLine());
		
		while(sToken.hasMoreTokens())
			sequences.add(sToken.nextToken().replaceAll("\"", ""));
		
		int index=1;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigReader.getLactoCheckDir() + File.separator + 
				File.separator + directoryName + File.separator + "fastaOut.txt"));
		
		for(String s : sequences)
		{
			writer.write(">seq_" + index + "\n");
			writer.write( s + "\n");
			index++;
		}
		
		writer.flush();  writer.close();
		
		writer = new BufferedWriter(new FileWriter(ConfigReader.getLactoCheckDir() + File.separator + 
				File.separator + directoryName + File.separator + "inferredAsColumns.txt"));
		
		writer.write("sample");
		
		for( int x=0; x < sequences.size(); x++)
			writer.write("\tseq_" + (x+1));
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			sToken = new StringTokenizer(s);
			
			writer.write(sToken.nextToken().replaceAll("\"", ""));
			
			while(sToken.hasMoreTokens())
				writer.write("\t" + sToken.nextToken());
			
			writer.write("\n");
		}
		
		writer.flush(); writer.close();
		reader.close();
	}
}
