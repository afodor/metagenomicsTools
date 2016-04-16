package scripts.topeExcludedRuns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class Choose3Or4
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> map = getThreeOrFourMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			
			BufferedReader reader = new BufferedReader(new FileReader(
					ConfigReader.getTopeFeb2016Dir() + File.separator + "spreadsheets" +
							File.separator + "pivoted_" + taxa +  "asColumns.txt"));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					ConfigReader.getTopeFeb2016Dir() + File.separator + "spreadsheetsChooseOne" +
							File.separator + "pivoted_" + taxa +  "asColumnsThreeOrFour.txt")));
			
			writer.write(reader.readLine() + "\n");
			
			for(String s = reader.readLine(); s != null; s = reader.readLine())
			{
				//System.out.println(s);
				String firstSplit = s.split("\t")[0];
				String key = firstSplit.substring(0, firstSplit.indexOf("_"));
				
				boolean include =true;
				
				Integer threeOrFourKey = map.get(key);
				
				if( threeOrFourKey == null)
				{
					System.out.println("adding " + key + " on null");
				}
				else if( threeOrFourKey == 3)
				{
					if( firstSplit.indexOf("set1") != -1 )
					{
						include = false;
					}
					else if ( firstSplit.indexOf("set3") != -1)
					{
						include = true;
					}
					else
					{
						throw new Exception("First split");
					}
				}
				else if( threeOrFourKey == 4)
				{
					if( firstSplit.indexOf("set1") != -1 )
					{
						include = true;
					}
					else if ( firstSplit.indexOf("set3") != -1)
					{
						include = false;
					}
					else
					{
						throw new Exception("First split");
					}
				}
				else throw new Exception("Unknown key");
				
				if( include)
					writer.write(s + "\n");
					
			}
			
			writer.flush();  writer.close();
			
			reader.close();
		}
		
		
	}
	
	private static HashMap<String, Integer> getThreeOrFourMap() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getTopeFeb2016Dir() + File.separator 
			+ "Copy of All Diversticulosis Illumina Primer Seq.txt"	)));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			int key = Integer.parseInt(splits[12]);
			
			if( key == 3)
			{
				if( ! splits[11].equals("4/29/2015"))
					throw new Exception("No");
			}
			else if ( key == 4)
			{
				if( ! splits[11].equals("9/24/2015"))
					throw new Exception("No");
			}
			else throw new Exception("Unknow key");
			
			map.put(splits[0], key);
		}
		
		return map;
	}
}
