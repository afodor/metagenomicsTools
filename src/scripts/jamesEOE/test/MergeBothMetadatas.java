package scripts.jamesEOE.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;
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
		
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getJamesEoeDirectory() + File.separator + "test" + 
				File.separator + level+ ".tsv")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			String id = splits[0];
			
			if( bMap.containsKey(id))
			{
				bMap.remove(id);
			}
			else if( eMap.containsKey(id))
			{
				eMap.remove(id);
			}
			else
			{
				System.out.println("Could not find " + id);
			}
		}
		
		System.out.println("Bmap size after =  " + bMap.size());
		System.out.println("Emap size after = " + eMap.size());
		reader.close();
	}
}
