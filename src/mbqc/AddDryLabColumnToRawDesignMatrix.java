package mbqc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class AddDryLabColumnToRawDesignMatrix
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getMbqcDir() + File.separator + 
				"dropbox" + File.separator + 
				"raw_design_matrix.txt"));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getMbqcDir() + File.separator 
				+ "dropbox" + File.separator + 
				"raw_design_matrixWithBioinformaticsIDColumn.txt")));
		
		writer.write("bioinformatics\t" + reader.readLine() + "\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			writer.write(new StringTokenizer(splits[0], ".").nextToken() + "\t");
			writer.write(s + "\n");
		}

		writer.flush();  writer.close();
		reader.close();
		
	}
}
