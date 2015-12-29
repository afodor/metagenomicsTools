package scripts.GoranOct2015;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class MergePCRData
{
	private static void addALevel(String inFile, String outFile, boolean fromR,
			HashMap<String, Double> pcrMap ) throws Exception
	{

		BufferedReader reader = new BufferedReader(new FileReader(new File(
			inFile)));
		
		String[] topSplits = reader.readLine().split("\t");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				outFile)));
		
		writer.write("fullKey\tpartialKey\tcategory\tmeanCD");
		
		int startPos = fromR ? 0 : 1;
		
		for(int x= startPos; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s2 = reader.readLine(); s2 != null; s2 = reader.readLine())
		{
			String[] splits = s2.split("\t");
			
			String firstToken =splits[0].replaceAll("\"", ""); 
			System.out.println(firstToken);
			
			StringTokenizer sToken = new StringTokenizer(firstToken,"_");
			
			String key = sToken.nextToken() + "_" + sToken.nextToken();
			String condition = sToken.nextToken();
			
			Double val = pcrMap.get(key);
			
			if( val == null)
				throw new Exception("Could not find " + key);
			
			writer.write(firstToken + "\t" + key  + "\t" + condition + "\t" + 
							pcrMap.get(key));
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		String[] levels = {"phylum","class","order","family","genus", "otu"};
		
		HashMap<String, Double> pcrMap = PcrFileLine.getCTMeanMap();
		
		for(String s : levels)
		{
			String inFile = ConfigReader.getGoranOct2015Dir() + File.separator + 
					"mds_" + s + ".txt";
			
			String outFile = ConfigReader.getGoranOct2015Dir() + File.separator + 
					"mds_" + s + "withMedata.txt";
			
			addALevel(inFile, outFile, true, pcrMap);
		}
		
		for(String s : levels)
		{
			String inFile = ConfigReader.getGoranOct2015Dir() + File.separator + 
					s+ "_asColumnsLogNorm.txt";
			
			String outFile = ConfigReader.getGoranOct2015Dir() + File.separator + 
					s+ "_asColumnsLogNormWithMetadata.txt";
			
			addALevel(inFile, outFile, false, pcrMap);
		}
	}
}
