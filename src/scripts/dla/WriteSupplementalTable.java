package scripts.dla;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WriteSupplementalTable
{
	public static void main(String[] args) throws Exception
	{
		writeUnsortedTable();
	}
	
	private static double getRatio(List<MetadataFileLine> list, String s, boolean vanderbilt)
		throws Exception
	{
		List<Integer> numeratorIndex = new ArrayList<>();
	
		String[] splits = s.split("\t");
		
		if( splits.length != list.size())
			throw new Exception("Parsing error " + splits.length + " " + list.size() + "\n" + s);
		
		for( int x=1; x < list.size(); x++)
		{
			if( vanderbilt && list.get(x).getPatient().equals("Vanderbilt-patient") )
				numeratorIndex.add(x);
			else if( ! vanderbilt && list.get(x).getPatient().equals("UNC-patient") )
				numeratorIndex.add(x);
			
		}
		
		double numeratorSum =0;
		
		for( Integer i : numeratorIndex)
		{
			numeratorSum += Double.parseDouble(splits[i]);
		}
		
		double numeratorAvg = numeratorSum / numeratorIndex.size();
		
		double denom = getExactlyOneVal(list, s, vanderbilt);
		
		if (denom == 0 )
			return 0;
		
		boolean flip = false;
		
		double ratio = numeratorAvg/denom;
		
		if(ratio == 0 )
			return 0;
		
		if( ratio < 1)
		{
			flip = true;
			ratio = 1 /ratio;
		}
		
		ratio = Math.log10(ratio);
		
		if( flip )
			ratio = - ratio;
		
		return ratio;
	}
	
	private static double getExactlyOneVal( List<MetadataFileLine> list, String s , boolean vanderbilt ) throws Exception
	{
		
		Integer index = null;
		
		for( int x=1; x < list.size(); x++)
		{
			if( vanderbilt && list.get(x).getPatient().equals("Vanderbilt-donor") )
				index = x;
			else if( ! vanderbilt && list.get(x).getPatient().equals("UNC-donor") )
				index = x;
			
		}
		
		if( index == null)
			throw new Exception("Could not find ");
		
		String[] splits = s.split("\t");
		
		return Double.parseDouble(splits[index]);
	}
	
	@SuppressWarnings("resource")
	private static void writeUnsortedTable() throws Exception
	{
		HashMap<String, MetadataFileLine> map = MetadataFileLine.getMetaMap();
		
		List<MetadataFileLine> metaList = new ArrayList<>();
		metaList.add(null);
		
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
			
			metaList.add(mfl);
			
			writer.write("\t" + mfl.getPatient() + "_" + mfl.getTimepoint());
		}
		
		writer.write("\tvanderbiltRatio\tuncRatio\taverageRatio\n");
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0].replaceAll(";", "_").replaceAll("\\|", "_").replaceAll(" ","_").
					replaceAll(":", "_").replaceAll("\\.","_").replaceAll("-", "_").replaceAll("\t", "_")
					.replaceAll("/","_").replaceAll("'", "_") );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			double vanderbiltRatio = getRatio(metaList, s, true);
			double chapelHillRatio = getRatio(metaList, s, false);
			
			writer.write("\t" + vanderbiltRatio + "\t" + chapelHillRatio + "\t");
			
			double averageRatio = ( vanderbiltRatio + chapelHillRatio ) / 2;
			
			writer.write(averageRatio + "\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
}
