package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class BiomFileFormat
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getBigDataScalingFactorsDir() + 
				File.separator + "July_StoolRemoved" + File.separator + "risk_PL_raw_counts.biom")));
		
		StringTokenizer sToken = new StringTokenizer(reader.readLine(), "[]{,\":} ");
		
		Integer numRows = null;
		Integer numCols = null;
		String nextToken = sToken.nextToken();
		
		while( ! nextToken.equals("data"))
		{
			nextToken = sToken.nextToken();
			
			if( nextToken.equals("shape"))
			{
				numRows = Integer.parseInt(sToken.nextToken());
				numCols = Integer.parseInt(sToken.nextToken());
			}
		}
		
		System.out.println(numRows);
		System.out.println(numCols);
	}
}
