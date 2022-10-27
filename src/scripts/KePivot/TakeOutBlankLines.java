package scripts.KePivot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class TakeOutBlankLines
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\kePivot\\bracken_genus_reads.csv")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\kePivot\\bracken_genus_readsNoBlanks.csv")));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			if( s.trim().length() >0 )
				writer.write(s + "\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}
