package creOrthologs.kmers.chunk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;

import utils.ConfigReader;

public class AddMetadataToPCA
{
	private static HashSet<Integer> getIncludedSet() throws Exception
	{
		HashSet<Integer> set = new HashSet<Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"pneuOnlyChunks_0.85_0.9.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			set.add(Integer.parseInt(splits[0]));
			set.add(Integer.parseInt(splits[1])+ 5000);
		}
		
		reader.close();
		return set;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashSet<Integer> includeSet = getIncludedSet();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"chunks_pcoa.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getCREOrthologsDir()
				+ File.separator + "pcoaPlusChunkMetadata.txt")));
		
		writer.write("name\tstart\tstop\tisBaseline\t" + reader.readLine() + "\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] lineSplits = s.split("\t");
			String[] splits = lineSplits[0].split("_");
			int firstNum = Integer.parseInt( splits[5] );
			int secondNum = Integer.parseInt(splits[6]);
			
			if( includeSet.contains(firstNum) != includeSet.contains(secondNum))
				throw new Exception("No " + firstNum + " " + secondNum);
			
			writer.write(lineSplits[0] + "\t" + firstNum + "\t" + secondNum+"\t" 
						+ !includeSet.contains(firstNum) );
			
			for( int x=1; x < lineSplits.length; x++ )
				writer.write("\t" + lineSplits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}
