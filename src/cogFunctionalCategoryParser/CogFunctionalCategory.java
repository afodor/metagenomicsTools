package cogFunctionalCategoryParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class CogFunctionalCategory
{
	private final String cogID;
	private final char functionalChar;
	private final String product;

	public String getCogID()
	{
		return cogID;
	}

	public char getFunctionalChar()
	{
		return functionalChar;
	}

	public String getProduct()
	{
		return product;
	}

	private CogFunctionalCategory(String line)
	{
		StringTokenizer sToken = new StringTokenizer(line);
		
		this.functionalChar = sToken.nextToken().replace("[", "").replace("]", "").trim().charAt(0);
		this.cogID = sToken.nextToken();
		StringBuffer buff = new StringBuffer();
		
		buff.append(sToken.nextToken());
		
		while(sToken.hasMoreTokens())
			buff.append(" " + sToken.nextToken());
		
		this.product = buff.toString();
	}
	
	/*
	 * Input file downloaded from ftp://ftp.ncbi.nih.gov/pub/COG/old
	 */
	public static HashMap<String, CogFunctionalCategory> getAsMap() throws Exception
	{
		HashMap<String, CogFunctionalCategory> map = new HashMap<String, CogFunctionalCategory>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"COG_Annotations" + File.separator + 
				"COGs.txt")));
		
		for(String s = reader.readLine();
				s != null;
					s = reader.readLine())
		{
			if( s.startsWith("["))
			{
				CogFunctionalCategory cfc = new CogFunctionalCategory(s);
				if(map.containsKey(cfc.cogID))
					throw new Exception("Duplicate " + cfc.cogID);
				
				map.put(cfc.cogID, cfc);
			}
		}
		
		reader.close();
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		System.out.println(getAsMap().size());
	}
}
