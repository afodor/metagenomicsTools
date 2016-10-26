package scripts.AdenomasRelease;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

/*
 * Eventual target of this :
 * java -classpath weka.jar weka.classifiers.trees.RandomForest  -t C:\adenomasRelease\spreadsheets\pivoted_familyLogNormalWithMetadata.arff -T C:\tope_Sep_2015\spreadsheets\familyasColumnsLogNormalPlusMetadataFilteredCaseControl.arff


 */

public class WriteToArff
{
	private static int getNumSamples(File file ) throws Exception
	{
		int count = 0;
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		for( String s = reader.readLine(); s != null; s= reader.readLine())
			count++;
		
		reader.close();
		
		return count;
	}
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			File inFile = new File(ConfigReader.getAdenomasReleaseDir() + File.separator + 
					"spreadsheets" + File.separator + "pivoted_" +  
					NewRDPParserFileLine.TAXA_ARRAY[x] + "LogNormalWithMetadata.txt");
			int numSamples = getNumSamples(inFile);
			
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					ConfigReader.getAdenomasReleaseDir() + File.separator + 
					"spreadsheets" + File.separator + "pivoted_" +  
					NewRDPParserFileLine.TAXA_ARRAY[x] + "LogNormalWithMetadata.arff")
					));
			
			writer.write("% " + NewRDPParserFileLine.TAXA_ARRAY[x] + "\n");
			
			writer.write("@relation " + NewRDPParserFileLine.TAXA_ARRAY[x]  + "_adenomas454\n");
			
			String[] topSplits = reader.readLine().replaceAll("\"","").split("\t");
			
			for( int y=3; y < topSplits.length; y++)
			{
				writer.write("@attribute " + topSplits[y].replaceAll(" ", "_") + " numeric\n");
			}
			
			writer.write("@attribute isCase { true, false }\n");
			
			writer.write("\n\n@data\n");
			
			writer.write("%\n% " + numSamples + " instances\n%\n");
			
			for( String s= reader.readLine(); s != null; s = reader.readLine())
			{
				s = s.replaceAll("\"", "");
				String[] splits = s.split("\t");
				
				if( splits.length != topSplits.length)
					throw new Exception("Parsing error!");
				
				for( int y=3; y < splits.length; y++)
				{
					writer.write( splits[y] + ",");
				}
				
				if( splits[1].equals("control") )
					writer.write("false\n");
				else if( splits[1].equals("case"))
					writer.write("true\n");
				else throw new Exception("Parsing error\n");
			}
			
			writer.flush();  writer.close();
			
			reader.close();
		}
	}
}
