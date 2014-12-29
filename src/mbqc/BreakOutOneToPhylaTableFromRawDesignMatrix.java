package mbqc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getMbqcDir() + File.separator + "dropbox" + File.separator + 
				"huttenhower_raw_design_matrixPhylaAsColumns.txt")));
		
		String firstLine = reader.readLine();
		
		List<String> list = getPhylaHeaders(firstLine);

		writer.write("sample");
		for(String s : list)
			if( s != null)
				writer.write("\t" + s);
		
		writer.write("\n");
		
		int lastIndex =0;
		
		for( int x=0; x < list.size(); x++)
			if( list.get(x) != null)
				lastIndex = x;
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			if(  s.startsWith("chuttenhower"))
			{
				String[] splits = s.split("\t");
				writer.write(splits[0]);
				
				for( int x=0; x <= lastIndex; x++)
					if( list.get(x) != null)
						writer.write(splits[x] + ( x == lastIndex ? "\n" : "\t" ));
			}
		}
		
		writer.flush();  writer.close();
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
