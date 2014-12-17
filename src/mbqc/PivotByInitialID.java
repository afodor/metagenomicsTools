package mbqc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class PivotByInitialID
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getMbqcDir() + 
				File.separator + "dropbox" + File.separator +  "merged_otu_filtered.txt")));
		
		String prefix = "jpetrosino";
		String firstLine = reader.readLine();
		HashSet<Integer> keepSet = getKeepSet(firstLine, prefix);
		
		System.out.println("Got keep set " + keepSet.size());
		
		BufferedWriter writer= new BufferedWriter(new FileWriter(new File(ConfigReader.getMbqcDir() + 
				File.separator + "dropbox" + File.separator +  "merged_otu_filtered_"+ prefix + ".txt")));
		
		writer.write("otuID");
		
		String[] splits = firstLine.split("\t");
		for(int i = 1; i < splits.length; i++)
			if( keepSet.contains(i))
				writer.write("\t" + splits[i]);
		
		writer.write("\n");
		
		int numDone=0;
		
		for(String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			splits = s.split("\t");
			
			boolean hasNonZero =false;
			
			for(int x=1; x < splits.length && ! hasNonZero; x++)
				if( keepSet.contains(x) && Double.parseDouble(splits[x]) >50 )
					hasNonZero = true;
			
			if( hasNonZero)
			{
				writer.write("" + splits[0]);
				
				for( int x=1; x < splits.length; x++)
					if( keepSet.contains(x) )
						writer.write("\t" + splits[x]);
				
				writer.write("\n");
				numDone++;
				
				if( numDone % 100 == 0 )
					System.out.println(numDone);
			}
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
	private static HashSet<Integer> getKeepSet(String firstLine, String prefix) throws Exception
	{
		String[] splits = firstLine.split("\t");
		
		HashSet<Integer> set = new LinkedHashSet<Integer>();
		
		set.add(0);
		
		for( int x=0; x < splits.length; x++)
		{
			StringTokenizer sToken = new StringTokenizer(splits[x], ".");
			
			if( sToken.nextToken().equals(prefix))
				set.add(x);
		}
		
		return set;
	}
}
