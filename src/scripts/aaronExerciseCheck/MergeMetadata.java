package scripts.aaronExerciseCheck;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class MergeMetadata
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getAaronExerciseDirectory() + File.separator + 
					"pcoa_genus.txt")));
		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getAaronExerciseDirectory() +File.separator + 
				"pcoa_genusSimpleMeta.txt")));
		
		
		writer.write("sampleId\tsubjectID\ttimepoint");
		
		String[] topSplits = reader.readLine().replaceAll("\"","").split("\t");
		
		for( int x=0; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine();s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"","");
			String[] splits = s.split("\t");
			
			writer.write(splits[0]);
			
			StringTokenizer sToken = new StringTokenizer(splits[0], "_");
			
		
			writer.write("\t"+ sToken.nextToken() + "\t" + sToken.nextToken());
			
			if(sToken.hasMoreTokens())
				throw new Exception("No");
			
			for( int x=1; x< splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush(); writer.close();
		reader.close();
	}
}
