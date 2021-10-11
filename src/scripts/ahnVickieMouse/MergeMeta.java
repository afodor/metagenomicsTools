package scripts.ahnVickieMouse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class MergeMeta
{
	public static final String[] LEVELS = { "otu", "k", "p", "c", "o", "f", "g" };
	
	public static void main(String[] args) throws Exception
	{
		
		for( String s : LEVELS)
		{
			System.out.println(s);
			
			File logFile = new File("C:\\AnhVickiMouseData\\pivoted_" +s+"_logNorm.txt");
			
			File metaFile = new File("C:\\AnhVickiMouseData\\pivoted_" +s+"_logNormPlusMeta.txt");
			pivotALevel(logFile, metaFile);
		}
		
	}
	
	private static void pivotALevel(File logFile, File metaFile) throws Exception
	{
		HashMap<String, MetadataFileLine> map = MetadataFileLine.getMetaMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(logFile));
		
		String[] splits = reader.readLine().split("\t");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(metaFile));
		
		writer.write(splits[0] + "\t" + "diet\tdisease_Status\tSample_Type\tMenopause\tPair\tStudyNum\tStudyYear");
		
		for(int x=1; x < splits.length; x++)
			writer.write("\t" + splits[x]);
		
		writer.write("\n");
		
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			splits = s.split("\t");
			writer.write( splits[0] );
			
			MetadataFileLine mfl = map.get(splits[0]);
			
			writer.write("\t" + mfl.getDiet() + "\t" + mfl.getDiseaseStatus() + "\t" + mfl.getSample_Type() + "\t" + 
			mfl.getMenopause() + "\t" + 
							mfl.getPair() + "\t" + mfl.getStudyNumber() + "\t" + mfl.getStudyYear());
		
			for(int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}

}
