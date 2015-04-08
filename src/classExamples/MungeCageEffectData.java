package classExamples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.OtuWrapper;

public class MungeCageEffectData
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = 
				new OtuWrapper("D:\\classes\\Advanced_Stats_Spring2015\\cageData\\phylumPivotedTaxaAsColumns.txt");
		
		File loggedData =
				new File("D:\\classes\\Advanced_Stats_Spring2015\\cageData\\phylumPivotedTaxaAsColumnsLogNormal.txt");
		wrapper.writeNormalizedLoggedDataToFile(loggedData);
		
		File loggedDataPlusMeta = 
				new File("D:\\classes\\Advanced_Stats_Spring2015\\cageData\\phylumPivotedTaxaAsColumnsLogNormalWithMetadata.txt");
		
		addMetadata(loggedData, loggedDataPlusMeta);
		
		addMetadata(new File("D:\\classes\\Advanced_Stats_Spring2015\\cageData\\mds_phylum.txt"), 
						new File("D:\\classes\\Advanced_Stats_Spring2015\\cageData\\mds_phylumWithMetadata.txt"));
	}
	
	private static void addMetadata(File inFile, File outFile) throws Exception
	{
		HashMap<String, CageFileLine> metaMap = CageFileLine.getMetaMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		String[] firstSplits = reader.readLine().split("\t");
		
		writer.write(firstSplits[0] + "\tcage\ttime\tgenotype");
		
		for( int x=1; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			CageFileLine cfl = metaMap.get(splits[0].replaceAll("\"", ""));
			
			if( cfl == null)
				throw new Exception("No " + splits[0]);
			
			writer.write(splits[0]  + "\t" + cfl.getCage() + "\t" + cfl.getTime() + "\t" + cfl.getGenotype());
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();   writer.close();
		reader.close();
	}
}
