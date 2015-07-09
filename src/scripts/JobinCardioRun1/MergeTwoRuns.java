package scripts.JobinCardioRun1;

import java.io.File;
import java.util.HashSet;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class MergeTwoRuns
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			HashSet<String> sampleIds = new HashSet<String>();
			
			OtuWrapper wrapper1 = new OtuWrapper(ConfigReader.getJobinCardioDir() + 
					File.separator + "spreadsheets" + File.separator + 
					"pivoted_" + taxa +  "asColumns.txt" );
			
			OtuWrapper wrapper2 = new OtuWrapper(ConfigReader.getJobinCardioDir() + 
					File.separator + "spreadsheetsRun1" + File.separator + 
					"pivoted_" + taxa +  "asColumns.txt" );
			
			for(String s : wrapper1.getSampleNames())
			{
				if( sampleIds.contains(s))
					throw new Exception("NO");
				
				sampleIds.add(s);
			}
			
			for(String s : wrapper2.getSampleNames())
			{
				if( sampleIds.contains(s))
					throw new Exception("NO");
				
				sampleIds.add(s);
			}
		}
		
	}
}
