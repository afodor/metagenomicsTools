package binomFit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;

public class RemoveDupes
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader("C:\\Farnaz_Biorx\\OneMismatchCluster.txt"));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Farnaz_Biorx\\derep.txt"));
		
		writer.write(reader.readLine() + "\n");
		
		HashSet<String> set = new HashSet<String>();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[1] + "_" + splits[2] + "_" + splits[4];
			
			if( ! set.contains(key))
			{
				writer.write(s + "\n");
				set.add(key);
			}
		}
				
		reader.readLine();
		writer.flush();  writer.close();
	}
}
