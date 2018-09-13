package scripts.jamesEOE.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class MergeBothMetadatas
{

	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			writeMergedMetadata(taxa);
			
		}
	}
	
	private static void writeMergedMetadata(String level) throws Exception
	{
		HashMap<String,BenitezMetadataParser> bMap = 
				BenitezMetadataParser.getMapBysampleRun();
		
		System.out.println("Bmap size before =" + bMap.size());
		
		HashMap<String, Integer> eMap = EvanMetadataParser.getEvanCaseControlMap();
		
		BufferedWriter writer = new BufferedWriter( new FileWriter(new File(
			ConfigReader.getJamesEoeDirectory() + File.separator + "test" + 
				File.separator + "AF_merged_" + level + ".txt")));
		
		writer.write("id\tcaseControl\tactiveInactive\tsource");
		
		File inFie = new File(ConfigReader.getJamesEoeDirectory() + File.separator + "test" + 
				File.separator + level+ ".tsv");
		
		File logFile = new File(ConfigReader.getJamesEoeDirectory() + File.separator + "test" + 
				File.separator + level+ "logNorm.tsv");
		
		OtuWrapper wrapper = new OtuWrapper(inFie);
		wrapper.writeNormalizedLoggedDataToFile(logFile);
				
		BufferedReader reader = new BufferedReader(new FileReader(logFile));
		
		String[] firstLine = reader.readLine().split("\t");
		
		for( int x=1; x < firstLine.length; x++)
			writer.write("\t" + firstLine[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			String id = splits[0];
			
			writer.write(id + "\t" );
			
			if( bMap.containsKey(id))
			{
				BenitezMetadataParser bmp = bMap.get(id);
				bMap.remove(id);
				writer.write(bmp.getStudyGroup() + "\t" + bmp.getStatus() + "\tBenitez");
			}
			else if( eMap.containsKey(id))
			{
				Integer caseControl = eMap.get(id);
				
				writer.write(caseControl + "\tNA\tEvan");
				
				eMap.remove(id);
			}
			else
			{
				writer.write("NA\tNA\tNA");
				System.out.println("Could not find " + id);
			}
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		System.out.println("Bmap size after =  " + bMap.size());
		System.out.println("Emap size after = " + eMap.size());
		reader.close();
		writer.flush();  writer.close();
	}
}
