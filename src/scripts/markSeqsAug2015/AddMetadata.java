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
		HashMap<String, MetadataParserFileLine> map = MetadataParserFileLine.getMap();
		for( String s :  PivotAllLevels.LEVELS)
		{
			System.out.println(s);
			
			BufferedReader reader = new BufferedReader(new FileReader(
					ConfigReader.getMarkAug2015Batch1Dir() 
					+ File.separator + s + "TaxaAsColumnsLogNorm.txt"));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					ConfigReader.getMarkAug2015Batch1Dir() 
					+ File.separator + s + "TaxaAsColumnsLogNormWithMetadata.txt")));
			
			String[] topSplits = reader.readLine().split("\t");
			
			writer.write("sample\tsampleName\tacuteOrChronic\tsex\ttreatment\tcage\texpriment\tbatch");
			
			for( int x=1; x < topSplits.length; x++)
				writer.write("\t" + topSplits[x]);
			
			writer.write("\n");
			
			for(String s2= reader.readLine(); s2 != null; s2 = reader.readLine())
			{
				String[] splits = s2.split("\t");
				MetadataParserFileLine mpfl = map.get(splits[0]);
				
				if( mpfl == null)
					throw new Exception("No " + splits[0]);
				
				writer.write(splits[0] + "\t" + mpfl.getSampleName() + "\t" + mpfl.getAcuteOrChronic() + "\t");
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
