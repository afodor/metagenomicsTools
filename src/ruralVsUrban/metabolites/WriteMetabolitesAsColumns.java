package ruralVsUrban.metabolites;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class WriteMetabolitesAsColumns
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"metabolites" + File.separator + "UNCH-01-15VW_scaled.txt")));
		
		List<String> subjects = new ArrayList<String>();
		List<String> urban_rural = new ArrayList<String>();
		
		for( int x=0; x < 4; x++)
			reader.readLine();
		
		urban_rural = parseAHeaderLine(reader.readLine());
		reader.readLine();
		subjects = parseAHeaderLine(reader.readLine());
		
		System.out.println(urban_rural);
		System.out.println(subjects);
		
		reader.close();
	}
	
	
	private static List<String> parseAHeaderLine(String s ) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(s, "\t");
		
		List<String> list = new ArrayList<String>();
		
		sToken.nextToken();
		
		for( int x=0; x < 40; x++)
			list.add(sToken.nextToken());
		
		if( sToken.hasMoreTokens())
			throw new Exception("No");
		
		return list;
	}
	
	
}
