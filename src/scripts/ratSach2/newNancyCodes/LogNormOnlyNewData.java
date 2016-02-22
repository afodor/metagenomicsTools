package scripts.ratSach2.newNancyCodes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class LogNormOnlyNewData
{
	public static void main(String[] args) throws Exception
	{

		HashMap<String, NewNancyCodesMetaline> newMap = NewNancyCodesMetaline.getMetaMap();
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getRachSachReanalysisDir() + File.separator +
					"rdpAnalysis" + File.separator + 
					"sparseThreeColumn_" + level + "_AsColumns.txt")));
			
			File filteredFile = new File(ConfigReader.getRachSachReanalysisDir() + File.separator +
					"NancyCoHouse" + File.separator + 
				"cohousingRun_" + level + "_AsColumns.txt");
			
			File logfilteredFile = new File(ConfigReader.getRachSachReanalysisDir() + File.separator +
					"NancyCoHouse" + File.separator + 
				"cohousingRun_" + level + "_AsColumnsLogNormalized.txt");
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(filteredFile));
			
			writer.write(reader.readLine() + "\n");
			
			for(String s= reader.readLine(); s != null; s= reader.readLine())
			{
				String[] splits = s.split("\t");
				
				if( newMap.containsKey(splits[0].replaceAll("\"", "")))
				{
					writer.write( s + "\n" );
				}
			}
			
			writer.flush();  writer.close();
			

			OtuWrapper wrapper = new OtuWrapper(filteredFile);
			wrapper.writeLoggedDataWithTaxaAsColumns(logfilteredFile);
		}
	}
}
