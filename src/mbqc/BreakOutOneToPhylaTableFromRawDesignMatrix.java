package mbqc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class BreakOutOneToPhylaTableFromRawDesignMatrix
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + "dropbox" + File.separator + 
				"raw_design_matrix.txt")));
		
		String firstLine = reader.readLine();
		
		List<String> list = getPhylaHeaders(firstLine);
		
		for(String s : list)
			System.out.println(s);
	}
	
	private static List<String> getPhylaHeaders(String firstLine) throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		String[] splits = firstLine.split("\t");
		
		for( int x=0; x < splits.length; x++)
		{
			if( splits[x].startsWith("k__") && splits[x].indexOf("unclassified") ==-1 )
			{
				StringTokenizer sToken = new StringTokenizer(splits[x] , ".");
				sToken.nextToken();
				String val = sToken.nextToken();
				
				if( val.trim().length() ==0 )
					val = sToken.nextToken();
				
				if( ! val.startsWith("p__"))
					throw new Exception("Parsing error " + splits[x] + " " + val);

				val = val.replace("p__", "");
				
				// just for k__Bacteria.p__.Thermi. which is a column header in the spreadsheet
				if( val.trim().length() ==0 )
					val = sToken.nextToken();
				
				list.add(val);
			}
			else
			{
				list.add(null);
			}
		}
		
		return list;
	}
}
