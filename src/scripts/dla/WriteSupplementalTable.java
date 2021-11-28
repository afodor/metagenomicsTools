package scripts.dla;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class WriteSupplementalTable
{
	public static void main(String[] args) throws Exception
	{
		writeUnsortedTable();
	}
	
	@SuppressWarnings("resource")
	private static void writeUnsortedTable() throws Exception
	{
		HashMap<String, MetadataFileLine> map = MetadataFileLine.getMetaMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\DLA_Analyses2021-main\\input\\hum"
				+ "anN2_pathabundance_relab.tsv"	)));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				"C:\\DLA_Analyses2021-main\\af_out\\tableWithRatios.txt")));
		
		String firstLine = reader.readLine().replaceAll("#", "");
		
		String[] firstSplits = firstLine.split("\t");
		
		writer.write(firstSplits[0].trim());
		
		for(int x=1; x < firstSplits.length; x++)
		{
			String s= firstSplits[x];
			s = s.substring(0, s.indexOf("-Emily"));
			
			MetadataFileLine mfl = map.get(s);
			
			if( mfl== null)
				throw new Exception("Could not find " + s);
			
			writer.write("\t" + mfl.getPatient() + "_" + mfl.getTimepoint());
		}
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0].replaceAll(";", "_").replaceAll("\\|", "_").replaceAll(" ","_").
					replaceAll(":", "_").replaceAll("\\.","_").replaceAll("-", "_").replaceAll("\t", "_")
					.replaceAll("/","_").replaceAll("'", "_") );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
}
