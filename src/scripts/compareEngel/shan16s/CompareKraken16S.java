package scripts.compareEngel.shan16s;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import utils.ConfigReader;

public class CompareKraken16S
{
	private static class Holder
	{
		Double pValue16 = null;
		Double pValueKraken = null;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = new HashMap<>();
		
		File krakenFile = new File(	ConfigReader.getEngelCheckDir() +File.separator + "krakenCheck" + 
						File.separator + "mikeRelease" + File.separator + "pValuesKrakengenus.txt");
		
		parseAFile(krakenFile, false, map);
		
		File qiimeFile =  new File(	ConfigReader.getEngelCheckDir() +File.separator + "shan16S" + 
								File.separator + "pValues6.txt");
		
		parseAFile(qiimeFile, true, map);
		writeResults(map);
	}
	
	private static void writeResults(HashMap<String, Holder> map) throws Exception
	{
		File outFile = new File(	ConfigReader.getEngelCheckDir() +File.separator + "krakenVsQiime_cMrace.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("taxa\tkrakenP\tqiimeP\n");
		
		for(String s : map.keySet())
		{
			writer.write(s + "\t" + getValOrNone(map.get(s).pValueKraken) + "\t" +
						getValOrNone(map.get(s).pValue16) + "\n");
		}
		writer.flush();  writer.close();
	}
	
	private static String getValOrNone(Double d )
	{
		if( d== null)
			return "";
		
		return d.toString();
	}
	
	private static void parseAFile(File file, boolean is_16s,HashMap<String, Holder> map ) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits = s.split("\t");
			
			if( splits.length != 6)
				throw new Exception("No ");
			
			if( splits[0].equals("c_mrace") && ! splits[2].equals("NA"))
			{
				String taxa = splits[1];
				
				if( taxa.toLowerCase().indexOf("mds") == -1 )
				{
					if( is_16s)
					{
						if( taxa.indexOf("g__") != -1 )
						{
							taxa = taxa.substring(taxa.indexOf("g__"), taxa.length());
							taxa =taxa.replace("g__", "");
						}					
					}
					
					Holder h = map.get(taxa);
					
					if( h == null)
					{
						h = new Holder();
						map.put(taxa, h);
					}
					
					double aPvalue = Math.log10( Double.parseDouble(splits[2]));
					
					if( Double.parseDouble(splits[4]) > Double.parseDouble(splits[3]))
					{
						aPvalue = - aPvalue;
					}
					
					if( is_16s)
					{
						h.pValue16 = aPvalue;
					}
					else
					{	
						h.pValueKraken = aPvalue;
					}
				}
			}
		}
		
		reader.close();
	}
}
