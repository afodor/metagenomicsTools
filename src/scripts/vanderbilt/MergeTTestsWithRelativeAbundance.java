package scripts.vanderbilt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class MergeTTestsWithRelativeAbundance
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			OtuWrapper wrapper = new OtuWrapper(ConfigReader.getVanderbiltDir() + File.separator + 
					"spreadsheets" + File.separator + 
					"pivoted_" +  NewRDPParserFileLine.TAXA_ARRAY[x] +"asColumns.txt");
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getVanderbiltDir() + File.separator + 
					"spreadsheets" + File.separator + 
					"pValuesForpostTaxa_" +  NewRDPParserFileLine.TAXA_ARRAY[x] +".txt")));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					ConfigReader.getVanderbiltDir() + File.separator + 
					"spreadsheets" + File.separator + 
					"pValuesForpostTaxa_" +  NewRDPParserFileLine.TAXA_ARRAY[x] +"WithAbundance.txt"
					)));
			
			writer.write(reader.readLine() + "\tnumCounts\taverageRelativeAbundance\n");
			
			for(String s= reader.readLine() ; s != null;  s= reader.readLine())
			{
				String[] splits =s.split("\t");
				String key = splits[0].replaceAll("\"", "");
				int taxaIndex = wrapper.getIndexForOtuName(key);
				
				if( taxaIndex != -1)
				{
					writer.write(s + "\t" + wrapper.getCountsForTaxa(taxaIndex));
					writer.write(wrapper.getAverageRelativeAbundnace(taxaIndex) + "\n");
				}
			}
			
			
			writer.flush();  writer.close();
			
			reader.close();
			
		}
	}
}
