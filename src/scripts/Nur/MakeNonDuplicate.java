package scripts.Nur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MakeNonDuplicate
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"c:\\temp\\simpler.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				"c:\\temp\\simplerUnique.txt")));
		
		String[] splits = reader.readLine().split("\t");
		
		writer.write(splits[0]);
		
		for( int x=1; x < splits.length; x++)
			writer.write("\t" + splits[x] + "_" + x);
		
		writer.write("\n");
		
		for( String s = reader.readLine(); s != null; s = reader.readLine())
		{
			writer.write(s + "\n");
		}
					
		
		writer.flush(); writer.close();
		
		reader.close();
	}
}
