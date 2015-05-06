package scripts.JobinApril2015.cjej;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class AddMetadata
{
	private static int getDayOfInfection(int group)  throws Exception
	{
		if( group== 1)
			return 3;
		else if ( group == 2)
			return 6;
		else if ( group== 3)
			return 10;
		else if ( group == 4)
			return 14;
		else if ( group == - 1)
			return -1;
		else throw new Exception("Unknow group id " + group);
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileLine> metaMap = MetadataFileLine.getMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getJobinApril2015Dir() + File.separator + 	"cjejR_taxaAsColumns_mergedF_R_phylaLogNormal.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getJobinApril2015Dir() + File.separator + "cjejR_taxaAsColumns_mergedF_R_phylaLogNormalWithMetadata.txt")));
		
		writer.write("sample\tread\tgroupID\tcageID\tdayOfInfection\tmouse\ttimepoint\tinfected");
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write( "\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			MetadataFileLine mfl = metaMap.get(splits[0].replaceAll("\"", "").split("_")[0]);
			
			writer.write( splits[0].replaceAll("\"", "").split("_")[0] + "\t" );
			writer.write( splits[0].charAt(splits[0].length() -1) + "\t");
			writer.write(mfl.getGroupID() + "\t");
			writer.write(mfl.getCageID() + "\t");
			writer.write( getDayOfInfection(mfl.getGroupID()) + "\t");
			writer.write(mfl.getMouse() + "\t");
			writer.write("t_" + mfl.getTimepoint() + "\t");
			writer.write(mfl.getInfected() );
			
			for( int x=1; x < splits.length; x++)
				writer.write( "\t" + splits[x]);
			
			writer.write("\n");
			
		}
		
		writer.flush(); writer.close();
		reader.close();
	}
}
