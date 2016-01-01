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
			
			double averageNum = getAverageNumberSeqs(unlogged) ;
			
			BufferedReader readerUnLogged = 
					new BufferedReader(new FileReader(unlogged));
			
			BufferedReader readerLogged = 
					new BufferedReader(new FileReader(new File(
							ConfigReader.getIanNov2015Dir() + File.separator + 
							"spreadsheets" + File.separator + 
								"pivoted_"+ level + "asColumnsLogNormal.txt" 	)));
			
			if( ! readerLogged.readLine().equals(readerUnLogged.readLine()))
				throw new Exception("No");
			
			for(String unLoggedS = readerUnLogged.readLine(); unLoggedS != null; 
							unLoggedS = readerUnLogged.readLine() )
			{
				String loggedS = readerLogged.readLine();
				
				String[] unloggedSplts = unLoggedS.split("\t");
				String[] loggedSplits = loggedS.split("\t");
				
				if( ! unloggedSplts[0].equals(loggedSplits[0]))
					throw new Exception("No");
				
				double rowTotal = 0;
				
				for( int y=1; y < unloggedSplts.length; y++)
					rowTotal += Long.parseLong(unloggedSplts[y]);
				
				for( int y=1 ; y < loggedSplits.length; y++)
				{
					double expected = Math.log10( averageNum * Long.parseLong(unloggedSplts[y])
														/ rowTotal   + 1 );
					
					//System.out.println(expected + " "+ loggedSplits[y]);
					
					if( Math.abs(expected-Double.parseDouble(loggedSplits[y])) > 0.0001)
						throw new Exception("No");
				}
			}
			
			readerUnLogged.close();
			readerLogged.close();
			
			System.out.println("Pass " + unlogged.getAbsolutePath());
		}
		System.out.println("Global pass");
	}
	
	private static double getAverageNumberSeqs(File filepath) throws Exception
	{
		long count = 0;
		double numRows = 0;
		
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			for( int x=1; x< splits.length; x++)
				count += Long.parseLong(splits[x]);
			
			numRows++;
		}
		
		reader.close();
		
		return count/numRows;
	}
}
