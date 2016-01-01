package scripts.IanNovember2015.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class CheckLogs
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];

			File unlogged = 
				new File(ConfigReader.getIanNov2015Dir() + File.separator + 
					"spreadsheets" + File.separator + 
						"pivoted_"+ level + "asColumns.txt" );
			
			double totalNum = getTotalNumberSeqs(unlogged);
			
			BufferedReader readerUnLogged = 
					new BufferedReader(new FileReader(unlogged));
			
			BufferedReader readerLogged = 
					new BufferedReader(new FileReader(new File(
							ConfigReader.getIanNov2015Dir() + File.separator + 
							"spreadsheets" + File.separator + 
								"pivoted_"+ level + "asColumnsLogNormal.txt" 	)));
			
			if( ! readerLogged.readLine().equals(readerUnLogged.readLine()))
				throw new Exception("No");
			
			readerUnLogged.close();
			readerLogged.close();
			
			System.out.println("Pass " + unlogged.getAbsolutePath());
		}
	}
	
	private static long getTotalNumberSeqs(File filepath) throws Exception
	{
		long count = 0;
		
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			for( int x=1; x< splits.length; x++)
				count = Long.parseLong(splits[x]);
		}
		
		reader.close();
		
		return count;
	}
}
