package jobinLabRNASeq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class ForGoMiner
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"12WeeksVs18WeeksPlusAnnotation.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"allGenes.txt")));
		
		BufferedWriter updownWriter = new BufferedWriter(new FileWriter(new File(ConfigReader.getJobinLabRNASeqDir() + File.separator +
				"updownGenes12WeeksVs18Weeks.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); 
				s != null;
					s = reader.readLine())
		{
			String[] splits = s.split("\t");
			writer.write(splits[0] + "\n");
			
			if( Double.parseDouble(splits[3]) <= 0.10 )
			{
				updownWriter.write(splits[0] + "\t");
				
				if( Double.parseDouble(splits[1]) >= 1)
					updownWriter.write("1\n");
				else
					updownWriter.write("-1\n");
			}
			
		}
		
		writer.flush();  writer.close();
		updownWriter.flush();  updownWriter.close();
	}
}
