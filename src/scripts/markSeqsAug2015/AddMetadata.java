package scripts.markSeqsAug2015;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		for( String s :  PivotAllLevels.LEVELS)
		{
			System.out.println(s);
			
			String inFile = ConfigReader.getMarkAug2015Batch1Dir() 
					+ File.separator + s + "TaxaAsColumnsLogNorm.txt";
					
			String outFile = ConfigReader.getMarkAug2015Batch1Dir() 
					+ File.separator + s + "TaxaAsColumnsLogNormWithMetadata.txt";
			
			addMetadata(inFile, outFile, false);
			
			if( ! s.equals("k"))
			{
				inFile = ConfigReader.getMarkAug2015Batch1Dir() 
						+ File.separator + "pcoa_" + s  +".txt";
				
				outFile = ConfigReader.getMarkAug2015Batch1Dir() 
						+ File.separator + "pcoa_" + s + "metadata.txt";
				
				addMetadata(inFile, outFile, true);
				
			}	
		}
	}
	
	public static void addMetadata(String inFile, String outFile, boolean fromR) throws Exception
	{
		HashMap<String, MetadataParserFileLine> map = MetadataParserFileLine.getMap();
		{
			
			BufferedReader reader = new BufferedReader(new FileReader(inFile
					));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile
					)));
			
			String[] topSplits = reader.readLine().split("\t");
			
			writer.write("sample\tsampleName\tacuteOrChronic\tsex\ttreatment\tcage\texpriment\tbatch");
			
			for( int x= fromR ? 0 : 1; x < topSplits.length; x++)
				writer.write("\t" + topSplits[x]);
			
			writer.write("\n");
			
			for(String s2= reader.readLine(); s2 != null; s2 = reader.readLine())
			{
				String[] splits = s2.split("\t");
				
				String key = splits[0].replaceAll("\"", "");
				
				MetadataParserFileLine mpfl = map.get(key);
				
				if( mpfl == null)
					throw new Exception("No " + splits[0]);
				
				writer.write("s_" + key + "\t" + mpfl.getSampleName() + "\t" + mpfl.getAcuteOrChronic() + "\t");
				writer.write( mpfl.getSex() + "\t" + mpfl.getTreatment() + "\t" + mpfl.getCage() + "\t" +
								mpfl.getExpriment() + "\t" + mpfl.getBatch());
				
				for( int x=1; x < splits.length; x++ )
					writer.write("\t" + splits[x]);
				
				writer.write("\n");
			}

			writer.flush();  writer.close();
			reader.close();
		}
	}
	
}
