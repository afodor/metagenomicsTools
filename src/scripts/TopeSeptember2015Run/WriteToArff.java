package scripts.TopeSeptember2015Run;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

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
			
			File inFile = new File(ConfigReader.getTopeSep2015Dir() + File.separator +
					"spreadsheets" + File.separator + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormalPlusMetadataFilteredCaseControl.txt");
			int numSamples = getNumSamples(inFile);
			
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					ConfigReader.getTopeSep2015Dir() + File.separator +
					"spreadsheets" + File.separator + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormalPlusMetadataFilteredCaseControl.arff"
					)));
			
			writer.write("% " + NewRDPParserFileLine.TAXA_ARRAY[x] + "\n");
			
			writer.write("@relation " + NewRDPParserFileLine.TAXA_ARRAY[x]  + "_sep15\n");
			
			String[] topSplits = reader.readLine().replaceAll("\"","").split("\t");
			
			for( int y=6; y < topSplits.length; y++)
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
				
				for( int y=6; y < splits.length; y++)
				{
					writer.write( splits[y] + ",");
				}
				
				int caseInt = Integer.parseInt(splits[4]);
				
				if( caseInt == 0 )
					writer.write("false\n");
				else if( caseInt ==1)
					writer.write("true\n");
				else throw new Exception("Parsing error\n");
			}
			
			writer.flush();  writer.close();
			
			reader.close();
		}
	}
}
