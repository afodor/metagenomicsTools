package scripts.machineLearningJournalClub;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class WriteCaseControlForR
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMachineLearningDir() + File.separator + 
				"testData")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getMachineLearningDir() + File.separator + 
				"testDataAs1Or0.txt")));
		
		writer.write(reader.readLine() + "\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			
			String firstToken = sToken.nextToken();
			
			if( firstToken.endsWith("case"))
				writer.write("1");
			else if( firstToken.endsWith("control"))
				writer.write("0");
			else throw new Exception("No");
			
			while(sToken.hasMoreTokens())
				writer.write("\t" + sToken.nextToken());
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	
	}
}
