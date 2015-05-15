package scripts.jobin.April2015.nec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class MergeForwardBackward
{
	public static void main(String[] args) throws Exception
	{

		File inFileF = new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
						"nec_Freads.txt");
		
		File inFileR = new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"nec_Freads.txt");
		
		File mergedFile = new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"nec_taxaAsColumns_mergedF_R.txt");
		File outFileF = new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"nec_FreadsNoTax.txt");
		
		addTag(inFileF, outFileF, "_1");
		
		File outFileR = new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"nec_RreadsNoTax.txt");

		addTag(inFileR, outFileR, "_2");
		
		
		File transposedFFile =  new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"necF_taxaAsColumns.txt");
		

		File transposedRFile =  new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"necR_taxaAsColumns.txt");
		
		
		OtuWrapper.transpose(outFileF.getAbsolutePath(), transposedFFile.getAbsolutePath(), false);
		OtuWrapper.transpose(outFileR.getAbsolutePath(), transposedRFile.getAbsolutePath(), false);
		
		OtuWrapper.merge(transposedFFile, transposedRFile, mergedFile);
		
		HashMap<String, String> otuToFamily = new HashMap<String, String>();
		
		addotuToFamily(otuToFamily , inFileF);
		addotuToFamily(otuToFamily,  inFileR);
		
		//for(String s : otuToFamily.keySet())
			//System.out.println(otuToFamily + " " + otuToFamily.get(s));
		
		File mergedFileFamily = new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"nec_taxaAsColumns_mergedF_R_phyla.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(mergedFileFamily));
		
		BufferedReader reader = new BufferedReader(new FileReader(mergedFile));
		
		String firstLine = reader.readLine();
		String[] firstLineSplits = firstLine.split("\t");
		
		writer.write(firstLineSplits[0]);
		HashMap<String, List<Integer>> familyColumns = getFamilyColumns(firstLine, otuToFamily);
		List<String> families = new ArrayList<String>(familyColumns.keySet());
		Collections.sort(families);
		
		for( String f : families)
			writer.write("\t" + f);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			writer.write(splits[0]);
			
			for( String f : families)
			{
				double sum = 0;
				
				List<Integer> list = familyColumns.get(f);
				
				for( Integer i : list)
				{
					sum += Double.parseDouble(splits[i]);
				}
				
				writer.write("\t" + sum);
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		
		File mergedFileFamilyLog = new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"nec_taxaAsColumns_mergedF_R_phylaLogNormal.txt");
		
		OtuWrapper wrapper = new OtuWrapper(mergedFileFamily);
		
		wrapper.writeNormalizedLoggedDataToFile(mergedFileFamilyLog);
		
	}
	
	private static HashMap<String, List<Integer>> getFamilyColumns(String line,
			HashMap<String, String> otuToFamily ) throws Exception
	{
		HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>();
		String[] splits = line.split("\t");
		
		
		for(int x=1; x < splits.length; x++)
		{
			String key = splits[x];
			//System.out.println(key);
			
			String family = otuToFamily.get(key);
			
			if( family == null)
				throw new Exception("No " + key  +  " " + splits[1]);
			
			List<Integer> list = map.get(family);
			
			if( list == null)
			{
				list = new ArrayList<Integer>();
				map.put(family,list);
			}
			
			list.add(x);
			
		}
		
		return map;
	}
	
	private static String getPhyla(String s) throws Exception
	{
		String[] splits = s.split(";");
		
		if( ! splits[1].startsWith(" p__"))
			throw new Exception("No");
		
		return splits[1].replaceAll(" p__", "");
	}
	
	private static void addotuToFamily(HashMap<String, String> map, File f) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		reader.readLine();
		
		for(String s=  reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String lastToken = splits[splits.length-1];
			
			StringTokenizer sToken = new StringTokenizer(lastToken, ";");
			
			while( sToken.hasMoreTokens())
			{
				String nextToken = sToken.nextToken().trim();
				
				if( nextToken.startsWith("p__"))
				{
					
					String family = nextToken.replaceAll("p__", "").replace("[", "").replace("]", "");
					
					if( family.length() == 0 )
						family = "unassigned_" + getPhyla(lastToken);
					
					if( map.containsKey(splits[0]) && ! family.equals(map.get(splits[0])) )
						throw new Exception("No " +  family + " " + map.get(splits[0]));
					
					map.put(splits[0], family);
				}
			}
		}
		
		reader.close();
	}
	
	private static void addTag(File inFile, File outFile, String tag) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		String[] firstTokens = reader.readLine().split("\t");
		
		writer.write(firstTokens[0]);
		
		for( int x=1; x < firstTokens.length - 1; x++)
			writer.write("\t" + firstTokens[x] + "_" + tag);
		
		writer.write("\n");
		
		for( String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			writer.write(splits[0]);
			
			for( int x=1; x < splits.length-1; x++ )
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
}
