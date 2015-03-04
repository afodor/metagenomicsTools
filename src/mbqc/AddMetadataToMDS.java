package mbqc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class AddMetadataToMDS
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
						File.separator + "mdsOut.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "dropbox" + File.separator + 
				"alpha-beta-div" + File.separator +  "beta-div" +
				 File.separator + "mdsOutWithMetadata.txt"
					)));
		
		writer.write("fullID\tinformaticsToken\tobscuredToken\tnumberToken\textractionWetlab\tsequencingWetlab\tmbqcID\textractionIsNA\tkitManufactuer\t");
		writer.write("mds1\tmds2\n");
		
		int numFound =0;
		int numMissed=0;
		
		for(String s= reader.readLine();  s !=null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( metaMap.containsKey(splits[0]))
			{
				numFound++;
				writer.write(splits[0] + "\t");
				
				StringTokenizer innerSplits = new StringTokenizer(splits[0], ".");
				writer.write(innerSplits.nextToken() + "\t" + innerSplits.nextToken() + "\t" + 
						innerSplits.nextToken()  + "\t");
				
				if( innerSplits.hasMoreTokens())
					throw new Exception("No");
				
				RawDesignMatrixParser rdmp = metaMap.get(splits[0]);
				writer.write(rdmp.getExtractionWetlab() + "\t" + rdmp.getSequecingWetlab() + "\t" + rdmp.getMbqcID() + "\t"
						+  rdmp.getExtractionWetlab().equals("NA") + "\t" );
				
				String kit = kitMap.get(rdmp.getExtractionWetlab());
				
				writer.write(kit + "\t");
				
				writer.write(splits[1] + "\t" + splits[2] + "\n");
				
				if( splits.length != 3)
					throw new Exception("No");
			}
			else
			{
				numMissed++;
			}
		}
		
		writer.flush();  writer.close();
		
		System.out.println(numFound + " " + numMissed);
	}
}
