package scripts.dla;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;

public class CheckRatios 
{
	public static void main(String[] args) throws Exception
	{

		double cutoff = 50;
		
		//WIAB_2 is UNC donor
		//WIAB_5 is UNC POST
		HashSet<String> set = getRatiosOverCutoff(cutoff, "WIAB_2", "WIAB_5");
		
		//WIAB_VBD is vanderbilt donor
		//WIAB_10 is POST
		HashSet<String> set2 = getRatiosOverCutoff(cutoff, "WIAB_VBD", "WIAB_10");
		

		set.retainAll(set2);
		System.out.println(set);
		
		writeMarked(set);

		System.out.println(set.size());
	}
	
	private static void writeMarked(HashSet<String> set ) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\DLA_Analyses2021-main\\input\\hum"
				+ "anN2_pathabundance_relab.tsv"	)));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				"C:\\DLA_Analyses2021-main\\af_out\\anN2_pathabundance_withMarked.txt")));
		
		String firstLine = reader.readLine().replaceAll("#", "");
		
		String[] firstSplits = firstLine.split("\t");
		
		writer.write(firstSplits[0].trim());
		
		for(int x=1; x < firstSplits.length; x++)
		{
			String s= firstSplits[x];
			s = s.substring(0, s.indexOf("-Emily"));
			writer.write("\t" + s);
		}
		
		writer.write("\tmarked\n");
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0].replaceAll(";", "_").replaceAll("\\|", "_").replaceAll(" ","_").
					replaceAll(":", "_").replaceAll("\\.","_").replaceAll("-", "_").replaceAll("\t", "_")
					.replaceAll("/","_").replaceAll("'", "_") );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			boolean isThere = set.contains(splits[0]);
		
			writer.write("\t" + (isThere ? "Marked" : "Unmarked") + "\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}

	public static HashSet<String> getRatiosOverCutoff( double cutoff , String colName1, String colName2) 
			throws Exception
	{
		HashSet<String> set = new HashSet<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			"C:\\DLA_Analyses2021-main\\input\\hum"
			+ "anN2_pathabundance_relab.tsv"	)));
		
		String topLine = reader.readLine();
		int firstCol = getExactlyOneColumnNumber(topLine, colName1);
		int secondCol = getExactlyOneColumnNumber(topLine, colName2);
		
		for(String s= reader.readLine();  s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			Double val1 = Double.parseDouble( splits[ firstCol]);
			Double val2 = Double.parseDouble(splits[secondCol]);
			
			if( val2 > 0 && val1/val2 >= cutoff)
				set.add(splits[0]);
			
			if( val1 > 0 && val2/val1 >= cutoff)
				set.add(splits[0]);
		}
		
		reader.close();
		return set;
	}
	
	private static Integer getExactlyOneColumnNumber(String s , String name) throws Exception
	{
		Integer i= null;
		
		String[] splits = s.split("\t");
		
		for( int x = 0; x < splits.length; x++)
		{
			if( splits[x] .indexOf(name) != -1)
			{
				if( i != null)
					throw new Exception("Duplicate " + name);
				
				i = x;
			}
		}
		
		if( i == null)
			throw new Exception("Could not find " + name);
		
		return i;
	}

}
