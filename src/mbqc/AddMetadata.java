package mbqc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, RawDesignMatrixParser> metaMap = 
				RawDesignMatrixParser.getByFullId();
		
		HashMap<String, String> kitMap = PValuesByExtraction.getManualKitManufacturer();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "dropbox" + File.separator + 
				"alpha-beta-div" + File.separator +  "beta-div" +
						File.separator + "merged_species.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "dropbox" + File.separator + 
				"alpha-beta-div" + File.separator +  "beta-div" +
				 File.separator + "metadataForMergedSpecies.txt"
					)));
		
		writer.write("fullID\tinformaticsToken\tobscuredToken\tnumberToken\textractionWetlab\tsequencingWetlab\tmbqcID\textractionIsShipped\tkitManufactuer\tindex\n");
		
		int numFound =0;
		int numMissed=0;
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x=1; x < splits.length; x++)
		{
			String key = splits[x];
			writer.write(key+ "\t");
			
			StringTokenizer innerSplits = new StringTokenizer(key, ".");
			writer.write(innerSplits.nextToken() + "\t" + innerSplits.nextToken() + "\t" + 
					innerSplits.nextToken()  + "\t");
			
			if( innerSplits.hasMoreTokens())
				throw new Exception("No");
			
			if( metaMap.containsKey(key))
			{
					numFound++;
					
					RawDesignMatrixParser rdmp = metaMap.get(key);
					writer.write((rdmp.getExtractionWetlab().equals("NA") ? "shipped" :rdmp.getExtractionWetlab())  
							+ "\t" + rdmp.getSequecingWetlab() + "\t" + rdmp.getMbqcID() + "\t"
							+  rdmp.getExtractionWetlab().equals("NA") + "\t" );
					
					String kit = kitMap.get(rdmp.getExtractionWetlab());
					
					writer.write(kit + "\t");
			}
			else
			{
				numMissed++;
				writer.write("NA"
						+ "\t" + "NA"+ "\t" + "NA"+ "\t"
						+  "NA"+ "\t" );
				
				writer.write("NA\t");
		
			}
			
			writer.write(x + "\n");
		}
		
		
		
		writer.flush();  writer.close();
		
		System.out.println(numFound + " " + numMissed);
	}
}
